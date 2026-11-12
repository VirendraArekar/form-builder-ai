import api from './api';
import { ApiResponse, Workspace, WorkspaceMember } from '@/types';

export const workspaceService = {
  async listWorkspaces() {
    const res = await api.get<ApiResponse<Workspace[]>>('/workspaces');
    return res.data.data;
  },

  async getWorkspace(id: string) {
    const res = await api.get<ApiResponse<Workspace>>(`/workspaces/${id}`);
    return res.data.data;
  },

  async createWorkspace(data: { name: string; description?: string; logoUrl?: string }) {
    const res = await api.post<ApiResponse<Workspace>>('/workspaces', data);
    return res.data.data;
  },

  async updateWorkspace(id: string, data: { name: string; description?: string; logoUrl?: string }) {
    const res = await api.put<ApiResponse<Workspace>>(`/workspaces/${id}`, data);
    return res.data.data;
  },

  async listMembers(workspaceId: string) {
    const res = await api.get<ApiResponse<WorkspaceMember[]>>(`/workspaces/${workspaceId}/members`);
    return res.data.data;
  },

  async inviteMember(workspaceId: string, data: { email: string; role: string }) {
    const res = await api.post<ApiResponse<WorkspaceMember>>(`/workspaces/${workspaceId}/members`, data);
    return res.data.data;
  },

  async updateMemberRole(workspaceId: string, memberId: string, role: string) {
    const res = await api.put<ApiResponse<null>>(`/workspaces/${workspaceId}/members/${memberId}`, { role });
    return res.data;
  },

  async removeMember(workspaceId: string, memberId: string) {
    const res = await api.delete<ApiResponse<null>>(`/workspaces/${workspaceId}/members/${memberId}`);
    return res.data;
  },
};
