import React, { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
} from 'recharts';
import { formService } from '@/services/formService';
import { analyticsService } from '@/services/analyticsService';
import { aiService } from '@/services/aiService';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Skeleton } from '@/components/ui/Skeleton';
import {
  ArrowLeft,
  Eye,
  CheckCircle2,
  TrendingUp,
  Clock,
  Sparkles,
  BarChart3,
  PieChart as PieIcon,
  Laptop,
  Smartphone,
  Tablet,
  Calendar,
  AlertCircle,
} from 'lucide-react';

const COLORS = ['#4f46e5', '#7c3aed', '#2563eb', '#06b6d4', '#10b981', '#f59e0b', '#ef4444'];

export const FormAnalyticsPage: React.FC = () => {
  const { formId } = useParams<{ formId: string }>();
  const [days, setDays] = useState(30);

  // Form details
  const { data: form } = useQuery({
    queryKey: ['form-details', formId],
    queryFn: () => formService.getForm(formId!),
    enabled: !!formId,
  });

  // Overview analytics
  const { data: overview, isLoading: overviewLoading } = useQuery({
    queryKey: ['form-overview-analytics', formId, days],
    queryFn: () => analyticsService.getFormOverview(formId!, days),
    enabled: !!formId,
  });

  // Per-question analytics
  const { data: questionsAnalytics, isLoading: questionsLoading } = useQuery({
    queryKey: ['form-questions-analytics', formId],
    queryFn: () => analyticsService.getQuestionAnalytics(formId!),
    enabled: !!formId,
  });

  // AI Insights
  const { data: aiInsights, isLoading: aiLoading } = useQuery({
    queryKey: ['form-ai-insights', formId],
    queryFn: () => aiService.generateInsights({ formId }),
    enabled: !!formId,
  });

  if (overviewLoading) {
    return (
      <DashboardLayout>
        <div className="max-w-7xl mx-auto space-y-6">
          <Skeleton className="h-10 w-64" />
          <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
            {[...Array(4)].map((_, i) => (
              <Skeleton key={i} className="h-28 rounded-xl" />
            ))}
          </div>
          <Skeleton className="h-80 w-full rounded-2xl" />
        </div>
      </DashboardLayout>
    );
  }

  const dailyTrends = overview?.dailyTrends || [];
  const deviceDistribution = overview?.deviceDistribution || {};
  const deviceData = Object.entries(deviceDistribution).map(([name, value]) => ({
    name,
    value,
  }));

  return (
    <DashboardLayout>
      <div className="max-w-7xl mx-auto space-y-8 pb-16">
        {/* Top Bar */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <Link
              to={`/builder/${formId}`}
              className="p-1.5 rounded-lg text-slate-500 hover:text-slate-800 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            >
              <ArrowLeft className="w-4 h-4" />
            </Link>
            <div>
              <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-white">
                Analytics: {form?.title || 'Form'}
              </h1>
              <p className="text-xs text-slate-500">
                Live submission performance, visitor funnel, and question response breakdowns.
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <div className="flex items-center border border-slate-200 dark:border-slate-800 rounded-lg p-0.5 bg-slate-100 dark:bg-slate-800 text-xs">
              {[7, 30, 90].map((d) => (
                <button
                  key={d}
                  onClick={() => setDays(d)}
                  className={`px-3 py-1.5 rounded-md font-semibold transition-colors ${
                    days === d
                      ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-xs'
                      : 'text-slate-500 hover:text-slate-800'
                  }`}
                >
                  Last {d}d
                </button>
              ))}
            </div>

            <Link to={`/forms/${formId}/responses`}>
              <Button size="sm" variant="outline">
                View Submissions
              </Button>
            </Link>
          </div>
        </div>

        {/* 4 Metric Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card>
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">
                  Total Views
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {overview?.totalViews ?? 0}
                </h3>
                <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                  <Eye className="w-3.5 h-3.5" />
                  Form impressions
                </p>
              </div>
              <div className="w-12 h-12 bg-indigo-50 dark:bg-indigo-950/60 text-indigo-600 rounded-xl flex items-center justify-center">
                <Eye className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">
                  Submissions
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {overview?.totalSubmissions ?? 0}
                </h3>
                <p className="text-xs text-emerald-600 mt-1 flex items-center gap-1">
                  <CheckCircle2 className="w-3.5 h-3.5" />
                  Successful responses
                </p>
              </div>
              <div className="w-12 h-12 bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 rounded-xl flex items-center justify-center">
                <CheckCircle2 className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">
                  Completion Rate
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {Math.round(overview?.completionRate ?? 0)}%
                </h3>
                <p className="text-xs text-indigo-600 mt-1 flex items-center gap-1">
                  <TrendingUp className="w-3.5 h-3.5" />
                  Drop-off: {Math.round(overview?.abandonmentRate ?? 0)}%
                </p>
              </div>
              <div className="w-12 h-12 bg-violet-50 dark:bg-violet-950/60 text-violet-600 rounded-xl flex items-center justify-center">
                <TrendingUp className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">
                  Avg. Completion Time
                </p>
                <h3 className="text-2xl font-extrabold text-slate-900 dark:text-white mt-1">
                  {overview?.avgDurationSeconds ? `${Math.round(overview.avgDurationSeconds)}s` : '—'}
                </h3>
                <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                  <Clock className="w-3.5 h-3.5" />
                  Speed to finish
                </p>
              </div>
              <div className="w-12 h-12 bg-amber-50 dark:bg-amber-950/60 text-amber-600 rounded-xl flex items-center justify-center">
                <Clock className="w-6 h-6" />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* AI Insight Highlight Card */}
        {aiInsights && (
          <div className="p-5 rounded-2xl bg-gradient-to-r from-indigo-900 via-indigo-950 to-slate-950 text-white border border-indigo-500/30 shadow-lg flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
            <div className="space-y-1">
              <span className="text-xs font-bold text-indigo-300 uppercase tracking-wider flex items-center gap-1.5">
                <Sparkles className="w-4 h-4 text-indigo-400" />
                Gemini AI Form Diagnostics
              </span>
              <p className="text-sm text-indigo-100/90 max-w-3xl leading-relaxed">
                {aiInsights.summary ||
                  'Submissions indicate solid engagement. Review question drop-off rates below to identify potential friction points.'}
              </p>
            </div>
            <Link to="/insights">
              <Button size="sm" className="bg-indigo-600 hover:bg-indigo-500 text-white shrink-0">
                Explore Full AI Insights
              </Button>
            </Link>
          </div>
        )}

        {/* Charts Row: Trend Area Chart (2 Cols) & Devices Donut (1 Col) */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Daily Trends Chart */}
          <Card className="lg:col-span-2">
            <CardHeader className="pb-2">
              <CardTitle className="text-base font-bold flex items-center gap-2">
                <BarChart3 className="w-4 h-4 text-indigo-500" />
                Submissions & Views Over Time
              </CardTitle>
            </CardHeader>
            <CardContent>
              {dailyTrends.length === 0 ? (
                <div className="h-64 flex items-center justify-center text-xs text-slate-400">
                  No trend data available for this date window.
                </div>
              ) : (
                <div className="h-72 w-full pt-4">
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={dailyTrends}>
                      <defs>
                        <linearGradient id="viewsGrad" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#4f46e5" stopOpacity={0.4} />
                          <stop offset="95%" stopColor="#4f46e5" stopOpacity={0.0} />
                        </linearGradient>
                        <linearGradient id="subsGrad" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#10b981" stopOpacity={0.4} />
                          <stop offset="95%" stopColor="#10b981" stopOpacity={0.0} />
                        </linearGradient>
                      </defs>
                      <XAxis dataKey="date" stroke="#94a3b8" fontSize={11} tickLine={false} />
                      <YAxis stroke="#94a3b8" fontSize={11} tickLine={false} />
                      <Tooltip
                        contentStyle={{
                          backgroundColor: '#0f172a',
                          border: 'none',
                          borderRadius: '8px',
                          color: '#fff',
                          fontSize: '12px',
                        }}
                      />
                      <Legend />
                      <Area
                        type="monotone"
                        dataKey="views"
                        name="Views"
                        stroke="#4f46e5"
                        fill="url(#viewsGrad)"
                        strokeWidth={2}
                      />
                      <Area
                        type="monotone"
                        dataKey="submissions"
                        name="Submissions"
                        stroke="#10b981"
                        fill="url(#subsGrad)"
                        strokeWidth={2}
                      />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
              )}
            </CardContent>
          </Card>

          {/* Device Distribution */}
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-base font-bold flex items-center gap-2">
                <Laptop className="w-4 h-4 text-indigo-500" />
                Device Breakdown
              </CardTitle>
            </CardHeader>
            <CardContent>
              {deviceData.length === 0 ? (
                <div className="h-64 flex items-center justify-center text-xs text-slate-400">
                  No device data captured yet.
                </div>
              ) : (
                <div className="h-72 w-full flex flex-col items-center justify-center">
                  <ResponsiveContainer width="100%" height={200}>
                    <PieChart>
                      <Pie
                        data={deviceData}
                        cx="50%"
                        cy="50%"
                        innerRadius={50}
                        outerRadius={75}
                        paddingAngle={4}
                        dataKey="value"
                      >
                        {deviceData.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                      </Pie>
                      <Tooltip />
                    </PieChart>
                  </ResponsiveContainer>
                  <div className="flex flex-wrap justify-center gap-3 text-xs pt-2">
                    {deviceData.map((d, i) => (
                      <span key={d.name} className="flex items-center gap-1.5">
                        <span
                          className="w-2.5 h-2.5 rounded-full"
                          style={{ backgroundColor: COLORS[i % COLORS.length] }}
                        />
                        <span className="capitalize text-slate-600 dark:text-slate-400">
                          {d.name}: {d.value}
                        </span>
                      </span>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </div>

        {/* Per-Question Breakdown Section */}
        <div className="space-y-4">
          <h2 className="text-xl font-bold tracking-tight text-slate-900 dark:text-white flex items-center gap-2">
            <PieIcon className="w-5 h-5 text-indigo-500" />
            Per-Question Response Breakdown
          </h2>

          {questionsLoading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {[...Array(4)].map((_, i) => (
                <Skeleton key={i} className="h-60 rounded-xl" />
              ))}
            </div>
          ) : !questionsAnalytics || questionsAnalytics.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center text-xs text-slate-400">
                No question responses recorded yet. Share your form to start collecting responses!
              </CardContent>
            </Card>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {questionsAnalytics.map((qa, idx) => (
                <Card key={qa.questionId || idx} className="flex flex-col justify-between">
                  <CardHeader className="pb-2">
                    <div className="flex items-start justify-between gap-2">
                      <CardTitle className="text-sm font-bold leading-tight">
                        {idx + 1}. {qa.questionTitle}
                      </CardTitle>
                      <Badge variant="secondary" className="text-[10px] shrink-0">
                        {qa.totalAnswers} answered
                      </Badge>
                    </div>
                  </CardHeader>
                  <CardContent className="pt-2 flex-1">
                    {/* Option-based questions (Single Choice, Multiple Choice, Dropdown) */}
                    {qa.optionMetrics && qa.optionMetrics.length > 0 ? (
                      <div className="space-y-2.5 pt-2">
                        {qa.optionMetrics.map((opt, oIdx) => (
                          <div key={oIdx} className="space-y-1">
                            <div className="flex items-center justify-between text-xs">
                              <span className="font-medium text-slate-700 dark:text-slate-300">
                                {opt.label}
                              </span>
                              <span className="font-semibold text-slate-500">
                                {opt.count} ({Math.round(opt.percentage)}%)
                              </span>
                            </div>
                            <div className="h-2 w-full bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                              <div
                                className="h-full bg-indigo-600 rounded-full transition-all duration-500"
                                style={{ width: `${Math.round(opt.percentage)}%` }}
                              />
                            </div>
                          </div>
                        ))}
                      </div>
                    ) : qa.numericStats ? (
                      /* Numeric Stats (Ratings, Sliders) */
                      <div className="grid grid-cols-3 gap-3 p-4 bg-slate-50 dark:bg-slate-800/40 rounded-xl text-center">
                        <div>
                          <span className="text-[10px] font-bold text-slate-400 uppercase">Average</span>
                          <p className="text-xl font-extrabold text-indigo-600 dark:text-indigo-400 mt-0.5">
                            {qa.numericStats.average.toFixed(1)}
                          </p>
                        </div>
                        <div>
                          <span className="text-[10px] font-bold text-slate-400 uppercase">Min</span>
                          <p className="text-xl font-extrabold text-slate-700 dark:text-slate-300 mt-0.5">
                            {qa.numericStats.min}
                          </p>
                        </div>
                        <div>
                          <span className="text-[10px] font-bold text-slate-400 uppercase">Max</span>
                          <p className="text-xl font-extrabold text-slate-700 dark:text-slate-300 mt-0.5">
                            {qa.numericStats.max}
                          </p>
                        </div>
                      </div>
                    ) : qa.sampleAnswers && qa.sampleAnswers.length > 0 ? (
                      /* Text Fields (Open-ended answers) */
                      <div className="space-y-2 pt-1">
                        <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">
                          Recent Submissions:
                        </span>
                        <div className="space-y-1.5 max-h-48 overflow-y-auto scrollbar-thin">
                          {qa.sampleAnswers.slice(0, 5).map((ans, aIdx) => (
                            <div
                              key={aIdx}
                              className="p-2 rounded bg-slate-50 dark:bg-slate-800/60 border border-slate-100 dark:border-slate-800 text-xs text-slate-700 dark:text-slate-300"
                            >
                              "{ans}"
                            </div>
                          ))}
                        </div>
                      </div>
                    ) : (
                      <p className="text-xs text-slate-400 italic">No answer distribution data.</p>
                    )}
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
};
