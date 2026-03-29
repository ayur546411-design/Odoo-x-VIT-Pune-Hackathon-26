import React, { useEffect, useState } from 'react'
import { getExpenseStats } from '../../api/expenses'
import { formatCurrency } from '../../utils/helper'
import { DollarSign, Users, FileText, TrendingUp } from 'lucide-react'

export default function AdminHome() {
  const [stats, setStats] = useState(null)

  useEffect(() => {
    getExpenseStats()
      .then((res) => setStats(res.data))
      .catch(() =>
        setStats({ totalUsers: 45, totalExpenses: 234, totalAmount: 1850000, approvalRate: 87 })
      )
  }, [])

  const cards = stats
    ? [
        { label: 'Total Users', value: stats.totalUsers, icon: Users, color: 'bg-blue-50 text-blue-600 border-blue-100' },
        { label: 'Total Expenses', value: stats.totalExpenses, icon: FileText, color: 'bg-violet-50 text-violet-600 border-violet-100' },
        { label: 'Total Amount', value: formatCurrency(stats.totalAmount), icon: DollarSign, color: 'bg-emerald-50 text-emerald-600 border-emerald-100' },
        { label: 'Approval Rate', value: `${stats.approvalRate}%`, icon: TrendingUp, color: 'bg-amber-50 text-amber-600 border-amber-100' },
      ]
    : []

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">Admin Dashboard</h1>
        <p className="text-sm text-navy-300 mt-1">Organization-wide expense overview</p>
      </div>
      {!stats ? (
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {[...Array(4)].map((_, i) => <div key={i} className="stat-card shimmer h-28" />)}
        </div>
      ) : (
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {cards.map((c) => (
            <div key={c.label} className="stat-card group">
              <div className="flex items-center justify-between mb-4">
                <span className="text-xs text-navy-300 uppercase tracking-wider font-semibold">{c.label}</span>
                <div className={`w-9 h-9 rounded-xl border flex items-center justify-center ${c.color}`}>
                  <c.icon size={16} />
                </div>
              </div>
              <div className="text-2xl font-bold text-navy-700">{c.value}</div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
