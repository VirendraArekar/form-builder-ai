import api from './api';
import { ApiResponse, FormResponseDetail, FormResponseSummary, PageResponse, ResponseNote, ResponseTag } from '@/types';

export const responseService = {
  async listResponses(formId: string, params: {
    status?: string;
    isReviewed?: boolean;
    page?: number;
    size?: number;
    sort?: string;
  }) {
    const res = await api.get<ApiResponse<PageResponse<FormResponseSummary>>>(`/forms/${formId}/responses`, { params });
    return res.data.data;
  },

  async getResponseDetails(formId: string, responseId: string) {
    const res = await api.get<ApiResponse<FormResponseDetail>>(`/forms/${formId}/responses/${responseId}`);
    return res.data.data;
  },

  async deleteResponse(formId: string, responseId: string) {
    const res = await api.delete<ApiResponse<null>>(`/forms/${formId}/responses/${responseId}`);
    return res.data;
  },

  async bulkDeleteResponses(formId: string, responseIds: string[]) {
    const res = await api.post<ApiResponse<null>>(`/forms/${formId}/responses/bulk-delete`, { responseIds });
    return res.data;
  },

  async toggleReview(formId: string, responseId: string) {
    const res = await api.patch<ApiResponse<null>>(`/forms/${formId}/responses/${responseId}/review`);
    return res.data;
  },

  async addNote(formId: string, responseId: string, noteText: string) {
    const res = await api.post<ApiResponse<ResponseNote>>(`/forms/${formId}/responses/${responseId}/notes`, { noteText });
    return res.data.data;
  },

  async addTag(formId: string, responseId: string, data: { tagName: string; color?: string }) {
    const res = await api.post<ApiResponse<ResponseTag>>(`/forms/${formId}/responses/${responseId}/tags`, data);
    return res.data.data;
  },

  async removeTag(formId: string, responseId: string, tagId: string) {
    const res = await api.delete<ApiResponse<null>>(`/forms/${formId}/responses/${responseId}/tags/${tagId}`);
    return res.data;
  },

  async exportCsv(formId: string) {
    const res = await api.get(`/forms/${formId}/responses/export`, { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `form-${formId}-responses.csv`);
    document.body.appendChild(link);
    link.click();
    link.remove();
  },
};
