import api from './axios'

export const getMyExpenses = () => api.get('/expenses/my')

export const getAllExpenses = () => api.get('/expenses')

export const getTeamExpenses = () => api.get('/expenses/team')

export const getPendingApprovals = () => api.get('/expenses/pending')

export const createExpense = (data) => {
  const formData = new FormData()
  Object.entries(data).forEach(([key, value]) => {
    if (value !== undefined && value !== null) formData.append(key, value)
  })
  return api.post('/expenses', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export const updateExpenseStatus = (id, status, comment) =>
  api.patch(`/expenses/${id}/status`, { status, comment })

export const getExpenseStats = () => api.get('/expenses/stats')

export const scanReceipt = (file) => {
  const formData = new FormData()
  formData.append('receipt', file)
  return api.post('/expenses/scan', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
