package com.example.reimbursementmanagement.Main_Ui.compo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Maps an expense category string to a Material icon.
 */
fun categoryToIcon(category: String): ImageVector {
    return when (category.uppercase()) {
        "TRAVEL" -> Icons.Default.DirectionsCar
        "MEALS" -> Icons.Default.LocalCafe
        "INFRASTRUCTURE" -> Icons.Default.Cloud
        "HARDWARE" -> Icons.Default.LaptopMac
        "SUPPLIES" -> Icons.Default.PointOfSale
        "SOFTWARE" -> Icons.Default.Code
        "OTHER" -> Icons.Default.Receipt
        else -> Icons.Default.Receipt
    }
}

@Composable
fun ActivityItem(
    icon: ImageVector,
    title: String,
    date: String,
    category: String,
    amount: String,
    status: String, // "APPROVED" or "PENDING"
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.CardRadius))
            .background(AppColors.CardBackground)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(AppDimens.IconRadius))
                .background(AppColors.IconSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$date • $category",
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
        }

        // Amount & Status
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = amount,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status Pill
            val isApproved = status == "APPROVED"
            val pillBg = if (isApproved) AppColors.PillApprovedBg else AppColors.PillPendingBg
            val pillText = if (isApproved) AppColors.PillApprovedText else AppColors.PillPendingText
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(pillBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status,
                    color = pillText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun DateSectionHeader(dateString: String) {
    Text(
        text = dateString.uppercase(),
        color = AppColors.TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
    )
}

@Composable
fun HistoryItemCard(
    icon: ImageVector,
    title: String,
    category: String,
    amount: String,
    transactionId: String,
    status: String, // "APPROVED", "PENDING", "REJECTED"
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.CardRadius))
            .background(AppColors.CardBackground)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(AppDimens.IconRadius))
                .background(AppColors.IconSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Category
        Column(
            modifier = Modifier.weight(1.5f)
        ) {
            Text(
                text = title,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category,
                color = AppColors.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
        }

        // Amount, Status Pill, and ID
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            val pillBg: androidx.compose.ui.graphics.Color
            val pillText: androidx.compose.ui.graphics.Color
            when (status) {
                "APPROVED" -> {
                    pillBg = AppColors.PillApprovedBg
                    pillText = AppColors.PillApprovedText
                }
                "REJECTED" -> {
                    pillBg = AppColors.PillRejectedBg
                    pillText = AppColors.PillRejectedText
                }
                else -> { // PENDING
                    pillBg = AppColors.PillPendingBg
                    pillText = AppColors.PillPendingText
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(pillBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = status,
                        color = pillText,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = amount,
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = transactionId,
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}
