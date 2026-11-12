import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/store/authStore';
import { analyticsService } from '@/services/analyticsService';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';
import {
  CreditCard,
  Check,
  Sparkles,
  Zap,
  Shield,
  Layers,
  ArrowRight,
} from 'lucide-react';

export const BillingPage: React.FC = () => {
  const { currentWorkspace } = useAuthStore();
  const { showToast } = useToast();
  const workspaceId = currentWorkspace?.id || '';

  const [currentTier, setCurrentTier] = useState<'FREE' | 'PRO' | 'ENTERPRISE'>('PRO');
  const [isUpgradeModalOpen, setIsUpgradeModalOpen] = useState(false);
  const [selectedPlan, setSelectedPlan] = useState<string>('ENTERPRISE');

  const { data: analytics } = useQuery({
    queryKey: ['workspace-analytics-billing', workspaceId],
    queryFn: () => analyticsService.getWorkspaceAnalytics(workspaceId, 30),
    enabled: !!workspaceId,
  });

  const totalForms = analytics?.totalForms ?? 3;
  const totalSubmissions = analytics?.totalSubmissions ?? 48;

  const handleSimulateUpgrade = () => {
    setCurrentTier(selectedPlan as any);
    setIsUpgradeModalOpen(false);
    showToast(`Successfully upgraded to the ${selectedPlan} Plan!`, 'success');
  };

  const PLANS = [
    {
      id: 'FREE',
      name: 'Starter',
      price: '$0',
      period: 'forever free',
      description: 'Ideal for personal surveys and prototype forms.',
      features: [
        'Up to 3 active forms',
        '100 submissions / month',
        'Basic conditional logic',
        'Standard form themes',
        'CSV data export',
      ],
      quotaForms: 3,
      quotaSubmissions: 100,
    },
    {
      id: 'PRO',
      name: 'Pro Creator',
      price: '$29',
      period: 'per month',
      description: 'Supercharged AI generation and unlimited forms for growing teams.',
      features: [
        'Unlimited active forms',
        '5,000 submissions / month',
        'Google Gemini AI Form Builder',
        'AI Response Insights & sentiment',
        'Custom URL slugs & passwords',
        'Outgoing webhook dispatches',
        'Remove FormGenius watermark',
      ],
      popular: true,
      quotaForms: 9999,
      quotaSubmissions: 5000,
    },
    {
      id: 'ENTERPRISE',
      name: 'Enterprise',
      price: '$99',
      period: 'per month',
      description: 'Advanced governance, team RBAC, and dedicated support for organizations.',
      features: [
        'Unlimited forms & submissions',
        'Workspace multi-tenancy & RBAC',
        'Priority Gemini AI throughput',
        'Custom domain CNAME mapping',
        'Dedicated webhook pipelines',
        '99.9% uptime SLA guarantee',
        '24/7 dedicated support',
      ],
      quotaForms: 99999,
      quotaSubmissions: 999999,
    },
  ];

  return (
    <DashboardLayout>
      <div className="max-w-6xl mx-auto space-y-8 pb-16">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
              Plans & Usage Quotas
            </h1>
            <Badge variant="indigo" className="text-xs">
              Current Plan: {currentTier}
            </Badge>
          </div>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
            Monitor real-time workspace consumption and scale your team with flexible billing tiers.
          </p>
        </div>

        {/* Real-time Usage Meters */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <Card>
            <CardContent className="p-5 space-y-2">
              <div className="flex items-center justify-between text-xs">
                <span className="font-bold text-slate-500 uppercase">Active Forms</span>
                <span className="font-semibold text-slate-900 dark:text-white">
                  {totalForms} / {currentTier === 'FREE' ? 3 : 'Unlimited'}
                </span>
              </div>
              <div className="h-2 w-full bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                <div
                  className="h-full bg-indigo-600 rounded-full"
                  style={{ width: `${Math.min((totalForms / (currentTier === 'FREE' ? 3 : 100)) * 100, 100)}%` }}
                />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-5 space-y-2">
              <div className="flex items-center justify-between text-xs">
                <span className="font-bold text-slate-500 uppercase">Submissions (This Month)</span>
                <span className="font-semibold text-slate-900 dark:text-white">
                  {totalSubmissions} / {currentTier === 'FREE' ? 100 : '5,000'}
                </span>
              </div>
              <div className="h-2 w-full bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                <div
                  className="h-full bg-emerald-500 rounded-full"
                  style={{ width: `${Math.min((totalSubmissions / (currentTier === 'FREE' ? 100 : 5000)) * 100, 100)}%` }}
                />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-5 space-y-2">
              <div className="flex items-center justify-between text-xs">
                <span className="font-bold text-slate-500 uppercase">Gemini AI Credits</span>
                <span className="font-semibold text-indigo-600 dark:text-indigo-400">
                  {currentTier === 'FREE' ? '5 / 10' : 'High Quota Active'}
                </span>
              </div>
              <div className="h-2 w-full bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                <div className="h-full bg-violet-600 rounded-full w-3/4" />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Pricing Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-4">
          {PLANS.map((plan) => {
            const isCurrent = currentTier === plan.id;
            return (
              <Card
                key={plan.id}
                className={`relative flex flex-col justify-between transition-all ${
                  plan.popular
                    ? 'border-indigo-500 ring-2 ring-indigo-500/20 dark:ring-indigo-500/40 shadow-lg'
                    : ''
                }`}
              >
                {plan.popular && (
                  <div className="absolute -top-3 left-1/2 -translate-x-1/2">
                    <Badge variant="indigo" className="font-bold tracking-wide uppercase text-[10px] px-3 py-0.5 shadow-sm">
                      Most Popular
                    </Badge>
                  </div>
                )}

                <CardContent className="p-6 flex flex-col justify-between h-full space-y-6">
                  <div>
                    <h3 className="font-bold text-lg text-slate-900 dark:text-white">
                      {plan.name}
                    </h3>
                    <p className="text-xs text-slate-500 mt-1">{plan.description}</p>

                    <div className="mt-4 flex items-baseline gap-1">
                      <span className="text-3xl font-extrabold text-slate-900 dark:text-white">
                        {plan.price}
                      </span>
                      <span className="text-xs text-slate-400">/{plan.period}</span>
                    </div>

                    <div className="mt-6 pt-6 border-t border-slate-100 dark:border-slate-800 space-y-2.5">
                      <span className="text-[11px] font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider block">
                        Included Features:
                      </span>
                      <ul className="space-y-2 text-xs text-slate-600 dark:text-slate-400">
                        {plan.features.map((feat, i) => (
                          <li key={i} className="flex items-start gap-2">
                            <Check className="w-4 h-4 text-emerald-500 shrink-0 mt-0.5" />
                            <span>{feat}</span>
                          </li>
                        ))}
                      </ul>
                    </div>
                  </div>

                  <div className="pt-4 border-t border-slate-100 dark:border-slate-800">
                    {isCurrent ? (
                      <Button variant="outline" className="w-full" disabled>
                        Current Plan
                      </Button>
                    ) : (
                      <Button
                        className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold"
                        onClick={() => {
                          setSelectedPlan(plan.id);
                          setIsUpgradeModalOpen(true);
                        }}
                      >
                        Switch to {plan.name}
                        <ArrowRight className="w-3.5 h-3.5 ml-1" />
                      </Button>
                    )}
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>

        {/* Upgrade Simulation Modal */}
        <Modal
          isOpen={isUpgradeModalOpen}
          onClose={() => setIsUpgradeModalOpen(false)}
          title={`Upgrade to ${selectedPlan} Plan`}
        >
          <div className="space-y-4">
            <div className="p-4 rounded-xl bg-indigo-50 dark:bg-indigo-950/40 border border-indigo-200 dark:border-indigo-900 text-xs space-y-1">
              <span className="font-bold text-indigo-950 dark:text-indigo-200 block">
                Instant Tier Activation
              </span>
              <p className="text-indigo-700 dark:text-indigo-300">
                You will immediately unlock higher submission quotas, increased Gemini AI capacity, and enhanced workspace permissions.
              </p>
            </div>

            <div className="space-y-2 text-xs">
              <label className="font-bold text-slate-700 dark:text-slate-300 uppercase">
                Payment Method (Simulated)
              </label>
              <div className="p-3 rounded-lg border border-slate-200 dark:border-slate-700 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <CreditCard className="w-4 h-4 text-indigo-600" />
                  <span className="font-mono text-slate-800 dark:text-slate-200">
                    Visa ending in 4242
                  </span>
                </div>
                <Badge variant="outline">Default</Badge>
              </div>
            </div>

            <div className="pt-2 flex justify-end gap-3">
              <Button variant="outline" onClick={() => setIsUpgradeModalOpen(false)}>
                Cancel
              </Button>
              <Button onClick={handleSimulateUpgrade}>
                Confirm Upgrade
              </Button>
            </div>
          </div>
        </Modal>
      </div>
    </DashboardLayout>
  );
};
