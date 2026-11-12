import React from 'react';

export interface CardProps extends React.HTMLAttributes<HTMLDivElement> {
  hover?: boolean;
}

export const Card: React.FC<CardProps> = ({ children, hover = false, className = '', ...props }) => {
  return (
    <div
      className={`bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800/80 shadow-xs transition-all duration-200 ${
        hover ? 'hover:shadow-md hover:border-slate-300 dark:hover:border-slate-700' : ''
      } ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

export const CardHeader: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({ children, className = '', ...props }) => {
  return (
    <div className={`p-5 pb-3 ${className}`} {...props}>
      {children}
    </div>
  );
};

export const CardTitle: React.FC<React.HTMLAttributes<HTMLHeadingElement>> = ({ children, className = '', ...props }) => {
  return (
    <h3 className={`font-bold text-slate-900 dark:text-white ${className}`} {...props}>
      {children}
    </h3>
  );
};

export const CardContent: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({ children, className = '', ...props }) => {
  return (
    <div className={`p-5 ${className}`} {...props}>
      {children}
    </div>
  );
};
