import api from './api';
import { ApiResponse } from '@/types';

export interface WebhookDto {
  id: string;
  workspaceId: string;
  name: string;
  targetUrl: string;
  secretKey?: string;
  events: string[];
  isActive: boolean;
  createdAt: string;
}

export const webhookService = {
  async listWebhooks(workspaceId: string) {
    const res = await api.get<ApiResponse<WebhookDto[]>>(`/workspaces/${workspaceId}/webhooks`);
    return res.data.data;
  },

  async createWebhook(workspaceId: string, data: { name: string; targetUrl: string; secretKey?: string; events: string[] }) {
    const res = await api.post<ApiResponse<WebhookDto>>(`/workspaces/${workspaceId}/webhooks`, data);
    return res.data.data;
  },

  async deleteWebhook(workspaceId: string, webhookId: string) {
    const res = await api.delete<ApiResponse<null>>(`/workspaces/${workspaceId}/webhooks/${webhookId}`);
    return res.data;
  },

  async testWebhook(workspaceId: string, webhookId: string) {
    const res = await api.post<ApiResponse<boolean>>(`/workspaces/${workspaceId}/webhooks/${webhookId}/test`);
    return res.data.data;
  },
};
