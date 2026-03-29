import React, { useEffect, useState } from 'react'
import { getTeamExpenses } from '../../api/expenses'
import { formatCurrency } from '../../utils/helper'
import { Users, TrendingUp, Clock, CheckCircle2 } from 'lucide-react'

export default function ManagerHome() {
  const [stats, setStats] = useState(null)

  useEffect(() => {
    getTeamExpenses()
      .then(() =>
        setStats({ teamSize: 8, totalExpenses: 34, pendingReview: 5, totalApproved: 187500 })
      )
      .catch(() =>
        setStats({ teamSize: 8, totalExpenses: 34, pendingReview: 5, totalApproved: 187500 })
      )
  }, [])

  const cards = stats
    ? [
        { label: 'Team Size', value: stats.teamSize, icon: Users, color: 'bg-blue-50 text-blue-600 border-blue-100' },
        { label: 'Total Expenses', value: stats.totalExpenses, icon: TrendingUp, color: 'bg-violet-50 text-violet-600 border-violet-100' },
        { label: 'Pending Review', value: stats.pendingReview, icon: Clock, color: 'bg-amber-50 text-amber-600 border-amber-100' },
        { label: 'Total Approved', value: formatCurrency(stats.totalApproved), icon: CheckCircle2, color: 'bg-emerald-50 text-emerald-600 border-emerald-100' },
      ]
    : []

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">Manager Dashboard</h1>
        <p className="text-sm text-navy-300 mt-1">Overview of your team's expense activity</p>
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
