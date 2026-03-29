package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AppColors
import com.example.reimbursementmanagement.Main_Ui.compo.AppDimens
import com.example.reimbursementmanagement.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var prefs by remember { mutableStateOf(AuthManager.getNotificationPrefs(context)) }

    fun savePrefs(newPrefs: AuthManager.NotificationPrefs) {
        prefs = newPrefs
        AuthManager.updateNotificationPrefs(context, newPrefs)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Background,
                    titleContentColor = AppColors.TextPrimary,
                    navigationIconContentColor = AppColors.TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Manage how and when you want to receive alerts and statements from Financier.",
                color = AppColors.TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(AppDimens.CardRadius))
                    .background(color = AppColors.CardBackground, shape = RoundedCornerShape(AppDimens.CardRadius))
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                // Push Notifications
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Push Notifications",
                            color = AppColors.TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Alerts directly on your device when an expense is submitted or approved.",
                            color = AppColors.TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = prefs.pushEnabled,
                        onCheckedChange = { savePrefs(prefs.copy(pushEnabled = it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AppColors.CardBackground,
                            checkedTrackColor = AppColors.PrimaryNavy,
                        )
                    )
                }

                Divider(color = AppColors.Divider, modifier = Modifier.padding(vertical = 8.dp))

                // Email Alerts
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Email Alerts",
                            color = AppColors.TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Receive email confirmations for critical actions.",
                            color = AppColors.TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = prefs.emailEnabled,
                        onCheckedChange = { savePrefs(prefs.copy(emailEnabled = it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AppColors.CardBackground,
                            checkedTrackColor = AppColors.PrimaryNavy,
                        )
                    )
                }

                Divider(color = AppColors.Divider, modifier = Modifier.padding(vertical = 8.dp))

                // Monthly Statement
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Monthly Statements",
                            color = AppColors.TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A compiled monthly report of all reimbursements and ledger balances.",
                            color = AppColors.TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = prefs.monthlyEnabled,
                        onCheckedChange = { savePrefs(prefs.copy(monthlyEnabled = it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AppColors.CardBackground,
                            checkedTrackColor = AppColors.PrimaryNavy,
                        )
                    )
                }
            }
        }
    }
}
