import React, { useEffect, useState } from 'react'
import { getTeamExpenses } from '../../api/expenses'
import { formatCurrency, formatDate } from '../../utils/helper'
import StatusBadge from '../common/StatusBadge'

export default function TeamExpenses() {
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getTeamExpenses()
      .then((res) => setExpenses(res.data))
      .catch(() =>
        setExpenses([
          { _id: '1', title: 'Office supplies', amount: 1500, currency: 'INR', category: 'Office Supplies', status: 'approved', date: '2026-03-20', submittedBy: { name: 'Ravi Kumar' } },
          { _id: '2', title: 'Team outing', amount: 12000, currency: 'INR', category: 'Food', status: 'approved', date: '2026-03-19', submittedBy: { name: 'Priya Sharma' } },
          { _id: '3', title: 'Monitor purchase', amount: 18000, currency: 'INR', category: 'Equipment', status: 'pending', date: '2026-03-22', submittedBy: { name: 'Amit Patel' } },
        ])
      )
      .finally(() => setLoading(false))
  }, [])

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">Team Expenses</h1>
        <p className="text-sm text-navy-300 mt-1">All expenses submitted by your team</p>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[...Array(3)].map((_, i) => <div key={i} className="card shimmer h-16" />)}
        </div>
      ) : (
        <div className="card p-0 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-surface-200 bg-surface-100/50">
                  <th className="table-header px-5 pt-4">Employee</th>
                  <th className="table-header px-5 pt-4">Title</th>
                  <th className="table-header px-5 pt-4">Amount</th>
                  <th className="table-header px-5 pt-4">Date</th>
                  <th className="table-header px-5 pt-4">Status</th>
                </tr>
              </thead>
              <tbody>
                {expenses.map((e) => (
                  <tr key={e._id} className="table-row">
                    <td className="py-4 px-5 text-sm text-navy-500">{e.submittedBy?.name || '—'}</td>
                    <td className="py-4 px-5 text-sm text-navy-700 font-medium">{e.title}</td>
                    <td className="py-4 px-5 text-sm text-navy-700 font-semibold">{formatCurrency(e.amount, e.currency)}</td>
                    <td className="py-4 px-5 text-sm text-navy-300">{formatDate(e.date)}</td>
                    <td className="py-4 px-5"><StatusBadge status={e.status} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
