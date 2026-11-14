import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Sparkles,
  ArrowRight,
  CheckCircle2,
  Zap,
  Sliders,
  BarChart3,
  ShieldCheck,
  Share2,
  Layers,
  HeartHandshake,
  Star,
  Users,
  Clock,
  Eye,
  Check,
} from 'lucide-react';
import { Button } from '@/components/ui/Button';
import { useAuthStore } from '@/store/authStore';

export const LandingPage: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const [promptInput, setPromptInput] = useState(
    'Create a customer feedback form for an online shopping platform with customer details, product rating, delivery experience, payment experience, suggestions, and an overall satisfaction score.'
  );

  const handleTryPrompt = (e: React.FormEvent) => {
    e.preventDefault();
    if (isAuthenticated) {
      navigate('/ai-generate', { state: { initialPrompt: promptInput } });
    } else {
      navigate('/register', { state: { initialPrompt: promptInput } });
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 selection:bg-emerald-500 selection:text-white">
      {/* Navigation */}
      <header className="sticky top-0 z-50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-md border-b border-slate-200/80 dark:border-slate-800/80">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-emerald-500 via-teal-500 to-green-600 flex items-center justify-center text-white font-extrabold text-xl shadow-md shadow-emerald-500/20">
              T
            </div>
            <span className="font-extrabold text-lg tracking-tight bg-gradient-to-r from-emerald-600 via-teal-600 to-green-600 dark:from-emerald-400 dark:to-teal-300 bg-clip-text text-transparent">
              Timely Forms AI
            </span>
          </div>

          <nav className="hidden md:flex items-center gap-8 text-sm font-semibold text-slate-600 dark:text-slate-300">
            <a href="#features" className="hover:text-emerald-600 dark:hover:text-emerald-400 transition-colors">
              Features
            </a>
            <a href="#builder" className="hover:text-emerald-600 dark:hover:text-emerald-400 transition-colors">
              Builder
            </a>
            <a href="#templates" className="hover:text-emerald-600 dark:hover:text-emerald-400 transition-colors">
              Templates
            </a>
            <a href="#pricing" className="hover:text-emerald-600 dark:hover:text-emerald-400 transition-colors">
              Pricing
            </a>
          </nav>

          <div className="flex items-center gap-3">
            {isAuthenticated ? (
              <Button onClick={() => navigate('/dashboard')} variant="primary" size="sm">
                Open Dashboard <ArrowRight className="w-4 h-4 ml-1" />
              </Button>
            ) : (
              <>
                <Button onClick={() => navigate('/login')} variant="ghost" size="sm">
                  Sign In
                </Button>
                <Button onClick={() => navigate('/register')} variant="primary" size="sm">
                  Get Started Free
                </Button>
              </>
            )}
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative overflow-hidden pt-20 pb-28 px-4 sm:px-6 lg:px-8 text-center">
        <div className="absolute inset-0 -z-10 bg-[radial-gradient(ellipse_at_top,rgba(16,185,129,0.15),transparent_60%)]" />

        <div className="max-w-4xl mx-auto">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-emerald-50 dark:bg-emerald-950/60 border border-emerald-200 dark:border-emerald-800 text-emerald-600 dark:text-emerald-400 text-xs font-bold mb-8 shadow-xs">
            <Sparkles className="w-3.5 h-3.5" /> Google Gemini AI Powered Form Builder
          </div>

          <h1 className="text-4xl sm:text-6xl font-black tracking-tight text-slate-900 dark:text-white leading-[1.15] mb-6">
            Build production forms in <br className="hidden sm:inline" />
            <span className="bg-gradient-to-r from-emerald-600 via-teal-600 to-green-600 dark:from-emerald-400 dark:to-teal-300 bg-clip-text text-transparent">
              one natural-language sentence.
            </span>
          </h1>

          <p className="text-lg sm:text-xl text-slate-600 dark:text-slate-300 max-w-2xl mx-auto leading-relaxed mb-10">
            Describe your form requirement. Gemini AI structures sections, generates 24+ field types, writes validation rules, adds conditional logic, and publishes shareable links instantly.
          </p>

          {/* Interactive AI Prompt Box */}
          <form
            onSubmit={handleTryPrompt}
            className="max-w-2xl mx-auto p-2 bg-white dark:bg-slate-900 rounded-2xl shadow-xl shadow-indigo-500/10 border border-slate-200 dark:border-slate-800 flex flex-col sm:flex-row items-center gap-2"
          >
            <div className="flex-1 w-full flex items-center px-3 py-2">
              <Sparkles className="w-5 h-5 text-indigo-500 shrink-0 mr-2.5" />
              <input
                type="text"
                value={promptInput}
                onChange={(e) => setPromptInput(e.target.value)}
                placeholder="Describe your form in plain English..."
                className="w-full bg-transparent text-sm focus:outline-none text-slate-800 dark:text-slate-100 placeholder:text-slate-400"
              />
            </div>
            <Button type="submit" variant="primary" className="w-full sm:w-auto shrink-0 shadow-md">
              Generate With AI <ArrowRight className="w-4 h-4 ml-1" />
            </Button>
          </form>

          {/* Prompt Suggestions */}
          <div className="flex flex-wrap items-center justify-center gap-2 mt-4 text-xs text-slate-500">
            <span className="font-semibold text-slate-400">Try examples:</span>
            {[
              'Tech conference registration with VIP pass options',
              'Software engineer candidate screening with portfolio link',
              'SaaS cancellation survey with churn reasons',
            ].map((ex, idx) => (
              <button
                key={idx}
                type="button"
                onClick={() => setPromptInput(ex)}
                className="px-2.5 py-1 rounded-lg bg-slate-100 dark:bg-slate-800/80 hover:bg-indigo-50 dark:hover:bg-indigo-950/60 hover:text-indigo-600 text-slate-600 dark:text-slate-300 transition-colors cursor-pointer"
              >
                {ex}
              </button>
            ))}
          </div>
        </div>

        {/* Hero Visual Preview */}
        <div className="max-w-5xl mx-auto mt-16 rounded-2xl p-2 bg-gradient-to-b from-indigo-500/20 to-slate-200/20 dark:from-indigo-500/10 dark:to-slate-800/20 border border-slate-200 dark:border-slate-800 shadow-2xl">
          <div className="bg-white dark:bg-slate-900 rounded-xl overflow-hidden border border-slate-200 dark:border-slate-800 p-6 sm:p-8 text-left">
            <div className="flex items-center justify-between border-b border-slate-100 dark:border-slate-800 pb-4 mb-6">
              <div className="flex items-center gap-3">
                <span className="w-3 h-3 rounded-full bg-rose-500 inline-block" />
                <span className="w-3 h-3 rounded-full bg-amber-500 inline-block" />
                <span className="w-3 h-3 rounded-full bg-emerald-500 inline-block" />
                <span className="text-xs font-mono text-slate-400 ml-2">formgenius.ai/f/customer-feedback-demo</span>
              </div>
              <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 bg-emerald-50 dark:bg-emerald-950 px-2.5 py-1 rounded-full">
                Active & Collecting
              </span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="col-span-2 space-y-4">
                <div className="space-y-1">
                  <h3 className="text-xl font-bold">Customer Feedback & Delivery Survey</h3>
                  <p className="text-xs text-slate-500">Section 1 of 3: Order & Delivery Details</p>
                </div>
                <div className="space-y-3 pt-2">
                  <div className="p-4 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-950/40">
                    <span className="text-xs font-semibold text-slate-600 dark:text-slate-300 block mb-2">
                      Did your package arrive on or before the estimated date? *
                    </span>
                    <div className="flex gap-3">
                      <button type="button" className="px-4 py-1.5 rounded-lg border border-indigo-600 bg-indigo-50 text-indigo-600 dark:bg-indigo-950 dark:text-indigo-300 font-semibold text-xs">
                        Yes
                      </button>
                      <button type="button" className="px-4 py-1.5 rounded-lg border border-slate-300 dark:border-slate-700 text-slate-600 dark:text-slate-400 text-xs">
                        No
                      </button>
                    </div>
                  </div>
                  <div className="p-4 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-950/40">
                    <span className="text-xs font-semibold text-slate-600 dark:text-slate-300 block mb-2">
                      How satisfied were you with packaging quality? (1-5 Stars) *
                    </span>
                    <div className="flex gap-2 text-amber-400">
                      {[1, 2, 3, 4, 5].map((s) => (
                        <Star key={s} className="w-5 h-5 fill-amber-400" />
                      ))}
                    </div>
                  </div>
                </div>
              </div>

              <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-200 dark:border-slate-700/60 flex flex-col justify-between">
                <div>
                  <div className="text-xs font-bold text-slate-400 uppercase tracking-wider mb-2">Live Analytics</div>
                  <div className="text-2xl font-black text-indigo-600 dark:text-indigo-400">92.4%</div>
                  <div className="text-xs text-slate-500 mb-4">Completion Rate</div>

                  <div className="space-y-2 text-xs">
                    <div className="flex justify-between text-slate-600 dark:text-slate-300 font-medium">
                      <span>Total Submissions</span>
                      <span className="font-bold">142</span>
                    </div>
                    <div className="flex justify-between text-slate-600 dark:text-slate-300 font-medium">
                      <span>Avg Duration</span>
                      <span className="font-bold">1m 14s</span>
                    </div>
                  </div>
                </div>

                <div className="mt-6 pt-4 border-t border-slate-200 dark:border-slate-700/60">
                  <div className="text-[11px] text-slate-400 mb-1 flex items-center gap-1 font-semibold">
                    <Sparkles className="w-3 h-3 text-indigo-500" /> AI Insights Preview
                  </div>
                  <p className="text-[11px] text-slate-600 dark:text-slate-300 italic">
                    "94% of respondents praised delivery speed. Minor delay comments isolated to regional courier hubs."
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Feature Grid */}
      <section id="features" className="py-24 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto border-t border-slate-200/80 dark:border-slate-800/80">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-xs font-bold text-indigo-600 dark:text-indigo-400 uppercase tracking-widest mb-3">
            Engineered For Scale
          </h2>
          <h3 className="text-3xl font-extrabold tracking-tight">
            Everything you need in a modern form platform.
          </h3>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {[
            {
              icon: <Sparkles className="w-6 h-6 text-indigo-500" />,
              title: 'Natural-Language Form AI',
              description: 'Turn sentences into multi-step forms with proper question categorization, descriptions, and validations powered by Google Gemini.',
            },
            {
              icon: <Sliders className="w-6 h-6 text-indigo-500" />,
              title: 'Visual 3-Panel Builder',
              description: 'Drag and drop 24+ field types with live canvas reordering, responsive mobile preview, inline editing, and theme customization.',
            },
            {
              icon: <Layers className="w-6 h-6 text-indigo-500" />,
              title: 'Advanced Conditional Logic',
              description: 'Show or hide questions and sections dynamically based on previous responses with nested AND/OR rules.',
            },
            {
              icon: <ShieldCheck className="w-6 h-6 text-indigo-500" />,
              title: 'Strict Server-Side Validation',
              description: 'Every submission is rigorously verified on Spring Boot. Validates field types, regex constraints, and limits before PostgreSQL persistence.',
            },
            {
              icon: <BarChart3 className="w-6 h-6 text-indigo-500" />,
              title: 'Deep SQL & Recharts Analytics',
              description: 'Real-time charts for question distributions, conversion funnels, completion velocity, device breakdowns, and daily submission trends.',
            },
            {
              icon: <HeartHandshake className="w-6 h-6 text-indigo-500" />,
              title: 'AI Sentiment & Complaint Insights',
              description: 'Gemini analyzes qualitative responses and synthesizes common customer complaints, satisfaction scores, and actionable recommendations.',
            },
          ].map((item, idx) => (
            <div
              key={idx}
              className="p-6 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800/80 shadow-xs hover:border-indigo-500/40 hover:shadow-md transition-all"
            >
              <div className="w-12 h-12 rounded-xl bg-indigo-50 dark:bg-indigo-950/60 flex items-center justify-center mb-5">
                {item.icon}
              </div>
              <h4 className="text-lg font-bold mb-2">{item.title}</h4>
              <p className="text-sm text-slate-500 dark:text-slate-400 leading-relaxed">{item.description}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Pricing Section */}
      <section id="pricing" className="py-24 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto border-t border-slate-200/80 dark:border-slate-800/80">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-xs font-bold text-indigo-600 dark:text-indigo-400 uppercase tracking-widest mb-3">
            Transparent Pricing
          </h2>
          <h3 className="text-3xl font-extrabold tracking-tight">
            Start free, scale seamlessly.
          </h3>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-5xl mx-auto">
          {/* Free Tier */}
          <div className="p-8 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 flex flex-col justify-between">
            <div>
              <span className="text-xs font-bold uppercase tracking-wider text-slate-400">Free Tier</span>
              <div className="text-3xl font-black mt-2 mb-4">$0 <span className="text-sm font-normal text-slate-400">/ forever</span></div>
              <p className="text-xs text-slate-500 mb-6">Ideal for individuals testing ideas and collecting early responses.</p>
              <ul className="space-y-3 text-sm text-slate-600 dark:text-slate-300">
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> Up to 5 Active Forms</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> 100 Responses / month</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> 20 AI Generations</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> Public Shareable URLs</li>
              </ul>
            </div>
            <Button onClick={() => navigate('/register')} variant="outline" className="w-full mt-8">
              Start Free
            </Button>
          </div>

          {/* Pro Tier */}
          <div className="p-8 rounded-2xl bg-white dark:bg-slate-900 border-2 border-indigo-600 dark:border-indigo-500 shadow-xl shadow-indigo-500/10 flex flex-col justify-between relative">
            <div className="absolute -top-3 left-1/2 -translate-x-1/2 px-3 py-0.5 rounded-full bg-indigo-600 text-white text-xs font-bold uppercase tracking-wider">
              Most Popular
            </div>
            <div>
              <span className="text-xs font-bold uppercase tracking-wider text-indigo-600 dark:text-indigo-400">Professional</span>
              <div className="text-3xl font-black mt-2 mb-4">$29 <span className="text-sm font-normal text-slate-400">/ month</span></div>
              <p className="text-xs text-slate-500 mb-6">For scaling startups and product teams collecting continuous feedback.</p>
              <ul className="space-y-3 text-sm text-slate-600 dark:text-slate-300">
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-indigo-500" /> 50 Active Forms</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-indigo-500" /> 5,000 Responses / month</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-indigo-500" /> 100 AI Generations</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-indigo-500" /> AI Response Sentiment Insights</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-indigo-500" /> Custom Branding & Logo</li>
              </ul>
            </div>
            <Button onClick={() => navigate('/register')} variant="primary" className="w-full mt-8 shadow-md shadow-indigo-500/20">
              Upgrade to Pro
            </Button>
          </div>

          {/* Business Tier */}
          <div className="p-8 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 flex flex-col justify-between">
            <div>
              <span className="text-xs font-bold uppercase tracking-wider text-slate-400">Enterprise</span>
              <div className="text-3xl font-black mt-2 mb-4">$79 <span className="text-sm font-normal text-slate-400">/ month</span></div>
              <p className="text-xs text-slate-500 mb-6">Advanced security, unlimited forms, team workspaces, and webhooks.</p>
              <ul className="space-y-3 text-sm text-slate-600 dark:text-slate-300">
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> Unlimited Forms</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> 50,000 Responses / month</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> Unlimited AI Generations</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> Webhook Integrations</li>
                <li className="flex items-center gap-2"><Check className="w-4 h-4 text-emerald-500" /> Priority Support & SLAs</li>
              </ul>
            </div>
            <Button onClick={() => navigate('/register')} variant="outline" className="w-full mt-8">
              Contact Sales
            </Button>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-slate-200 dark:border-slate-800 py-12 px-4 sm:px-6 lg:px-8 text-center text-xs text-slate-500 dark:text-slate-400">
        <p>&copy; {new Date().getFullYear()} Timely Forms AI — Full-Stack AI Form Builder SaaS. Built with React 19, Spring Boot, PostgreSQL & Google Gemini.</p>
      </footer>
    </div>
  );
};
