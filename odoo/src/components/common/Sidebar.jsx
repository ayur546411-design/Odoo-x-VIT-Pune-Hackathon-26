import React from 'react'
import { NavLink } from 'react-router-dom'
import { Zap } from 'lucide-react'

export default function Sidebar({ links }) {
  return (
    <aside className="hidden lg:flex flex-col w-64 min-h-[calc(100vh-4rem)] border-r border-surface-200 bg-white pt-6 px-4">
      <div className="space-y-1">
        {links.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            end={to.split('/').length <= 2}
            className={({ isActive }) =>
              `sidebar-link ${isActive ? 'sidebar-link-active' : 'sidebar-link-inactive'}`
            }
          >
            <Icon size={18} />
            {label}
          </NavLink>
        ))}
      </div>
    </aside>
  )
}
