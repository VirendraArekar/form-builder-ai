import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { inboxService } from '@/services/inboxService';
import { formService } from '@/services/formService';
import { useAuthStore } from '@/store/authStore';
import { InboxItemDto } from '@/types';
import { Card, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Skeleton } from '@/components/ui/Skeleton';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';
import {
  Inbox,
  Search,
  Filter,
  RefreshCw,
  Clock,
  Laptop,
  Smartphone,
  Tablet,
  FileText,
  Mail,
  User,
  ExternalLink,
  ChevronLeft,
  ChevronRight,
  Eye,
  CheckCircle2,
  Calendar,
} from 'lucide-react';

export const InboxPage: React.FC = () => {
  const { currentWorkspace } = useAuthStore();
  const { showToast } = useToast();
  const workspaceId = currentWorkspace?.id || '';

  const [selectedFormId, setSelectedFormId] = useState<string>('');
  const [search, setSearch] = useState<string>('');
  const [page, setPage] = useState<number>(0);
  const [inspectItem, setInspectItem] = useState<InboxItemDto | null>(null);

  // Fetch list of user's forms for the filter dropdown
  const { data: formsData } = useQuery({
    queryKey: ['workspace-forms-filter', workspaceId],
    queryFn: () =>
      formService.listForms({
        workspaceId,
        size: 50,
      }),
    enabled: !!workspaceId,
  });

  // Fetch unified inbox submissions
  const {
    data: inboxData,
    isLoading,
    refetch,
    isFetching,
  } = useQuery({
    queryKey: ['inbox-submissions', selectedFormId, search, page],
    queryFn: () =>
      inboxService.getInbox({
        formId: selectedFormId || undefined,
        search: search.trim() || undefined,
        page,
        size: 15,
      }),
  });

  const submissions = inboxData?.content || [];
  const totalElements = inboxData?.totalElements || 0;
  const totalPages = inboxData?.totalPages || 1;

  const formatDuration = (seconds: number) => {
    if (!seconds || seconds <= 0) return '< 30s';
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    if (m === 0) return `${s}s`;
    return `${m}m ${s}s`;
  };

  const formatDate = (dateStr: string) => {
    if (!dateStr) return '';
    try {
      const d = new Date(dateStr);
      return d.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return dateStr;
    }
  };

  const getDeviceIcon = (device: string) => {
    switch (device?.toLowerCase()) {
      case 'mobile':
        return <Smartphone className="w-3.5 h-3.5 text-slate-400" />;
      case 'tablet':
        return <Tablet className="w-3.5 h-3.5 text-slate-400" />;
      default:
        return <Laptop className="w-3.5 h-3.5 text-slate-400" />;
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-7xl mx-auto space-y-6 pb-16">
        {/* Top Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 border border-emerald-200 dark:border-emerald-900 mb-2">
              <Inbox className="w-3.5 h-3.5" />
              Unified Stream
            </div>
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
              Responses Inbox
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
              Live incoming submissions across all forms in your workspace.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <Button
              variant="outline"
              size="sm"
              onClick={() => refetch()}
              isLoading={isFetching}
            >
              <RefreshCw className="w-4 h-4 mr-1.5" />
              Refresh
            </Button>
          </div>
        </div>

        {/* Filter and Search Bar */}
        <div className="flex flex-col sm:flex-row items-center gap-3 bg-white dark:bg-slate-900 p-3.5 rounded-2xl border border-slate-200/80 dark:border-slate-800 shadow-xs">
          {/* Form Filter Selector */}
          <div className="w-full sm:w-64">
            <select
              value={selectedFormId}
              onChange={(e) => {
                setSelectedFormId(e.target.value);
                setPage(0);
              }}
              className="w-full text-xs font-semibold px-3 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-800 dark:text-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-500 cursor-pointer"
            >
              <option value="">All Workspace Forms</option>
              {(formsData?.content || []).map((f) => (
                <option key={f.id} value={f.id}>
                  {f.title}
                </option>
              ))}
            </select>
          </div>

          {/* Search Box */}
          <div className="relative flex-1 w-full">
            <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              value={search}
              onChange={(e) => {
                setSearch(e.target.value);
                setPage(0);
              }}
              placeholder="Search by respondent name, email, question or answer..."
              className="w-full text-xs pl-10 pr-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-emerald-500"
            />
          </div>

          <div className="text-xs text-slate-400 font-semibold px-2 shrink-0">
            {totalElements} {totalElements === 1 ? 'submission' : 'submissions'}
          </div>
        </div>

        {/* Submissions List */}
        {isLoading ? (
          <div className="space-y-3">
            {[...Array(5)].map((_, i) => (
              <Skeleton key={i} className="h-20 w-full rounded-2xl" />
            ))}
          </div>
        ) : submissions.length === 0 ? (
          <div className="text-center py-20 bg-white dark:bg-slate-900 rounded-3xl border border-slate-200/80 dark:border-slate-800 shadow-xs space-y-3">
            <div className="w-14 h-14 bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 rounded-2xl flex items-center justify-center mx-auto">
              <Inbox className="w-7 h-7" />
            </div>
            <h3 className="text-base font-bold text-slate-900 dark:text-white">
              No Submissions Found
            </h3>
            <p className="text-xs text-slate-400 max-w-sm mx-auto">
              {search || selectedFormId
                ? 'Try clearing your filters or searching for different keywords.'
                : 'Responses will appear here in real-time as respondents submit your published forms.'}
            </p>
          </div>
        ) : (
          <div className="space-y-3">
            {submissions.map((item) => (
              <div
                key={item.responseId}
                onClick={() => setInspectItem(item)}
                className="flex flex-col md:flex-row md:items-center justify-between p-4 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800 hover:border-emerald-400 dark:hover:border-emerald-700 shadow-xs hover:shadow-md transition-all cursor-pointer group gap-4"
              >
                <div className="flex items-start md:items-center gap-3.5 min-w-0">
                  <div
                    className="w-10 h-10 rounded-xl flex items-center justify-center text-white font-bold text-xs uppercase shrink-0 shadow-xs"
                    style={{ backgroundColor: item.formThemeColor || '#10b981' }}
                  >
                    {item.respondentName?.charAt(0) || 'R'}
                  </div>
                  <div className="min-w-0 truncate">
                    <div className="flex items-center gap-2 flex-wrap">
                      <span className="text-xs font-extrabold text-slate-900 dark:text-white truncate">
                        {item.respondentName}
                      </span>
                      <span className="text-[11px] text-slate-400">•</span>
                      <span className="text-[11px] text-slate-500 dark:text-slate-400 truncate">
                        {item.respondentEmail}
                      </span>
                      <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 border border-slate-200 dark:border-slate-700 truncate">
                        {item.formTitle}
                      </span>
                    </div>
                    <p className="text-xs text-slate-600 dark:text-slate-300 mt-1 truncate max-w-xl">
                      "{item.responsePreview}"
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-3 text-xs text-slate-400 shrink-0 self-end md:self-center">
                  <div className="flex items-center gap-1.5" title={`Device: ${item.deviceType}`}>
                    {getDeviceIcon(item.deviceType)}
                    <span className="text-[11px]">{item.deviceType}</span>
                  </div>
                  <span className="text-slate-300 dark:text-slate-700">•</span>
                  <div className="flex items-center gap-1" title="Time taken to complete">
                    <Clock className="w-3.5 h-3.5" />
                    <span className="text-[11px]">{formatDuration(item.durationSeconds)}</span>
                  </div>
                  <span className="text-slate-300 dark:text-slate-700">•</span>
                  <span className="text-[11px] text-slate-500 dark:text-slate-400">
                    {formatDate(item.submittedAt)}
                  </span>
                  <Button
                    variant="ghost"
                    size="sm"
                    className="opacity-0 group-hover:opacity-100 transition-opacity ml-1"
                    onClick={(e) => {
                      e.stopPropagation();
                      setInspectItem(item);
                    }}
                  >
                    <Eye className="w-4 h-4 text-emerald-600" />
                  </Button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Pagination Bar */}
        {totalPages > 1 && (
          <div className="flex items-center justify-between pt-4 border-t border-slate-200 dark:border-slate-800">
            <span className="text-xs text-slate-400">
              Page {page + 1} of {totalPages}
            </span>
            <div className="flex items-center gap-2">
              <Button
                variant="outline"
                size="sm"
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
              >
                <ChevronLeft className="w-4 h-4 mr-1" />
                Previous
              </Button>
              <Button
                variant="outline"
                size="sm"
                onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                disabled={page >= totalPages - 1}
              >
                Next
                <ChevronRight className="w-4 h-4 ml-1" />
              </Button>
            </div>
          </div>
        )}

        {/* Inspect Submission Modal */}
        {inspectItem && (
          <Modal
            isOpen={!!inspectItem}
            onClose={() => setInspectItem(null)}
            title="Submission Details"
            maxWidth="lg"
          >
            <div className="space-y-6">
              {/* Metadata Header */}
              <div className="p-4 rounded-2xl bg-slate-50 dark:bg-slate-800/60 border border-slate-200/80 dark:border-slate-700/60 space-y-3">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <div
                      className="w-3 h-3 rounded-full"
                      style={{ backgroundColor: inspectItem.formThemeColor || '#10b981' }}
                    />
                    <h4 className="font-extrabold text-sm text-slate-900 dark:text-white">
                      {inspectItem.formTitle}
                    </h4>
                  </div>
                  <Link
                    to={`/forms/${inspectItem.formId}/responses`}
                    className="text-xs font-semibold text-emerald-600 dark:text-emerald-400 hover:underline flex items-center gap-1"
                  >
                    Open in Form Responses <ExternalLink className="w-3.5 h-3.5" />
                  </Link>
                </div>

                <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 text-xs pt-1">
                  <div>
                    <span className="text-slate-400 block text-[10px] uppercase font-bold">Respondent</span>
                    <span className="font-semibold text-slate-900 dark:text-white">
                      {inspectItem.respondentName}
                    </span>
                  </div>
                  <div>
                    <span className="text-slate-400 block text-[10px] uppercase font-bold">Email</span>
                    <span className="font-semibold text-slate-900 dark:text-white">
                      {inspectItem.respondentEmail}
                    </span>
                  </div>
                  <div>
                    <span className="text-slate-400 block text-[10px] uppercase font-bold">Submitted At</span>
                    <span className="font-semibold text-slate-900 dark:text-white">
                      {formatDate(inspectItem.submittedAt)}
                    </span>
                  </div>
                  <div>
                    <span className="text-slate-400 block text-[10px] uppercase font-bold">Duration</span>
                    <span className="font-semibold text-slate-900 dark:text-white">
                      {formatDuration(inspectItem.durationSeconds)}
                    </span>
                  </div>
                </div>
              </div>

              {/* Answers Breakdown */}
              <div className="space-y-3">
                <h5 className="text-xs font-bold uppercase tracking-wider text-slate-400">
                  Question Responses ({Object.keys(inspectItem.answers || {}).length})
                </h5>
                <div className="space-y-2.5 max-h-96 overflow-y-auto pr-1">
                  {Object.entries(inspectItem.answers || {}).map(([key, val], idx) => (
                    <div
                      key={idx}
                      className="p-3.5 rounded-xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 space-y-1 text-xs"
                    >
                      <p className="font-bold text-slate-700 dark:text-slate-300 capitalize">
                        {key.replace(/_/g, ' ')}
                      </p>
                      <p className="text-slate-900 dark:text-white font-medium bg-slate-50 dark:bg-slate-800/50 p-2 rounded-lg">
                        {val || <span className="text-slate-400 italic">No answer provided</span>}
                      </p>
                    </div>
                  ))}
                </div>
              </div>

              {/* Modal Footer */}
              <div className="flex justify-end pt-2">
                <Button onClick={() => setInspectItem(null)}>
                  Close
                </Button>
              </div>
            </div>
          </Modal>
        )}
      </div>
    </DashboardLayout>
  );
};
