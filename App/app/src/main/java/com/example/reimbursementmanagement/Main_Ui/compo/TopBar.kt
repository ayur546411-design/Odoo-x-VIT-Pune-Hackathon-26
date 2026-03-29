package com.example.reimbursementmanagement.Main_Ui.compo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.Background)
            .padding(horizontal = AppDimens.ScreenPadding, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryNavy),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = AppColors.Background,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Executive Ledger",
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = AppColors.TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun DetailTopBar(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.Background)
            .padding(horizontal = AppDimens.ScreenPadding, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = AppColors.TextPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Executive Ledger",
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryNavy),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = AppColors.Background,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
