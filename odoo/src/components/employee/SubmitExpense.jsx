import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createExpense } from '../../api/expenses'
import { Upload, Send, X, FileText, Calendar, DollarSign, Tag, AlignLeft } from 'lucide-react'
import toast from 'react-hot-toast'

const categories = ['Travel', 'Food', 'Office Supplies', 'Software', 'Equipment', 'Utilities', 'Other']

export default function SubmitExpense() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [file, setFile] = useState(null)
  const [form, setForm] = useState({
    title: '',
    amount: '',
    currency: 'INR',
    category: '',
    description: '',
    date: new Date().toISOString().split('T')[0],
  })

  const handleFile = (e) => {
    const f = e.target.files?.[0]
    if (f) setFile(f)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await createExpense({ ...form, receipt: file })
      toast.success('Expense submitted successfully!')
      navigate('/employee')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to submit')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">Submit Expense</h1>
        <p className="text-sm text-navy-300 mt-1">Fill in the details or upload a receipt</p>
      </div>

      <form onSubmit={handleSubmit} className="card space-y-5">
        <div className="grid sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-semibold text-navy-600 mb-2">Title</label>
            <div className="relative">
              <FileText size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
              <input
                required
                className="input-field pl-11"
                placeholder="e.g. Client lunch"
                value={form.title}
                onChange={(e) => setForm({ ...form, title: e.target.value })}
              />
            </div>
          </div>
          <div>
            <label className="block text-sm font-semibold text-navy-600 mb-2">Date</label>
            <div className="relative">
              <Calendar size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
              <input
                type="date"
                required
                className="input-field pl-11"
                value={form.date}
                onChange={(e) => setForm({ ...form, date: e.target.value })}
              />
            </div>
          </div>
        </div>

        <div className="grid sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-semibold text-navy-600 mb-2">Amount</label>
            <div className="relative">
              <DollarSign size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
              <input
                type="number"
                required
                min="1"
                step="0.01"
                className="input-field pl-11"
                placeholder="0.00"
                value={form.amount}
                onChange={(e) => setForm({ ...form, amount: e.target.value })}
              />
            </div>
          </div>
          <div>
            <label className="block text-sm font-semibold text-navy-600 mb-2">Currency</label>
            <select
              className="input-field"
              value={form.currency}
              onChange={(e) => setForm({ ...form, currency: e.target.value })}
            >
              <option value="INR">INR (₹)</option>
              <option value="USD">USD ($)</option>
              <option value="EUR">EUR (€)</option>
              <option value="GBP">GBP (£)</option>
            </select>
          </div>
        </div>

        <div>
          <label className="block text-sm font-semibold text-navy-600 mb-2">Category</label>
          <div className="relative">
            <Tag size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
            <select
              required
              className="input-field pl-11 appearance-none"
              value={form.category}
              onChange={(e) => setForm({ ...form, category: e.target.value })}
            >
              <option value="">Select category…</option>
              {categories.map((c) => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label className="block text-sm font-semibold text-navy-600 mb-2">Description</label>
          <div className="relative">
            <AlignLeft size={16} className="absolute left-4 top-4 text-navy-200" />
            <textarea
              className="input-field pl-11 min-h-[80px] resize-none"
              placeholder="Additional details…"
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
            />
          </div>
        </div>

        <div>
          <label className="block text-sm font-semibold text-navy-600 mb-2">Receipt</label>
          {file ? (
            <div className="flex items-center gap-3 p-3.5 rounded-xl bg-surface-100 border border-surface-200">
              <div className="w-9 h-9 rounded-lg bg-primary-50 border border-primary-100 flex items-center justify-center">
                <Upload size={16} className="text-primary-500" />
              </div>
              <span className="text-sm text-navy-600 truncate flex-1">{file.name}</span>
              <button type="button" onClick={() => setFile(null)} className="text-navy-300 hover:text-red-500 transition-colors">
                <X size={16} />
              </button>
            </div>
          ) : (
            <label className="flex flex-col items-center justify-center gap-3 p-8 rounded-2xl border-2 border-dashed border-surface-300 hover:border-primary-300 cursor-pointer transition-all duration-300 hover:bg-primary-50/30">
              <div className="w-12 h-12 rounded-2xl bg-surface-100 flex items-center justify-center">
                <Upload size={22} className="text-navy-300" />
              </div>
              <div className="text-center">
                <span className="text-sm font-medium text-navy-600">Click to upload receipt</span>
                <p className="text-xs text-navy-200 mt-1">PNG, JPG, PDF up to 10MB</p>
              </div>
              <input type="file" accept="image/*,.pdf" className="hidden" onChange={handleFile} />
            </label>
          )}
        </div>

        <button
          type="submit"
          disabled={loading}
          className="btn-primary w-full justify-center py-3.5 text-base rounded-xl"
        >
          {loading ? (
            <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
          ) : (
            <>Submit Expense <Send size={16} /></>
          )}
        </button>
      </form>
    </div>
  )
}
