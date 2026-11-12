import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { useAuthStore } from '@/store/authStore';
import { formService } from '@/services/formService';
import { analyticsService } from '@/services/analyticsService';
import { aiService } from '@/services/aiService';
import { Button } from '@/components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { EmptyState } from '@/components/ui/EmptyState';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  Sparkles,
  Plus,
  FileText,
  CheckCircle,
  Eye,
  MessageSquare,
  TrendingUp,
  ArrowRight,
  Search,
  ExternalLink,
  Copy,
  Trash2,
  BarChart3,
  Lightbulb,
  Clock,
  LayoutTemplate,
  Globe,
  SlidersHorizontal,
} from 'lucide-react';

export const DashboardPage: React.FC = () => {
  const navigate = useNavigate();
  const { showToast } = useToast();
  const { currentWorkspace } = useAuthStore();
  const [search, setSearch] = useState('');
  const [quickPrompt, setQuickPrompt] = useState('');

  const workspaceId = currentWorkspace?.id || '';

  // Fetch workspace forms
  const {
    data: formsData,
    isLoading: formsLoading,
    refetch: refetchForms,
  } = useQuery({
    queryKey: ['workspace-forms', workspaceId, search],
    queryFn: () =>
      formService.listForms({
        workspaceId,
        search: search || undefined,
        size: 8,
      }),
    enabled: !!workspaceId,
  });

  // Fetch workspace analytics
  const { data: analytics, isLoading: analyticsLoading } = useQuery({
    queryKey: ['workspace-analytics', workspaceId],
    queryFn: () => analyticsService.getWorkspaceAnalytics(workspaceId, 30),
    enabled: !!workspaceId,
  });

  // Fetch AI insights
  const { data: aiInsights, isLoading: insightsLoading } = useQuery({
    queryKey: ['workspace-ai-insights', workspaceId],
    queryFn: () => aiService.generateInsights({ workspaceId }),
    enabled: !!workspaceId,
  });

  const handleCreateScratch = async () => {
    if (!workspaceId) return;
    try {
      const newForm = await formService.createForm({
        workspaceId,
        title: 'Untitled Form',
        description: 'Collect responses effortlessly',
      });
      showToast('Form created successfully', 'success');
      navigate(`/builder/${newForm.id}`);
    } catch (err: any) {
      showToast(err.message || 'Failed to create form', 'error');
    }
  };

  const handleQuickAiGenerate = (e: React.FormEvent) => {
    e.preventDefault();
    if (!quickPrompt.trim()) return;
    navigate(`/generate-ai?prompt=${encodeURIComponent(quickPrompt.trim())}`);
  };

  const handleDuplicate = async (id: string) => {
    try {
      await formService.duplicateForm(id);
      showToast('Form duplicated successfully', 'success');
      refetchForms();
    } catch (err: any) {
      showToast(err.message || 'Failed to duplicate form', 'error');
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Are you sure you want to delete this form?')) return;
    try {
      await formService.deleteForm(id);
      showToast('Form moved to trash', 'success');
      refetchForms();
    } catch (err: any) {
      showToast(err.message || 'Failed to delete form', 'error');
    }
  };

  const copyPublicLink = (slug: string) => {
    const url = `${window.location.origin}/f/${slug}`;
    navigator.clipboard.writeText(url);
    showToast('Public link copied to clipboard', 'info');
  };

  const forms = formsData?.content || [];
  const totalForms = analytics?.totalForms ?? forms.length;
  const publishedForms = analytics?.publishedForms ?? forms.filter((f) => f.status === 'PUBLISHED').length;
  const totalSubmissions = analytics?.totalSubmissions ?? 0;
  const avgCompletionRate = analytics?.averageCompletionRate ?? 0;

  return (
    <DashboardLayout>
      <div className="space-y-8 max-w-7xl mx-auto">
        {/* Top Header / Greeting */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-white">
              Workspace Overview
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400">
              Welcome back to <span className="font-semibold text-slate-700 dark:text-slate-300">{currentWorkspace?.name || 'My Workspace'}</span>. Here is what's happening with your forms.
            </p>
          </div>
          <div className="flex items-center gap-3">
            <Link to="/templates">
              <Button variant="outline" size="sm">
                Explore Templates
              </Button>
            </Link>
            <Button variant="outline" size="sm" onClick={handleCreateScratch}>
              <Plus className="w-4 h-4 mr-1.5" />
              Blank Form
            </Button>
            <Link to="/generate-ai">
              <Button size="sm" className="font-bold text-xs shadow-md">
                <Sparkles className="w-4 h-4 mr-1.5" />
                Build with AI
              </Button>
            </Link>
          </div>
        </div>

        {/* 4 Quick Action Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {/* Card 1: Create Form */}
          <button
            onClick={handleCreateScratch}
            className="flex items-center gap-4 p-4 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 transition-all shadow-xs hover:shadow-md text-left group cursor-pointer"
          >
            <div className="w-12 h-12 rounded-xl bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform">
              <Plus className="w-6 h-6" />
            </div>
            <div>
              <h4 className="font-bold text-sm text-slate-900 dark:text-white group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors">
                Create Form
              </h4>
              <p className="text-xs text-slate-400">Start from a blank canvas</p>
            </div>
          </button>

          {/* Card 2: Generate with AI */}
          <Link
            to="/generate-ai"
            className="flex items-center gap-4 p-4 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 transition-all shadow-xs hover:shadow-md text-left group cursor-pointer"
          >
            <div className="w-12 h-12 rounded-xl bg-gradient-to-tr from-emerald-500 to-teal-600 text-white flex items-center justify-center shrink-0 shadow-sm shadow-emerald-500/20 group-hover:scale-105 transition-transform">
              <Sparkles className="w-6 h-6 animate-pulse" />
            </div>
            <div>
              <h4 className="font-bold text-sm text-slate-900 dark:text-white group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors flex items-center gap-1.5">
                Generate with AI
                <span className="text-[10px] font-bold px-1.5 py-0.2 rounded bg-emerald-100 dark:bg-emerald-950 text-emerald-600 dark:text-emerald-400">AI</span>
              </h4>
              <p className="text-xs text-slate-400">Describe in plain English</p>
            </div>
          </Link>

          {/* Card 3: Use Template */}
          <Link
            to="/templates"
            className="flex items-center gap-4 p-4 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 transition-all shadow-xs hover:shadow-md text-left group cursor-pointer"
          >
            <div className="w-12 h-12 rounded-xl bg-teal-50 dark:bg-teal-950/60 text-teal-600 dark:text-teal-400 flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform">
              <LayoutTemplate className="w-6 h-6" />
            </div>
            <div>
              <h4 className="font-bold text-sm text-slate-900 dark:text-white group-hover:text-teal-600 dark:group-hover:text-teal-400 transition-colors">
                Use Template
              </h4>
              <p className="text-xs text-slate-400">Pick from 20+ presets</p>
            </div>
          </Link>

          {/* Card 4: View All Forms */}
          <Link
            to="/forms"
            className="flex items-center gap-4 p-4 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 transition-all shadow-xs hover:shadow-md text-left group cursor-pointer"
          >
            <div className="w-12 h-12 rounded-xl bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-200 flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform">
              <FileText className="w-6 h-6" />
            </div>
            <div>
              <h4 className="font-bold text-sm text-slate-900 dark:text-white group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors">
                View All Forms
              </h4>
              <p className="text-xs text-slate-400">{totalForms} forms in workspace</p>
            </div>
          </Link>
        </div>

        {/* AI Quick Prompt Banner */}
        <div className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-emerald-950 via-teal-950 to-slate-950 p-6 md:p-8 text-white border border-emerald-500/30 shadow-xl">
          <div className="absolute top-0 right-0 -mr-16 -mt-16 w-64 h-64 bg-emerald-500/20 rounded-full blur-3xl pointer-events-none" />
          <div className="absolute bottom-0 right-20 -mb-16 w-64 h-64 bg-teal-500/20 rounded-full blur-3xl pointer-events-none" />

          <div className="relative z-10 max-w-3xl">
            <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-500/20 border border-emerald-400/30 text-emerald-300 mb-3">
              <Sparkles className="w-3.5 h-3.5" />
              Gemini AI Form Engine
            </div>
            <h2 className="text-xl md:text-2xl font-bold mb-2">
              Generate a production-ready form in seconds
            </h2>
            <p className="text-sm text-emerald-200/80 mb-5">
              Type what you want to collect. Gemini creates fields, conditional logic, validation rules, sections, and styling automatically.
            </p>

            <form onSubmit={handleQuickAiGenerate} className="flex flex-col sm:flex-row items-center gap-2">
              <div className="relative flex-1 w-full">
                <input
                  type="text"
                  value={quickPrompt}
                  onChange={(e) => setQuickPrompt(e.target.value)}
                  placeholder="e.g. Employee onboarding survey with department, start date, laptop preferences, and dietary needs"
                  className="w-full bg-slate-900/90 text-white placeholder-slate-400 text-sm rounded-xl px-4 py-3 border border-emerald-500/40 focus:outline-none focus:ring-2 focus:ring-emerald-400 shadow-inner"
                />
              </div>
              <Button
                type="submit"
                className="w-full sm:w-auto font-semibold px-6 py-3 shrink-0 shadow-lg"
              >
                <Sparkles className="w-4 h-4 mr-2" />
                Generate Form
              </Button>
            </form>
          </div>
        </div>

        {/* 4 Workspace KPI Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card className="hover:shadow-md transition-shadow">
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-medium text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                  Total Forms
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {analyticsLoading ? <Skeleton className="h-8 w-16" /> : totalForms}
                </h3>
                <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                  <FileText className="w-3.5 h-3.5" />
                  In active workspace
                </p>
              </div>
              <div className="w-12 h-12 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-200 rounded-xl flex items-center justify-center">
                <FileText className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>

          <Card className="hover:shadow-md transition-shadow">
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-medium text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                  Live Published
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {analyticsLoading ? <Skeleton className="h-8 w-16" /> : publishedForms}
                </h3>
                <p className="text-xs text-emerald-600 dark:text-emerald-400 mt-1 flex items-center gap-1">
                  <CheckCircle className="w-3.5 h-3.5" />
                  Accepting responses
                </p>
              </div>
              <div className="w-12 h-12 bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 rounded-xl flex items-center justify-center">
                <Globe className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>

          <Card className="hover:shadow-md transition-shadow">
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-medium text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                  Total Responses
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {analyticsLoading ? <Skeleton className="h-8 w-16" /> : totalSubmissions}
                </h3>
                <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                  <MessageSquare className="w-3.5 h-3.5" />
                  Submissions captured
                </p>
              </div>
              <div className="w-12 h-12 bg-teal-50 dark:bg-teal-950/60 text-teal-600 dark:text-teal-400 rounded-xl flex items-center justify-center">
                <MessageSquare className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>

          <Card className="hover:shadow-md transition-shadow">
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-medium text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                  Avg. Completion Rate
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {analyticsLoading ? <Skeleton className="h-8 w-16" /> : `${Math.round(avgCompletionRate)}%`}
                </h3>
                <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                  <TrendingUp className="w-3.5 h-3.5" />
                  Form conversion
                </p>
              </div>
              <div className="w-12 h-12 bg-amber-50 dark:bg-amber-950/60 text-amber-600 dark:text-amber-400 rounded-xl flex items-center justify-center">
                <TrendingUp className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* 2-Column Section: Recent Forms + AI Insights */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Recent Forms (2 Cols) */}
          <div className="lg:col-span-2 space-y-4">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-lg font-bold text-slate-900 dark:text-white">
                  Recent Forms
                </h2>
                <p className="text-xs text-slate-500">Quickly access and edit your active forms</p>
              </div>
              <div className="flex items-center gap-2">
                <div className="relative w-48 sm:w-64">
                  <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input
                    type="text"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder="Search recent forms..."
                    className="w-full pl-8 pr-3 py-1.5 text-xs bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg focus:outline-none focus:ring-1 focus:ring-emerald-500 text-slate-900 dark:text-white"
                  />
                </div>
                <Link to="/forms">
                  <Button variant="ghost" size="sm" className="text-xs">
                    View All
                  </Button>
                </Link>
              </div>
            </div>

            {formsLoading ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {[...Array(4)].map((_, i) => (
                  <Skeleton key={i} className="h-44 w-full rounded-2xl" />
                ))}
              </div>
            ) : forms.length === 0 ? (
              <EmptyState
                icon={<FileText className="w-12 h-12 text-slate-400" />}
                title="No forms found"
                description={
                  search
                    ? `No forms match your search "${search}".`
                    : 'Start creating powerful forms with Google Gemini AI or from a clean canvas.'
                }
                action={
                  <div className="flex items-center gap-2">
                    <Link to="/generate-ai">
                      <Button size="sm">
                        <Sparkles className="w-4 h-4 mr-1.5" />
                        Create with AI
                      </Button>
                    </Link>
                    <Button variant="outline" size="sm" onClick={handleCreateScratch}>
                      Blank Form
                    </Button>
                  </div>
                }
              />
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {forms.map((form) => (
                  <Card key={form.id} className="hover:border-emerald-400 dark:hover:border-emerald-700 transition-all group">
                    <CardContent className="p-5 flex flex-col justify-between h-full">
                      <div>
                        <div className="flex items-center justify-between mb-3">
                          <Badge
                            variant={
                              form.status === 'PUBLISHED'
                                ? 'success'
                                : form.status === 'ARCHIVED'
                                ? 'outline'
                                : 'secondary'
                            }
                          >
                            {form.status}
                          </Badge>
                          <span className="text-[11px] text-slate-400 flex items-center gap-1">
                            <Clock className="w-3 h-3" />
                            {new Date(form.updatedAt).toLocaleDateString()}
                          </span>
                        </div>

                        <Link to={`/builder/${form.id}`}>
                          <h4 className="font-semibold text-slate-900 dark:text-white group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors line-clamp-1">
                            {form.title}
                          </h4>
                          <p className="text-xs text-slate-500 line-clamp-2 mt-1 leading-relaxed">
                            {form.description || 'No description provided'}
                          </p>
                        </Link>
                      </div>

                      <div className="pt-4 mt-4 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between">
                        <div className="flex items-center gap-3 text-xs text-slate-500">
                          <span className="flex items-center gap-1">
                            <MessageSquare className="w-3.5 h-3.5 text-emerald-500" />
                            {form.submissionCount}
                          </span>
                          <span>{form.questionCount} questions</span>
                        </div>

                        <div className="flex items-center gap-1">
                          {form.publishedSlug && (
                            <button
                              onClick={() => copyPublicLink(form.publishedSlug!)}
                              className="p-1.5 text-slate-400 hover:text-emerald-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                              title="Copy Public Link"
                            >
                              <ExternalLink className="w-3.5 h-3.5" />
                            </button>
                          )}
                          <button
                            onClick={() => handleDuplicate(form.id)}
                            className="p-1.5 text-slate-400 hover:text-emerald-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                            title="Duplicate"
                          >
                            <Copy className="w-3.5 h-3.5" />
                          </button>
                          <button
                            onClick={() => handleDelete(form.id)}
                            className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                            title="Delete"
                          >
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            )}
          </div>

          {/* AI Insights & Quick Stats (1 Col) */}
          <div className="space-y-4">
            <h2 className="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <Sparkles className="w-5 h-5 text-emerald-500" />
              AI Intelligence
            </h2>

            <Card className="bg-gradient-to-br from-emerald-50/50 via-white to-teal-50/30 dark:from-emerald-950/20 dark:via-slate-900 dark:to-slate-950 border-emerald-200/60 dark:border-emerald-900/40">
              <CardHeader className="pb-2">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-semibold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider flex items-center gap-1.5">
                    <Lightbulb className="w-4 h-4" />
                    Response Insights
                  </span>
                  <Badge variant="outline" className="bg-white/80 dark:bg-slate-900/80 text-[10px]">
                    Live AI
                  </Badge>
                </div>
                <CardTitle className="text-base font-bold mt-1">
                  Workspace Sentiment
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {insightsLoading ? (
                  <div className="space-y-2">
                    <Skeleton className="h-4 w-full" />
                    <Skeleton className="h-4 w-5/6" />
                    <Skeleton className="h-16 w-full rounded-xl" />
                  </div>
                ) : (
                  <>
                    <p className="text-xs text-slate-600 dark:text-slate-300 leading-relaxed">
                      {aiInsights?.summary ||
                        'Across active forms, response completion rates are pacing high with predominantly positive user sentiment. Keep questions below 8 per section to maximize mobile completion.'}
                    </p>

                    {aiInsights?.recommendations && aiInsights.recommendations.length > 0 && (
                      <div className="space-y-2 pt-2 border-t border-slate-200/60 dark:border-slate-800">
                        <span className="text-[11px] font-bold text-slate-700 dark:text-slate-300 uppercase">
                          Key Recommendations:
                        </span>
                        <ul className="space-y-1.5">
                          {aiInsights.recommendations.slice(0, 3).map((rec, i) => (
                            <li key={i} className="text-xs text-slate-600 dark:text-slate-400 flex items-start gap-2">
                              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 shrink-0 mt-1.5" />
                              <span>{rec}</span>
                            </li>
                          ))}
                        </ul>
                      </div>
                    )}

                    <Link to="/insights" className="block pt-2">
                      <Button variant="outline" size="sm" className="w-full text-xs">
                        View Deep Workspace Insights
                        <ArrowRight className="w-3.5 h-3.5 ml-1" />
                      </Button>
                    </Link>
                  </>
                )}
              </CardContent>
            </Card>

            {/* Quick Links Card */}
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-sm font-bold text-slate-900 dark:text-white">
                  Quick Links
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-2 text-xs">
                <Link
                  to="/templates"
                  className="flex items-center justify-between p-2.5 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                >
                  <span className="font-medium text-slate-700 dark:text-slate-200">
                    Browse 20+ Templates
                  </span>
                  <ArrowRight className="w-3.5 h-3.5 text-slate-400" />
                </Link>
                <Link
                  to="/inbox"
                  className="flex items-center justify-between p-2.5 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                >
                  <span className="font-medium text-slate-700 dark:text-slate-200">
                    Central Response Inbox
                  </span>
                  <ArrowRight className="w-3.5 h-3.5 text-slate-400" />
                </Link>
                <Link
                  to="/settings"
                  className="flex items-center justify-between p-2.5 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                >
                  <span className="font-medium text-slate-700 dark:text-slate-200">
                    Workspace Settings
                  </span>
                  <ArrowRight className="w-3.5 h-3.5 text-slate-400" />
                </Link>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};
