import React from 'react'

const stats = [
  { value: '7.7x', label: 'Recurring ROI' },
  { value: '74%', label: 'Productivity Gains' },
  { value: '32K+', label: 'Hours Saved Per Year' },
  { value: '<24h', label: 'Avg. Approval Time' },
]

export default function Stats() {
  return (
    <section id="results" className="py-20 px-4 bg-white">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-14 animate-fade-up">
          <h2 className="section-title mb-4">
            Results you can <span className="gradient-text-primary">measure</span>
          </h2>
          <p className="section-subtitle mx-auto">
            Real impact for real businesses — from day one.
          </p>
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-6">
          {stats.map((s, i) => (
            <div
              key={s.label}
              className="text-center p-6 rounded-3xl bg-surface-100 border border-surface-200 animate-fade-up group hover:shadow-card hover:border-primary-200 transition-all duration-300"
              style={{ animationDelay: `${i * 100}ms` }}
            >
              <div className="text-4xl sm:text-5xl font-extrabold gradient-text-primary mb-3 group-hover:scale-105 transition-transform">
                {s.value}
              </div>
              <div className="text-sm text-navy-300 font-medium">{s.label}</div>
            </div>
          ))}
        </div>

        <p className="text-center text-xs text-navy-200 mt-8 max-w-lg mx-auto">
          Based on analysis for organizations with 500+ active users on the Enterprise plan.
        </p>
      </div>
    </section>
  )
}
