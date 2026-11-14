import React from 'react';
import { CheckCircle2, AlertCircle, AlertTriangle, Info, X } from 'lucide-react';
import { useToastStore, ToastType, ToastItem, notify } from '@/store/toastStore';

export type { ToastType, ToastItem };

export interface ToastContextType {
  toast: (message: string, type?: ToastType, duration?: number) => void;
  showToast: (message: string, type?: ToastType, duration?: number) => void;
  success: (message: string, duration?: number) => void;
  error: (message: string, duration?: number) => void;
  warning: (message: string, duration?: number) => void;
  info: (message: string, duration?: number) => void;
}

const ToastContext = React.createContext<ToastContextType | undefined>(undefined);

export const ToastProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { toasts, addToast, removeToast } = useToastStore();

  const contextValue: ToastContextType = {
    toast: addToast,
    showToast: addToast,
    success: (msg, dur) => addToast(msg, 'success', dur),
    error: (msg, dur) => addToast(msg, 'error', dur),
    warning: (msg, dur) => addToast(msg, 'warning', dur),
    info: (msg, dur) => addToast(msg, 'info', dur),
  };

  return (
    <ToastContext.Provider value={contextValue}>
      {children}
      {/* Top Left Toaster Container */}
      <div
        className="fixed top-5 left-5 z-[9999] flex flex-col gap-2.5 pointer-events-none max-w-sm w-full"
        aria-live="polite"
      >
        {toasts.map((t) => {
          const icons = {
            success: <CheckCircle2 className="w-5 h-5 text-emerald-500 shrink-0" />,
            error: <AlertCircle className="w-5 h-5 text-rose-500 shrink-0" />,
            warning: <AlertTriangle className="w-5 h-5 text-amber-500 shrink-0" />,
            info: <Info className="w-5 h-5 text-emerald-500 shrink-0" />,
          };

          const styles = {
            success:
              'bg-white dark:bg-slate-900 border-emerald-300 dark:border-emerald-800 text-slate-800 dark:text-slate-100 shadow-xl shadow-emerald-500/10',
            error:
              'bg-white dark:bg-slate-900 border-rose-300 dark:border-rose-800 text-slate-800 dark:text-slate-100 shadow-xl shadow-rose-500/10',
            warning:
              'bg-white dark:bg-slate-900 border-amber-300 dark:border-amber-800 text-slate-800 dark:text-slate-100 shadow-xl shadow-amber-500/10',
            info:
              'bg-white dark:bg-slate-900 border-slate-300 dark:border-slate-700 text-slate-800 dark:text-slate-100 shadow-xl shadow-slate-500/10',
          };

          return (
            <div
              key={t.id}
              className={`flex items-start gap-3 p-4 rounded-2xl border text-sm pointer-events-auto backdrop-blur-md transition-all duration-300 animate-slide-in-left ${styles[t.type]}`}
              role="alert"
            >
              <div className="mt-0.5">{icons[t.type]}</div>
              <div className="flex-1 font-medium text-xs leading-relaxed break-words">
                {t.message}
              </div>
              <button
                onClick={() => removeToast(t.id)}
                className="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 cursor-pointer p-0.5 -mr-1 -mt-1 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                title="Dismiss"
              >
                <X className="w-4 h-4" />
              </button>
            </div>
          );
        })}
      </div>
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const context = React.useContext(ToastContext);
  if (!context) {
    // If used outside provider, fall back to global store
    return {
      toast: notify.info,
      showToast: (msg: string, type: ToastType = 'info', dur?: number) =>
        useToastStore.getState().addToast(msg, type, dur),
      success: notify.success,
      error: notify.error,
      warning: notify.warning,
      info: notify.info,
    };
  }
  return context;
};
