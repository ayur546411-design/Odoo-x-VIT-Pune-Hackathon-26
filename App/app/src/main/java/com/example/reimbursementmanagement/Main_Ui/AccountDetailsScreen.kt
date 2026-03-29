package com.example.reimbursementmanagement.Main_Ui

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AppColors
import com.example.reimbursementmanagement.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    var name by remember { mutableStateOf(AuthManager.getCurrentUserName(context) ?: "") }
    var country by remember { mutableStateOf(AuthManager.getCurrentUserCountry(context) ?: "") }
    val email = AuthManager.getCurrentUserEmail(context) ?: ""

    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        topBar = {
            TopAppBar(
                title = { Text("Account Details", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
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

            // Info text
            Text(
                text = "Update your personal information. Your email address cannot be changed as it is used for login.",
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email (Read only)
            OutlinedTextField(
                value = email,
                onValueChange = { },
                label = { Text("Email Address") },
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = AppColors.TextSecondary,
                    disabledBorderColor = AppColors.InputBorder,
                    disabledLabelColor = AppColors.TextSecondary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryNavy,
                    unfocusedBorderColor = AppColors.InputBorder
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Country
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country / Region") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryNavy,
                    unfocusedBorderColor = AppColors.InputBorder
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Save Button
            Button(
                onClick = {
                    isLoading = true
                    val result = AuthManager.updateUserDetails(context, name, country)
                    isLoading = false

                    if (result.success) {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        onBack()
                    } else {
                        Toast.makeText(context, result.errorMessage ?: "Error", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryNavy)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = AppColors.Background, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", color = AppColors.Background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
