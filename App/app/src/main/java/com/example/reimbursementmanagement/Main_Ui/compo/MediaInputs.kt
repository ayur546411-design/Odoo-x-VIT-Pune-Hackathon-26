package com.example.reimbursementmanagement.Main_Ui.compo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton

@Composable
fun ReceiptUploaderSection(
    selectedBitmap: ImageBitmap?,
    onScanClick: () -> Unit,
    onUploadClick: () -> Unit,
    onClearImage: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader("RECEIPT & DOCUMENTATION")
        
        Spacer(modifier = Modifier.height(8.dp))

        // Dotted Container
        val stroke = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppColors.Divider,
                    shape = RoundedCornerShape(AppDimens.CardRadius)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Receipt Box
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD3D8E0)),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedBitmap != null) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                bitmap = selectedBitmap,
                                contentDescription = "Selected Receipt",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                            // Clear Button Overlay
                            IconButton(
                                onClick = onClearImage,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), shape = androidx.compose.foundation.shape.CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Image",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = "Receipt",
                                tint = AppColors.PrimaryNavy,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "NO FILE CHOSEN",
                                color = AppColors.PrimaryNavy,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MediaActionButton(
                        icon = Icons.Default.CameraAlt,
                        text = "Scan",
                        onClick = onScanClick
                    )
                    MediaActionButton(
                        icon = Icons.Default.FileUpload,
                        text = "Upload",
                        onClick = onUploadClick
                    )
                }
            }
        }
    }
}

@Composable
fun MediaActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.CardBackground)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = AppColors.PrimaryNavy,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = AppColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
