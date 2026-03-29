import React from 'react'
import { Upload, CheckCircle2, Banknote, ScanLine, Bell, ArrowRight } from 'lucide-react'

const steps = [
  {
    num: '01',
    icon: Upload,
    title: 'Submit',
    desc: 'Capture expenses in real-time. Upload receipts, scan with OCR, or fill the form. Our AI auto-extracts amount, vendor, and category in seconds.',
    color: 'from-violet-500 to-purple-600',
  },
  {
    num: '02',
    icon: CheckCircle2,
    title: 'Approve',
    desc: 'Managers review with AI-powered triage. Automated approvals for low-risk expenses. Multi-level workflows route to the right approver automatically.',
    color: 'from-blue-500 to-indigo-600',
  },
  {
    num: '03',
    icon: Banknote,
    title: 'Reimburse',
    desc: 'Finance teams get complete control. Approved expenses are processed for instant reimbursement with real-time dashboards and compliance tools.',
    color: 'from-emerald-500 to-teal-600',
  },
]

export default function HowItWorks() {
  return (
    <section id="how-it-works" className="py-24 px-4 bg-surface-100">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-16 animate-fade-up">
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-primary-50 border border-primary-100 text-primary-600 text-sm font-semibold mb-5">
            How It Works
          </div>
          <h2 className="section-title mb-5">
            Three steps to <span className="gradient-text-primary">stress-free</span> expenses
          </h2>
          <p className="section-subtitle mx-auto">
            From submission to reimbursement — automated, transparent, and fast.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8">
          {steps.map((s, i) => (
            <div
              key={s.title}
              className="relative animate-fade-up"
              style={{ animationDelay: `${(i + 1) * 150}ms` }}
            >
              <div className="card card-hover text-center group h-full">
                {/* Step Number */}
                <div className={`inline-flex w-14 h-14 rounded-2xl bg-gradient-to-br ${s.color} items-center justify-center mx-auto mb-6 shadow-lg group-hover:scale-110 group-hover:shadow-glow transition-all duration-300`}>
                  <s.icon size={24} className="text-white" />
                </div>
                
                <div className="text-xs font-bold text-primary-400 uppercase tracking-widest mb-2">
                  Step {s.num}
                </div>
                
                <h3 className="text-xl font-bold text-navy-700 mb-3">{s.title}</h3>
                <p className="text-sm text-navy-300 leading-relaxed">{s.desc}</p>
              </div>

              {/* Connector Arrow */}
              {i < 2 && (
                <div className="hidden md:flex absolute top-1/2 -right-4 transform -translate-y-1/2 z-10">
                  <div className="w-8 h-8 rounded-full bg-white border border-surface-300 flex items-center justify-center shadow-sm">
                    <ArrowRight size={14} className="text-primary-400" />
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
