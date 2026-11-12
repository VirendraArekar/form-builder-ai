import React from 'react';
import { Loader2 } from 'lucide-react';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  isLoading = false,
  leftIcon,
  rightIcon,
  className = '',
  disabled,
  ...props
}) => {
  const baseStyles =
    'inline-flex items-center justify-center font-semibold rounded-xl transition-all duration-200 cursor-pointer focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed select-none active:scale-[0.98] transform-gpu whitespace-nowrap';

  const variants = {
    primary:
      'bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 hover:from-emerald-600 hover:to-teal-600 text-white shadow-md shadow-emerald-500/20 hover:shadow-lg hover:shadow-emerald-500/30 hover:-translate-y-0.5 active:translate-y-0 focus:ring-emerald-500 border border-transparent font-bold',
    secondary:
      'bg-slate-100 text-slate-800 hover:bg-slate-200 dark:bg-slate-800 dark:text-slate-200 dark:hover:bg-slate-700 hover:-translate-y-0.5 active:translate-y-0 focus:ring-slate-400 border border-transparent',
    outline:
      'bg-transparent text-slate-700 dark:text-slate-300 border border-slate-300 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/60 hover:-translate-y-0.5 active:translate-y-0 focus:ring-slate-400',
    ghost:
      'bg-transparent text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-slate-100 focus:ring-slate-400',
    danger:
      'bg-rose-600 text-white hover:bg-rose-700 shadow-sm shadow-rose-500/20 hover:shadow-md hover:shadow-rose-500/30 hover:-translate-y-0.5 active:translate-y-0 focus:ring-rose-500 border border-transparent font-bold',
  };

  const sizes = {
    sm: 'text-xs px-3 py-1.5 gap-1.5 leading-none',
    md: 'text-sm px-4 py-2.5 gap-2 leading-none',
    lg: 'text-base px-6 py-3 gap-2.5 leading-none',
  };

  return (
    <button
      className={`${baseStyles} ${variants[variant]} ${sizes[size]} ${className}`}
      disabled={disabled || isLoading}
      {...props}
    >
      {isLoading ? (
        <Loader2 className="w-4 h-4 animate-spin shrink-0" />
      ) : (
        leftIcon && <span className="inline-flex shrink-0 items-center justify-center leading-none">{leftIcon}</span>
      )}
      {children && <span className="inline-flex items-center whitespace-nowrap">{children}</span>}
      {!isLoading && rightIcon && (
        <span className="inline-flex shrink-0 items-center justify-center leading-none">{rightIcon}</span>
      )}
    </button>
  );
};
