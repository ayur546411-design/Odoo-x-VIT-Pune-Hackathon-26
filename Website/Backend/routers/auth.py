from fastapi import APIRouter, HTTPException, Depends
from models.otp import OtpRequest, OtpVerify, get_utc_now
from models.user import UserResponse
from core.database import get_db
from core.security import generate_otp, create_access_token
from core.config import settings
import httpx
from bson import ObjectId

router = APIRouter(prefix="/api/auth", tags=["Auth"])

async def send_brevo_email(to_email: str, otp: str):
    url = "https://api.brevo.com/v3/smtp/email"
    headers = {
        "accept": "application/json",
        "api-key": settings.BREVO_API_KEY,
        "content-type": "application/json"
    }
    payload = {
        "sender": {"email": settings.BREVO_SENDER_EMAIL, "name": "Reimbursement Management"},
        "to": [{"email": to_email}],
        "subject": "Your Registration OTP Code",
        "htmlContent": f"<html><body><h3>Your OTP code is: <strong>{otp}</strong></h3><p>It will expire in 5 minutes.</p></body></html>"
    }
    
    async with httpx.AsyncClient() as client:
        response = await client.post(url, headers=headers, json=payload)
        if response.status_code not in (200, 201, 202):
            raise Exception(f"Failed to send email: {response.text}")
        return response.json()

@router.post("/send-otp")
async def send_otp(request: OtpRequest):
    db = get_db()
    
    otp_code = generate_otp(6)
    
    try:
        await send_brevo_email(request.email, otp_code)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
    await db.otps.delete_many({"email": request.email})
    
    otp_doc = {
        "email": request.email,
        "otp": otp_code,
        "createdAt": get_utc_now()
    }
    await db.otps.insert_one(otp_doc)
    
    return {"message": "OTP sent successfully."}

@router.post("/verify-otp")
async def verify_otp(request: OtpVerify):
    db = get_db()
    
    otp_record = await db.otps.find_one({
        "email": request.email,
        "otp": request.otp
    })
    
    if not otp_record:
        raise HTTPException(status_code=400, detail="Invalid or expired OTP")
    
    await db.otps.delete_one({"_id": otp_record["_id"]})
    
    user = await db.users.find_one({"email": request.email})
    if not user:
        # Create new user storing the new Registration form payload
        new_user = {
            "email": request.email,
            "name": request.name,
            "role": request.role or "employee",
            "department": request.department
        }
        result = await db.users.insert_one(new_user)
        new_user["_id"] = result.inserted_id
        user = new_user
        
    user_id = str(user["_id"])
    
    access_token = create_access_token(
        data={"sub": user["email"], "id": user_id, "role": user.get("role", "employee")}
    )
    
    return {
        "access_token": access_token,
        "token_type": "bearer",
        "user": {
            "id": user_id,
            "email": user["email"],
            "name": user.get("name"),
            "role": user.get("role"),
            "department": user.get("department")
        }
    }
