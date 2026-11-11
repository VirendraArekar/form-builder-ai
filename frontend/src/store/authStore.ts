import { create } from 'zustand';
import { User, WorkspaceSummary } from '@/types';
import { authService } from '@/services/authService';

interface AuthState {
  user: User | null;
  activeWorkspace: any;
  currentWorkspace: any;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, pass: string) => Promise<void>;
  register: (data: { email: string; password: string; firstName: string; lastName: string; companyName?: string }) => Promise<void>;
  logout: () => void;
  checkAuth: () => Promise<void>;
  setActiveWorkspace: (ws: any) => void;
  setCurrentWorkspace: (ws: any) => void;
  updateUser: (user: Partial<User>) => void;
  setUser: (user: User | null) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  activeWorkspace: null,
  currentWorkspace: null,
  token: localStorage.getItem('formgenius_access_token'),
  isAuthenticated: !!localStorage.getItem('formgenius_access_token'),
  isLoading: true,

  login: async (email, password) => {
    const data = await authService.login({ email, password });
    localStorage.setItem('formgenius_access_token', data.accessToken);
    localStorage.setItem('formgenius_refresh_token', data.refreshToken);
    set({
      user: data.user,
      activeWorkspace: data.activeWorkspace || null,
      currentWorkspace: data.activeWorkspace || null,
      token: data.accessToken,
      isAuthenticated: true,
      isLoading: false,
    });
  },

  register: async (registerData) => {
    const data = await authService.register(registerData);
    localStorage.setItem('formgenius_access_token', data.accessToken);
    localStorage.setItem('formgenius_refresh_token', data.refreshToken);
    set({
      user: data.user,
      activeWorkspace: data.activeWorkspace || null,
      currentWorkspace: data.activeWorkspace || null,
      token: data.accessToken,
      isAuthenticated: true,
      isLoading: false,
    });
  },

  logout: () => {
    localStorage.removeItem('formgenius_access_token');
    localStorage.removeItem('formgenius_refresh_token');
    set({
      user: null,
      activeWorkspace: null,
      currentWorkspace: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
    });
  },

  checkAuth: async () => {
    const token = localStorage.getItem('formgenius_access_token');
    if (!token) {
      set({ isAuthenticated: false, isLoading: false, user: null, activeWorkspace: null, currentWorkspace: null, token: null });
      return;
    }
    try {
      const data = await authService.getCurrentUser();
      set({
        user: data.user,
        activeWorkspace: data.activeWorkspace || null,
        currentWorkspace: data.activeWorkspace || null,
        token,
        isAuthenticated: true,
        isLoading: false,
      });
    } catch {
      localStorage.removeItem('formgenius_access_token');
      localStorage.removeItem('formgenius_refresh_token');
      set({ isAuthenticated: false, isLoading: false, user: null, activeWorkspace: null, currentWorkspace: null, token: null });
    }
  },

  setActiveWorkspace: (ws) => set({ activeWorkspace: ws, currentWorkspace: ws }),
  setCurrentWorkspace: (ws) => set({ activeWorkspace: ws, currentWorkspace: ws }),
  updateUser: (updated) => set((state) => ({ user: state.user ? { ...state.user, ...updated } : null })),
  setUser: (user) => set({ user }),
}));
