package com.example.reimbursementmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.reimbursementmanagement.Main_Ui.*
import com.example.reimbursementmanagement.auth.AuthManager
import com.example.reimbursementmanagement.ui.theme.ReimbursementManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContent {
            ReimbursementManagementTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ExecutiveLedgerApp()
                }
            }
        }
    }
}

@Composable
fun ExecutiveLedgerApp() {
    val context = LocalContext.current

    // Check if user is already logged in (session persistence)
    val initialScreen = if (AuthManager.isLoggedIn(context)) "Home" else "Login"
    var currentScreen by remember { mutableStateOf(initialScreen) }

    // State to pass email between forgot-password flow screens
    var forgotPasswordEmail by remember { mutableStateOf("") }

    // State to pass expense ID to detail screen
    var selectedExpenseId by remember { mutableStateOf("") }

    Crossfade(
        targetState = currentScreen,
        animationSpec = tween(500),
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            "Login" -> LoginScreen(
                onLoginSuccess = { currentScreen = "Home" },
                onNavigateToSignup = { currentScreen = "Signup" },
                onNavigateToForgot = { currentScreen = "ForgotPasswordEmail" }
            )
            "Signup" -> SignupScreen(
                onNavigateToLogin = { currentScreen = "Login" },
                onSignupSuccess = { currentScreen = "Home" }
            )
            "ForgotPasswordEmail" -> ForgotPasswordEmailScreen(
                onOtpSent = { email ->
                    forgotPasswordEmail = email
                    currentScreen = "PasswordRecovery"
                },
                onBack = { currentScreen = "Login" }
            )
            "PasswordRecovery" -> PasswordRecoveryScreen(
                emailAddress = forgotPasswordEmail,
                onVerifySuccess = { email ->
                    forgotPasswordEmail = email
                    currentScreen = "ResetPassword"
                },
                onBack = { currentScreen = "ForgotPasswordEmail" }
            )
            "ResetPassword" -> ResetPasswordScreen(
                email = forgotPasswordEmail,
                onResetSuccess = { currentScreen = "Login" },
                onBack = { currentScreen = "PasswordRecovery" }
            )
            "Home" -> HomeScreen(
                onNavigate = { route ->
                    when {
                        route == "expenses" -> currentScreen = "Expenses"
                        route == "profile" -> currentScreen = "Profile"
                        route.startsWith("ExpenseDetail:") -> {
                            selectedExpenseId = route.removePrefix("ExpenseDetail:")
                            currentScreen = "ExpenseDetail"
                        }
                    }
                },
                onNavigateToScan = { currentScreen = "NewExpense" }
            )
            "Expenses" -> ExpensesScreen(
                onNavigate = { route ->
                    if (route == "home") currentScreen = "Home"
                    if (route == "profile") currentScreen = "Profile"
                },
                onNavigateToScan = { currentScreen = "NewExpense" },
                onItemClick = { expenseId ->
                    selectedExpenseId = expenseId
                    currentScreen = "ExpenseDetail"
                }
            )
            "NewExpense" -> NewExpenseScreen(
                onBackClick = { currentScreen = "Home" },
                onSubmitSuccess = { currentScreen = "Expenses" }
            )
            "ExpenseDetail" -> ExpenseDetailScreen(
                expenseId = selectedExpenseId,
                onBackClick = { currentScreen = "Expenses" }
            )
            "Profile" -> ProfileScreen(
                onNavigate = { route ->
                    if (route == "home") currentScreen = "Home"
                    if (route == "expenses") currentScreen = "Expenses"
                },
                onNavigateToScan = { currentScreen = "NewExpense" },
                onNavigateToAccount = { currentScreen = "AccountDetails" },
                onNavigateToNotifications = { currentScreen = "Notifications" },
                onNavigateToSecurity = { currentScreen = "PrivacySecurity" },
                onNavigateToHelp = { currentScreen = "HelpSupport" },
                onLogOut = {
                    AuthManager.logout(context)
                    currentScreen = "Login"
                }
            )
            "AccountDetails" -> AccountDetailsScreen(
                onBack = { currentScreen = "Profile" }
            )
            "Notifications" -> NotificationsScreen(
                onBack = { currentScreen = "Profile" }
            )
            "PrivacySecurity" -> PrivacySecurityScreen(
                onBack = { currentScreen = "Profile" }
            )
            "HelpSupport" -> HelpSupportScreen(
                onBack = { currentScreen = "Profile" }
            )
        }
    }
}