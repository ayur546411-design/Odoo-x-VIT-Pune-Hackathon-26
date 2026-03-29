import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Eye, EyeOff, Zap, ArrowRight, Mail, Lock } from 'lucide-react'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [showPw, setShowPw] = useState(false)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const user = await login(form)
      navigate(`/${user.role}`)
    } catch {
      // toast handled in context
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex">
      {/* Left Panel — Form */}
      <div className="flex-1 flex items-center justify-center p-6 bg-white">
        <div className="w-full max-w-md animate-fade-up">
          <Link to="/" className="inline-flex items-center gap-2.5 mb-10">
            <div className="w-10 h-10 rounded-xl bg-primary-800 flex items-center justify-center shadow-md">
              <Zap size={20} className="text-white" />
            </div>
            <span className="text-2xl font-bold text-navy-700">ExpenseFlow</span>
          </Link>

          <h1 className="text-3xl font-bold text-navy-700 mb-2">Welcome back</h1>
          <p className="text-navy-300 mb-8">Log in to your expense management dashboard</p>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-sm font-semibold text-navy-600 mb-2">Email address</label>
              <div className="relative">
                <Mail size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
                <input
                  type="email"
                  required
                  className="input-field pl-11"
                  placeholder="john@company.com"
                  value={form.email}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-semibold text-navy-600 mb-2">Password</label>
              <div className="relative">
                <Lock size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
                <input
                  type={showPw ? 'text' : 'password'}
                  required
                  className="input-field pl-11 pr-11"
                  placeholder="Enter your password"
                  value={form.password}
                  onChange={(e) => setForm({ ...form, password: e.target.value })}
                />
                <button
                  type="button"
                  onClick={() => setShowPw(!showPw)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-navy-200 hover:text-navy-400 transition-colors"
                >
                  {showPw ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn-primary w-full justify-center py-3.5 text-base rounded-xl"
            >
              {loading ? (
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              ) : (
                <>Sign in <ArrowRight size={16} /></>
              )}
            </button>
          </form>

          <p className="text-center text-sm text-navy-300 mt-8">
            Don&apos;t have an account?{' '}
            <Link to="/signup" className="text-primary-600 hover:text-primary-700 font-semibold transition-colors">
              Create account
            </Link>
          </p>
        </div>
      </div>

      {/* Right Panel — Branding */}
      <div className="hidden lg:flex flex-1 gradient-hero items-center justify-center p-12 relative overflow-hidden">
        <div className="absolute inset-0">
          <div className="absolute top-20 right-20 w-64 h-64 bg-primary-400/10 rounded-full blur-[100px]" />
          <div className="absolute bottom-20 left-20 w-80 h-80 bg-primary-300/8 rounded-full blur-[120px]" />
        </div>
        <div className="relative z-10 max-w-md text-center">
          <div className="w-20 h-20 rounded-3xl bg-white/10 border border-white/15 flex items-center justify-center mx-auto mb-8 backdrop-blur-sm">
            <Zap size={36} className="text-white" />
          </div>
          <h2 className="text-3xl font-bold text-white mb-4">
            Manage expenses smarter
          </h2>
          <p className="text-white/50 text-base leading-relaxed">
            Join 1000+ organizations using ExpenseFlow to automate expense management, 
            simplify reimbursements, and gain full control over spending.
          </p>

          <div className="grid grid-cols-3 gap-4 mt-10">
            {[
              { val: '99.2%', label: 'OCR Accuracy' },
              { val: '4x', label: 'Faster Processing' },
              { val: '24h', label: 'Avg. Approval' },
            ].map((s) => (
              <div key={s.label} className="bg-white/8 rounded-2xl p-4 border border-white/10">
                <div className="text-xl font-bold text-white mb-1">{s.val}</div>
                <div className="text-xs text-white/40">{s.label}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
