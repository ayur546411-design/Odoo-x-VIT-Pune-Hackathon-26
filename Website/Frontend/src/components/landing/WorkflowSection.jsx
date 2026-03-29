import React from 'react'
import { UserPlus, Settings, FileText, ScanLine, GitBranch, CheckCircle2, Brain, Bell } from 'lucide-react'

const steps = [
  { icon: UserPlus, label: 'User registers', desc: 'Company created automatically' },
  { icon: Settings, label: 'Admin configures', desc: 'Roles & approval rules set' },
  { icon: FileText, label: 'Employee submits', desc: 'Digital expense submission' },
  { icon: ScanLine, label: 'OCR extracts', desc: 'Receipt data auto-extracted' },
  { icon: GitBranch, label: 'System routes', desc: 'Expense sent to approvers' },
  { icon: CheckCircle2, label: 'Approvers review', desc: 'Multi-level review & action' },
  { icon: Brain, label: 'Logic decides', desc: 'Conditional approval rules' },
  { icon: Bell, label: 'Status updated', desc: 'Real-time notification' },
]

export default function WorkflowSection() {
  return (
    <section className="py-24 px-4 bg-white">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-16 animate-fade-up">
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-primary-50 border border-primary-100 text-primary-600 text-sm font-semibold mb-5">
            System Workflow
          </div>
          <h2 className="section-title mb-5">
            End-to-end <span className="gradient-text-primary">automation</span>
          </h2>
          <p className="section-subtitle mx-auto">
            From registration to reimbursement — every step is automated, tracked, and transparent.
          </p>
        </div>

        <div className="relative">
          {/* Timeline line */}
          <div className="absolute left-1/2 top-0 bottom-0 w-px bg-surface-300 hidden lg:block" />
          
          <div className="grid lg:grid-cols-2 gap-x-16 gap-y-6">
            {steps.map((s, i) => (
              <div
                key={s.label}
                className={`flex items-start gap-4 animate-fade-up ${
                  i % 2 === 0 ? 'lg:justify-end lg:text-right' : 'lg:col-start-2'
                }`}
                style={{ animationDelay: `${i * 80}ms` }}
              >
                <div className={`flex items-start gap-4 max-w-sm ${i % 2 === 0 ? 'lg:flex-row-reverse' : ''}`}>
                  <div className="relative shrink-0">
                    <div className="w-12 h-12 rounded-2xl bg-primary-50 border border-primary-100 flex items-center justify-center group-hover:scale-110 transition-transform">
                      <s.icon size={20} className="text-primary-500" />
                    </div>
                    <div className="absolute -top-1 -right-1 w-5 h-5 rounded-full bg-primary-600 text-white text-[10px] font-bold flex items-center justify-center">
                      {i + 1}
                    </div>
                  </div>
                  <div>
                    <h4 className="text-sm font-bold text-navy-700">{s.label}</h4>
                    <p className="text-xs text-navy-300 mt-0.5">{s.desc}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}
