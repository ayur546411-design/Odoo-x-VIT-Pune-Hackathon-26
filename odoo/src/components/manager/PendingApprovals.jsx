import React, { useEffect, useState } from 'react'
import { getPendingApprovals, updateExpenseStatus } from '../../api/expenses'
import { formatCurrency, formatDate } from '../../utils/helper'
import { Check, X, Eye } from 'lucide-react'
import Modal from '../common/Modal'
import toast from 'react-hot-toast'

export default function PendingApprovals() {
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)
  const [selected, setSelected] = useState(null)

  useEffect(() => {
    getPendingApprovals()
      .then((res) => setExpenses(res.data))
      .catch(() =>
        setExpenses([
          { _id: '1', title: 'Client dinner', amount: 3200, currency: 'INR', category: 'Food', status: 'pending', date: '2026-03-25', submittedBy: { name: 'Ravi Kumar' }, description: 'Dinner with client at hotel' },
          { _id: '2', title: 'Flight to Mumbai', amount: 8500, currency: 'INR', category: 'Travel', status: 'pending', date: '2026-03-23', submittedBy: { name: 'Priya Sharma' }, description: 'Round trip for project meeting' },
          { _id: '3', title: 'Figma subscription', amount: 1200, currency: 'USD', category: 'Software', status: 'pending', date: '2026-03-21', submittedBy: { name: 'Amit Patel' }, description: 'Annual team plan' },
        ])
      )
      .finally(() => setLoading(false))
  }, [])

  const handleAction = async (id, status) => {
    try {
      await updateExpenseStatus(id, status)
      toast.success(`Expense ${status}!`)
      setExpenses((prev) => prev.filter((e) => e._id !== id))
      setSelected(null)
    } catch {
      toast.error('Action failed')
    }
  }

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">Pending Approvals</h1>
        <p className="text-sm text-navy-300 mt-1">Review and approve team expenses</p>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[...Array(3)].map((_, i) => <div key={i} className="card shimmer h-20" />)}
        </div>
      ) : expenses.length === 0 ? (
        <div className="card text-center py-16">
          <div className="w-16 h-16 rounded-2xl bg-emerald-50 flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl">🎉</span>
          </div>
          <h3 className="text-lg font-semibold text-navy-700 mb-2">All caught up!</h3>
          <p className="text-navy-300 text-sm">No pending approvals at the moment.</p>
        </div>
      ) : (
        <div className="space-y-3">
          {expenses.map((e) => (
            <div key={e._id} className="card flex flex-col sm:flex-row sm:items-center gap-4 card-hover">
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2.5 mb-1.5">
                  <span className="text-navy-700 font-semibold text-sm">{e.title}</span>
                  <span className="badge badge-pending text-[10px]">{e.category}</span>
                </div>
                <div className="text-xs text-navy-300">
                  by <span className="font-medium text-navy-400">{e.submittedBy?.name || 'Unknown'}</span> • {formatDate(e.date)}
                </div>
              </div>
              <div className="text-xl font-bold text-navy-700 shrink-0">
                {formatCurrency(e.amount, e.currency)}
              </div>
              <div className="flex items-center gap-2 shrink-0">
                <button onClick={() => setSelected(e)} className="p-2.5 rounded-xl hover:bg-surface-100 text-navy-300 hover:text-navy-600 transition-colors border border-surface-200">
                  <Eye size={16} />
                </button>
                <button onClick={() => handleAction(e._id, 'approved')} className="p-2.5 rounded-xl bg-emerald-50 hover:bg-emerald-100 text-emerald-600 transition-colors border border-emerald-100">
                  <Check size={16} />
                </button>
                <button onClick={() => handleAction(e._id, 'rejected')} className="p-2.5 rounded-xl bg-red-50 hover:bg-red-100 text-red-600 transition-colors border border-red-100">
                  <X size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal open={!!selected} onClose={() => setSelected(null)} title="Expense Details">
        {selected && (
          <div className="space-y-5">
            <div className="grid grid-cols-2 gap-4">
              {[
                { label: 'Title', value: selected.title },
                { label: 'Amount', value: formatCurrency(selected.amount, selected.currency) },
                { label: 'Category', value: selected.category },
                { label: 'Date', value: formatDate(selected.date) },
                { label: 'Submitted By', value: selected.submittedBy?.name },
              ].map((f) => (
                <div key={f.label} className="bg-surface-100 rounded-xl p-3 border border-surface-200">
                  <span className="text-xs text-navy-300 uppercase font-semibold">{f.label}</span>
                  <p className="text-navy-700 text-sm font-semibold mt-1">{f.value || '—'}</p>
                </div>
              ))}
            </div>
            {selected.description && (
              <div className="bg-surface-100 rounded-xl p-3 border border-surface-200">
                <span className="text-xs text-navy-300 uppercase font-semibold">Description</span>
                <p className="text-navy-500 text-sm mt-1">{selected.description}</p>
              </div>
            )}
            <div className="flex gap-3 pt-2">
              <button onClick={() => handleAction(selected._id, 'approved')} className="btn-primary flex-1 justify-center rounded-xl">
                <Check size={16} /> Approve
              </button>
              <button onClick={() => handleAction(selected._id, 'rejected')} className="btn-secondary flex-1 justify-center rounded-xl text-red-600 border-red-200 hover:bg-red-50">
                <X size={16} /> Reject
              </button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  )
}
