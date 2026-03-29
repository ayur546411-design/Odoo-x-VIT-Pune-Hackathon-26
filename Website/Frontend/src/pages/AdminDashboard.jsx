import React from 'react'
import { Routes, Route } from 'react-router-dom'
import Navbar from '../components/common/Navbar'
import Sidebar from '../components/common/Sidebar'
import AdminHome from '../components/admin/AdminHome'
import AllExpenses from '../components/admin/AllExpenses'
import UserManagement from '../components/admin/UserManagement'
import WorkFlowBuilder from '../components/admin/WorkFlowBuilder'
import { LayoutDashboard, FileText, Users, GitBranch } from 'lucide-react'

const links = [
  { to: '/admin', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/admin/expenses', label: 'All Expenses', icon: FileText },
  { to: '/admin/users', label: 'Users', icon: Users },
  { to: '/admin/workflows', label: 'Workflows', icon: GitBranch },
]

export default function AdminDashboard() {
  return (
    <div className="min-h-screen bg-surface-100">
      <Navbar />
      <div className="flex pt-16">
        <Sidebar links={links} />
        <main className="flex-1 p-6 lg:p-8">
          <Routes>
            <Route index element={<AdminHome />} />
            <Route path="expenses" element={<AllExpenses />} />
            <Route path="users" element={<UserManagement />} />
            <Route path="workflows" element={<WorkFlowBuilder />} />
          </Routes>
        </main>
      </div>
    </div>
  )
}
