import React, { useState, useEffect } from 'react';
import { CheckCircle2, AlertCircle, Info, AlertTriangle, X } from 'lucide-react';

export interface ToastMessage {
  id: string;
  title?: string;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  action?: {
    label: string;
    onClick: () => void;
  };
}

type ToastListener = (toasts: ToastMessage[]) => void;

let listeners: ToastListener[] = [];
let activeToasts: ToastMessage[] = [];

const notify = () => {
  listeners.forEach((l) => l([...activeToasts]));
};

export const toast = {
  success: (message: string, options?: { title?: string; action?: { label: string; onClick: () => void } }) => {
    addToast(message, 'success', options?.title, options?.action);
  },
  error: (message: string, options?: { title?: string; action?: { label: string; onClick: () => void } }) => {
    addToast(message, 'error', options?.title, options?.action);
  },
  info: (message: string, options?: { title?: string; action?: { label: string; onClick: () => void } }) => {
    addToast(message, 'info', options?.title, options?.action);
  },
  warning: (message: string, options?: { title?: string; action?: { label: string; onClick: () => void } }) => {
    addToast(message, 'warning', options?.title, options?.action);
  },
};

const addToast = (
  message: string,
  type: 'success' | 'error' | 'info' | 'warning',
  title?: string,
  action?: { label: string; onClick: () => void }
) => {
  const id = Math.random().toString(36).substring(2, 9);
  const newToast: ToastMessage = { id, message, type, title, action };
  activeToasts = [...activeToasts, newToast];
  notify();

  setTimeout(() => {
    activeToasts = activeToasts.filter((t) => t.id !== id);
    notify();
  }, 4000);
};

export const Toaster: React.FC = () => {
  const [toasts, setToasts] = useState<ToastMessage[]>(activeToasts);

  useEffect(() => {
    listeners.push(setToasts);
    return () => {
      listeners = listeners.filter((l) => l !== setToasts);
    };
  }, []);

  const remove = (id: string) => {
    activeToasts = activeToasts.filter((t) => t.id !== id);
    notify();
  };

  return (
    <div className="fixed bottom-5 right-5 z-50 flex flex-col gap-2.5 pointer-events-none max-w-sm w-full">
      {toasts.map((t) => {
        const icons = {
          success: <CheckCircle2 className="w-5 h-5 text-emerald-500 shrink-0" />,
          error: <AlertCircle className="w-5 h-5 text-rose-500 shrink-0" />,
          info: <Info className="w-5 h-5 text-teal-500 shrink-0" />,
          warning: <AlertTriangle className="w-5 h-5 text-amber-500 shrink-0" />,
        };

        const borderStyles = {
          success: 'border-emerald-200 dark:border-emerald-800/60 shadow-emerald-500/10',
          error: 'border-rose-200 dark:border-rose-800/60 shadow-rose-500/10',
          info: 'border-teal-200 dark:border-teal-800/60 shadow-teal-500/10',
          warning: 'border-amber-200 dark:border-amber-800/60 shadow-amber-500/10',
        };

        return (
          <div
            key={t.id}
            className={`pointer-events-auto flex items-start gap-3 p-4 rounded-2xl border bg-white/95 dark:bg-slate-900/95 backdrop-blur-md shadow-xl transition-all duration-300 animate-in slide-in-from-bottom-5 ${borderStyles[t.type]}`}
          >
            {icons[t.type]}
            <div className="flex-1 min-w-0">
              {t.title && (
                <h5 className="text-xs font-bold text-slate-900 dark:text-white mb-0.5">{t.title}</h5>
              )}
              <p className="text-xs font-medium text-slate-700 dark:text-slate-200 leading-relaxed">
                {t.message}
              </p>
              {t.action && (
                <button
                  onClick={t.action.onClick}
                  className="mt-2 text-xs font-bold text-emerald-600 dark:text-emerald-400 hover:underline"
                >
                  {t.action.label}
                </button>
              )}
            </div>
            <button
              onClick={() => remove(t.id)}
              className="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 p-0.5 rounded-lg transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        );
      })}
    </div>
  );
};
