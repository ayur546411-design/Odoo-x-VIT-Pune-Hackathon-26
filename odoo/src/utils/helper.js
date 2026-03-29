import { format, parseISO, isValid } from 'date-fns'

export function formatCurrency(amount, currency = 'INR') {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency,
    minimumFractionDigits: 0,
    maximumFractionDigits: 2,
  }).format(amount)
}

export function formatDate(dateString) {
  if (!dateString) return '—'
  const date = typeof dateString === 'string' ? parseISO(dateString) : dateString
  return isValid(date) ? format(date, 'dd MMM yyyy') : '—'
}

export function formatDateTime(dateString) {
  if (!dateString) return '—'
  const date = typeof dateString === 'string' ? parseISO(dateString) : dateString
  return isValid(date) ? format(date, 'dd MMM yyyy, hh:mm a') : '—'
}

export function getStatusColor(status) {
  const map = {
    pending: 'text-amber-400 bg-amber-400/10 border-amber-400/20',
    approved: 'text-emerald-400 bg-emerald-400/10 border-emerald-400/20',
    rejected: 'text-red-400 bg-red-400/10 border-red-400/20',
    paid: 'text-blue-400 bg-blue-400/10 border-blue-400/20',
    draft: 'text-slate-400 bg-slate-400/10 border-slate-400/20',
  }
  return map[status?.toLowerCase()] || map.draft
}

export function capitalize(str) {
  if (!str) return ''
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase()
}

export function truncate(str, len = 40) {
  if (!str) return ''
  return str.length > len ? str.slice(0, len) + '…' : str
}

export function getInitials(name) {
  if (!name) return '??'
  return name
    .split(' ')
    .map((w) => w[0])
    .join('')
    .toUpperCase()
    .slice(0, 2)
}
