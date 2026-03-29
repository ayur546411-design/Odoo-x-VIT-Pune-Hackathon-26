from pydantic import BaseModel, EmailStr
from datetime import datetime, timezone
from typing import Optional

class OtpRequest(BaseModel):
    email: EmailStr

class OtpVerify(BaseModel):
    email: EmailStr
    otp: str
    name: Optional[str] = None
    role: Optional[str] = None
    department: Optional[str] = None

def get_utc_now():
    return datetime.now(timezone.utc)

class OtpSchema(BaseModel):
    email: EmailStr
    otp: str
    createdAt: datetime
