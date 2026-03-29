package com.example.reimbursementmanagement.data

/**
 * Data class representing an expense document stored in MongoDB.
 */
data class Expense(
    val id: String = "",                // MongoDB _id (ObjectId string)
    val employeeName: String = "",
    val employeeEmail: String = "",
    val title: String = "",             // e.g. "Uber", "AWS"
    val amount: Double = 0.0,
    val currency: String = "USD",
    val category: String = "",          // "TRAVEL", "MEALS", "INFRASTRUCTURE", etc.
    val date: String = "",              // "2024-10-24"
    val description: String = "",
    val imageUrl: String = "",          // Cloudinary URL
    val status: String = "PENDING",     // "PENDING", "APPROVED", "REJECTED"
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Data extracted from a bill image by Gemini API.
 */
data class ExtractedBillData(
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = ""
)
