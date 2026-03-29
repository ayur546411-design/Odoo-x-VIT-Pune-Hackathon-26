# Android Rules — ExpenseFlow

## Framework & Libraries
- Kotlin 1.9+ with Jetpack Compose (Material 3)
- Hilt for dependency injection
- Retrofit + OkHttp for networking
- Room for local offline cache
- Coil for image loading
- ML Kit Text Recognition for on-device OCR
- CameraX for camera capture
- Jetpack Navigation Compose for routing
- Kotlin Coroutines + Flow for async

## Architecture: MVVM + Clean Architecture
```
ui/          → Compose screens + ViewModels (presentation layer)
domain/      → Use cases + domain models (business logic)
data/        → Repositories + Retrofit API + Room DB (data layer)
di/          → Hilt modules (dependency injection)
```

## Package Structure
```
com.expenseflow/
├── di/
│   ├── NetworkModule.kt        # Retrofit, OkHttp, JWT interceptor
│   ├── DatabaseModule.kt       # Room database
│   └── RepositoryModule.kt     # Repository bindings
├── data/
│   ├── remote/
│   │   ├── ApiService.kt       # Retrofit interface
│   │   ├── dto/                # Data Transfer Objects (match API)
│   │   └── AuthInterceptor.kt  # JWT token interceptor
│   ├── local/
│   │   ├── AppDatabase.kt     # Room database
│   │   └── dao/               # Room DAOs
│   └── repository/
│       ├── AuthRepository.kt
│       ├── ExpenseRepository.kt
│       ├── ApprovalRepository.kt
│       └── CurrencyRepository.kt
├── domain/
│   ├── model/                  # Domain models (not DTOs)
│   │   ├── User.kt
│   │   ├── Expense.kt
│   │   ├── Workflow.kt
│   │   └── ApprovalAction.kt
│   └── usecase/
│       ├── SubmitExpenseUseCase.kt
│       ├── ProcessApprovalUseCase.kt
│       └── ScanReceiptUseCase.kt
├── ui/
│   ├── auth/
│   │   ├── LoginScreen.kt
│   │   ├── SignupScreen.kt
│   │   └── AuthViewModel.kt
│   ├── expense/
│   │   ├── SubmitExpenseScreen.kt
│   │   ├── ExpenseHistoryScreen.kt
│   │   ├── ExpenseDetailScreen.kt
│   │   └── ExpenseViewModel.kt
│   ├── approval/
│   │   ├── ApprovalQueueScreen.kt
│   │   └── ApprovalViewModel.kt
│   ├── admin/
│   │   ├── UserManagementScreen.kt
│   │   ├── WorkflowConfigScreen.kt
│   │   └── AdminViewModel.kt
│   ├── ocr/
│   │   ├── CameraCaptureScreen.kt
│   │   ├── OcrResultScreen.kt
│   │   └── OcrViewModel.kt
│   ├── components/             # Shared reusable components
│   │   ├── StatusBadge.kt
│   │   ├── CurrencyText.kt
│   │   ├── ExpenseCard.kt
│   │   ├── LoadingState.kt
│   │   └── ErrorState.kt
│   ├── navigation/
│   │   └── NavGraph.kt        # Role-based navigation
│   └── theme/
│       ├── Theme.kt
│       ├── Color.kt
│       └── Type.kt
└── ExpenseFlowApp.kt          # @HiltAndroidApp Application class
```

## Coding Rules
1. Every ViewModel uses a sealed class for UI state:
   ```kotlin
   sealed class UiState<out T> {
       object Loading : UiState<Nothing>()
       data class Success<T>(val data: T) : UiState<T>()
       data class Error(val message: String) : UiState<Nothing>()
   }
   ```

2. API calls go through Repository → UseCase → ViewModel → Screen
   - Never call Retrofit directly from a ViewModel

3. Role-based navigation in NavGraph:
   ```kotlin
   when (currentUser.role) {
       Role.ADMIN -> AdminNavGraph()
       Role.MANAGER -> ManagerNavGraph()
       Role.EMPLOYEE -> EmployeeNavGraph()
   }
   ```

4. Currency amounts display with Kotlin's NumberFormat:
   ```kotlin
   fun formatCurrency(amount: Double, currencyCode: String): String {
       val format = NumberFormat.getCurrencyInstance()
       format.currency = Currency.getInstance(currencyCode)
       return format.format(amount)
   }
   ```

5. JWT stored in EncryptedSharedPreferences — NEVER plain SharedPreferences

6. All network calls on Dispatchers.IO, UI updates on Dispatchers.Main

7. Image compression before upload (receipts): max 1920px width, 80% quality

8. Strings in res/values/strings.xml — no hardcoded strings in Compose

## Navigation Routes
```
auth/login              → Login screen
auth/signup             → Signup (creates company + admin)
employee/expenses       → Expense history list
employee/expenses/new   → Submit expense form
employee/expenses/{id}  → Expense detail + approval timeline
employee/ocr            → Camera capture → OCR result → auto-fill
manager/approvals       → Pending approval queue
manager/team            → Team expense overview
admin/users             → User management (create, assign roles)
admin/workflows         → Workflow configuration
admin/rules             → Conditional rule setup
```

## Naming Conventions
- Screens: PascalCase with `Screen` suffix (`SubmitExpenseScreen.kt`)
- ViewModels: PascalCase with `ViewModel` suffix (`ExpenseViewModel.kt`)
- Repositories: PascalCase with `Repository` suffix (`ExpenseRepository.kt`)
- UseCases: PascalCase with `UseCase` suffix (`SubmitExpenseUseCase.kt`)
- DTOs: PascalCase with `Dto` suffix (`ExpenseDto.kt`)
- Room DAOs: PascalCase with `Dao` suffix (`ExpenseDao.kt`)
- Compose components: PascalCase function names (`StatusBadge()`)
- Constants: UPPER_SNAKE_CASE in companion objects