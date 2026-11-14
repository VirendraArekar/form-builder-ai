import api from './api';
import { ApiResponse, FormDetail, FormTemplate } from '@/types';

export const templateService = {
  async listTemplates(params?: { category?: string; search?: string }) {
    const res = await api.get<ApiResponse<FormTemplate[]>>('/templates', { params });
    return res.data.data;
  },

  async getTemplate(id: string) {
    const res = await api.get<ApiResponse<FormTemplate>>(`/templates/${id}`);
    return res.data.data;
  },

  async useTemplate(templateId: string, workspaceId: string) {
    const res = await api.post<ApiResponse<FormDetail>>(`/templates/${templateId}/use`, null, {
      params: { workspaceId },
    });
    return res.data.data;
  },
};
