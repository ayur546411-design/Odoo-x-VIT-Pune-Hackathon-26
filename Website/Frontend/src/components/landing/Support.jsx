import React from 'react'
import { Headphones, UserCheck, Globe } from 'lucide-react'

const supports = [
  {
    icon: Headphones,
    title: 'Effortless Onboarding',
    desc: 'Get set up quickly and confidently. Our expert team ensures a smooth go-live, guiding you through every step.',
    color: 'from-violet-500 to-purple-600',
  },
  {
    icon: UserCheck,
    title: 'Dedicated Success Manager',
    desc: 'A dedicated success manager will be with you at every stage — offering expert guidance and answering your questions.',
    color: 'from-blue-500 to-indigo-600',
  },
  {
    icon: Globe,
    title: 'Fast & Multilingual Support',
    desc: 'Our experienced support team is available to assist with any questions or technical needs, ensuring you are never left waiting.',
    color: 'from-emerald-500 to-teal-600',
  },
]

export default function Support() {
  return (
    <section className="py-24 px-4 bg-white">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-16 animate-fade-up">
          <h2 className="section-title mb-5">
            We're with you <span className="gradient-text-primary">every step</span>
          </h2>
          <p className="section-subtitle mx-auto">
            From onboarding to daily operations, our team has your back.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-6">
          {supports.map((s, i) => (
            <div
              key={s.title}
              className="card card-hover text-center group animate-fade-up"
              style={{ animationDelay: `${i * 120}ms` }}
            >
              <div className={`inline-flex w-14 h-14 rounded-2xl bg-gradient-to-br ${s.color} items-center justify-center mx-auto mb-5 shadow-lg group-hover:scale-110 transition-all duration-300`}>
                <s.icon size={24} className="text-white" />
              </div>
              <h3 className="text-lg font-bold text-navy-700 mb-3">{s.title}</h3>
              <p className="text-sm text-navy-300 leading-relaxed">{s.desc}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
