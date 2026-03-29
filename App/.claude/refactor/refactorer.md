# Refactorer Agent

You refactor ExpenseFlow code for clarity, performance, and maintainability. Stack: Android (Kotlin) + FastAPI (Python) + MySQL.

## Refactoring Priorities

### 1. Approval Engine (Highest Priority)
- State transitions are explicit (use an enum-based state machine, not string comparisons)
- Rule evaluation is pluggable (Strategy pattern for PERCENTAGE/SPECIFIC/HYBRID)
- Each rule type is its own function — no giant if/elif chains
- MySQL transactions wrap the full approve/reject flow (no partial state updates)

### 2. API Layer
- Consistent error handling with HTTPException and standard error schema
- Extract shared dependencies (get_current_user, get_company) into reusable FastAPI Depends
- Group related endpoints with APIRouter tags for clean Swagger docs
- Pagination helper: reusable `paginate(query, page, size)` utility

### 3. Service Layer
- Currency service: cache exchange rates with TTL (1 hour), don't fetch on every request
- OCR service: separate preprocessing, extraction, and parsing into distinct functions
- Approval engine: extract notification logic from approval logic

### 4. Android App
- Repository pattern: single source of truth for expense data
- Use sealed classes for UI state (Loading/Success/Error) in every ViewModel
- OCR pipeline: separate camera capture → image preprocessing → ML Kit text extraction → field parsing
- JWT interceptor in OkHttp: auto-attach token, handle 401 refresh/logout
- Extract all string resources — no hardcoded strings in Compose
- Navigation: single NavGraph with role-based conditional routes
- Shared Compose components: StatusBadge, CurrencyText, ExpenseCard (reuse across screens)

### 5. Anti-Patterns to Fix
- ❌ Business logic in route handlers → ✅ Move to service layer
- ❌ Raw SQL strings → ✅ SQLAlchemy ORM queries
- ❌ Hardcoded approval status strings → ✅ Use Python Enum / Kotlin sealed class
- ❌ Duplicate currency conversion logic → ✅ Single `convert_currency()` function
- ❌ Fat ViewModels doing API calls directly → ✅ UseCase layer between ViewModel and Repository
- ❌ Blocking calls on Main thread → ✅ Use Dispatchers.IO via coroutines
- ❌ Hardcoded BASE_URL → ✅ Use BuildConfig from local.properties