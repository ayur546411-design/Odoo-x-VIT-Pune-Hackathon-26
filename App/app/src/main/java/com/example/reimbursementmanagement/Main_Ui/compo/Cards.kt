package com.example.reimbursementmanagement.Main_Ui.compo

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SummaryCard(
    title: String,
    targetValue: Float,
    isCurrency: Boolean,
    statusText: String,
    statusIconType: Int, // 0 = none, 1 = trendUp, 2 = dot, 3 = check
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    var isLaunched by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(100)
        isLaunched = true
    }

    val animatedValue by animateFloatAsState(
        targetValue = if (isLaunched) targetValue else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "ValueAnimation"
    )

    val formattedValue = if (isCurrency) {
        String.format("$%,.2f", animatedValue)
    } else {
        animatedValue.toInt().toString()
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground),
        shape = RoundedCornerShape(AppDimens.CardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title.uppercase(),
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formattedValue,
                color = AppColors.TextPrimary,
                fontSize = if (isLarge) 36.sp else 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                when (statusIconType) {
                    1 -> Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Trend up",
                        tint = AppColors.TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    2 -> Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AppColors.PrimaryNavyLight)
                    )
                    3 -> Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Check",
                        tint = AppColors.PrimaryNavyLight,
                        modifier = Modifier.size(14.dp)
                    )
                }
                
                if (statusIconType != 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Text(
                    text = statusText,
                    color = AppColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun FilterCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground),
        shape = RoundedCornerShape(AppDimens.CardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Search Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.IconSurface)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AppColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search merchants...",
                    color = AppColors.TextTertiary,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dropdowns row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Dropdown 1
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.IconSurface)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("All Statuses", fontSize = 14.sp, color = AppColors.TextPrimary)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AppColors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Dropdown 2
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.IconSurface)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Last 30 Days", fontSize = 14.sp, color = AppColors.TextPrimary)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AppColors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Export CSV Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.PrimaryNavyLight)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Export CSV",
                    color = AppColors.CardBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
