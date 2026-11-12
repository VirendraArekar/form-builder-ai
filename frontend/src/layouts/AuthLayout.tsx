import React from 'react';
import { Link, Outlet } from 'react-router-dom';
import {
  Sparkles,
  CheckCircle2,
  ShieldCheck,
  Cpu,
  Database,
  Layers,
  Zap,
  Star,
  Activity,
  Server,
  Sun,
  Moon,
} from 'lucide-react';
import { useThemeStore } from '@/store/themeStore';

export interface AuthLayoutProps {
  children?: React.ReactNode;
  title?: string;
  subtitle?: string;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({ children, title, subtitle }) => {
  const { isDark, toggleTheme } = useThemeStore();

  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-12 bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 font-sans selection:bg-emerald-500 selection:text-white">
      {/* Left Panel: Rich SaaS Graphics, Architecture, and System Info (7 cols on desktop) */}
      <div className="hidden lg:flex lg:col-span-7 flex-col justify-between p-10 xl:p-14 bg-gradient-to-br from-slate-950 via-emerald-950 to-slate-900 text-white relative overflow-hidden border-r border-emerald-900/40">
        {/* Glow Spheres & Background Grid */}
        <div className="absolute inset-0 bg-[radial-gradient(ellipse_80%_80%_at_50%_-20%,rgba(16,185,129,0.22),transparent_70%)] pointer-events-none" />
        <div className="absolute top-1/4 -left-20 w-96 h-96 bg-emerald-500/15 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute bottom-10 right-0 w-80 h-80 bg-teal-500/15 rounded-full blur-3xl pointer-events-none" />

        {/* Top Header: Brand & Status Pill */}
        <div className="relative z-10 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-3 group">
            <div className="w-11 h-11 rounded-2xl bg-gradient-to-tr from-emerald-500 via-teal-500 to-green-600 text-white flex items-center justify-center font-black text-2xl shadow-lg shadow-emerald-500/25 group-hover:scale-105 transition-transform duration-200">
              T
            </div>
            <div>
              <span className="text-xl font-extrabold tracking-tight block">Timely Forms AI</span>
              <span className="text-[10px] text-emerald-400 font-semibold tracking-wider uppercase block">
                Enterprise AI Form Architecture
              </span>
            </div>
          </Link>

          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-500/15 border border-emerald-400/30 text-emerald-300 text-xs font-semibold backdrop-blur-md">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            System v2.4 Active • 100% Operational
          </div>
        </div>

        {/* Center: Hero Heading + Interactive Graphic Mockup Card */}
        <div className="relative z-10 my-auto py-8 space-y-6">
          <div>
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-gradient-to-r from-emerald-500/20 to-teal-500/20 border border-emerald-400/30 text-emerald-300 text-xs font-bold mb-3.5 shadow-xs">
              <Sparkles className="w-3.5 h-3.5 text-emerald-400" />
              Prompt-to-Form AI Generation Engine
            </div>
            <h1 className="text-3xl xl:text-4xl font-black leading-tight tracking-tight text-white mb-3">
              Transform simple prompts into complete, intelligent forms in seconds.
            </h1>
            <p className="text-emerald-100/75 text-sm xl:text-base leading-relaxed max-w-xl">
              Describe your objective in plain English. Google Gemini generates structured questions, multi-page sections, conditional logic, and submission workflows automatically.
            </p>
          </div>

          {/* Interactive Graphics Showcase Card */}
          <div className="bg-slate-900/80 backdrop-blur-xl border border-emerald-500/20 rounded-2xl p-5 shadow-2xl shadow-black/60 relative overflow-hidden group">
            <div className="absolute top-0 right-0 w-48 h-48 bg-emerald-500/10 rounded-full blur-2xl pointer-events-none" />

            {/* Window bar */}
            <div className="flex items-center justify-between pb-3 mb-3 border-b border-white/10 text-xs">
              <div className="flex items-center gap-1.5">
                <span className="w-2.5 h-2.5 rounded-full bg-rose-500/80 inline-block" />
                <span className="w-2.5 h-2.5 rounded-full bg-amber-500/80 inline-block" />
                <span className="w-2.5 h-2.5 rounded-full bg-emerald-500/80 inline-block" />
                <span className="ml-2 font-mono text-[11px] text-slate-400">gemini-form-synthesizer.ts</span>
              </div>
              <span className="px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 font-mono text-[10px] font-bold">
                Generation Latency: 42ms
              </span>
            </div>

            {/* Prompt input simulation */}
            <div className="bg-slate-950/70 border border-emerald-500/30 rounded-xl p-3 mb-3.5 flex items-start gap-2.5">
              <Sparkles className="w-4 h-4 text-emerald-400 shrink-0 mt-0.5" />
              <div className="text-xs font-mono text-emerald-200">
                <span className="text-slate-400">prompt: </span>
                &quot;Create an interactive customer feedback and product NPS survey with rating stars, delivery speed, and custom submit action...&quot;
              </div>
            </div>

            {/* Form Mockup Preview */}
            <div className="bg-slate-950/50 border border-white/5 rounded-xl p-3.5 space-y-3">
              <div className="flex items-center justify-between text-xs">
                <span className="font-bold text-white">Customer Feedback & Satisfaction NPS</span>
                <span className="text-[10px] font-semibold text-emerald-400 bg-emerald-950/80 px-2 py-0.5 rounded-full border border-emerald-800">
                  Step 1 of 2
                </span>
              </div>

              {/* Progress bar */}
              <div className="w-full bg-slate-800 h-1.5 rounded-full overflow-hidden">
                <div className="bg-gradient-to-r from-emerald-500 to-teal-400 h-full w-2/3 rounded-full" />
              </div>

              {/* Simulated question */}
              <div className="space-y-1.5 pt-1">
                <label className="text-[11px] font-medium text-slate-300 block">
                  1. Overall shopping & delivery experience:
                </label>
                <div className="flex gap-2">
                  {[1, 2, 3, 4, 5].map((s) => (
                    <div
                      key={s}
                      className="w-7 h-7 rounded-lg bg-emerald-950/60 border border-emerald-500/40 flex items-center justify-center text-amber-400 shadow-xs"
                    >
                      <Star className="w-3.5 h-3.5 fill-amber-400" />
                    </div>
                  ))}
                </div>
              </div>

              {/* Simulated button */}
              <div className="pt-1 flex items-center justify-between">
                <span className="text-[10px] text-slate-400 flex items-center gap-1">
                  <CheckCircle2 className="w-3 h-3 text-emerald-400" />
                  Auto-validated with Spring Boot 3.4
                </span>
                <div className="px-3 py-1 rounded-lg bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white font-bold text-[11px] shadow-sm shadow-emerald-500/30">
                  Next Step →
                </div>
              </div>
            </div>
          </div>

          {/* System Info & Tech Stack Matrix */}
          <div className="grid grid-cols-2 xl:grid-cols-4 gap-2.5 pt-2">
            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 backdrop-blur-sm text-left">
              <div className="flex items-center gap-1.5 text-emerald-400 text-xs font-bold mb-0.5">
                <Server className="w-3.5 h-3.5" />
                Backend
              </div>
              <div className="text-[11px] font-semibold text-slate-200">Spring Boot 3.4</div>
              <div className="text-[10px] text-slate-400">Java 21 • Security 7</div>
            </div>

            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 backdrop-blur-sm text-left">
              <div className="flex items-center gap-1.5 text-emerald-400 text-xs font-bold mb-0.5">
                <Database className="w-3.5 h-3.5" />
                Database
              </div>
              <div className="text-[11px] font-semibold text-slate-200">PostgreSQL 16</div>
              <div className="text-[10px] text-slate-400">JSONB Schemas • Flyway</div>
            </div>

            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 backdrop-blur-sm text-left">
              <div className="flex items-center gap-1.5 text-emerald-400 text-xs font-bold mb-0.5">
                <Cpu className="w-3.5 h-3.5" />
                AI Engine
              </div>
              <div className="text-[11px] font-semibold text-slate-200">Google Gemini</div>
              <div className="text-[10px] text-slate-400">Flash 2.0 + Heuristics</div>
            </div>

            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 backdrop-blur-sm text-left">
              <div className="flex items-center gap-1.5 text-emerald-400 text-xs font-bold mb-0.5">
                <Layers className="w-3.5 h-3.5" />
                Frontend
              </div>
              <div className="text-[11px] font-semibold text-slate-200">React 19 + Vite</div>
              <div className="text-[10px] text-slate-400">Tailwind v4 • Zustand</div>
            </div>
          </div>
        </div>

        {/* Bottom Status & Trust Banner */}
        <div className="relative z-10 pt-4 border-t border-emerald-900/40 flex items-center justify-between text-xs text-emerald-200/70">
          <div className="flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-emerald-400" />
            <span>Stateless JWT Authentication • BCrypt Security • Role-Based Access</span>
          </div>
          <div className="flex items-center gap-1 font-mono text-[11px] text-emerald-400">
            <Activity className="w-3.5 h-3.5" />
            <span>PostgreSQL: Connected</span>
          </div>
        </div>
      </div>

      {/* Right Panel: Aligned Form Container (5 cols on desktop) */}
      <div className="lg:col-span-5 flex flex-col justify-between min-h-screen p-6 sm:p-10 xl:p-12 relative overflow-y-auto bg-white dark:bg-slate-950">
        {/* Top bar on right: Mobile branding & Theme Toggle */}
        <div className="flex items-center justify-between mb-6">
          <div className="lg:hidden flex items-center gap-2.5">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-emerald-500 via-teal-500 to-green-600 text-white flex items-center justify-center font-bold text-lg shadow-sm">
              T
            </div>
            <span className="font-extrabold text-base tracking-tight text-slate-900 dark:text-white">
              Timely Forms AI
            </span>
          </div>

          <div className="ml-auto">
            <button
              onClick={toggleTheme}
              className="p-2 rounded-xl text-slate-500 hover:text-slate-800 dark:text-slate-400 dark:hover:text-slate-100 bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 cursor-pointer transition-colors"
              title="Toggle Light/Dark Theme"
            >
              {isDark ? <Sun className="w-4 h-4 text-amber-400" /> : <Moon className="w-4 h-4" />}
            </button>
          </div>
        </div>

        {/* Main Centered / Right-Aligned Form Wrapper */}
        <div className="w-full max-w-md mx-auto my-auto py-4">
          {title && (
            <div className="mb-6 text-left">
              <h2 className="text-2xl font-black tracking-tight text-slate-900 dark:text-white">{title}</h2>
              {subtitle && <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{subtitle}</p>}
            </div>
          )}
          {children || <Outlet />}
        </div>

        {/* Right Footer */}
        <div className="text-center text-xs text-slate-400 pt-6">
          &copy; {new Date().getFullYear()} Timely Forms AI. Enterprise SaaS Form Architecture.
        </div>
      </div>
    </div>
  );
};
