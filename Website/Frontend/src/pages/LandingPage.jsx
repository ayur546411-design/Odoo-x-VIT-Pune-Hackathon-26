import React from 'react'
import Navbar from '../components/common/Navbar'
import Hero from '../components/landing/Hero'
import Stats from '../components/landing/Stats'
import Features from '../components/landing/Features'
import HowItWorks from '../components/landing/HowItWorks'
import WorkflowSection from '../components/landing/WorkflowSection'
import Support from '../components/landing/Support'
import Testimonials from '../components/landing/Testimonials'
import Footer from '../components/landing/Footer'

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-white">
      <Navbar />
      <Hero />
      <Stats />
      <Features />
      <HowItWorks />
      <WorkflowSection />
      <Support />
      <Testimonials />
      <Footer />
    </div>
  )
}
