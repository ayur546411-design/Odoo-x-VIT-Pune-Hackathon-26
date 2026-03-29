import React from 'react'
import { Routes, Route } from 'react-router-dom'
import Navbar from '../components/common/Navbar'
import Sidebar from '../components/common/Sidebar'
import ManagerHome from '../components/manager/ManagerHome'
import PendingApprovals from '../components/manager/PendingApprovals'
import TeamExpenses from '../components/manager/TeamExpenses'
import { LayoutDashboard, ClipboardCheck, Users } from 'lucide-react'

const links = [
  { to: '/manager', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/manager/approvals', label: 'Approvals', icon: ClipboardCheck },
  { to: '/manager/team', label: 'Team Expenses', icon: Users },
]

export default function ManagerDashboard() {
  return (
    <div className="min-h-screen bg-surface-100">
      <Navbar />
      <div className="flex pt-16">
        <Sidebar links={links} />
        <main className="flex-1 p-6 lg:p-8">
          <Routes>
            <Route index element={<ManagerHome />} />
            <Route path="approvals" element={<PendingApprovals />} />
            <Route path="team" element={<TeamExpenses />} />
          </Routes>
        </main>
      </div>
    </div>
  )
}
