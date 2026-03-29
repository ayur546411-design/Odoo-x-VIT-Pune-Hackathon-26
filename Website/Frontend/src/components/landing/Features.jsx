import React from 'react'
import { ScanLine, Bot, LineChart, ShieldCheck, Globe, Zap, Workflow, Users, CreditCard } from 'lucide-react'
import { Link } from 'react-router-dom'

const features = [
  {
    icon: ScanLine,
    title: 'OCR Receipt Scanning',
    desc: 'Turn any receipt into a pre-filled expense in under 10 seconds. Our AI extracts amount, vendor, date, and category instantly — reducing manual data entry.',
    color: 'bg-violet-50 text-violet-600 border-violet-100',
    link: '#',
  },
  {
    icon: Bot,
    title: 'Smart Audit with AI',
    desc: 'Analyse all expenses with AI to detect non-compliant spend, flag duplicates, and reduce manual reviews. Smart rules catch policy violations before they cost you.',
    color: 'bg-blue-50 text-blue-600 border-blue-100',
    link: '#',
  },
  {
    icon: Workflow,
    title: 'Multi-Level Approval Workflow',
    desc: 'Configure flexible approval hierarchies — Manager → Finance → Director. Each expense moves step-by-step with automatic notifications at every stage.',
    color: 'bg-amber-50 text-amber-600 border-amber-100',
    link: '#',
  },
  {
    icon: ShieldCheck,
    title: 'Conditional Approval Logic',
    desc: 'Percentage-based approvals, specific approver overrides, or hybrid logic. Ensure flexibility for real-world enterprise scenarios with intelligent routing.',
    color: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    link: '#',
  },
  {
    icon: LineChart,
    title: 'Dashboard & Analytics',
    desc: 'Visual dashboards for total expenses, pending approvals, approved vs rejected, and spending trends. Make data-driven decisions with real-time insights.',
    color: 'bg-rose-50 text-rose-600 border-rose-100',
    link: '#',
  },
  {
    icon: Globe,
    title: 'Multi-Currency Support',
    desc: 'Submit expenses in any currency with real-time conversion rates. Country & currency detection ensures global usability for multinational teams.',
    color: 'bg-cyan-50 text-cyan-600 border-cyan-100',
    link: '#',
  },
  {
    icon: Users,
    title: 'Role-Based Access Control',
    desc: 'Secure role-based permissions — Admins manage workflows, Managers approve team expenses, Employees submit and track. Hierarchical employee-manager mapping.',
    color: 'bg-indigo-50 text-indigo-600 border-indigo-100',
    link: '#',
  },
  {
    icon: CreditCard,
    title: 'Instant Reimbursement',
    desc: 'Submit, approve, and reimburse in one simple flow. Once approved, reimbursements are processed instantly to reduce wait times for employees.',
    color: 'bg-teal-50 text-teal-600 border-teal-100',
    link: '#',
  },
]

export default function Features() {
  return (
    <section id="features" className="py-24 px-4 bg-white">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-16 animate-fade-up">
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-primary-50 border border-primary-100 text-primary-600 text-sm font-semibold mb-5">
            Product Features
          </div>
          <h2 className="section-title mb-5">
            Expense management. <span className="gradient-text-primary">Simplified.</span>
          </h2>
          <p className="section-subtitle mx-auto">
            From receipt scanning to reimbursement, we handle the entire expense lifecycle 
            with automation, transparency, and control.
          </p>
        </div>

        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-5">
          {features.map((f, i) => (
            <div
              key={f.title}
              className="card card-hover group cursor-pointer"
              style={{ animationDelay: `${i * 80}ms` }}
            >
              <div className={`w-12 h-12 rounded-2xl border flex items-center justify-center mb-5 group-hover:scale-110 transition-transform duration-300 ${f.color}`}>
                <f.icon size={22} />
              </div>
              <h3 className="text-base font-bold text-navy-700 mb-2.5 group-hover:text-primary-600 transition-colors">
                {f.title}
              </h3>
              <p className="text-sm text-navy-300 leading-relaxed mb-4">
                {f.desc}
              </p>
              <span className="inline-flex items-center gap-1.5 text-sm font-semibold text-primary-600 group-hover:gap-2.5 transition-all">
                Know more <span className="text-lg">→</span>
              </span>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
