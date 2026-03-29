import api from './axios'

export const getAllUsers = () => api.get('/users')

export const updateUserRole = (id, role) => api.patch(`/users/${id}/role`, { role })

export const deleteUser = (id) => api.delete(`/users/${id}`)

export const getUserStats = () => api.get('/users/stats')
