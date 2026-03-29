package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.reimbursementmanagement.Main_Ui.compo.*
import com.example.reimbursementmanagement.auth.AuthManager
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }

    // Read real user data from AuthManager
    val userName = AuthManager.getCurrentUserName(context) ?: "User"
    val userEmail = AuthManager.getCurrentUserEmail(context) ?: "user@example.com"
    val initials = userName.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "U" }

    LaunchedEffect(Unit) {
        delay(150)
        visible = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        bottomBar = {
            CustomBottomNavigationBar(
                currentRoute = "profile",
                onNavigate = onNavigate,
                onScanClick = onNavigateToScan
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.ScreenPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile Settings",
                    color = AppColors.TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { 50 }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = AppDimens.ScreenPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(2.dp, CircleShape)
                            .background(AppColors.CardBackground, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .background(AppColors.PrimaryNavy, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                initials,
                                color = AppColors.CardBackground,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = userName,
                        color = AppColors.TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userEmail,
                        color = AppColors.TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .background(AppColors.PillApprovedBg, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Company Admin",
                            color = AppColors.PillApprovedText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 100)) + slideInVertically(tween(600, delayMillis = 100)) { 100 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.ScreenPadding)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(AppDimens.CardRadius))
                        .background(color = AppColors.CardBackground, shape = RoundedCornerShape(AppDimens.CardRadius))
                        .padding(vertical = 8.dp)
                ) {
                    ProfileSettingItem(
                        icon = Icons.Default.Person,
                        title = "Account Details",
                        subtitle = "Edit personal information",
                        onClick = onNavigateToAccount
                    )
                    Divider(color = AppColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileSettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Manage alerts & emails",
                        onClick = onNavigateToNotifications
                    )
                    Divider(color = AppColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileSettingItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & Security",
                        subtitle = "Passwords and encryption setup",
                        onClick = onNavigateToSecurity
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(700, delayMillis = 200)) + slideInVertically(tween(700, delayMillis = 200)) { 100 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.ScreenPadding)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(AppDimens.CardRadius))
                        .background(color = AppColors.CardBackground, shape = RoundedCornerShape(AppDimens.CardRadius))
                        .padding(vertical = 8.dp)
                ) {
                    ProfileSettingItem(
                        icon = Icons.Default.Info,
                        title = "Help & Support",
                        subtitle = "FAQs and contact",
                        onClick = onNavigateToHelp
                    )
                    Divider(color = AppColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileSettingItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Log Out",
                        subtitle = "Sign out from the application",
                        isDestructive = true,
                        onClick = {
                            AuthManager.logout(context)
                            onLogOut()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom nav overlap
        }
    }
}

@Composable
fun ProfileSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val tintColor = if (isDestructive) AppColors.PillRejectedText else AppColors.PrimaryNavy

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(if (isDestructive) AppColors.PillRejectedBg else AppColors.IconSurface, RoundedCornerShape(AppDimens.IconRadius)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (isDestructive) tintColor else AppColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = AppColors.TextSecondary,
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Forward",
            tint = AppColors.TextTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}
