package com.example.reimbursementmanagement.Main_Ui.compo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomBottomNavigationBar(
    currentRoute: String = "home",
    onNavigate: (String) -> Unit,
    onScanClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Main White Background Bar extending behind system navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(top = 32.dp) // Provide space for floating button
                .shadow(elevation = 16.dp, spotColor = AppColors.PrimaryNavy.copy(alpha = 0.1f))
                .background(AppColors.CardBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // Safely pads content above gesture nav
                    .height(72.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "HOME",
                    isSelected = currentRoute == "home",
                    onClick = { onNavigate("home") }
                )
                BottomNavItem(
                    icon = Icons.Default.List,
                    label = "EXPENSES",
                    isSelected = currentRoute == "expenses",
                    onClick = { onNavigate("expenses") }
                )

                // Spacing for the floating button
                Spacer(modifier = Modifier.width(72.dp))

                BottomNavItem(
                    icon = Icons.Default.Notifications,
                    label = "ALERTS",
                    isSelected = currentRoute == "alerts",
                    onClick = { onNavigate("alerts") }
                )
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "PROFILE",
                    isSelected = currentRoute == "profile",
                    onClick = { onNavigate("profile") }
                )
            }
        }

        // Floating Center Scan Button
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 12.dp) // Perfectly overlap the white bar's top edge
                .clickable(
                    indication = null, 
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                ) { onScanClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryNavy),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Scan",
                    tint = AppColors.CardBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "SCAN",
                color = AppColors.TextPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) AppColors.TextPrimary else AppColors.TextSecondary
    
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(12.dp)
            .width(56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}
