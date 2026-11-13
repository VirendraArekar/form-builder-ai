import api from './api';
import { AiFormSchema, AiInsights, ApiResponse } from '@/types';

export const aiService = {
  async generateForm(workspaceId: string, prompt: string) {
    const res = await api.post<ApiResponse<AiFormSchema>>('/ai/forms/generate', {
      workspaceId,
      prompt,
    });
    return res.data.data;
  },

  async improveQuestion(data: { questionTitle: string; questionType?: string; goal?: string }) {
    const res = await api.post<ApiResponse<Record<string, string>>>('/ai/questions/improve', data);
    return res.data.data;
  },

  async translateForm(formId: string, targetLanguage: string) {
    const res = await api.post<ApiResponse<AiFormSchema>>('/ai/forms/translate', {
      formId,
      targetLanguage,
    });
    return res.data.data;
  },

  async generateInsights(params: { formId?: string; workspaceId?: string; question?: string }) {
    const res = await api.post<ApiResponse<AiInsights>>('/ai/insights/generate', params);
    return res.data.data;
  },
};
