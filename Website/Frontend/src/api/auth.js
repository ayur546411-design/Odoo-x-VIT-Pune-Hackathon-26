import api from './axios'

export const sendOtp = (data) => api.post('/auth/send-otp', data)

export const verifyOtp = (data) => api.post('/auth/verify-otp', data)

export const getProfile = () => api.get('/auth/profile')

export const logoutUser = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}
