import api from './axios'

export const getSupportedCurrencies = () => api.get('/currency/supported')

export const convertCurrency = (from, to, amount) =>
  api.get(`/currency/convert?from=${from}&to=${to}&amount=${amount}`)
