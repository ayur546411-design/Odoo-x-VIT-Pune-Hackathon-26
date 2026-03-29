import React, { useEffect, useState } from 'react'
import { getAllUsers, updateUserRole, deleteUser } from '../../api/users'
import { getInitials } from '../../utils/helper'
import { Trash2, ChevronDown } from 'lucide-react'
import toast from 'react-hot-toast'

export default function UserManagement() {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getAllUsers()
      .then((res) => setUsers(res.data))
      .catch(() =>
        setUsers([
          { _id: '1', name: 'Ravi Kumar', email: 'ravi@co.com', role: 'employee' },
          { _id: '2', name: 'Priya Sharma', email: 'priya@co.com', role: 'employee' },
          { _id: '3', name: 'Amit Patel', email: 'amit@co.com', role: 'manager' },
          { _id: '4', name: 'Sneha Gupta', email: 'sneha@co.com', role: 'admin' },
        ])
      )
      .finally(() => setLoading(false))
  }, [])

  const handleRoleChange = async (id, role) => {
    try {
      await updateUserRole(id, role)
      setUsers((prev) => prev.map((u) => (u._id === id ? { ...u, role } : u)))
      toast.success('Role updated')
    } catch {
      toast.error('Failed to update role')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this user?')) return
    try {
      await deleteUser(id)
      setUsers((prev) => prev.filter((u) => u._id !== id))
      toast.success('User deleted')
    } catch {
      toast.error('Failed to delete user')
    }
  }

  const getRoleBadge = (role) => {
    const map = {
      admin: 'bg-purple-50 text-purple-700 border-purple-200',
      manager: 'bg-blue-50 text-blue-700 border-blue-200',
      employee: 'bg-slate-50 text-slate-600 border-slate-200',
    }
    return map[role] || map.employee
  }

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">User Management</h1>
        <p className="text-sm text-navy-300 mt-1">Manage users and assign roles</p>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[...Array(4)].map((_, i) => <div key={i} className="card shimmer h-16" />)}
        </div>
      ) : (
        <div className="space-y-3">
          {users.map((u) => (
            <div key={u._id} className="card flex flex-col sm:flex-row sm:items-center gap-4 card-hover">
              <div className="w-10 h-10 rounded-full bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center shrink-0">
                <span className="text-sm font-bold text-white">{getInitials(u.name)}</span>
              </div>
              <div className="flex-1 min-w-0">
                <div className="text-sm text-navy-700 font-semibold">{u.name}</div>
                <div className="text-xs text-navy-300">{u.email}</div>
              </div>
              <div className="flex items-center gap-3 shrink-0">
                <div className="relative">
                  <select
                    value={u.role}
                    onChange={(e) => handleRoleChange(u._id, e.target.value)}
                    className={`appearance-none px-3 py-1.5 pr-7 rounded-full text-xs font-semibold border cursor-pointer transition-colors ${getRoleBadge(u.role)}`}
                    style={{ background: 'transparent' }}
                  >
                    <option value="employee">Employee</option>
                    <option value="manager">Manager</option>
                    <option value="admin">Admin</option>
                  </select>
                  <ChevronDown size={12} className="absolute right-2.5 top-1/2 -translate-y-1/2 pointer-events-none text-navy-300" />
                </div>
                <button
                  onClick={() => handleDelete(u._id)}
                  className="p-2.5 rounded-xl hover:bg-red-50 text-navy-300 hover:text-red-500 transition-colors border border-transparent hover:border-red-100"
                >
                  <Trash2 size={15} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
