import api from './api';
import { ApiResponse, PageResponse, InboxItemDto } from '@/types';

export const inboxService = {
  async getInbox(params: {
    formId?: string;
    search?: string;
    page?: number;
    size?: number;
  }) {
    const res = await api.get<ApiResponse<PageResponse<InboxItemDto>>>('/inbox', { params });
    return res.data.data;
  },
};
