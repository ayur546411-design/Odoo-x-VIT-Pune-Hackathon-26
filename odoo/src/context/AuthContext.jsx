import React, { createContext, useContext, useState, useEffect } from 'react'
import { loginUser, signupUser, logoutUser } from '../api/auth'
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

  const login = async (credentials) => {
    try {
      const res = await loginUser(credentials)
      const { token, user: userData } = res.data
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(userData))
      setUser(userData)
      toast.success('Welcome back!')
      return userData
    } catch (err) {
      const msg = err.response?.data?.message || 'Login failed'
      toast.error(msg)
      throw err
    }
  }

  const signup = async (data) => {
    try {
      const res = await signupUser(data)
      const { token, user: userData } = res.data
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(userData))
      setUser(userData)
      toast.success('Account created!')
      return userData
    } catch (err) {
      const msg = err.response?.data?.message || 'Signup failed'
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
    <AuthContext.Provider value={{ user, loading, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
