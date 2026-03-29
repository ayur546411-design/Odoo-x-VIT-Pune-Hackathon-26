import React from 'react'
import { Star, Quote } from 'lucide-react'

const testimonials = [
  {
    name: 'Priya Sharma',
    role: 'Finance Director, TechCorp',
    text: 'ExpenseFlow eliminated 80% of our manual expense processing. The OCR scanner is incredibly accurate and the approval workflows save us hours every week.',
    rating: 5,
  },
  {
    name: 'Amit Patel',
    role: 'CFO, GlobalTech Solutions',
    text: 'The multi-currency support and real-time analytics give us complete visibility across our international offices. Implementation was seamless.',
    rating: 5,
  },
  {
    name: 'Sneha Gupta',
    role: 'HR Manager, InnovateLabs',
    text: 'Our employees love how easy it is to submit expenses. Scan a receipt, and it auto-fills everything. Reimbursement time dropped from 2 weeks to 24 hours.',
    rating: 5,
  },
]

export default function Testimonials() {
  return (
    <section className="py-24 px-4 bg-surface-100">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-16 animate-fade-up">
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-primary-50 border border-primary-100 text-primary-600 text-sm font-semibold mb-5">
            Customer Stories
          </div>
          <h2 className="section-title mb-5">
            Loved by <span className="gradient-text-primary">finance teams</span> everywhere
          </h2>
          <p className="section-subtitle mx-auto">
            See why organizations trust ExpenseFlow for their expense management.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-6">
          {testimonials.map((t, i) => (
            <div
              key={t.name}
              className="card card-hover animate-fade-up"
              style={{ animationDelay: `${i * 120}ms` }}
            >
              {/* Quote icon */}
              <div className="w-10 h-10 rounded-xl bg-primary-50 border border-primary-100 flex items-center justify-center mb-5">
                <Quote size={18} className="text-primary-400" />
              </div>

              {/* Stars */}
              <div className="flex gap-1 mb-4">
                {[...Array(t.rating)].map((_, j) => (
                  <Star key={j} size={14} className="text-amber-400 fill-amber-400" />
                ))}
              </div>

              {/* Text */}
              <p className="text-sm text-navy-400 leading-relaxed mb-6">
                "{t.text}"
              </p>

              {/* Author */}
              <div className="flex items-center gap-3 pt-4 border-t border-surface-200">
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center">
                  <span className="text-sm font-bold text-white">
                    {t.name.split(' ').map(n => n[0]).join('')}
                  </span>
                </div>
                <div>
                  <div className="text-sm font-semibold text-navy-700">{t.name}</div>
                  <div className="text-xs text-navy-300">{t.role}</div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
