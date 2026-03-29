package com.example.reimbursementmanagement.Main_Ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        topBar = {
            TopAppBar(
                title = { Text("Help & Support", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Find answers to frequently asked questions or contact our support team for assistance.",
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // FAQs
            Text(
                text = "Frequently Asked Questions",
                color = AppColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FaqItem(
                question = "How do I submit a new expense?",
                answer = "Navigate to the Home or Expenses screen and tap the floating '+' button or the Scan icon in the bottom navigation. A form will appear for you to enter details and upload receipts."
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            FaqItem(
                question = "How long does reimbursement usually take?",
                answer = "Once an expense is approved, the reimbursement takes typically 3-5 business days to reflect in your registered bank account depending on your region and bank policies."
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            FaqItem(
                question = "What happens if my expense is rejected?",
                answer = "If your expense is rejected, you will receive a notification with the manager's comments. You can review the rejection reason in the Expense Details screen, correct the issue, and resubmit if allowed."
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            FaqItem(
                question = "Can I change my registered email address?",
                answer = "For security reasons, the registered email address cannot be changed from the app. Please contact your IT administrator or support if a change is absolutely necessary."
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Contact Support
            Text(
                text = "Still need help?",
                color = AppColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Opening support email...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryNavy)
            ) {
                Text("Contact Support Team", color = AppColors.Background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(AppDimens.CardRadius))
            .background(color = AppColors.CardBackground, shape = RoundedCornerShape(AppDimens.CardRadius))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                color = AppColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle",
                tint = AppColors.TextTertiary,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        
        AnimatedVisibility(visible = expanded) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = AppColors.Divider)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = answer,
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
