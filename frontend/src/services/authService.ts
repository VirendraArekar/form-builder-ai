import api from './api';
import { ApiResponse, User, WorkspaceSummary } from '@/types';

export interface AuthResponseData {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  user: User;
  activeWorkspace?: WorkspaceSummary;
}

export const authService = {
  async register(data: { email: string; password: string; firstName: string; lastName: string; companyName?: string }) {
    const res = await api.post<ApiResponse<AuthResponseData>>('/auth/register', data);
    return res.data.data;
  },

  async login(data: { email: string; password: string }) {
    const res = await api.post<ApiResponse<AuthResponseData>>('/auth/login', data);
    return res.data.data;
  },

  async getCurrentUser() {
    const res = await api.get<ApiResponse<AuthResponseData>>('/auth/me');
    return res.data.data;
  },

  async forgotPassword(email: string) {
    const res = await api.post<ApiResponse<null>>('/auth/forgot-password', { email });
    return res.data;
  },

  async resetPassword(token: string, newPassword: string) {
    const res = await api.post<ApiResponse<null>>('/auth/reset-password', { token, newPassword });
    return res.data;
  },

  async getProfile() {
    const res = await api.get<ApiResponse<User>>('/users/profile');
    return res.data.data;
  },

  async updateProfile(data: Partial<User>) {
    const res = await api.put<ApiResponse<User>>('/users/profile', data);
    return res.data.data;
  },

  async changePassword(data: { currentPassword: string; newPassword: string }) {
    const res = await api.put<ApiResponse<null>>('/users/password', data);
    return res.data;
  },

  async deleteAccount() {
    const res = await api.delete<ApiResponse<null>>('/users/account');
    return res.data;
  },
};
