from fastapi import APIRouter, Depends, HTTPException, status, UploadFile, File, Form
from typing import List, Optional
from datetime import datetime
from bson import ObjectId
from core.database import get_db
from core.dependencies import get_current_user, require_role
from models.expense import ExpenseResponse, ExpenseUpdateStatus
import base64

router = APIRouter(prefix="/api/expenses", tags=["Expenses"])

def format_expense(doc) -> dict:
    doc["id"] = str(doc["_id"])
    doc["user_id"] = str(doc["user_id"])
    return doc

@router.get("/my", response_model=List[ExpenseResponse])
async def get_my_expenses(current_user: dict = Depends(get_current_user)):
    db = get_db()
    cursor = db.expenses.find({"user_id": ObjectId(current_user["id"])})
    expenses = await cursor.to_list(length=100)
    return [format_expense(ex) for ex in expenses]

@router.get("/team", response_model=List[ExpenseResponse])
async def get_team_expenses(current_user: dict = Depends(require_role(["manager", "admin"]))):
    db = get_db()
    # Find all users in the manager's department
    manager_dept = current_user.get("department")
    if not manager_dept:
        return []

    users_cursor = db.users.find({"department": manager_dept})
    dept_users = await users_cursor.to_list(length=1000)
    dept_user_ids = [u["_id"] for u in dept_users]

    cursor = db.expenses.find({"user_id": {"$in": dept_user_ids}})
    expenses = await cursor.to_list(length=500)
    return [format_expense(ex) for ex in expenses]

@router.get("/pending", response_model=List[ExpenseResponse])
async def get_pending_approvals(current_user: dict = Depends(require_role(["manager", "admin"]))):
    db = get_db()
    if current_user.get("role") == "admin":
        cursor = db.expenses.find({"status": "pending"})
    else:
        # manager filtering
        manager_dept = current_user.get("department")
        users_cursor = db.users.find({"department": manager_dept})
        dept_users = await users_cursor.to_list(length=1000)
        dept_user_ids = [u["_id"] for u in dept_users]
        cursor = db.expenses.find({"status": "pending", "user_id": {"$in": dept_user_ids}})
        
    expenses = await cursor.to_list(length=500)
    return [format_expense(ex) for ex in expenses]

@router.get("", response_model=List[ExpenseResponse])
async def get_all_expenses(current_user: dict = Depends(require_role(["admin"]))):
    db = get_db()
    cursor = db.expenses.find()
    expenses = await cursor.to_list(length=1000)
    return [format_expense(ex) for ex in expenses]

@router.post("", response_model=ExpenseResponse)
async def create_expense(
    title: str = Form(...),
    amount: float = Form(...),
    category: str = Form(...),
    description: Optional[str] = Form(None),
    receipt: Optional[UploadFile] = File(None),
    current_user: dict = Depends(get_current_user)
):
    db = get_db()
    receipt_url = None
    
    if receipt:
        content = await receipt.read()
        b64 = base64.b64encode(content).decode('utf-8')
        receipt_url = f"data:{receipt.content_type};base64,{b64}"

    expense_doc = {
        "title": title,
        "amount": amount,
        "category": category,
        "description": description,
        "receipt_url": receipt_url,
        "status": "pending",
        "user_id": ObjectId(current_user["id"]),
        "manager_comment": None,
        "created_at": datetime.utcnow(),
        "updated_at": datetime.utcnow()
    }

    result = await db.expenses.insert_one(expense_doc)
    expense_doc["_id"] = result.inserted_id
    
    return format_expense(expense_doc)

@router.patch("/{expense_id}/status")
async def update_expense_status(
    expense_id: str,
    update_data: ExpenseUpdateStatus,
    current_user: dict = Depends(require_role(["manager", "admin"]))
):
    db = get_db()
    expense = await db.expenses.find_one({"_id": ObjectId(expense_id)})
    if not expense:
        raise HTTPException(status_code=404, detail="Expense not found")

    result = await db.expenses.update_one(
        {"_id": ObjectId(expense_id)},
        {"$set": {
            "status": update_data.status,
            "manager_comment": update_data.comment,
            "updated_at": datetime.utcnow()
        }}
    )

    if result.modified_count == 0:
        raise HTTPException(status_code=400, detail="Failed to update")

    return {"message": "Expense status updated"}

@router.get("/stats")
async def get_expense_stats(current_user: dict = Depends(require_role(["admin", "manager"]))):
    db = get_db()
    total_expenses = await db.expenses.count_documents({})
    pending = await db.expenses.count_documents({"status": "pending"})
    approved = await db.expenses.count_documents({"status": "approved"})
    rejected = await db.expenses.count_documents({"status": "rejected"})
    return {
        "total": total_expenses,
        "pending": pending,
        "approved": approved,
        "rejected": rejected
    }

@router.post("/scan")
async def scan_receipt(
    receipt: UploadFile = File(...),
    current_user: dict = Depends(get_current_user)
):
    # Dummy implementation for simple OCR scan simulation
    return {
        "text": "Scanned receipt mock data",
        "amount": 100.0,
        "category": "Travel",
        "title": "Mock OCR"
    }
