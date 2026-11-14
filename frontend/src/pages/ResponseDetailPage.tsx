import React, { useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { responseService } from '@/services/responseService';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  ArrowLeft,
  CheckCircle2,
  Trash2,
  Tag,
  MessageSquare,
  Clock,
  Laptop,
  Globe,
  Plus,
  X,
  FileText,
  Calendar,
} from 'lucide-react';

export const ResponseDetailPage: React.FC = () => {
  const { formId, responseId } = useParams<{ formId: string; responseId: string }>();
  const navigate = useNavigate();
  const { showToast } = useToast();

  const [newNote, setNewNote] = useState('');
  const [isAddingNote, setIsAddingNote] = useState(false);
  const [newTag, setNewTag] = useState('');

  const {
    data: response,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['response-detail', formId, responseId],
    queryFn: () => responseService.getResponseDetails(formId!, responseId!),
    enabled: !!formId && !!responseId,
  });

  const handleToggleReview = async () => {
    if (!formId || !responseId) return;
    try {
      await responseService.toggleReview(formId, responseId);
      refetch();
      showToast('Review status updated', 'success');
    } catch (err: any) {
      showToast('Failed to update review status', 'error');
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this submission?')) return;
    if (!formId || !responseId) return;
    try {
      await responseService.deleteResponse(formId, responseId);
      showToast('Submission deleted', 'success');
      navigate(`/forms/${formId}/responses`);
    } catch (err: any) {
      showToast('Failed to delete response', 'error');
    }
  };

  const handleAddNote = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formId || !responseId || !newNote.trim()) return;

    setIsAddingNote(true);
    try {
      await responseService.addNote(formId, responseId, newNote.trim());
      setNewNote('');
      refetch();
      showToast('Note added', 'success');
    } catch (err: any) {
      showToast('Failed to add note', 'error');
    } finally {
      setIsAddingNote(false);
    }
  };

  const handleAddTag = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formId || !responseId || !newTag.trim()) return;

    try {
      await responseService.addTag(formId, responseId, { tagName: newTag.trim() });
      setNewTag('');
      refetch();
      showToast('Tag added', 'success');
    } catch (err: any) {
      showToast('Failed to add tag', 'error');
    }
  };

  const handleRemoveTag = async (tagId: string) => {
    if (!formId || !responseId) return;
    try {
      await responseService.removeTag(formId, responseId, tagId);
      refetch();
      showToast('Tag removed', 'info');
    } catch (err: any) {
      showToast('Failed to remove tag', 'error');
    }
  };

  if (isLoading || !response) {
    return (
      <DashboardLayout>
        <div className="max-w-4xl mx-auto space-y-6">
          <Skeleton className="h-8 w-48" />
          <Skeleton className="h-64 w-full rounded-2xl" />
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="max-w-4xl mx-auto space-y-6 pb-16">
        {/* Top Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <Link
              to={`/forms/${formId}/responses`}
              className="p-1.5 rounded-lg text-slate-500 hover:text-slate-800 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            >
              <ArrowLeft className="w-4 h-4" />
            </Link>
            <div>
              <div className="flex items-center gap-2">
                <h1 className="text-xl font-bold tracking-tight text-slate-900 dark:text-white">
                  Submission Detail
                </h1>
                <Badge variant={response.isReviewed ? 'success' : 'secondary'}>
                  {response.isReviewed ? 'Reviewed' : 'Unreviewed'}
                </Badge>
              </div>
              <p className="text-xs text-slate-500 font-mono mt-0.5">{response.id}</p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <Button
              variant={response.isReviewed ? 'outline' : 'primary'}
              size="sm"
              onClick={handleToggleReview}
            >
              <CheckCircle2 className="w-3.5 h-3.5 mr-1.5" />
              {response.isReviewed ? 'Mark Unreviewed' : 'Mark Reviewed'}
            </Button>
            <Button variant="danger" size="sm" onClick={handleDelete}>
              <Trash2 className="w-3.5 h-3.5 mr-1.5" />
              Delete
            </Button>
          </div>
        </div>

        {/* 2-Column Split: Answers (Left 2 cols) & Metadata/Notes/Tags (Right 1 col) */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left Column: Form Answers List */}
          <div className="lg:col-span-2 space-y-4">
            <Card>
              <CardHeader>
                <CardTitle className="text-base flex items-center gap-2">
                  <FileText className="w-4 h-4 text-indigo-500" />
                  Submitted Answers ({response.answers?.length || 0})
                </CardTitle>
              </CardHeader>
              <CardContent className="divide-y divide-slate-100 dark:divide-slate-800">
                {(response.answers || []).map((ans, idx) => (
                  <div key={ans.id || idx} className="py-4 first:pt-0 last:pb-0 space-y-1.5">
                    <span className="text-xs font-semibold text-slate-500 dark:text-slate-400">
                      {idx + 1}. {ans.questionTitle || ans.fieldKey}
                    </span>
                    <div className="text-sm font-medium text-slate-900 dark:text-white bg-slate-50 dark:bg-slate-800/60 p-3 rounded-xl border border-slate-100 dark:border-slate-800">
                      {typeof ans.answerValue === 'object'
                        ? JSON.stringify(ans.answerValue, null, 2)
                        : String(ans.answerValue || '—')}
                    </div>
                  </div>
                ))}
              </CardContent>
            </Card>
          </div>

          {/* Right Column: Metadata, Tags, Internal Notes */}
          <div className="space-y-4">
            {/* Metadata Card */}
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-xs font-bold uppercase tracking-wider text-slate-500">
                  Submission Metadata
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3 text-xs">
                <div className="flex items-center justify-between text-slate-600 dark:text-slate-400">
                  <span className="flex items-center gap-1.5">
                    <Calendar className="w-3.5 h-3.5 text-slate-400" />
                    Submitted
                  </span>
                  <span className="font-semibold text-slate-900 dark:text-white">
                    {new Date(response.submittedAt).toLocaleString()}
                  </span>
                </div>

                <div className="flex items-center justify-between text-slate-600 dark:text-slate-400">
                  <span className="flex items-center gap-1.5">
                    <Clock className="w-3.5 h-3.5 text-slate-400" />
                    Completion Time
                  </span>
                  <span className="font-semibold text-slate-900 dark:text-white">
                    {response.durationSeconds ? `${response.durationSeconds}s` : 'Unknown'}
                  </span>
                </div>

                {response.ipAddress && (
                  <div className="flex items-center justify-between text-slate-600 dark:text-slate-400">
                    <span className="flex items-center gap-1.5">
                      <Globe className="w-3.5 h-3.5 text-slate-400" />
                      IP Address
                    </span>
                    <span className="font-mono text-slate-700 dark:text-slate-300">
                      {response.ipAddress}
                    </span>
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Tags Card */}
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-xs font-bold uppercase tracking-wider text-slate-500 flex items-center justify-between">
                  <span className="flex items-center gap-1.5">
                    <Tag className="w-3.5 h-3.5" />
                    Tags
                  </span>
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="flex flex-wrap gap-1.5">
                  {(response.tags || []).map((t) => (
                    <span
                      key={t.id}
                      className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-indigo-50 dark:bg-indigo-950/60 text-indigo-700 dark:text-indigo-300 border border-indigo-200 dark:border-indigo-900"
                    >
                      {t.tagName}
                      <button
                        onClick={() => handleRemoveTag(t.id)}
                        className="hover:text-rose-600 ml-0.5"
                      >
                        <X className="w-3 h-3" />
                      </button>
                    </span>
                  ))}
                  {(response.tags || []).length === 0 && (
                    <span className="text-xs text-slate-400 italic">No tags attached</span>
                  )}
                </div>

                <form onSubmit={handleAddTag} className="flex gap-2 pt-1">
                  <input
                    type="text"
                    placeholder="Add tag (e.g. VIP, Bug)..."
                    value={newTag}
                    onChange={(e) => setNewTag(e.target.value)}
                    className="flex-1 text-xs px-2.5 py-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-900 dark:text-white"
                  />
                  <Button type="submit" size="sm" variant="outline" className="text-xs h-7 px-2">
                    <Plus className="w-3.5 h-3.5" />
                  </Button>
                </form>
              </CardContent>
            </Card>

            {/* Notes Card */}
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-xs font-bold uppercase tracking-wider text-slate-500 flex items-center gap-1.5">
                  <MessageSquare className="w-3.5 h-3.5" />
                  Internal Team Notes
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="space-y-2 max-h-52 overflow-y-auto scrollbar-thin">
                  {(response.notes || []).map((n) => (
                    <div
                      key={n.id}
                      className="p-2.5 rounded-lg bg-slate-50 dark:bg-slate-800/60 border border-slate-100 dark:border-slate-800 text-xs space-y-1"
                    >
                      <p className="text-slate-800 dark:text-slate-200">{n.noteText}</p>
                      <span className="text-[10px] text-slate-400 block">
                        {new Date(n.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                  ))}
                  {(response.notes || []).length === 0 && (
                    <span className="text-xs text-slate-400 italic">No notes yet</span>
                  )}
                </div>

                <form onSubmit={handleAddNote} className="space-y-2 pt-2 border-t border-slate-100 dark:border-slate-800">
                  <textarea
                    rows={2}
                    placeholder="Write a team note..."
                    value={newNote}
                    onChange={(e) => setNewNote(e.target.value)}
                    className="w-full text-xs p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-900 dark:text-white resize-none"
                  />
                  <Button
                    type="submit"
                    size="sm"
                    className="w-full text-xs"
                    isLoading={isAddingNote}
                    disabled={!newNote.trim()}
                  >
                    Add Note
                  </Button>
                </form>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};
