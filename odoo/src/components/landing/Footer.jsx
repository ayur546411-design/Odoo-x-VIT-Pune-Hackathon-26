import React from 'react'
import { Link } from 'react-router-dom'
import { Zap, ArrowRight, Shield, Award, Lock } from 'lucide-react'

export default function Footer() {
  return (
    <footer className="bg-navy-700 text-white">
      {/* CTA Banner */}
      <div className="relative overflow-hidden">
        <div className="gradient-hero py-20 px-4">
          <div className="absolute inset-0 overflow-hidden">
            <div className="absolute top-10 left-1/4 w-64 h-64 bg-primary-400/10 rounded-full blur-[100px]" />
            <div className="absolute bottom-10 right-1/4 w-80 h-80 bg-primary-300/8 rounded-full blur-[120px]" />
          </div>
          <div className="relative z-10 max-w-3xl mx-auto text-center">
            <h2 className="text-3xl sm:text-4xl font-bold text-white mb-4">
              Trusted by 1000+ professionals
            </h2>
            <p className="text-white/60 text-lg mb-8 max-w-xl mx-auto">
              Join organizations that have transformed their expense management with ExpenseFlow.
            </p>
            <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
              <Link
                to="/signup"
                className="bg-white text-primary-800 font-semibold px-8 py-4 rounded-full text-base shadow-elevated hover:-translate-y-0.5 transition-all duration-300 flex items-center gap-2"
              >
                Get Started <ArrowRight size={18} />
              </Link>
            </div>
            {/* Trust badges */}
            <div className="flex items-center justify-center gap-6 mt-8">
              {[
                { icon: Shield, label: 'GDPR Compliant' },
                { icon: Lock, label: 'ISO 27001' },
                { icon: Award, label: 'SOC 2 Type 2' },
              ].map((b) => (
                <div key={b.label} className="flex items-center gap-1.5 text-white/50 text-xs font-medium">
                  <b.icon size={14} />
                  {b.label}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Footer Links */}
      <div className="max-w-6xl mx-auto px-4 py-16">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-10">
          <div>
            <div className="flex items-center gap-2.5 mb-6">
              <div className="w-9 h-9 rounded-xl bg-primary-600 flex items-center justify-center">
                <Zap size={18} className="text-white" />
              </div>
              <span className="text-lg font-bold">ExpenseFlow</span>
            </div>
            <p className="text-white/40 text-sm leading-relaxed">
              Smart expense management for modern organizations.
            </p>
          </div>

          {[
            {
              title: 'Product',
              links: [
                { label: 'Expense Management', href: '#features' },
                { label: 'OCR Scanner', href: '#features' },
                { label: 'Smart Approvals', href: '#features' },
                { label: 'Analytics', href: '#features' },
              ],
            },
            {
              title: 'Company',
              links: [
                { label: 'About Us', href: '#' },
                { label: 'Careers', href: '#' },
                { label: 'Security', href: '#' },
                { label: 'Contact', href: '#' },
              ],
            },
            {
              title: 'Resources',
              links: [
                { label: 'Help Center', href: '#' },
                { label: 'Documentation', href: '#' },
                { label: 'API Reference', href: '#' },
                { label: 'Blog', href: '#' },
              ],
            },
          ].map((section) => (
            <div key={section.title}>
              <h4 className="text-sm font-semibold text-white/90 uppercase tracking-wider mb-5">
                {section.title}
              </h4>
              <ul className="space-y-3">
                {section.links.map((link) => (
                  <li key={link.label}>
                    <a
                      href={link.href}
                      className="text-sm text-white/40 hover:text-white transition-colors"
                    >
                      {link.label}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        <div className="border-t border-white/10 mt-12 pt-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p className="text-white/30 text-sm">
            © {new Date().getFullYear()} ExpenseFlow — Built for Odoo × VIT Pune Hackathon
          </p>
          <div className="flex items-center gap-6 text-white/30 text-sm">
            <a href="#" className="hover:text-white/60 transition-colors">Privacy Policy</a>
            <a href="#" className="hover:text-white/60 transition-colors">Terms of Service</a>
          </div>
        </div>
      </div>
    </footer>
  )
}
