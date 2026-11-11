import axios, { AxiosError } from 'axios';
import { notify } from '@/store/toastStore';

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('formgenius_access_token');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as any;

    // Handle token refresh on 401
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = localStorage.getItem('formgenius_refresh_token');
      if (refreshToken) {
        try {
          const res = await axios.post('/api/v1/auth/refresh', { refreshToken });
          const newAccess = res.data.data.accessToken;
          const newRefresh = res.data.data.refreshToken;
          localStorage.setItem('formgenius_access_token', newAccess);
          if (newRefresh) localStorage.setItem('formgenius_refresh_token', newRefresh);
          originalRequest.headers.Authorization = `Bearer ${newAccess}`;
          return api(originalRequest);
        } catch {
          localStorage.removeItem('formgenius_access_token');
          localStorage.removeItem('formgenius_refresh_token');
          window.location.href = '/login';
          return Promise.reject(error);
        }
      }
    }

    // Extract error message for notification
    const status = error.response?.status;
    const responseData = error.response?.data as any;
    const rawMsg = responseData?.message || responseData?.error || error.message;

    // Only notify if not a 401 redirect or duplicate toast
    if (status !== 401 && !originalRequest?.skipErrorToast) {
      let displayMessage = rawMsg;
      if (!error.response) {
        displayMessage = 'Network connection error. Please verify your internet or server status.';
      } else if (status === 403) {
        displayMessage = 'Access Denied: You do not have permission to perform this action.';
      } else if (status === 404) {
        displayMessage = rawMsg || 'The requested resource was not found.';
      } else if (status === 500) {
        displayMessage = rawMsg || 'Server error occurred. Please try again shortly.';
      }
      notify.error(displayMessage);
    }

    return Promise.reject(error);
  }
);

export default api;
