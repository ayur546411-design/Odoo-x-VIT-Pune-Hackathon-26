package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reimbursementmanagement.Main_Ui.compo.AppColors
import com.example.reimbursementmanagement.Main_Ui.compo.AppDimens
import com.example.reimbursementmanagement.Main_Ui.compo.categoryToIcon
import com.example.reimbursementmanagement.data.Expense
import com.example.reimbursementmanagement.data.MongoRepository
import kotlinx.coroutines.launch

@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var expense by remember { mutableStateOf<Expense?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Load expense from MongoDB
    LaunchedEffect(expenseId) {
        if (expenseId.isNotBlank()) {
            scope.launch {
                try {
                    expense = MongoRepository.getExpenseById(expenseId)
                } catch (e: Exception) {
                    expense = null
                } finally {
                    isLoading = false
                }
            }
        } else {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            DetailHeaderTopBar(onBackClick)

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.PrimaryNavy)
                }
            } else if (expense == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Expense not found.", color = AppColors.TextSecondary, fontSize = 16.sp)
                }
            } else {
                val exp = expense!!
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding)) {
                    // Status Pill
                    val (pillBg, pillTextColor) = when (exp.status) {
                        "APPROVED" -> AppColors.PillApprovedBg to AppColors.PillApprovedText
                        "REJECTED" -> AppColors.PillRejectedBg to AppColors.PillRejectedText
                        else -> AppColors.PillPendingBg to AppColors.PillPendingText
                    }
                    val statusLabel = when (exp.status) {
                        "APPROVED" -> "Approved"
                        "REJECTED" -> "Rejected"
                        else -> "Pending Manager Review"
                    }

                    Row(
                        modifier = Modifier
                            .background(color = pillBg, shape = RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = statusLabel,
                            color = pillTextColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Title
                    Text(
                        text = exp.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.PrimaryNavy
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Date & Category
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Date",
                            modifier = Modifier.size(12.dp),
                            tint = AppColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${formatDateDisplay(exp.date)} • ${exp.category}",
                            fontSize = 12.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Amount
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${String.format("%,.2f", exp.amount)}",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AppColors.PrimaryNavy
                        )
                        Text(
                            text = "TOTAL ${exp.currency}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextSecondary,
                            letterSpacing = 1.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Receipt Image
                    if (exp.imageUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AppColors.TealBoard),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(exp.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Receipt",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        // Placeholder receipt board
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppColors.TealBoard),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .rotate(-5f)
                                    .width(180.dp)
                                    .height(140.dp)
                                    .shadow(16.dp)
                                    .background(Color.White)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        categoryToIcon(exp.category), 
                                        contentDescription = "Receipt", 
                                        modifier = Modifier.size(32.dp), 
                                        tint = Color.LightGray
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(exp.title.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                                    Spacer(Modifier.height(4.dp))
                                    Divider(color = Color.LightGray)
                                    Spacer(Modifier.height(4.dp))
                                    Text("Total: $${String.format("%.2f", exp.amount)}", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Description
                    if (exp.description.isNotBlank()) {
                        Text(
                            text = "Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(AppColors.NoteBackground)
                                .padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(AppColors.PillPendingBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Description, contentDescription = "Desc", modifier = Modifier.size(16.dp), tint = AppColors.PillPendingText)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = exp.description,
                                fontSize = 13.sp,
                                color = AppColors.TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Employee Info
                    Text(
                        text = "Submitted By",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AppColors.NoteBackground)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AppColors.PrimaryNavy, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = exp.employeeName.firstOrNull()?.uppercase() ?: "?",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = exp.employeeName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.TextPrimary
                            )
                            Text(
                                text = exp.employeeEmail,
                                fontSize = 12.sp,
                                color = AppColors.TextSecondary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Approval Timeline
                    Text(
                        text = "Approval Timeline",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ExpenseApprovalTimeline(status = exp.status, date = exp.date)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppColors.Divider)
                            .clickable { }
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp), tint = AppColors.PrimaryNavy)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Expense Details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppColors.PrimaryNavy)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, AppColors.Divider, RoundedCornerShape(8.dp))
                            .clickable { }
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(16.dp), tint = AppColors.TextPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download Receipt PDF", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Submission timestamp
                    val submittedAt = try {
                        val sdf = java.text.SimpleDateFormat("MMM dd, hh:mm a", java.util.Locale.US)
                        sdf.format(java.util.Date(exp.createdAt)).uppercase()
                    } catch (_: Exception) { "" }

                    if (submittedAt.isNotBlank()) {
                        Text(
                            text = "SUBMITTED: $submittedAt",
                            fontSize = 9.sp,
                            color = AppColors.TextTertiary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }
        }
    }
}

@Composable
fun DetailHeaderTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.ScreenPadding, vertical = 24.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.Divider)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Expense Detail",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryNavy
            )
        }
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = AppColors.PrimaryNavy
        )
    }
}

@Composable
fun ExpenseApprovalTimeline(status: String, date: String) {
    Column {
        TimelineItem(
            isFirst = true,
            isLast = false,
            isCompleted = true,
            title = "Submitted",
            subtitle = "${formatDateDisplay(date)}",
            icon = Icons.Default.Check
        )
        TimelineItem(
            isFirst = false,
            isLast = false,
            isCompleted = status == "APPROVED" || status == "REJECTED",
            isInProgress = status == "PENDING",
            title = "Manager Review",
            subtitle = if (status == "PENDING") "IN PROGRESS" else if (status == "APPROVED") "Approved" else "Rejected",
            icon = Icons.Default.Circle
        )
        TimelineItem(
            isFirst = false,
            isLast = false,
            isCompleted = status == "APPROVED",
            title = "Finance Approval",
            subtitle = if (status == "APPROVED") "Completed" else "Pending",
            icon = Icons.Default.AccountBalance
        )
        TimelineItem(
            isFirst = false,
            isLast = true,
            isCompleted = status == "APPROVED",
            title = "Reimbursement",
            subtitle = if (status == "APPROVED") "Processed" else "Pending",
            icon = Icons.Default.Gavel
        )
    }
}

@Composable
fun TimelineItem(
    isFirst: Boolean,
    isLast: Boolean,
    isCompleted: Boolean,
    isInProgress: Boolean = false,
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            if (!isFirst) {
                Box(modifier = Modifier.width(2.dp).weight(1f).background(AppColors.Divider))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            val iconBgColor = when {
                isCompleted -> AppColors.PrimaryNavyLight
                isInProgress -> AppColors.PillPendingBg
                else -> AppColors.IconSurface
            }
            
            val iconTintColor = when {
                isCompleted -> Color.White
                isInProgress -> AppColors.PillPendingText
                else -> AppColors.TextTertiary
            }
            
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isInProgress) {
                    Box(modifier = Modifier.size(10.dp).background(iconTintColor, CircleShape))
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(14.dp),
                        tint = iconTintColor
                    )
                }
            }
            
            if (!isLast) {
                Box(modifier = Modifier.width(2.dp).weight(1f).background(AppColors.Divider))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.padding(bottom = 32.dp, top = if (isFirst) 24.dp else 4.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted || isInProgress) AppColors.TextPrimary else AppColors.TextTertiary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = if (isInProgress) AppColors.SlatePrimary else AppColors.TextTertiary,
                lineHeight = 16.sp,
                fontWeight = if (isInProgress && subtitle.contains("IN PROGRESS")) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
