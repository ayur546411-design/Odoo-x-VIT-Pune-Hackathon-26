import api from './axios'

export const loginUser = (credentials) => api.post('/auth/login', credentials)

export const signupUser = (data) => api.post('/auth/register', data)

export const getProfile = () => api.get('/auth/profile')

export const logoutUser = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}
