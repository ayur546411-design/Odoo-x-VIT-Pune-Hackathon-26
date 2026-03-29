package com.example.reimbursementmanagement.Main_Ui

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.*
import com.example.reimbursementmanagement.auth.AuthManager
import com.example.reimbursementmanagement.data.Expense
import com.example.reimbursementmanagement.data.GeminiApi
import com.example.reimbursementmanagement.data.MongoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NewExpenseScreen(
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    // Form fields
    var amountText by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    // Media states
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }

    // Loading states
    var isScanning by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Resolve chosen gallery URI to ImageBitmap + bytes
    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            withContext(Dispatchers.IO) {
                try {
                    val stream = context.contentResolver.openInputStream(imageUri!!)
                    val bytes = stream?.readBytes()
                    stream?.close()
                    if (bytes != null) {
                        val bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        withContext(Dispatchers.Main) {
                            imageBitmap = bm?.asImageBitmap()
                            imageBytes = bytes
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // When we have image bytes, trigger Gemini scan
    LaunchedEffect(imageBytes) {
        val bytes = imageBytes ?: return@LaunchedEffect
        if (bytes.isEmpty()) return@LaunchedEffect
        
        isScanning = true
        try {
            val data = GeminiApi.extractBillData(bytes)
            titleText = data.title
            amountText = if (data.amount > 0) String.format("%.2f", data.amount) else ""
            categoryText = data.category
            dateText = data.date
        } catch (e: Exception) {
            Toast.makeText(context, "Gemini scan failed: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isScanning = false
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imageBitmap = bitmap.asImageBitmap()
            imageUri = null
            imageBytes = GeminiApi.bitmapToBytes(bitmap)
        }
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        bottomBar = {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 400)) + slideInVertically(tween(500, delayMillis = 400)) { it / 2 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.Background)
                        .navigationBarsPadding()
                        .padding(horizontal = AppDimens.ScreenPadding, vertical = 24.dp)
                ) {
                    PrimaryActionButton(
                        text = when {
                            isSubmitting -> "Submitting..."
                            isScanning -> "Scanning bill..."
                            else -> "Submit Expense"
                        },
                        onClick = {
                            if (isSubmitting || isScanning) return@PrimaryActionButton

                            // Validate
                            if (amountText.isBlank()) {
                                Toast.makeText(context, "Amount is required", Toast.LENGTH_SHORT).show()
                                return@PrimaryActionButton
                            }

                            isSubmitting = true
                            scope.launch {
                                try {
                                    // Upload image to Cloudinary
                                    var uploadedUrl = ""
                                    val bytesToUpload = imageBytes ?: if (imageBitmap != null) {
                                        withContext(Dispatchers.Default) {
                                            val byteStream = java.io.ByteArrayOutputStream()
                                            imageBitmap!!.asAndroidBitmap().compress(
                                                android.graphics.Bitmap.CompressFormat.JPEG, 90, byteStream
                                            )
                                            byteStream.toByteArray()
                                        }
                                    } else null

                                    if (bytesToUpload != null) {
                                        try {
                                            withContext(Dispatchers.IO) {
                                                val appInfo = context.packageManager.getApplicationInfo(
                                                    context.packageName,
                                                    android.content.pm.PackageManager.GET_META_DATA
                                                )
                                                val url = appInfo.metaData.getString("CLOUDINARY_URL")
                                                if (!url.isNullOrEmpty()) {
                                                    val cloudinary = com.cloudinary.Cloudinary(url)
                                                    val result = cloudinary.uploader().upload(
                                                        bytesToUpload,
                                                        mapOf("resource_type" to "auto")
                                                    )
                                                    uploadedUrl = result["secure_url"] as? String ?: ""
                                                }
                                            }
                                        } catch (e: Exception) {
                                            // Cloudinary upload failed — continue without image URL
                                            uploadedUrl = ""
                                        }
                                    }

                                    // Build expense document
                                    val userName = AuthManager.getCurrentUserName(context) ?: "Unknown"
                                    val userEmail = AuthManager.getCurrentUserEmail(context) ?: "unknown@email.com"

                                    val expense = Expense(
                                        employeeName = userName,
                                        employeeEmail = userEmail,
                                        title = titleText.ifBlank { "Expense" },
                                        amount = amountText.toDoubleOrNull() ?: 0.0,
                                        currency = "USD",
                                        category = categoryText.ifBlank { "OTHER" },
                                        date = dateText.ifBlank {
                                            java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                                                .format(java.util.Date())
                                        },
                                        description = descriptionText,
                                        imageUrl = uploadedUrl,
                                        status = "PENDING",
                                        createdAt = System.currentTimeMillis()
                                    )

                                    // Save to MongoDB
                                    MongoRepository.insertExpense(expense)

                                    isSubmitting = false
                                    Toast.makeText(context, "Expense submitted!", Toast.LENGTH_SHORT).show()
                                    onSubmitSuccess()
                                } catch (e: Exception) {
                                    isSubmitting = false
                                    Toast.makeText(context, "Submit failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            DetailTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Header Section
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { 50 }
            ) {
                Column(modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding)) {
                    Text(
                        text = "New Expense",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Upload your bill — Gemini AI will extract the\ndetails automatically.",
                        fontSize = 14.sp,
                        color = AppColors.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 100)) + slideInVertically(tween(500, delayMillis = 100)) { 50 }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = AppDimens.ScreenPadding),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ReceiptUploaderSection(
                        selectedBitmap = imageBitmap,
                        onScanClick = { takePictureLauncher.launch(null) },
                        onUploadClick = {
                            pickMediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onClearImage = {
                            imageBitmap = null
                            imageUri = null
                            imageBytes = null
                            titleText = ""
                            amountText = ""
                            categoryText = ""
                            dateText = ""
                        }
                    )

                    // Scanning indicator
                    if (isScanning) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AppColors.PrimaryNavy,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Gemini AI is analyzing your bill...",
                                color = AppColors.TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Title (auto-filled by Gemini)
                    if (titleText.isNotBlank()) {
                        Column {
                            SectionHeader("MERCHANT / TITLE")
                            InputContainer {
                                Text(
                                    text = titleText,
                                    color = AppColors.TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    AmountInput(
                        currency = "USD",
                        amount = amountText,
                        onAmountChange = { amountText = it }
                    )

                    DropdownInput(
                        title = "CATEGORY",
                        hint = categoryText.ifBlank { "Select Category" },
                        onClick = { /* Category will be auto-filled by Gemini */ }
                    )

                    DateInput(
                        date = dateText.ifBlank { "Select Date" },
                        onClick = { /* Date will be auto-filled by Gemini */ }
                    )

                    TextAreaInput(
                        value = descriptionText,
                        onValueChange = { descriptionText = it }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
