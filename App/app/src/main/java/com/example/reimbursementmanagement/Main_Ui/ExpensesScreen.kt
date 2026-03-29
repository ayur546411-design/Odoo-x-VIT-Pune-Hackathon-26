package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun ExpensesScreen(
    onNavigate: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit // now passes expense ID
) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val userEmail = AuthManager.getCurrentUserEmail(context) ?: ""

    // Load expenses from MongoDB
    LaunchedEffect(Unit) {
        delay(150)
        visible = true

        scope.launch {
            try {
                expenses = MongoRepository.getExpenses(userEmail)
            } catch (e: Exception) {
                expenses = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    // Group expenses by date
    val groupedExpenses = expenses.groupBy { it.date }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        bottomBar = {
            CustomBottomNavigationBar(
                currentRoute = "expenses",
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

            // Header Section
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { 50 }
            ) {
                Column(modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding)) {
                    Text(
                        text = "LEDGER ARCHIVE",
                        color = AppColors.TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Expense History",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Comprehensive record of all reconciled and pending transactions across your corporate accounts.",
                        fontSize = 14.sp,
                        color = AppColors.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Filter Card Area
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 100)) + slideInVertically(tween(600, delayMillis = 100)) { 100 }
            ) {
                Box(modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding)) {
                    FilterCard()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.PrimaryNavy)
                }
            } else if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses found.\nSubmit your first expense to see it here!",
                        color = AppColors.TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            } else {
                // Dynamic expense groups
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(700, delayMillis = 200)) + slideInVertically(tween(700, delayMillis = 200)) { 100 }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        groupedExpenses.forEach { (date, expensesInGroup) ->
                            DateSectionHeader(formatDateDisplay(date))

                            expensesInGroup.forEach { expense ->
                                val expId = expense.id.ifBlank { expense.createdAt.toString() }
                                HistoryItemCard(
                                    icon = categoryToIcon(expense.category),
                                    title = expense.title,
                                    category = expense.category,
                                    amount = "$${String.format("%,.2f", expense.amount)}",
                                    transactionId = "ID: #${expId.takeLast(8).uppercase()}",
                                    status = expense.status,
                                    onClick = { onItemClick(expense.id) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
