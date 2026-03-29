# API Rules — ExpenseFlow (FastAPI + MySQL, consumed by Android app)

## Framework
- FastAPI with async endpoints
- Pydantic v2 for request/response validation
- JWT authentication via `Authorization: Bearer <token>` header
- API versioning: all routes under `/api/v1/`
- MySQL via SQLAlchemy async (aiomysql driver)

## Response Format
Every endpoint returns this structure:
```json
{
  "status": "success",
  "data": { ... },
  "message": "Expense submitted successfully"
}
```

Error responses:
```json
{
  "status": "error",
  "data": null,
  "message": "Expense not found",
  "error_code": "EXPENSE_NOT_FOUND"
}
```

**Important**: Android Retrofit DTOs must match these schemas exactly. Any change to response shape requires updating the corresponding Kotlin data class.

## HTTP Status Codes
- `200` — Success (GET, PATCH)
- `201` — Created (POST that creates a resource)
- `400` — Validation error (bad input)
- `401` — Unauthorized (missing or invalid JWT)
- `403` — Forbidden (valid JWT but wrong role)
- `404` — Not found
- `409` — Conflict (e.g., approving already-rejected expense)
- `422` — Unprocessable entity (Pydantic validation failure)
- `500` — Internal server error (never expose stack trace)

## Authentication
```python
async def get_current_user(token: str = Depends(oauth2_scheme)) -> User:
    # Decode JWT, fetch user from MySQL, verify company access
    ...

def require_role(*roles: str):
    async def checker(user: User = Depends(get_current_user)):
        if user.role not in roles:
            raise HTTPException(403, "Insufficient permissions")
        return user
    return checker

# Usage
@router.post("/users")
async def create_user(data: CreateUserSchema, admin: User = Depends(require_role("ADMIN"))):
    ...
```

## Endpoint Conventions
- Use plural nouns: `/expenses`, `/users`, `/workflows`
- Resource actions use nested paths: `/expenses/{id}/approve`, `/expenses/{id}/reject`
- List endpoints support: `?page=1&size=25&status=PENDING&sort=-created_at`
- Always validate `company_id` matches the authenticated user's company
- UUIDs are CHAR(36) strings in MySQL — validate format in Pydantic

## Pagination Response
```json
{
  "status": "success",
  "data": {
    "items": [...],
    "total": 142,
    "page": 1,
    "size": 25,
    "pages": 6
  }
}
```

## File Upload
- Receipt uploads via `multipart/form-data`
- Max file size: 10MB
- Allowed types: image/jpeg, image/png, application/pdf
- Store with randomized UUID filename, not original name
- Return the file URL in the expense response
- Android app should compress images before upload (max 1920px, 80% quality)

## Android Client Contract
- Every API change must be reflected in `android/.../data/remote/ApiService.kt`
- Retrofit DTOs live in `android/.../data/remote/dto/`
- Error responses parsed by a shared `ErrorHandler` utility
- 401 responses trigger automatic logout and redirect to login screen