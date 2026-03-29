from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class WorkflowBase(BaseModel):
    name: str
    description: Optional[str] = None
    steps: List[dict]

class WorkflowCreate(WorkflowBase):
    pass

class WorkflowResponse(WorkflowBase):
    id: str
    created_by: str
    created_at: datetime
    updated_at: datetime

    class Config:
        populate_by_name = True
