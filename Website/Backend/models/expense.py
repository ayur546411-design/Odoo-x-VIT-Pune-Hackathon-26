from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime

class ExpenseBase(BaseModel):
    title: str = Field(..., description="Short title of the expense")
    amount: float = Field(..., gt=0, description="Amount of the expense")
    category: str = Field(..., description="Category of the expense")
    description: Optional[str] = None
    receipt_url: Optional[str] = None

class ExpenseCreate(ExpenseBase):
    pass

class ExpenseUpdateStatus(BaseModel):
    status: str
    comment: Optional[str] = None

class ExpenseResponse(ExpenseBase):
    id: str
    user_id: str
    status: str = "pending"  # pending, approved, rejected
    manager_comment: Optional[str] = None
    created_at: datetime
    updated_at: datetime

    class Config:
        populate_by_name = True
