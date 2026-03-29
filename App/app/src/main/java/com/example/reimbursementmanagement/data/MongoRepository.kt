package com.example.reimbursementmanagement.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * MongoDB Atlas Data API repository.
 * Uses REST endpoints to CRUD expense documents.
 *
 * ⚠️ SETUP REQUIRED:
 * 1. Go to MongoDB Atlas → App Services → Create Application
 * 2. Enable Data API
 * 3. Copy the App ID and generate an API Key
 * 4. Replace the placeholders below
 */
object MongoRepository {

    // ⚠️ PLACEHOLDERS — Replace with your MongoDB Atlas Data API credentials
    private const val DATA_API_URL = "https://data.mongodb-api.com/app/YOUR_APP_ID/endpoint/data/v1"
    private const val API_KEY = "YOUR_MONGODB_API_KEY_HERE"
    private const val DATA_SOURCE = "ReimbursementManagement" // Your cluster name
    private const val DATABASE = "reimbursement_db"
    private const val COLLECTION = "expenses"

    private fun buildBaseBody(): JSONObject {
        return JSONObject().apply {
            put("dataSource", DATA_SOURCE)
            put("database", DATABASE)
            put("collection", COLLECTION)
        }
    }

    private suspend fun postRequest(action: String, body: JSONObject): String = withContext(Dispatchers.IO) {
        val url = URL("$DATA_API_URL/action/$action")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/ejson")
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("api-key", API_KEY)
        connection.doOutput = true
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        try {
            connection.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("MongoRepo", "$action success: $response")
                response
            } else {
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                Log.e("MongoRepo", "$action error $responseCode: $error")
                throw Exception("MongoDB $action failed ($responseCode): $error")
            }
        } finally {
            connection.disconnect()
        }
    }

    // ─── INSERT ─────────────────────────────────────────────────

    suspend fun insertExpense(expense: Expense): String {
        val document = JSONObject().apply {
            put("employeeName", expense.employeeName)
            put("employeeEmail", expense.employeeEmail)
            put("title", expense.title)
            put("amount", expense.amount)
            put("currency", expense.currency)
            put("category", expense.category)
            put("date", expense.date)
            put("description", expense.description)
            put("imageUrl", expense.imageUrl)
            put("status", expense.status)
            put("createdAt", expense.createdAt)
        }

        val body = buildBaseBody().apply {
            put("document", document)
        }

        val response = postRequest("insertOne", body)
        val insertedId = JSONObject(response).optJSONObject("insertedId")?.optString("\$oid", "")
            ?: JSONObject(response).optString("insertedId", "")
        return insertedId
    }

    // ─── FIND ALL BY EMAIL ──────────────────────────────────────

    suspend fun getExpenses(employeeEmail: String): List<Expense> {
        val body = buildBaseBody().apply {
            put("filter", JSONObject().put("employeeEmail", employeeEmail))
            put("sort", JSONObject().put("createdAt", -1))
        }

        return try {
            val response = postRequest("find", body)
            val documents = JSONObject(response).optJSONArray("documents") ?: JSONArray()
            parseExpenseList(documents)
        } catch (e: Exception) {
            Log.e("MongoRepo", "getExpenses failed", e)
            emptyList()
        }
    }

    // ─── FIND ONE BY ID ─────────────────────────────────────────

    suspend fun getExpenseById(id: String): Expense? {
        val body = buildBaseBody().apply {
            put("filter", JSONObject().put("_id", JSONObject().put("\$oid", id)))
        }

        return try {
            val response = postRequest("findOne", body)
            val document = JSONObject(response).optJSONObject("document") ?: return null
            parseExpense(document)
        } catch (e: Exception) {
            Log.e("MongoRepo", "getExpenseById failed", e)
            null
        }
    }

    // ─── PARSING ────────────────────────────────────────────────

    private fun parseExpenseList(documents: JSONArray): List<Expense> {
        val list = mutableListOf<Expense>()
        for (i in 0 until documents.length()) {
            list.add(parseExpense(documents.getJSONObject(i)))
        }
        return list
    }

    private fun parseExpense(doc: JSONObject): Expense {
        val id = doc.optJSONObject("_id")?.optString("\$oid", "")
            ?: doc.optString("_id", "")

        return Expense(
            id = id,
            employeeName = doc.optString("employeeName", ""),
            employeeEmail = doc.optString("employeeEmail", ""),
            title = doc.optString("title", ""),
            amount = doc.optDouble("amount", 0.0),
            currency = doc.optString("currency", "USD"),
            category = doc.optString("category", ""),
            date = doc.optString("date", ""),
            description = doc.optString("description", ""),
            imageUrl = doc.optString("imageUrl", ""),
            status = doc.optString("status", "PENDING"),
            createdAt = doc.optLong("createdAt", 0L)
        )
    }
}
