import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Zap, ArrowRight, Mail, KeyRound, User, Building2 } from 'lucide-react'

export default function SignupPage() {
  const { requestOtp, verifyOtpAndLogin } = useAuth()
  const navigate = useNavigate()
  
  // Single form state managing all values (Registration details + OTP)
  const [form, setForm] = useState({
    name: '',
    email: '',
    role: 'employee',
    department: '',
    otp: ''
  })
  
  const [step, setStep] = useState('DETAILS') // "DETAILS" or "OTP"
  const [loading, setLoading] = useState(false)

  const handleSendOtp = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await requestOtp(form.email)
      setStep('OTP')
    } catch (error) {
    } finally {
      setLoading(false)
    }
  }

  const handleVerifyOtp = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      // The backend gets the entire form structure to initialize correctly.
      const payload = {
        email: form.email,
        otp: form.otp,
        name: form.name,
        role: form.role,
        department: form.department
      }
      const user = await verifyOtpAndLogin(payload)
      navigate(`/${user.role || 'employee'}`)
    } catch (error) {
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex">
      {/* Left Panel */}
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
            Set up your organization in minutes. We use passwordless authentication!
          </p>
        </div>
      </div>

      {/* Right Panel */}
      <div className="flex-1 flex items-center justify-center p-6 bg-white overflow-y-auto">
        <div className="w-full max-w-md animate-fade-up py-10">
          <Link to="/" className="inline-flex items-center gap-2.5 mb-10">
            <div className="w-10 h-10 rounded-xl bg-primary-800 flex items-center justify-center shadow-md">
              <Zap size={20} className="text-white" />
            </div>
            <span className="text-2xl font-bold text-navy-700">ExpenseFlow</span>
          </Link>

          <h1 className="text-3xl font-bold text-navy-700 mb-2">Create account</h1>
          <p className="text-navy-300 mb-8">
            {step === 'DETAILS' 
              ? "Tell us about yourself before we authenticate you." 
              : `We've sent a 6-digit code to ${form.email}`}
          </p>

          {step === 'DETAILS' ? (
            <form onSubmit={handleSendOtp} className="space-y-4">
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

              <div className="grid grid-cols-2 gap-4">
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

                <div>
                  <label className="block text-sm font-semibold text-navy-600 mb-2">Department</label>
                  <input
                    type="text"
                    className="input-field"
                    placeholder="e.g. Engineering"
                    value={form.department}
                    onChange={(e) => setForm({ ...form, department: e.target.value })}
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading || !form.email || !form.name}
                className="btn-primary w-full justify-center py-3.5 text-base rounded-xl mt-4"
              >
                {loading ? (
                  <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                ) : (
                  <>Continue <ArrowRight size={16} /></>
                )}
              </button>
            </form>
          ) : (
            <form onSubmit={handleVerifyOtp} className="space-y-5">
              <div>
                <label className="block text-sm font-semibold text-navy-600 mb-2">Secure OTP Code</label>
                <div className="relative">
                  <KeyRound size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
                  <input
                    type="text"
                    required
                    maxLength={6}
                    className="input-field tracking-[0.5em] text-center pl-11"
                    placeholder="000000"
                    value={form.otp}
                    onChange={(e) => setForm({ ...form, otp: e.target.value })}
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading || form.otp.length < 6}
                className="btn-primary w-full justify-center py-3.5 text-base rounded-xl"
              >
                {loading ? (
                  <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                ) : (
                  <>Verify & Create Account <ArrowRight size={16} /></>
                )}
              </button>
              
              <div className="text-center mt-4">
                <button
                  type="button"
                  onClick={() => setStep('DETAILS')}
                  className="text-sm font-medium text-navy-400 hover:text-navy-600 transition-colors"
                >
                  Edit Registration Details
                </button>
              </div>
            </form>
          )}

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
