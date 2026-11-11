import { create } from 'zustand';

interface ThemeState {
  isDark: boolean;
  toggleTheme: () => void;
  setTheme: (dark: boolean) => void;
}

const initialDark = (() => {
  const saved = localStorage.getItem('formgenius_theme');
  if (saved) return saved === 'dark';
  return window.matchMedia('(prefers-color-scheme: dark)').matches;
})();

if (initialDark) {
  document.documentElement.classList.add('dark');
} else {
  document.documentElement.classList.remove('dark');
}

export const useThemeStore = create<ThemeState>((set) => ({
  isDark: initialDark,
  toggleTheme: () =>
    set((state) => {
      const next = !state.isDark;
      if (next) {
        document.documentElement.classList.add('dark');
        localStorage.setItem('formgenius_theme', 'dark');
      } else {
        document.documentElement.classList.remove('dark');
        localStorage.setItem('formgenius_theme', 'light');
      }
      return { isDark: next };
    }),
  setTheme: (dark) => {
    if (dark) {
      document.documentElement.classList.add('dark');
      localStorage.setItem('formgenius_theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('formgenius_theme', 'light');
    }
    set({ isDark: dark });
  },
}));
