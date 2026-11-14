import api from './api';
import { ApiResponse, FormAnalyticsOverview, QuestionAnalytics, WorkspaceAnalytics } from '@/types';

export const analyticsService = {
  async getFormOverview(formId: string, days = 30) {
    const res = await api.get<ApiResponse<FormAnalyticsOverview>>(`/forms/${formId}/analytics/overview`, {
      params: { days },
    });
    return res.data.data;
  },

  async getQuestionAnalytics(formId: string) {
    const res = await api.get<ApiResponse<QuestionAnalytics[]>>(`/forms/${formId}/analytics/questions`);
    return res.data.data;
  },

  async getWorkspaceAnalytics(workspaceId: string, days = 30) {
    const res = await api.get<ApiResponse<WorkspaceAnalytics>>(`/workspaces/${workspaceId}/analytics`, {
      params: { days },
    });
    return res.data.data;
  },
};
