package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.*
import com.example.reimbursementmanagement.auth.AuthManager
import com.example.reimbursementmanagement.data.Expense
import com.example.reimbursementmanagement.data.MongoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val userName = AuthManager.getCurrentUserName(context) ?: "User"
    val userEmail = AuthManager.getCurrentUserEmail(context) ?: ""

    // Determine greeting based on time of day
    val greeting = remember {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    // Current month name
    val currentMonth = remember {
        java.text.SimpleDateFormat("MMMM", java.util.Locale.US)
            .format(java.util.Date())
    }

    // Load expenses from MongoDB
    LaunchedEffect(Unit) {
        delay(150)
        visible = true

        scope.launch {
            try {
                expenses = MongoRepository.getExpenses(userEmail)
            } catch (e: Exception) {
                // If MongoDB fails, show empty state
                expenses = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    // Calculate summary values from real data
    val totalExpenses = expenses.sumOf { it.amount }
    val pendingCount = expenses.count { it.status == "PENDING" }
    val approvedAmount = expenses.filter { it.status == "APPROVED" }.sumOf { it.amount }
    val recentExpenses = expenses.take(4)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        bottomBar = {
            CustomBottomNavigationBar(
                currentRoute = "home",
                onNavigate = onNavigate,
                onScanClick = onNavigateToScan
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            DashboardTopBar()

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome Section
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { 50 }
            ) {
                Column(modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding)) {
                    Text(
                        text = "$greeting,\n$userName",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Here is your expense overview for $currentMonth.",
                        fontSize = 14.sp,
                        color = AppColors.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Summary Cards
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 100)) + slideInVertically(tween(600, delayMillis = 100)) { 100 }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SummaryCard(
                        title = "Total Expenses",
                        targetValue = totalExpenses.toFloat(),
                        isCurrency = true,
                        statusText = "${expenses.size} transactions",
                        statusIconType = 1,
                        isLarge = true
                    )

                    SummaryCard(
                        title = "Pending Approvals",
                        targetValue = pendingCount.toFloat(),
                        isCurrency = false,
                        statusText = "Awaiting manager review",
                        statusIconType = 2
                    )

                    SummaryCard(
                        title = "Approved Amount",
                        targetValue = approvedAmount.toFloat(),
                        isCurrency = true,
                        statusText = "Processed for next payroll",
                        statusIconType = 3
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Recent Activity Header
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(700, delayMillis = 200)) + slideInVertically(tween(700, delayMillis = 200)) { 100 }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.ScreenPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent\nActivity",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary,
                        lineHeight = 24.sp
                    )
                    
                    SecondaryActionButton(
                        text = "Submit Expense",
                        onClick = onNavigateToScan
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Activity List
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800, delayMillis = 300)) + slideInVertically(tween(800, delayMillis = 300)) { 100 }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AppColors.PrimaryNavy)
                        }
                    } else if (recentExpenses.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No expenses yet.\nTap \"Submit Expense\" to get started!",
                                color = AppColors.TextSecondary,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    } else {
                        recentExpenses.forEach { expense ->
                            ActivityItem(
                                icon = categoryToIcon(expense.category),
                                title = expense.title,
                                date = formatDateDisplay(expense.date),
                                category = expense.category,
                                amount = "$${String.format("%,.2f", expense.amount)}",
                                status = expense.status,
                                onClick = { onNavigate("ExpenseDetail:${expense.id}") }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

/**
 * Format a date from "2024-10-24" to "OCT 24, 2024".
 */
fun formatDateDisplay(date: String): String {
    return try {
        val input = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val output = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
        val parsed = input.parse(date) ?: return date
        output.format(parsed).uppercase()
    } catch (e: Exception) {
        date
    }
}
