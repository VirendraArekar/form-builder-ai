import api from './api';
import { ApiResponse, FormDetail, FormSummary, PageResponse } from '@/types';

export const formService = {
  async listForms(params: {
    workspaceId: string;
    status?: string;
    search?: string;
    page?: number;
    size?: number;
    sort?: string;
  }) {
    const res = await api.get<ApiResponse<PageResponse<FormSummary>>>('/forms', { params });
    return res.data.data;
  },

  async getForm(id: string) {
    const res = await api.get<ApiResponse<FormDetail>>(`/forms/${id}`);
    return res.data.data;
  },

  async createForm(data: { workspaceId: string; title: string; description?: string }) {
    const res = await api.post<ApiResponse<FormDetail>>('/forms', data);
    return res.data.data;
  },

  async updateForm(id: string, data: Partial<FormDetail> & { accessPassword?: string }) {
    const res = await api.put<ApiResponse<FormDetail>>(`/forms/${id}`, data);
    return res.data.data;
  },

  async saveFormSchema(id: string, schema: {
    title?: string;
    description?: string;
    sections: any[];
    questions: any[];
    logicRules: any[];
    theme: any;
  }) {
    const res = await api.put<ApiResponse<FormDetail>>(`/forms/${id}/schema`, schema);
    return res.data.data;
  },

  async duplicateForm(id: string) {
    const res = await api.post<ApiResponse<FormDetail>>(`/forms/${id}/duplicate`);
    return res.data.data;
  },

  async publishForm(id: string, customSlug?: string) {
    const res = await api.post<ApiResponse<FormDetail>>(`/forms/${id}/publish`, { customSlug });
    return res.data.data;
  },

  async unpublishForm(id: string) {
    const res = await api.post<ApiResponse<FormDetail>>(`/forms/${id}/unpublish`);
    return res.data.data;
  },

  async archiveForm(id: string) {
    const res = await api.post<ApiResponse<null>>(`/forms/${id}/archive`);
    return res.data;
  },

  async restoreForm(id: string) {
    const res = await api.post<ApiResponse<null>>(`/forms/${id}/restore`);
    return res.data;
  },

  async deleteForm(id: string) {
    const res = await api.delete<ApiResponse<null>>(`/forms/${id}`);
    return res.data;
  },
};
