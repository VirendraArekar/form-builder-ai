import React, { useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { formService } from '@/services/formService';
import { responseService } from '@/services/responseService';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { EmptyState } from '@/components/ui/EmptyState';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  ArrowLeft,
  Download,
  Search,
  Filter,
  CheckCircle2,
  Clock,
  Trash2,
  Eye,
  Tag,
  MessageSquare,
  FileSpreadsheet,
  Check,
  ChevronRight,
  ExternalLink,
} from 'lucide-react';

export const ResponsesPage: React.FC = () => {
  const { formId } = useParams<{ formId: string }>();
  const navigate = useNavigate();
  const { showToast } = useToast();

  const [filterReviewed, setFilterReviewed] = useState<'ALL' | 'REVIEWED' | 'UNREVIEWED'>('ALL');
  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [isExporting, setIsExporting] = useState(false);

  // Form metadata
  const { data: form } = useQuery({
    queryKey: ['form-meta', formId],
    queryFn: () => formService.getForm(formId!),
    enabled: !!formId,
  });

  // Responses list
  const {
    data: responsesData,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['form-responses', formId, filterReviewed],
    queryFn: () =>
      responseService.listResponses(formId!, {
        isReviewed:
          filterReviewed === 'ALL' ? undefined : filterReviewed === 'REVIEWED',
        size: 50,
      }),
    enabled: !!formId,
  });

  const handleToggleReview = async (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      await responseService.toggleReview(formId!, id);
      refetch();
      showToast('Review status updated', 'success');
    } catch (err: any) {
      showToast('Failed to update review status', 'error');
    }
  };

  const handleDelete = async (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    if (!window.confirm('Are you sure you want to delete this response?')) return;
    try {
      await responseService.deleteResponse(formId!, id);
      refetch();
      showToast('Response deleted', 'success');
    } catch (err: any) {
      showToast('Failed to delete response', 'error');
    }
  };

  const handleBulkDelete = async () => {
    if (!window.confirm(`Delete ${selectedIds.length} responses?`)) return;
    try {
      await responseService.bulkDeleteResponses(formId!, selectedIds);
      setSelectedIds([]);
      refetch();
      showToast('Selected responses deleted', 'success');
    } catch (err: any) {
      showToast('Bulk delete failed', 'error');
    }
  };

  const handleExportCsv = async () => {
    if (!formId) return;
    setIsExporting(true);
    try {
      await responseService.exportCsv(formId);
      showToast('CSV downloaded successfully', 'success');
    } catch (err: any) {
      showToast('Failed to export CSV', 'error');
    } finally {
      setIsExporting(false);
    }
  };

  const toggleSelect = (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    setSelectedIds((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const responses = responsesData?.content || [];

  return (
    <DashboardLayout>
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Top Header */}
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
                Responses: {form?.title || 'Form'}
              </h1>
              <p className="text-xs text-slate-500">
                Total submissions captured: <strong className="text-slate-700 dark:text-slate-300">{responses.length}</strong>
              </p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <Button
              variant="outline"
              size="sm"
              onClick={handleExportCsv}
              isLoading={isExporting}
              disabled={responses.length === 0}
            >
              <Download className="w-4 h-4 mr-1.5" />
              Export CSV
            </Button>
            <Link to={`/forms/${formId}/analytics`}>
              <Button size="sm" variant="outline">
                View Analytics
              </Button>
            </Link>
          </div>
        </div>

        {/* Filter bar */}
        <div className="flex items-center justify-between border-b border-slate-200 dark:border-slate-800 pb-3">
          <div className="flex items-center gap-2">
            {(['ALL', 'UNREVIEWED', 'REVIEWED'] as const).map((tab) => (
              <button
                key={tab}
                onClick={() => setFilterReviewed(tab)}
                className={`text-xs px-3 py-1.5 rounded-lg font-semibold transition-colors ${
                  filterReviewed === tab
                    ? 'bg-indigo-600 text-white'
                    : 'text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
                }`}
              >
                {tab === 'ALL' ? 'All Submissions' : tab === 'REVIEWED' ? 'Reviewed' : 'Unreviewed'}
              </button>
            ))}
          </div>

          {selectedIds.length > 0 && (
            <div className="flex items-center gap-2">
              <span className="text-xs font-semibold text-slate-600 dark:text-slate-400">
                {selectedIds.length} selected
              </span>
              <Button size="sm" variant="danger" onClick={handleBulkDelete}>
                <Trash2 className="w-3.5 h-3.5 mr-1" />
                Delete Selected
              </Button>
            </div>
          )}
        </div>

        {/* Content */}
        {isLoading ? (
          <div className="space-y-3">
            {[...Array(5)].map((_, i) => (
              <Skeleton key={i} className="h-16 w-full rounded-xl" />
            ))}
          </div>
        ) : responses.length === 0 ? (
          <EmptyState
            icon={<MessageSquare className="w-12 h-12 text-slate-400" />}
            title="No responses yet"
            description="When users fill out and submit your form, their answers will appear here in real time."
            action={
              form?.publishedSlug ? (
                <a
                  href={`/f/${form.publishedSlug}`}
                  target="_blank"
                  rel="noreferrer"
                >
                  <Button size="sm">
                    Open Public Form
                    <ExternalLink className="w-3.5 h-3.5 ml-1.5" />
                  </Button>
                </a>
              ) : undefined
            }
          />
        ) : (
          <Card className="overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-xs">
                <thead>
                  <tr className="bg-slate-50 dark:bg-slate-800/60 border-b border-slate-200 dark:border-slate-800 text-slate-500 uppercase font-semibold">
                    <th className="p-3 w-10">
                      <input
                        type="checkbox"
                        checked={selectedIds.length === responses.length}
                        onChange={() => {
                          if (selectedIds.length === responses.length) {
                            setSelectedIds([]);
                          } else {
                            setSelectedIds(responses.map((r) => r.id));
                          }
                        }}
                        className="rounded border-slate-300 text-indigo-600"
                      />
                    </th>
                    <th className="p-3">Submission ID</th>
                    <th className="p-3">Submitted At</th>
                    <th className="p-3">Duration</th>
                    <th className="p-3">Status</th>
                    <th className="p-3">Answer Summary</th>
                    <th className="p-3 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 dark:divide-slate-800 text-slate-700 dark:text-slate-300">
                  {responses.map((res) => {
                    const isSelected = selectedIds.includes(res.id);
                    return (
                      <tr
                        key={res.id}
                        onClick={() => navigate(`/forms/${formId}/responses/${res.id}`)}
                        className={`hover:bg-slate-50 dark:hover:bg-slate-800/40 cursor-pointer transition-colors ${
                          isSelected ? 'bg-indigo-50/50 dark:bg-indigo-950/20' : ''
                        }`}
                      >
                        <td className="p-3" onClick={(e) => toggleSelect(res.id, e)}>
                          <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => {}}
                            className="rounded border-slate-300 text-indigo-600"
                          />
                        </td>
                        <td className="p-3 font-mono font-medium text-indigo-600 dark:text-indigo-400">
                          {res.id.substring(0, 12)}...
                        </td>
                        <td className="p-3 text-slate-500">
                          {new Date(res.submittedAt).toLocaleString()}
                        </td>
                        <td className="p-3 text-slate-500">
                          {res.durationSeconds ? `${res.durationSeconds}s` : '—'}
                        </td>
                        <td className="p-3">
                          <Badge variant={res.isReviewed ? 'success' : 'secondary'}>
                            {res.isReviewed ? 'Reviewed' : 'Unreviewed'}
                          </Badge>
                        </td>
                        <td className="p-3 max-w-xs truncate text-slate-500">
                          {res.answersSummary || 'No answer preview'}
                        </td>
                        <td className="p-3 text-right">
                          <div className="flex items-center justify-end gap-1">
                            <button
                              onClick={(e) => handleToggleReview(res.id, e)}
                              className="p-1.5 text-slate-400 hover:text-emerald-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800"
                              title={res.isReviewed ? 'Mark as Unreviewed' : 'Mark as Reviewed'}
                            >
                              <CheckCircle2 className="w-3.5 h-3.5" />
                            </button>
                            <button
                              onClick={(e) => handleDelete(res.id, e)}
                              className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800"
                              title="Delete submission"
                            >
                              <Trash2 className="w-3.5 h-3.5" />
                            </button>
                            <ChevronRight className="w-3.5 h-3.5 text-slate-400" />
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </Card>
        )}
      </div>
    </DashboardLayout>
  );
};
