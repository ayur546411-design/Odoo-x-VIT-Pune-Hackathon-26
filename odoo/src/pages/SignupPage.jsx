import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Eye, EyeOff, Zap, ArrowRight, Mail, Lock, User, Building2 } from 'lucide-react'

export default function SignupPage() {
  const { signup } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'employee' })
  const [showPw, setShowPw] = useState(false)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const user = await signup(form)
      navigate(`/${user.role}`)
    } catch {
      // toast handled in context
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex">
      {/* Left Panel — Branding */}
      <div className="hidden lg:flex flex-1 gradient-hero items-center justify-center p-12 relative overflow-hidden">
        <div className="absolute inset-0">
          <div className="absolute top-20 left-20 w-72 h-72 bg-primary-400/10 rounded-full blur-[100px]" />
          <div className="absolute bottom-20 right-20 w-80 h-80 bg-primary-300/8 rounded-full blur-[120px]" />
        </div>
        <div className="relative z-10 max-w-md text-center">
          <div className="w-20 h-20 rounded-3xl bg-white/10 border border-white/15 flex items-center justify-center mx-auto mb-8 backdrop-blur-sm">
            <Zap size={36} className="text-white" />
          </div>
          <h2 className="text-3xl font-bold text-white mb-4">
            Start your free account
          </h2>
          <p className="text-white/50 text-base leading-relaxed">
            Set up your organization in minutes. Submit expenses, configure workflows, 
            and get your team on board with zero friction.
          </p>

          <div className="mt-10 space-y-3">
            {[
              'OCR receipt scanning — auto-extract data',
              'Multi-level approval workflows',
              'Real-time analytics & dashboards',
              'Multi-currency support',
            ].map((feature) => (
              <div key={feature} className="flex items-center gap-3 text-left bg-white/8 rounded-xl p-3 border border-white/10">
                <div className="w-5 h-5 rounded-full bg-emerald-400/20 flex items-center justify-center shrink-0">
                  <span className="text-emerald-300 text-xs">✓</span>
                </div>
                <span className="text-sm text-white/70">{feature}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Right Panel — Form */}
      <div className="flex-1 flex items-center justify-center p-6 bg-white">
        <div className="w-full max-w-md animate-fade-up">
          <Link to="/" className="inline-flex items-center gap-2.5 mb-10">
            <div className="w-10 h-10 rounded-xl bg-primary-800 flex items-center justify-center shadow-md">
              <Zap size={20} className="text-white" />
            </div>
            <span className="text-2xl font-bold text-navy-700">ExpenseFlow</span>
          </Link>

          <h1 className="text-3xl font-bold text-navy-700 mb-2">Create account</h1>
          <p className="text-navy-300 mb-8">Get started with your free expense management account</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-navy-600 mb-2">Full name</label>
              <div className="relative">
                <User size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
                <input
                  type="text"
                  required
                  className="input-field pl-11"
                  placeholder="John Doe"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                />
              </div>
            </div>

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
                  minLength={6}
                  className="input-field pl-11 pr-11"
                  placeholder="Min 6 characters"
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

            <div>
              <label className="block text-sm font-semibold text-navy-600 mb-2">Role</label>
              <div className="relative">
                <Building2 size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
                <select
                  className="input-field pl-11 appearance-none"
                  value={form.role}
                  onChange={(e) => setForm({ ...form, role: e.target.value })}
                >
                  <option value="employee">Employee</option>
                  <option value="manager">Manager</option>
                  <option value="admin">Admin</option>
                </select>
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
                <>Create Account <ArrowRight size={16} /></>
              )}
            </button>
          </form>

          <p className="text-center text-sm text-navy-300 mt-8">
            Already have an account?{' '}
            <Link to="/login" className="text-primary-600 hover:text-primary-700 font-semibold transition-colors">
              Log in
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}
