import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Zap, ArrowRight, Mail, KeyRound } from 'lucide-react'

export default function LoginPage() {
  const { requestOtp, verifyOtpAndLogin } = useAuth()
  const navigate = useNavigate()
  
  const [email, setEmail] = useState('')
  const [otp, setOtp] = useState('')
  const [step, setStep] = useState('EMAIL') // "EMAIL" or "OTP"
  const [loading, setLoading] = useState(false)

  const handleSendOtp = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await requestOtp(email)
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
      const user = await verifyOtpAndLogin(email, otp)
      navigate(`/${user.role || 'employee'}`)
    } catch (error) {
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex">
      <div className="flex-1 flex items-center justify-center p-6 bg-white">
        <div className="w-full max-w-md animate-fade-up">
          <Link to="/" className="inline-flex items-center gap-2.5 mb-10">
            <div className="w-10 h-10 rounded-xl bg-primary-800 flex items-center justify-center shadow-md">
              <Zap size={20} className="text-white" />
            </div>
            <span className="text-2xl font-bold text-navy-700">ExpenseFlow</span>
          </Link>

          <h1 className="text-3xl font-bold text-navy-700 mb-2">Welcome back</h1>
          <p className="text-navy-300 mb-8">
            {step === 'EMAIL' 
              ? "Log in seamlessly using a one-time passcode." 
              : `We've sent a 6-digit code to ${email}`}
          </p>

          {step === 'EMAIL' ? (
            <form onSubmit={handleSendOtp} className="space-y-5">
              <div>
                <label className="block text-sm font-semibold text-navy-600 mb-2">Email address</label>
                <div className="relative">
                  <Mail size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-navy-200" />
                  <input
                    type="email"
                    required
                    className="input-field pl-11"
                    placeholder="john@company.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                </div>
              </div>
              
              <button
                type="submit"
                disabled={loading || !email}
                className="btn-primary w-full justify-center py-3.5 text-base rounded-xl"
              >
                {loading ? (
                  <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                ) : (
                  <>Send Magic OTP <ArrowRight size={16} /></>
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
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading || otp.length < 6}
                className="btn-primary w-full justify-center py-3.5 text-base rounded-xl"
              >
                {loading ? (
                  <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                ) : (
                  <>Verify & Sign In <ArrowRight size={16} /></>
                )}
              </button>

              <div className="text-center mt-4">
                <button
                  type="button"
                  onClick={() => setStep('EMAIL')}
                  className="text-sm font-medium text-navy-400 hover:text-navy-600 transition-colors"
                >
                  Use a different email
                </button>
              </div>
            </form>
          )}

          <p className="text-center text-sm text-navy-300 mt-8">
            Don&apos;t have an account?{' '}
            <Link to="/signup" className="text-primary-600 hover:text-primary-700 font-semibold transition-colors">
              Create account
            </Link>
          </p>
        </div>
      </div>

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
            Join 1000+ organizations using ExpenseFlow to automate expense management.
          </p>
        </div>
      </div>
    </div>
  )
}
