import api from './api';
import { ApiResponse, WorkspaceInsightsDetailDto } from '@/types';

export const insightsService = {
  async getGlobalInsights() {
    const res = await api.get<ApiResponse<WorkspaceInsightsDetailDto>>('/insights');
    return res.data.data;
  },
};
