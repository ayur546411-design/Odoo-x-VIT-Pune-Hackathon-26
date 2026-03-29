# Android UI Design Skill — ExpenseFlow

## Design System (Material 3 + Jetpack Compose)

### Color Palette (Theme.kt / Color.kt)
```kotlin
// Primary brand colors
val Teal500 = Color(0xFF0D9488)       // Primary
val Teal100 = Color(0xFFCCFBF1)       // Primary container
val Teal800 = Color(0xFF115E59)       // On primary container

// Status colors
val Green500 = Color(0xFF16A34A)      // Approved
val Amber500 = Color(0xFFF59E0B)      // Pending
val Blue500 = Color(0xFF3B82F6)       // In review
val Red500 = Color(0xFFDC2626)        // Rejected

// Neutral
val Gray50 = Color(0xFFF9FAFB)
val Gray100 = Color(0xFFF3F4F6)
val Gray500 = Color(0xFF6B7280)
val Gray900 = Color(0xFF111827)
```

### Typography (Type.kt)
```kotlin
val Typography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
    // Currency amounts — use monospace
    labelMedium = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp),
)
```

### Component Patterns

**StatusBadge composable**:
```kotlin
@Composable
fun StatusBadge(status: ExpenseStatus) {
    val (bgColor, textColor, label) = when (status) {
        PENDING -> Triple(Amber100, Amber800, "Pending")
        IN_REVIEW -> Triple(Blue100, Blue800, "In Review")
        APPROVED -> Triple(Green100, Green800, "Approved")
        REJECTED -> Triple(Red100, Red800, "Rejected")
    }
    Surface(color = bgColor, shape = RoundedCornerShape(4.dp)) {
        Text(label, color = textColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
    }
}
```

**CurrencyText composable**:
```kotlin
@Composable
fun CurrencyText(amount: Double, currencyCode: String, style: TextStyle = MaterialTheme.typography.titleLarge) {
    val formatted = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(currencyCode)
    }.format(amount)
    Text(text = formatted, style = style, fontFamily = FontFamily.Monospace)
}
```

**ExpenseCard layout**:
```
┌──────────────────────────────────────┐
│ 🏷️ Category          📅 12 Mar 2026 │
│ Client dinner at Singapore           │
│                                      │
│ ₹42,500.00        [PENDING] badge    │
│ (USD 510.00)                         │
│                                      │
│ Approval: Step 2/3 — Finance Head    │
└──────────────────────────────────────┘
```

**ApprovalActionCard layout**:
```
┌──────────────────────────────────────┐
│ 👤 Rahul Sharma       ₹42,500.00    │
│ Travel — Client dinner in Singapore  │
│ Submitted: 12 Mar 2026              │
│                                      │
│ Comment: [________________________]  │
│                                      │
│ [ ❌ Reject ]          [ ✅ Approve ]│
└──────────────────────────────────────┘
```

### Screen Layout by Role

**Employee screens**:
- Dashboard → recent expenses + quick "Submit" FAB
- Submit expense → form with OCR camera button
- Expense history → filterable list (status tabs)
- Expense detail → full info + approval timeline

**Manager screens**:
- Approval queue (primary) → pending items with approve/reject
- Team expenses → list grouped by employee
- Own expenses → same as employee view

**Admin screens**:
- Dashboard → company stats overview
- User management → create/edit users, assign roles & managers
- Workflow config → create approval chains, set step order
- Rule config → set percentage/specific/hybrid rules
- All expenses → company-wide view with filters

### Navigation Pattern
- Bottom navigation bar with 2-4 tabs depending on role
- Employee: Expenses | OCR Scan
- Manager: Approvals | Team | My Expenses
- Admin: Dashboard | Users | Workflows | All Expenses

### UX Guidelines
- Pull-to-refresh on all list screens
- Skeleton loading shimmer (not spinner) during data fetch
- Snackbar for success/error feedback (not Toast)
- Swipe-to-refresh on approval queue
- Receipt image: tap to zoom (full-screen preview)
- Empty state illustrations for no expenses / no pending approvals