# ExpenseFlow — AI-Powered Expense Reimbursement Platform

## Project Overview
ExpenseFlow is a mobile-first (Android) expense reimbursement and multi-level approval platform. It supports sequential approval chains, conditional approval rules (percentage, specific approver, hybrid), OCR-based receipt scanning, and multi-currency conversion.

## Architecture
- **Backend**: Python 3.11+ / FastAPI with SQLAlchemy ORM, MySQL 8.0+
- **Mobile App**: Kotlin + Jetpack Compose (Android) — the ONLY client
- **OCR (On-Device)**: Google ML Kit Text Recognition
- **OCR (Server)**: Tesseract / PaddleOCR as fallback
- **Auth**: JWT (PyJWT) + bcrypt password hashing
- **No web frontend, no Docker** — backend runs directly via uvicorn

## Key Directories
- `backend/app/` — FastAPI application code
- `backend/app/models/` — SQLAlchemy ORM models (User, Company, Expense, Workflow, ApprovalAction)
- `backend/app/routers/` — API endpoint handlers
- `backend/app/services/` — Business logic (approval_engine, currency_service, ocr_service)
- `backend/app/schemas/` — Pydantic validation schemas
- `android/app/src/main/java/com/expenseflow/` — Kotlin Android app
- `android/app/src/main/java/com/expenseflow/ui/` — Jetpack Compose screens
- `android/app/src/main/java/com/expenseflow/data/` — Repository + Retrofit API client
- `android/app/src/main/java/com/expenseflow/domain/` — Use cases & domain models

## Critical Business Rules
1. First signup auto-creates Company (with country-based currency) + Admin user
2. Admin assigns roles: ADMIN, MANAGER, EMPLOYEE
3. `is_manager_first` flag: if True, employee's direct manager is always Step 1
4. Sequential approval: expense moves to next step ONLY after current approves
5. Conditional rules override sequential flow:
   - PERCENTAGE: if X% of approvers approve → auto-approved
   - SPECIFIC_APPROVER: if designated person (e.g. CFO) approves → auto-approved
   - HYBRID: either condition triggers approval
6. Rejection at ANY step stops the chain immediately
7. Currency conversion happens at submission time using Exchange Rate API
8. All amounts displayed to managers/admins in company's default currency

## External APIs
- Countries: `https://restcountries.com/v3.1/all?fields=name,currencies`
- Exchange Rates: `https://api.exchangerate-api.com/v4/latest/{BASE_CURRENCY}`

## Database
- MySQL 8.0+ with CHAR(36) UUID primary keys
- Tables: companies, users, expenses, approval_workflows, workflow_steps, approval_rules, approval_actions
- Engine: InnoDB, charset: utf8mb4
- Migrations via Alembic
- Connection string: `mysql+aiomysql://user:pass@localhost:3306/expenseflow`

## Android App Architecture
- **Pattern**: MVVM + Clean Architecture (data/domain/ui layers)
- **DI**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp with JWT interceptor
- **Local cache**: Room (for offline expense drafts)
- **Image loading**: Coil
- **Navigation**: Jetpack Navigation Compose with role-based routing
- **Min SDK**: 26, Target SDK: 34
- **OCR**: ML Kit Text Recognition (on-device, no internet needed)

## Coding Standards
- Python: PEP 8, type hints on all functions, async/await for I/O
- Kotlin: Coroutines + Flow for async, MVVM with Repository pattern
- Kotlin: Sealed classes for UI state (Loading/Success/Error)
- All API responses follow `{ "status": "success/error", "data": {...}, "message": "..." }`
- Never commit `.env` files or secrets
- Use Kotlin version catalog (libs.versions.toml) for dependency management

## Testing
- Backend: pytest with httpx AsyncClient
- Android: JUnit + Mockk for unit tests, Compose Testing for UI tests
- Minimum coverage target: 80% for business logic (approval engine, currency service)

## Common Tasks
- Add new approval rule type → `backend/app/services/approval_engine.py`
- Add new expense category → `backend/app/schemas/expense.py` (update enum)
- Modify OCR extraction → `backend/app/services/ocr_service.py`
- Add new API endpoint → create route in `backend/app/routers/`, register in `main.py`
- Add new Android screen → create in `android/.../ui/`, add route in `navigation/NavGraph.kt`
- Modify Retrofit API → `android/.../data/remote/ApiService.kt`