import React, { useEffect, useState } from 'react'
import { getWorkflows, createWorkflow, deleteWorkflow } from '../../api/workflow'
import { Plus, Trash2, GitBranch, ArrowRight } from 'lucide-react'
import Modal from '../common/Modal'
import toast from 'react-hot-toast'

export default function WorkFlowBuilder() {
  const [workflows, setWorkflows] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [form, setForm] = useState({ name: '', minAmount: '', maxAmount: '', approvers: '' })

  const fetchWorkflows = () => {
    getWorkflows()
      .then((res) => setWorkflows(res.data))
      .catch(() =>
        setWorkflows([
          { _id: '1', name: 'Low-value Expenses', minAmount: 0, maxAmount: 5000, approvers: ['Manager'], status: 'active' },
          { _id: '2', name: 'Mid-value Expenses', minAmount: 5001, maxAmount: 50000, approvers: ['Manager', 'Finance Head'], status: 'active' },
          { _id: '3', name: 'High-value Expenses', minAmount: 50001, maxAmount: 999999, approvers: ['Manager', 'Finance Head', 'VP'], status: 'active' },
        ])
      )
      .finally(() => setLoading(false))
  }

  useEffect(() => { fetchWorkflows() }, [])

  const handleCreate = async (e) => {
    e.preventDefault()
    try {
      const data = {
        name: form.name,
        minAmount: Number(form.minAmount),
        maxAmount: Number(form.maxAmount),
        approvers: form.approvers.split(',').map((a) => a.trim()),
      }
      await createWorkflow(data)
      toast.success('Workflow created!')
      setShowModal(false)
      setForm({ name: '', minAmount: '', maxAmount: '', approvers: '' })
      fetchWorkflows()
    } catch {
      toast.error('Failed to create workflow')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this workflow?')) return
    try {
      await deleteWorkflow(id)
      setWorkflows((prev) => prev.filter((w) => w._id !== id))
      toast.success('Workflow deleted')
    } catch {
      toast.error('Failed to delete')
    }
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl font-bold text-navy-700">Workflow Builder</h1>
          <p className="text-sm text-navy-300 mt-1">Configure approval hierarchies and rules</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary text-sm">
          <Plus size={16} /> New Workflow
        </button>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[...Array(3)].map((_, i) => <div key={i} className="card shimmer h-28" />)}
        </div>
      ) : workflows.length === 0 ? (
        <div className="card text-center py-16">
          <div className="w-16 h-16 rounded-2xl bg-primary-50 flex items-center justify-center mx-auto mb-4">
            <GitBranch size={24} className="text-primary-500" />
          </div>
          <h3 className="text-lg font-semibold text-navy-700 mb-2">No workflows yet</h3>
          <p className="text-navy-300 text-sm">Create your first approval workflow to get started.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {workflows.map((w) => (
            <div key={w._id} className="card card-hover">
              <div className="flex items-start justify-between">
                <div className="flex items-start gap-4">
                  <div className="w-11 h-11 rounded-2xl bg-primary-50 border border-primary-100 flex items-center justify-center shrink-0">
                    <GitBranch size={18} className="text-primary-500" />
                  </div>
                  <div>
                    <h3 className="text-navy-700 font-semibold text-sm">{w.name}</h3>
                    <p className="text-xs text-navy-300 mt-1">
                      Range: ₹{w.minAmount?.toLocaleString()} – ₹{w.maxAmount?.toLocaleString()}
                    </p>
                    <div className="flex flex-wrap items-center gap-2 mt-3">
                      {(w.approvers || []).map((a, i) => (
                        <React.Fragment key={i}>
                          <span className="text-xs px-3 py-1 rounded-full bg-primary-50 text-primary-600 font-semibold border border-primary-100">
                            {a}
                          </span>
                          {i < w.approvers.length - 1 && (
                            <ArrowRight size={12} className="text-navy-200" />
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <button
                  onClick={() => handleDelete(w._id)}
                  className="p-2.5 rounded-xl hover:bg-red-50 text-navy-300 hover:text-red-500 transition-colors border border-transparent hover:border-red-100"
                >
                  <Trash2 size={15} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal open={showModal} onClose={() => setShowModal(false)} title="Create Workflow">
        <form onSubmit={handleCreate} className="space-y-4">
          <div>
            <label className="block text-sm font-semibold text-navy-600 mb-2">Workflow Name</label>
            <input
              required
              className="input-field"
              placeholder="e.g. High-value Expenses"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-semibold text-navy-600 mb-2">Min Amount (₹)</label>
              <input
                type="number"
                required
                className="input-field"
                placeholder="0"
                value={form.minAmount}
                onChange={(e) => setForm({ ...form, minAmount: e.target.value })}
              />
            </div>
            <div>
              <label className="block text-sm font-semibold text-navy-600 mb-2">Max Amount (₹)</label>
              <input
                type="number"
                required
                className="input-field"
                placeholder="50000"
                value={form.maxAmount}
                onChange={(e) => setForm({ ...form, maxAmount: e.target.value })}
              />
            </div>
          </div>
          <div>
            <label className="block text-sm font-semibold text-navy-600 mb-2">Approvers (comma-separated)</label>
            <input
              required
              className="input-field"
              placeholder="Manager, Finance Head, VP"
              value={form.approvers}
              onChange={(e) => setForm({ ...form, approvers: e.target.value })}
            />
          </div>
          <button type="submit" className="btn-primary w-full justify-center py-3 rounded-xl">
            Create Workflow
          </button>
        </form>
      </Modal>
    </div>
  )
}
