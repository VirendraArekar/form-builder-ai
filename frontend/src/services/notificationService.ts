import api from './api';
import { ApiResponse } from '@/types';

export interface NotificationItem {
  id: string;
  title: string;
  message: string;
  type: string;
  isRead: boolean;
  linkUrl?: string;
  createdAt: string;
}

export const notificationService = {
  async listNotifications() {
    const res = await api.get<ApiResponse<NotificationItem[]>>('/notifications');
    return res.data.data;
  },

  async markAsRead(id: string) {
    const res = await api.patch<ApiResponse<null>>(`/notifications/${id}/read`);
    return res.data;
  },

  async markAllAsRead() {
    const res = await api.patch<ApiResponse<null>>('/notifications/read-all');
    return res.data;
  },
};
