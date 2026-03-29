import React from 'react'
import { Routes, Route } from 'react-router-dom'
import Navbar from '../components/common/Navbar'
import Sidebar from '../components/common/Sidebar'
import EmployeeHome from '../components/employee/EmployeeHome'
import SubmitExpense from '../components/employee/SubmitExpense'
import ExpenseHistory from '../components/employee/ExpenseHistory'
import OCRScanner from '../components/employee/OCRScanner'
import { LayoutDashboard, PlusCircle, History, ScanLine } from 'lucide-react'

const links = [
  { to: '/employee', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/employee/submit', label: 'Submit Expense', icon: PlusCircle },
  { to: '/employee/history', label: 'History', icon: History },
  { to: '/employee/scan', label: 'OCR Scanner', icon: ScanLine },
]

export default function EmployeeDashboard() {
  return (
    <div className="min-h-screen bg-surface-100">
      <Navbar />
      <div className="flex pt-16">
        <Sidebar links={links} />
        <main className="flex-1 p-6 lg:p-8">
          <Routes>
            <Route index element={<EmployeeHome />} />
            <Route path="submit" element={<SubmitExpense />} />
            <Route path="history" element={<ExpenseHistory />} />
            <Route path="scan" element={<OCRScanner />} />
          </Routes>
        </main>
      </div>
    </div>
  )
}
