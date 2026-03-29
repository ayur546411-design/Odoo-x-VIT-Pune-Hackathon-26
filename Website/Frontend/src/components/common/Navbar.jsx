import React, { useState, useEffect } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { LogOut, Menu, X, ChevronDown, Zap } from 'lucide-react'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [mobileOpen, setMobileOpen] = useState(false)
  const [scrolled, setScrolled] = useState(false)

  const isLanding = location.pathname === '/'
  const isDark = isLanding && !scrolled

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20)
    window.addEventListener('scroll', onScroll)
    return () => window.removeEventListener('scroll', onScroll)
  }, [])

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  return (
    <nav className={`fixed top-0 left-0 right-0 z-50 transition-all duration-500 ${
      scrolled
        ? 'glass-nav shadow-glass py-2'
        : isLanding
          ? 'bg-transparent py-4'
          : 'glass-nav shadow-glass py-2'
    }`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-14">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2.5 group">
            <div className={`w-9 h-9 rounded-xl flex items-center justify-center transition-all duration-300 group-hover:scale-105 ${
              isDark
                ? 'bg-white/15 border border-white/20'
                : 'bg-primary-800 shadow-md'
            }`}>
              <Zap size={18} className="text-white" />
            </div>
            <span className={`text-xl font-bold tracking-tight transition-colors ${
              isDark ? 'text-white' : 'text-navy-700'
            }`}>
              ExpenseFlow
            </span>
          </Link>

          {/* Desktop Nav */}
          <div className="hidden md:flex items-center gap-1">
            {isLanding && (
              <>
                {[
                  { label: 'Features', href: '#features' },
                  { label: 'How it Works', href: '#how-it-works' },
                  { label: 'Results', href: '#results' },
                ].map((item) => (
                  <a
                    key={item.label}
                    href={item.href}
                    className={`px-4 py-2 rounded-full text-sm font-medium transition-all duration-200 ${
                      isDark
                        ? 'text-white/80 hover:text-white hover:bg-white/10'
                        : 'text-navy-400 hover:text-navy-700 hover:bg-surface-100'
                    }`}
                  >
                    {item.label}
                  </a>
                ))}
              </>
            )}
          </div>

          {/* Desktop CTA */}
          <div className="hidden md:flex items-center gap-3">
            {!user ? (
              <>
                <Link
                  to="/login"
                  className={`px-5 py-2.5 rounded-full text-sm font-semibold border-2 transition-all duration-300 hover:-translate-y-0.5 ${
                    isDark
                      ? 'border-white/30 text-white hover:bg-white/10 hover:border-white/50'
                      : 'border-primary-800 text-primary-800 hover:bg-primary-50'
                  }`}
                >
                  Login
                </Link>
                <Link
                  to="/signup"
                  className={`px-5 py-2.5 rounded-full text-sm font-semibold transition-all duration-300 hover:-translate-y-0.5 shadow-md hover:shadow-lg ${
                    isDark
                      ? 'bg-white text-primary-800 hover:bg-white/90'
                      : 'bg-primary-800 text-white hover:bg-primary-900'
                  }`}
                >
                  Get Started
                </Link>
              </>
            ) : (
              <>
                <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-surface-100 border border-surface-300">
                  <div className="w-7 h-7 rounded-full bg-primary-100 flex items-center justify-center">
                    <span className="text-xs font-bold text-primary-600">
                      {user.name?.charAt(0)?.toUpperCase() || 'U'}
                    </span>
                  </div>
                  <span className="text-sm font-medium text-navy-600">{user.name}</span>
                  <span className="text-xs px-2 py-0.5 rounded-full bg-primary-50 text-primary-600 font-semibold capitalize">
                    {user.role}
                  </span>
                </div>
                <button onClick={handleLogout} className="btn-ghost text-sm text-navy-400 hover:text-red-500">
                  <LogOut size={16} /> Logout
                </button>
              </>
            )}
          </div>

          {/* Mobile Toggle */}
          <button
            className={`md:hidden p-2 rounded-xl transition-colors ${
              isDark ? 'text-white hover:bg-white/10' : 'text-navy-600 hover:bg-surface-100'
            }`}
            onClick={() => setMobileOpen(!mobileOpen)}
          >
            {mobileOpen ? <X size={22} /> : <Menu size={22} />}
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileOpen && (
        <div className="md:hidden bg-white border-t border-surface-200 shadow-elevated animate-fade-in">
          <div className="px-4 py-5 space-y-2">
            {isLanding && (
              <>
                {['Features', 'How it Works', 'Results'].map((label) => (
                  <a
                    key={label}
                    href={`#${label.toLowerCase().replace(/\s/g, '-')}`}
                    className="block px-4 py-2.5 rounded-xl text-sm font-medium text-navy-500 hover:bg-surface-100"
                    onClick={() => setMobileOpen(false)}
                  >
                    {label}
                  </a>
                ))}
                <div className="border-t border-surface-200 my-3" />
              </>
            )}
            {!user ? (
              <div className="space-y-2">
                <Link
                  to="/login"
                  className="block text-center btn-secondary text-sm justify-center"
                  onClick={() => setMobileOpen(false)}
                >
                  Login
                </Link>
                <Link
                  to="/signup"
                  className="block text-center btn-primary text-sm justify-center"
                  onClick={() => setMobileOpen(false)}
                >
                  Get Started
                </Link>
              </div>
            ) : (
              <button
                onClick={() => { handleLogout(); setMobileOpen(false); }}
                className="w-full btn-secondary text-sm justify-center text-red-500 border-red-200 hover:bg-red-50"
              >
                <LogOut size={16} /> Logout
              </button>
            )}
          </div>
        </div>
      )}
    </nav>
  )
}
