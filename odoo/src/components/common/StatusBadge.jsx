import React from 'react'
import { capitalize } from '../../utils/helper'

const statusMap = {
  pending: 'badge-pending',
  approved: 'badge-approved',
  rejected: 'badge-rejected',
  paid: 'badge-paid',
  draft: 'badge-draft',
}

export default function StatusBadge({ status }) {
  const cls = statusMap[status?.toLowerCase()] || statusMap.draft
  return (
    <span className={`badge ${cls}`}>
      <span className="w-1.5 h-1.5 rounded-full bg-current" />
      {capitalize(status)}
    </span>
  )
}
