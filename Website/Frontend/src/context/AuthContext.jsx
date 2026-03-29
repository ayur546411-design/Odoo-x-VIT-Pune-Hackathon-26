import React, { createContext, useContext, useState, useEffect } from 'react'
import { sendOtp, verifyOtp, logoutUser } from '../api/auth'
import toast from 'react-hot-toast'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const stored = localStorage.getItem('user')
    if (stored) {
      try {
        setUser(JSON.parse(stored))
      } catch {
        localStorage.removeItem('user')
      }
    }
    setLoading(false)
  }, [])

  const requestOtp = async (email) => {
    try {
      await sendOtp({ email })
      toast.success('OTP sent to your email!')
    } catch (err) {
      const msg = err.response?.data?.detail || err.response?.data?.message || 'Failed to send OTP'
      toast.error(msg)
      throw err
    }
  }

  // Accepts an object matching the OtpVerify schema from Backend
  const verifyOtpAndLogin = async (payload) => {
    try {
      const res = await verifyOtp(payload)
      const { access_token, user: userData } = res.data
      localStorage.setItem('token', access_token)
      localStorage.setItem('user', JSON.stringify(userData))
      setUser(userData)
      toast.success('Authentication successful!')
      return userData
    } catch (err) {
      const msg = err.response?.data?.detail || err.response?.data?.message || 'Invalid or expired OTP'
      toast.error(msg)
      throw err
    }
  }

  const logout = () => {
    logoutUser()
    setUser(null)
    toast.success('Logged out')
  }

  return (
    <AuthContext.Provider value={{ user, loading, requestOtp, verifyOtpAndLogin, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
