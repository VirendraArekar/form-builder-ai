import React from 'react';

export type BadgeVariant =
  | 'indigo'
  | 'emerald'
  | 'amber'
  | 'rose'
  | 'slate'
  | 'cyan'
  | 'secondary'
  | 'outline'
  | 'success'
  | 'warning'
  | 'danger';

export interface BadgeProps {
  children: React.ReactNode;
  variant?: BadgeVariant;
  dot?: boolean;
  className?: string;
}

export const Badge: React.FC<BadgeProps> = ({
  children,
  variant = 'slate',
  dot = false,
  className = '',
}) => {
  const normalizedVariant =
    variant === 'secondary'
      ? 'slate'
      : variant === 'success'
      ? 'emerald'
      : variant === 'warning'
      ? 'amber'
      : variant === 'danger'
      ? 'rose'
      : variant;

  const variants = {
    indigo:
      'bg-emerald-50 text-emerald-700 dark:bg-emerald-950/60 dark:text-emerald-300 border-emerald-200 dark:border-emerald-800/60',
    emerald:
      'bg-emerald-50 text-emerald-700 dark:bg-emerald-950/60 dark:text-emerald-300 border-emerald-200 dark:border-emerald-800/60',
    amber:
      'bg-amber-50 text-amber-700 dark:bg-amber-950/60 dark:text-amber-300 border-amber-200 dark:border-amber-800/60',
    rose:
      'bg-rose-50 text-rose-700 dark:bg-rose-950/60 dark:text-rose-300 border-rose-200 dark:border-rose-800/60',
    slate:
      'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300 border-slate-200 dark:border-slate-700',
    cyan:
      'bg-cyan-50 text-cyan-700 dark:bg-cyan-950/60 dark:text-cyan-300 border-cyan-200 dark:border-cyan-800/60',
    outline:
      'bg-transparent text-slate-700 dark:text-slate-300 border-slate-300 dark:border-slate-700',
  };

  const dotColors: Record<string, string> = {
    indigo: 'bg-indigo-500',
    emerald: 'bg-emerald-500',
    amber: 'bg-amber-500',
    rose: 'bg-rose-500',
    slate: 'bg-slate-500',
    cyan: 'bg-cyan-500',
    outline: 'bg-slate-400',
  };

  const variantClass = variants[normalizedVariant as keyof typeof variants] || variants.slate;
  const dotColorClass = dotColors[normalizedVariant as keyof typeof dotColors] || 'bg-slate-400';

  return (
    <span
      className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-semibold border ${variantClass} ${className}`}
    >
      {dot && <span className={`w-1.5 h-1.5 rounded-full ${dotColorClass}`} />}
      {children}
    </span>
  );
};
