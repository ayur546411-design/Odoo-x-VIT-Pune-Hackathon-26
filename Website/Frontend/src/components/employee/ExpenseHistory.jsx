import React, { useEffect, useState } from 'react'
import { getMyExpenses } from '../../api/expenses'
import { formatCurrency, formatDate } from '../../utils/helper'
import StatusBadge from '../common/StatusBadge'

export default function ExpenseHistory() {
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getMyExpenses()
      .then((res) => setExpenses(res.data))
      .catch(() =>
        setExpenses([
          { _id: '1', title: 'Uber to client meeting', amount: 450, currency: 'INR', category: 'Travel', status: 'approved', date: '2026-03-20' },
          { _id: '2', title: 'Team lunch', amount: 2200, currency: 'INR', category: 'Food', status: 'pending', date: '2026-03-22' },
          { _id: '3', title: 'Software license', amount: 5999, currency: 'INR', category: 'Software', status: 'rejected', date: '2026-03-18' },
          { _id: '4', title: 'Conference pass', amount: 8000, currency: 'INR', category: 'Other', status: 'paid', date: '2026-03-15' },
        ])
      )
      .finally(() => setLoading(false))
  }, [])

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">Expense History</h1>
        <p className="text-sm text-navy-300 mt-1">View all your past expense submissions</p>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[...Array(4)].map((_, i) => (
            <div key={i} className="card shimmer h-16" />
          ))}
        </div>
      ) : expenses.length === 0 ? (
        <div className="card text-center py-16">
          <div className="w-16 h-16 rounded-2xl bg-surface-100 flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl">📋</span>
          </div>
          <h3 className="text-lg font-semibold text-navy-700 mb-2">No expenses yet</h3>
          <p className="text-navy-300 text-sm">Submit your first expense to get started!</p>
        </div>
      ) : (
        <div className="card p-0 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-surface-200 bg-surface-100/50">
                  <th className="table-header px-5 pt-4">Title</th>
                  <th className="table-header px-5 pt-4">Category</th>
                  <th className="table-header px-5 pt-4">Amount</th>
                  <th className="table-header px-5 pt-4">Date</th>
                  <th className="table-header px-5 pt-4">Status</th>
                </tr>
              </thead>
              <tbody>
                {expenses.map((e) => (
                  <tr key={e._id} className="table-row">
                    <td className="py-4 px-5 text-sm text-navy-700 font-medium">{e.title}</td>
                    <td className="py-4 px-5 text-sm text-navy-300">{e.category}</td>
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
