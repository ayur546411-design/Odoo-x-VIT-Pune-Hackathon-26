import React from 'react'
import { Link } from 'react-router-dom'
import { ArrowRight, Sparkles, ScanLine, ShieldCheck, LineChart, Globe } from 'lucide-react'

export default function Hero() {
  return (
    <section className="relative overflow-hidden">
      {/* Gradient Background */}
      <div className="gradient-hero min-h-[92vh] flex flex-col items-center justify-center relative">
        {/* Decorative Shapes */}
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute top-20 left-10 w-72 h-72 bg-primary-400/10 rounded-full blur-[100px] animate-pulse-soft" />
          <div className="absolute bottom-20 right-10 w-96 h-96 bg-primary-300/8 rounded-full blur-[120px] animate-pulse-soft delay-500" />
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-primary-500/5 rounded-full blur-[150px]" />
          {/* Grid pattern overlay */}
          <div className="absolute inset-0 opacity-[0.03]" style={{
            backgroundImage: 'linear-gradient(rgba(255,255,255,0.1) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,0.1) 1px, transparent 1px)',
            backgroundSize: '60px 60px',
          }} />
        </div>

        <div className="relative z-10 max-w-5xl mx-auto px-4 text-center pt-24 pb-8">
          {/* Badge */}
          <div className="inline-flex items-center gap-2 px-5 py-2 rounded-full bg-white/10 border border-white/15 text-white/90 text-sm font-medium mb-8 animate-fade-up backdrop-blur-sm">
            <Sparkles size={14} className="text-primary-200" />
            Smart Expense Management Platform
          </div>

          {/* Headline */}
          <h1 className="text-4xl sm:text-5xl lg:text-[3.75rem] font-extrabold leading-[1.1] mb-6 animate-fade-up delay-100">
            <span className="text-white/90">The smartest way to </span>
            <br className="hidden sm:block" />
            <span className="text-white">manage business expenses</span>
          </h1>

          {/* Subtitle */}
          <p className="text-white/60 text-lg sm:text-xl max-w-2xl mx-auto mb-10 leading-relaxed animate-fade-up delay-200">
            ExpenseFlow is a smart expense management platform that gives you full control 
            over employee spending, ensures compliance, and increases efficiency by 4x.
          </p>

          {/* CTAs */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 animate-fade-up delay-300">
            <Link
              to="/signup"
              className="bg-white text-primary-800 font-semibold px-8 py-4 rounded-full text-base shadow-elevated hover:shadow-glow hover:-translate-y-1 transition-all duration-300 flex items-center gap-2"
            >
              Get Started Free <ArrowRight size={18} />
            </Link>
            <Link
              to="/login"
              className="border-2 border-white/25 text-white font-semibold px-8 py-4 rounded-full text-base hover:bg-white/10 hover:border-white/40 transition-all duration-300 flex items-center gap-2"
            >
              Log in to Dashboard
            </Link>
          </div>
        </div>

        {/* Floating Product Cards */}
        <div className="relative z-10 max-w-6xl mx-auto px-4 pb-8 w-full animate-fade-up delay-500">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 sm:gap-4">
            {[
              { icon: ScanLine, label: 'OCR Receipt Scanning', desc: 'Auto-extract data' },
              { icon: ShieldCheck, label: 'Smart Approvals', desc: 'AI-powered audit' },
              { icon: LineChart, label: 'Real-time Analytics', desc: 'Track spending' },
              { icon: Globe, label: 'Multi-Currency', desc: 'Global support' },
            ].map((item, i) => (
              <div
                key={item.label}
                className="bg-white/10 backdrop-blur-md border border-white/10 rounded-2xl p-4 sm:p-5 hover:bg-white/15 hover:border-white/20 transition-all duration-300 group"
              >
                <div className="w-10 h-10 rounded-xl bg-white/10 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <item.icon size={20} className="text-white/80" />
                </div>
                <h3 className="text-white text-sm font-semibold mb-1">{item.label}</h3>
                <p className="text-white/50 text-xs">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Wave Divider */}
        <div className="absolute bottom-0 left-0 right-0">
          <svg viewBox="0 0 1440 100" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-full">
            <path d="M0 40C240 80 480 100 720 80C960 60 1200 20 1440 40V100H0V40Z" fill="white"/>
          </svg>
        </div>
      </div>
    </section>
  )
}
