import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import {
  Mail,
  Lock,
  ArrowRight,
  Sparkles,
  AlertCircle,
  Eye,
  EyeOff,
  CheckCircle2,
  Zap,
  ShieldAlert,
} from 'lucide-react';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import { useAuthStore } from '@/store/authStore';
import { AuthLayout } from '@/layouts/AuthLayout';

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuthStore();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isDemoLoading, setIsDemoLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [selectedDemoUser, setSelectedDemoUser] = useState<'admin' | 'demo'>('admin');

  const DEMO_ACCOUNTS = {
    admin: {
      email: 'alex@formgenius.ai',
      password: 'Password123!',
      role: 'Super Admin',
      name: 'Alex Mercer',
    },
    demo: {
      email: 'demo@timelyforms.ai',
      password: 'Password123!',
      role: 'Workspace Owner',
      name: 'Demo Admin',
    },
  };

  const handleSelectDemo = (type: 'admin' | 'demo') => {
    setSelectedDemoUser(type);
    setEmail(DEMO_ACCOUNTS[type].email);
    setPassword(DEMO_ACCOUNTS[type].password);
    setErrorMsg('');
  };

  const handleSubmit = async (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    setErrorMsg('');
    setIsLoading(true);

    try {
      await login(email.trim(), password);
      const redirectPath = (location.state as any)?.from?.pathname || '/dashboard';
      navigate(redirectPath, { replace: true });
    } catch (err: any) {
      setErrorMsg(
        err.response?.data?.message ||
          'Invalid email or password. Please verify credentials or use the one-click demo login below.'
      );
    } finally {
      setIsLoading(false);
    }
  };

  const handleInstantDemoLogin = async (type: 'admin' | 'demo') => {
    setErrorMsg('');
    setIsDemoLoading(true);
    const acc = DEMO_ACCOUNTS[type];
    setEmail(acc.email);
    setPassword(acc.password);

    try {
      await login(acc.email, acc.password);
      const redirectPath = (location.state as any)?.from?.pathname || '/dashboard';
      navigate(redirectPath, { replace: true });
    } catch (err: any) {
      setErrorMsg(
        err.response?.data?.message || 'Demo login failed. Please ensure the backend is running.'
      );
    } finally {
      setIsDemoLoading(false);
    }
  };

  return (
    <AuthLayout>
      <div className="space-y-6 text-left">
        {/* Welcome Header */}
        <div className="space-y-1.5">
          <div className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[11px] font-bold bg-emerald-100 dark:bg-emerald-950/70 text-emerald-800 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800/80 mb-1">
            <Sparkles className="w-3 h-3 text-emerald-600 dark:text-emerald-400" />
            Timely Forms AI Access Portal
          </div>
          <h2 className="text-2xl sm:text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">
            Sign in to your account
          </h2>
          <p className="text-xs sm:text-sm text-slate-500 dark:text-slate-400">
            Access your forms, Gemini AI prompt tools, and real-time response analytics.
          </p>
        </div>

        {/* Error message alert */}
        {errorMsg && (
          <div className="p-3.5 rounded-xl bg-rose-50 dark:bg-rose-950/60 border border-rose-200 dark:border-rose-900 text-rose-700 dark:text-rose-300 text-xs font-semibold flex items-center gap-2.5 shadow-sm">
            <AlertCircle className="w-4 h-4 shrink-0 text-rose-500" />
            <span>{errorMsg}</span>
          </div>
        )}

        {/* Main Login Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Work or Personal Email"
            type="email"
            required
            placeholder="alex@formgenius.ai"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            leftIcon={<Mail className="w-4 h-4 text-slate-400" />}
          />

          <div className="space-y-1">
            <div className="flex items-center justify-between">
              <span className="text-xs font-bold uppercase tracking-wider text-slate-700 dark:text-slate-300">
                Password
              </span>
              <Link
                to="/forgot-password"
                className="text-xs font-semibold text-emerald-600 dark:text-emerald-400 hover:text-emerald-500 transition-colors"
              >
                Forgot password?
              </Link>
            </div>
            <div className="relative">
              <Input
                type={showPassword ? 'text' : 'password'}
                required
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                leftIcon={<Lock className="w-4 h-4 text-slate-400" />}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 p-1 cursor-pointer transition-colors"
                title={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
              </button>
            </div>
          </div>

          <Button
            type="submit"
            variant="primary"
            size="lg"
            className="w-full mt-2 font-bold shadow-lg shadow-emerald-500/20"
            isLoading={isLoading}
            rightIcon={<ArrowRight className="w-4 h-4" />}
          >
            Sign In to Workspace
          </Button>
        </form>

        {/* Demo Credentials Fast Fill Card */}
        <div className="p-4 rounded-2xl bg-gradient-to-br from-emerald-50/80 via-teal-50/40 to-slate-50 dark:from-emerald-950/40 dark:via-teal-950/20 dark:to-slate-900/60 border border-emerald-200/80 dark:border-emerald-800/60 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-6 h-6 rounded-lg bg-emerald-500/20 flex items-center justify-center text-emerald-600 dark:text-emerald-400">
                <Sparkles className="w-3.5 h-3.5" />
              </div>
              <span className="text-xs font-bold text-slate-900 dark:text-slate-100 uppercase tracking-wider">
                System Demo Credentials
              </span>
            </div>
            <span className="text-[10px] font-bold text-emerald-700 dark:text-emerald-300 bg-emerald-100 dark:bg-emerald-900/60 px-2 py-0.5 rounded-full">
              Verified Active
            </span>
          </div>

          {/* Quick User Selection Tabs */}
          <div className="grid grid-cols-2 gap-2">
            <button
              type="button"
              onClick={() => handleSelectDemo('admin')}
              className={`p-2 rounded-xl text-left border transition-all cursor-pointer ${
                selectedDemoUser === 'admin'
                  ? 'border-emerald-500 bg-white dark:bg-slate-800 shadow-sm ring-1 ring-emerald-500/30'
                  : 'border-slate-200 dark:border-slate-800 bg-white/50 dark:bg-slate-900/40 text-slate-500 hover:bg-white dark:hover:bg-slate-800'
              }`}
            >
              <div className="text-xs font-bold text-slate-800 dark:text-slate-200">Alex Mercer</div>
              <div className="text-[10px] text-emerald-600 dark:text-emerald-400 font-semibold">Super Admin</div>
            </button>

            <button
              type="button"
              onClick={() => handleSelectDemo('demo')}
              className={`p-2 rounded-xl text-left border transition-all cursor-pointer ${
                selectedDemoUser === 'demo'
                  ? 'border-emerald-500 bg-white dark:bg-slate-800 shadow-sm ring-1 ring-emerald-500/30'
                  : 'border-slate-200 dark:border-slate-800 bg-white/50 dark:bg-slate-900/40 text-slate-500 hover:bg-white dark:hover:bg-slate-800'
              }`}
            >
              <div className="text-xs font-bold text-slate-800 dark:text-slate-200">Demo Account</div>
              <div className="text-[10px] text-emerald-600 dark:text-emerald-400 font-semibold">Workspace Owner</div>
            </button>
          </div>

          {/* Credentials Display */}
          <div className="p-2.5 rounded-xl bg-white dark:bg-slate-900/80 border border-slate-200 dark:border-slate-800 space-y-1 font-mono text-xs">
            <div className="flex items-center justify-between text-slate-600 dark:text-slate-300">
              <span className="text-[11px] text-slate-400">Email:</span>
              <span className="font-semibold text-slate-900 dark:text-white select-all">
                {DEMO_ACCOUNTS[selectedDemoUser].email}
              </span>
            </div>
            <div className="flex items-center justify-between text-slate-600 dark:text-slate-300">
              <span className="text-[11px] text-slate-400">Password:</span>
              <span className="font-semibold text-emerald-600 dark:text-emerald-400 select-all">
                {DEMO_ACCOUNTS[selectedDemoUser].password}
              </span>
            </div>
          </div>

          {/* Quick Actions: Auto-Fill & 1-Click Sign In */}
          <div className="grid grid-cols-2 gap-2 pt-1">
            <button
              type="button"
              onClick={() => handleSelectDemo(selectedDemoUser)}
              className="px-3 py-2 text-xs font-bold text-slate-700 dark:text-slate-200 bg-white dark:bg-slate-800 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-xl border border-slate-200 dark:border-slate-700 shadow-xs cursor-pointer transition-all active:scale-[0.98] text-center"
            >
              Auto-Fill Form
            </button>

            <button
              type="button"
              onClick={() => handleInstantDemoLogin(selectedDemoUser)}
              disabled={isDemoLoading}
              className="px-3 py-2 text-xs font-bold text-white bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 hover:from-emerald-600 hover:to-teal-600 rounded-xl shadow-sm shadow-emerald-500/30 cursor-pointer transition-all active:scale-[0.98] text-center flex items-center justify-center gap-1.5"
            >
              <Zap className="w-3.5 h-3.5 fill-current" />
              1-Click Sign In
            </button>
          </div>
        </div>

        {/* Link to Register */}
        <div className="text-center text-xs text-slate-500 pt-1">
          Don&apos;t have an account yet?{' '}
          <Link
            to="/register"
            className="font-bold text-emerald-600 dark:text-emerald-400 hover:text-emerald-500 hover:underline transition-colors"
          >
            Create a new account
          </Link>
        </div>
      </div>
    </AuthLayout>
  );
};
