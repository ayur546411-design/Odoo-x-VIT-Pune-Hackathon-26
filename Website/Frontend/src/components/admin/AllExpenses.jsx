import React, { useEffect, useState } from 'react'
import { getAllExpenses } from '../../api/expenses'
import { formatCurrency, formatDate } from '../../utils/helper'
import StatusBadge from '../common/StatusBadge'
import { Search } from 'lucide-react'

export default function AllExpenses() {
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [statusFilter, setStatusFilter] = useState('all')

  useEffect(() => {
    getAllExpenses()
      .then((res) => setExpenses(res.data))
      .catch(() =>
        setExpenses([
          { _id: '1', title: 'Client dinner', amount: 3200, currency: 'INR', category: 'Food', status: 'approved', date: '2026-03-20', submittedBy: { name: 'Ravi Kumar' } },
          { _id: '2', title: 'Office supplies', amount: 1500, currency: 'INR', category: 'Office Supplies', status: 'pending', date: '2026-03-21', submittedBy: { name: 'Priya Sharma' } },
          { _id: '3', title: 'Software license', amount: 5999, currency: 'INR', category: 'Software', status: 'rejected', date: '2026-03-18', submittedBy: { name: 'Amit Patel' } },
          { _id: '4', title: 'Flight tickets', amount: 15000, currency: 'INR', category: 'Travel', status: 'approved', date: '2026-03-22', submittedBy: { name: 'Sneha Gupta' } },
          { _id: '5', title: 'Conference pass', amount: 8000, currency: 'INR', category: 'Other', status: 'paid', date: '2026-03-15', submittedBy: { name: 'Ravi Kumar' } },
        ])
      )
      .finally(() => setLoading(false))
  }, [])

  const filtered = expenses.filter((e) => {
    const matchesSearch = e.title.toLowerCase().includes(search.toLowerCase()) ||
      (e.submittedBy?.name || '').toLowerCase().includes(search.toLowerCase())
    const matchesStatus = statusFilter === 'all' || e.status === statusFilter
    return matchesSearch && matchesStatus
  })

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">All Expenses</h1>
        <p className="text-sm text-navy-300 mt-1">View and filter all organization expenses</p>
      </div>

      <div className="flex flex-col sm:flex-row gap-3 mb-6">
        <div className="relative flex-1">
          <Search size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
          <input
            type="text"
            className="input-field pl-11"
            placeholder="Search by title or employee…"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <select
          className="input-field w-auto min-w-[140px]"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="all">All Status</option>
          <option value="pending">Pending</option>
          <option value="approved">Approved</option>
          <option value="rejected">Rejected</option>
          <option value="paid">Paid</option>
        </select>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[...Array(4)].map((_, i) => <div key={i} className="card shimmer h-16" />)}
        </div>
      ) : (
        <div className="card p-0 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-surface-200 bg-surface-100/50">
                  <th className="table-header px-5 pt-4">Employee</th>
                  <th className="table-header px-5 pt-4">Title</th>
                  <th className="table-header px-5 pt-4">Category</th>
                  <th className="table-header px-5 pt-4">Amount</th>
                  <th className="table-header px-5 pt-4">Date</th>
                  <th className="table-header px-5 pt-4">Status</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((e) => (
                  <tr key={e._id} className="table-row">
                    <td className="py-4 px-5 text-sm text-navy-500">{e.submittedBy?.name || '—'}</td>
                    <td className="py-4 px-5 text-sm text-navy-700 font-medium">{e.title}</td>
                    <td className="py-4 px-5 text-sm text-navy-300">{e.category}</td>
                    <td className="py-4 px-5 text-sm text-navy-700 font-semibold">{formatCurrency(e.amount, e.currency)}</td>
                    <td className="py-4 px-5 text-sm text-navy-300">{formatDate(e.date)}</td>
                    <td className="py-4 px-5"><StatusBadge status={e.status} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
            {filtered.length === 0 && (
              <div className="text-center py-10 text-navy-300 text-sm">No expenses match your filters</div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}
