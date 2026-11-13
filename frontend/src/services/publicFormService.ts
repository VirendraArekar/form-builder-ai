import api from './api';
import { ApiResponse, PublicFormSchema } from '@/types';

export interface FormSubmissionRequest {
  accessPassword?: string;
  durationSeconds?: number;
  answers: Record<string, any> | Array<{ questionId: string; fieldKey: string; answerValue: any }>;
  metadata?: Record<string, any>;
}

export interface FormSubmissionResult {
  submissionId: string;
  submittedAt: string;
  thankYouTitle?: string;
  thankYouMessage?: string;
  redirectUrl?: string;
}

export const publicFormService = {
  async getPublicForm(slug: string, password?: string): Promise<PublicFormSchema> {
    const res = await api.get<ApiResponse<PublicFormSchema>>(`/public/forms/${slug}`, {
      params: password ? { password } : undefined,
      headers: password ? { 'X-Form-Password': password } : undefined,
    });
    return res.data.data;
  },

  async verifyPassword(slug: string, password: string): Promise<{ valid: boolean }> {
    try {
      await this.getPublicForm(slug, password);
      return { valid: true };
    } catch {
      return { valid: false };
    }
  },

  async submitForm(slug: string, submission: FormSubmissionRequest): Promise<FormSubmissionResult> {
    let answersMap: Record<string, any> = {};
    if (Array.isArray(submission.answers)) {
      submission.answers.forEach((ans) => {
        answersMap[ans.questionId] = ans.answerValue;
        if (ans.fieldKey) answersMap[ans.fieldKey] = ans.answerValue;
      });
    } else {
      answersMap = submission.answers;
    }

    const payload = {
      accessPassword: submission.accessPassword,
      durationSeconds: submission.durationSeconds || 0,
      answers: answersMap,
    };

    const res = await api.post<ApiResponse<FormSubmissionResult>>(`/public/forms/${slug}/submissions`, payload);
    return res.data.data;
  },

  async submitPublicForm(slug: string, data: { accessPassword?: string; durationSeconds: number; answers: Record<string, any> }): Promise<FormSubmissionResult> {
    return this.submitForm(slug, data);
  },
};
