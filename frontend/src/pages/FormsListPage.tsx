import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { useAuthStore } from '@/store/authStore';
import { formService } from '@/services/formService';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Card, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Tabs } from '@/components/ui/Tabs';
import { EmptyState } from '@/components/ui/EmptyState';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  Plus,
  Sparkles,
  Search,
  Filter,
  FileText,
  Clock,
  MessageSquare,
  BarChart3,
  ExternalLink,
  Copy,
  Trash2,
  Archive,
  RotateCcw,
  LayoutGrid,
  List,
  CheckCircle2,
  MoreVertical,
  Globe,
  Lock,
} from 'lucide-react';

export const FormsListPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const { showToast } = useToast();
  const { currentWorkspace } = useAuthStore();
  const workspaceId = currentWorkspace?.id || '';

  // Sync tab with URL search parameter
  const statusParam = (searchParams.get('status') || 'ALL').toUpperCase();
  const initialTab = ['ALL', 'DRAFT', 'PUBLISHED', 'ARCHIVED'].includes(statusParam)
    ? (statusParam as 'ALL' | 'DRAFT' | 'PUBLISHED' | 'ARCHIVED')
    : 'ALL';

  const [activeTab, setActiveTab] = useState<'ALL' | 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'>(initialTab);
  const [search, setSearch] = useState('');
  const [sortBy, setSortBy] = useState('updatedAt,desc');
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
  const [selectedIds, setSelectedIds] = useState<string[]>([]);

  // Update activeTab if URL query param changes
  useEffect(() => {
    const current = (searchParams.get('status') || 'ALL').toUpperCase();
    if (['ALL', 'DRAFT', 'PUBLISHED', 'ARCHIVED'].includes(current)) {
      setActiveTab(current as any);
    } else {
      setActiveTab('ALL');
    }
  }, [searchParams]);

  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId as any);
    if (tabId === 'ALL') {
      searchParams.delete('status');
      setSearchParams(searchParams);
    } else {
      searchParams.set('status', tabId);
      setSearchParams(searchParams);
    }
  };

  const {
    data: formsData,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['workspace-forms-list', workspaceId, activeTab, search, sortBy],
    queryFn: () =>
      formService.listForms({
        workspaceId,
        status: activeTab === 'ALL' ? undefined : activeTab,
        search: search || undefined,
        sort: sortBy,
        size: 50,
      }),
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
      showToast('Form created', 'success');
      navigate(`/builder/${newForm.id}`);
    } catch (err: any) {
      showToast(err.message || 'Failed to create form', 'error');
    }
  };

  const handleDuplicate = async (id: string) => {
    try {
      await formService.duplicateForm(id);
      showToast('Form duplicated', 'success');
      refetch();
    } catch (err: any) {
      showToast(err.message || 'Failed to duplicate form', 'error');
    }
  };

  const handleArchive = async (id: string) => {
    try {
      await formService.archiveForm(id);
      showToast('Form archived', 'success');
      refetch();
    } catch (err: any) {
      showToast(err.message || 'Failed to archive form', 'error');
    }
  };

  const handleRestore = async (id: string) => {
    try {
      await formService.restoreForm(id);
      showToast('Form restored to draft', 'success');
      refetch();
    } catch (err: any) {
      showToast(err.message || 'Failed to restore form', 'error');
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Are you sure you want to delete this form permanently?')) return;
    try {
      await formService.deleteForm(id);
      showToast('Form deleted', 'success');
      refetch();
    } catch (err: any) {
      showToast(err.message || 'Failed to delete form', 'error');
    }
  };

  const copyPublicLink = (slug: string) => {
    const url = `${window.location.origin}/f/${slug}`;
    navigator.clipboard.writeText(url);
    showToast('Public link copied to clipboard', 'info');
  };

  const toggleSelect = (id: string) => {
    setSelectedIds((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const selectAll = () => {
    if (selectedIds.length === forms.length) {
      setSelectedIds([]);
    } else {
      setSelectedIds(forms.map((f) => f.id));
    }
  };

  const handleBulkArchive = async () => {
    try {
      for (const id of selectedIds) {
        await formService.archiveForm(id);
      }
      showToast(`Archived ${selectedIds.length} forms`, 'success');
      setSelectedIds([]);
      refetch();
    } catch (err: any) {
      showToast('Error during bulk action', 'error');
    }
  };

  const forms = formsData?.content || [];

  return (
    <DashboardLayout>
      <div className="space-y-6 max-w-7xl mx-auto">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-white">
              {activeTab === 'PUBLISHED'
                ? 'Published Forms'
                : activeTab === 'DRAFT'
                ? 'Draft Forms'
                : activeTab === 'ARCHIVED'
                ? 'Archived Forms'
                : 'All Forms'}
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400">
              {activeTab === 'PUBLISHED'
                ? 'Public forms currently active and collecting respondent submissions.'
                : activeTab === 'DRAFT'
                ? 'Unpublished working drafts you can design, edit, and configure.'
                : 'Manage, publish, and monitor your AI and custom forms.'}
            </p>
          </div>
          <div className="flex items-center gap-3">
            <Link to="/generate-ai">
              <Button className="font-bold text-xs shadow-md">
                <Sparkles className="w-4 h-4 mr-1.5" />
                Build with AI
              </Button>
            </Link>
            <Button variant="outline" onClick={handleCreateScratch}>
              <Plus className="w-4 h-4 mr-1.5" />
              Blank Form
            </Button>
          </div>
        </div>

        {/* Toolbar: Tabs, Search, Sort & View Mode */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-4">
          <Tabs
            tabs={[
              { id: 'ALL', label: 'All Forms' },
              { id: 'PUBLISHED', label: 'Published' },
              { id: 'DRAFT', label: 'Drafts' },
              { id: 'ARCHIVED', label: 'Archived' },
            ]}
            activeTab={activeTab}
            onChange={handleTabChange}
          />

          <div className="flex items-center gap-3">
            {/* Search */}
            <div className="relative w-full sm:w-60">
              <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Search forms..."
                className="w-full pl-9 pr-3 py-1.5 text-xs bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg focus:outline-none focus:ring-1 focus:ring-emerald-500 text-slate-900 dark:text-white"
              />
            </div>

            {/* Sort */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="text-xs bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg px-2.5 py-1.5 text-slate-700 dark:text-slate-300 focus:outline-none focus:ring-1 focus:ring-emerald-500"
            >
              <option value="updatedAt,desc">Last updated</option>
              <option value="createdAt,desc">Newest first</option>
              <option value="createdAt,asc">Oldest first</option>
              <option value="title,asc">Title (A-Z)</option>
              <option value="submissionCount,desc">Most responses</option>
            </select>

            {/* View toggle */}
            <div className="flex items-center border border-slate-200 dark:border-slate-800 rounded-lg p-0.5 bg-slate-100 dark:bg-slate-900">
              <button
                onClick={() => setViewMode('grid')}
                className={`p-1.5 rounded-md ${
                  viewMode === 'grid'
                    ? 'bg-white dark:bg-slate-800 text-emerald-600 dark:text-emerald-400 shadow-xs'
                    : 'text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'
                }`}
                title="Grid view"
              >
                <LayoutGrid className="w-3.5 h-3.5" />
              </button>
              <button
                onClick={() => setViewMode('list')}
                className={`p-1.5 rounded-md ${
                  viewMode === 'list'
                    ? 'bg-white dark:bg-slate-800 text-emerald-600 dark:text-emerald-400 shadow-xs'
                    : 'text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'
                }`}
                title="List view"
              >
                <List className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        </div>

        {/* Bulk Action Bar */}
        {selectedIds.length > 0 && (
          <div className="flex items-center justify-between p-3 bg-emerald-50/70 dark:bg-emerald-950/40 border border-emerald-200 dark:border-emerald-900 rounded-xl text-xs">
            <span className="font-semibold text-emerald-900 dark:text-emerald-300">
              {selectedIds.length} forms selected
            </span>
            <div className="flex items-center gap-2">
              <Button size="sm" variant="outline" onClick={handleBulkArchive}>
                <Archive className="w-3.5 h-3.5 mr-1" />
                Archive Selected
              </Button>
              <Button size="sm" variant="ghost" onClick={() => setSelectedIds([])}>
                Clear
              </Button>
            </div>
          </div>
        )}

        {/* Content */}
        {isLoading ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {[...Array(6)].map((_, i) => (
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
                : activeTab !== 'ALL'
                ? `You have no ${activeTab.toLowerCase()} forms in this workspace.`
                : 'Start creating powerful forms with Google Gemini AI or from a clean canvas.'
            }
            action={
              <div className="flex items-center gap-3">
                <Link to="/generate-ai">
                  <Button>
                    <Sparkles className="w-4 h-4 mr-2" />
                    Build with AI
                  </Button>
                </Link>
                <Button variant="outline" onClick={handleCreateScratch}>
                  Blank Form
                </Button>
              </div>
            }
          />
        ) : viewMode === 'grid' ? (
          /* Grid View */
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {forms.map((form) => {
              const isSelected = selectedIds.includes(form.id);
              return (
                <Card
                  key={form.id}
                  className={`relative hover:border-emerald-400 dark:hover:border-emerald-700 transition-all group flex flex-col justify-between ${
                    isSelected ? 'ring-2 ring-emerald-500 border-emerald-500' : ''
                  }`}
                >
                  <CardContent className="p-5 flex flex-col justify-between h-full">
                    <div>
                      {/* Top status & Select */}
                      <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-2">
                          <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => toggleSelect(form.id)}
                            className="rounded border-slate-300 text-emerald-600 focus:ring-emerald-500"
                          />
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
                        </div>
                        <span className="text-[11px] text-slate-400 flex items-center gap-1">
                          <Clock className="w-3 h-3" />
                          {new Date(form.updatedAt).toLocaleDateString()}
                        </span>
                      </div>

                      {/* Title & Description */}
                      <Link to={`/builder/${form.id}`} className="block">
                        <h3 className="font-bold text-slate-900 dark:text-white group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors line-clamp-1 text-base">
                          {form.title}
                        </h3>
                        <p className="text-xs text-slate-500 dark:text-slate-400 line-clamp-2 mt-1.5 leading-relaxed">
                          {form.description || 'No description provided'}
                        </p>
                      </Link>
                    </div>

                    {/* Stats & Actions */}
                    <div className="pt-4 mt-4 border-t border-slate-100 dark:border-slate-800">
                      <div className="flex items-center justify-between text-xs text-slate-500 mb-3">
                        <span className="flex items-center gap-1.5 font-medium text-slate-700 dark:text-slate-300">
                          <MessageSquare className="w-3.5 h-3.5 text-emerald-500" />
                          {form.submissionCount} responses
                        </span>
                        <span>{form.questionCount} questions</span>
                      </div>

                      <div className="flex items-center justify-between gap-1">
                        <div className="flex items-center gap-1">
                          <Link to={`/builder/${form.id}`}>
                            <Button size="sm" variant="outline" className="text-xs h-7 px-2.5">
                              Edit
                            </Button>
                          </Link>
                          <Link to={`/forms/${form.id}/responses`}>
                            <Button size="sm" variant="ghost" className="text-xs h-7 px-2">
                              Responses
                            </Button>
                          </Link>
                          <Link to={`/forms/${form.id}/analytics`}>
                            <Button size="sm" variant="ghost" className="text-xs h-7 px-2" title="Analytics">
                              <BarChart3 className="w-3.5 h-3.5" />
                            </Button>
                          </Link>
                        </div>

                        <div className="flex items-center gap-1">
                          {form.publishedSlug && (
                            <button
                              onClick={() => copyPublicLink(form.publishedSlug!)}
                              className="p-1.5 text-slate-400 hover:text-emerald-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                              title="Copy public link"
                            >
                              <ExternalLink className="w-3.5 h-3.5" />
                            </button>
                          )}
                          <button
                            onClick={() => handleDuplicate(form.id)}
                            className="p-1.5 text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                            title="Duplicate"
                          >
                            <Copy className="w-3.5 h-3.5" />
                          </button>
                          {form.status === 'ARCHIVED' ? (
                            <button
                              onClick={() => handleRestore(form.id)}
                              className="p-1.5 text-slate-400 hover:text-emerald-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                              title="Restore"
                            >
                              <RotateCcw className="w-3.5 h-3.5" />
                            </button>
                          ) : (
                            <button
                              onClick={() => handleArchive(form.id)}
                              className="p-1.5 text-slate-400 hover:text-amber-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                              title="Archive"
                            >
                              <Archive className="w-3.5 h-3.5" />
                            </button>
                          )}
                          <button
                            onClick={() => handleDelete(form.id)}
                            className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                            title="Delete"
                          >
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        ) : (
          /* List View */
          <Card>
            <div className="divide-y divide-slate-200 dark:divide-slate-800">
              <div className="p-3 bg-slate-50 dark:bg-slate-900/60 flex items-center justify-between text-xs font-semibold text-slate-500 uppercase tracking-wider">
                <div className="flex items-center gap-3">
                  <input
                    type="checkbox"
                    checked={selectedIds.length === forms.length && forms.length > 0}
                    onChange={selectAll}
                    className="rounded border-slate-300 text-emerald-600"
                  />
                  <span>Form Title</span>
                </div>
                <div className="flex items-center gap-12 pr-6">
                  <span>Status</span>
                  <span>Responses</span>
                  <span>Updated</span>
                  <span>Actions</span>
                </div>
              </div>

              {forms.map((form) => (
                <div
                  key={form.id}
                  className="p-4 flex items-center justify-between hover:bg-slate-50 dark:hover:bg-slate-800/40 transition-colors"
                >
                  <div className="flex items-center gap-3">
                    <input
                      type="checkbox"
                      checked={selectedIds.includes(form.id)}
                      onChange={() => toggleSelect(form.id)}
                      className="rounded border-slate-300 text-emerald-600"
                    />
                    <div>
                      <Link
                        to={`/builder/${form.id}`}
                        className="font-bold text-slate-900 dark:text-white hover:text-emerald-600 text-sm"
                      >
                        {form.title}
                      </Link>
                      <p className="text-xs text-slate-500 line-clamp-1">
                        {form.description || 'No description'}
                      </p>
                    </div>
                  </div>

                  <div className="flex items-center gap-8">
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
                    <span className="text-xs font-medium text-slate-700 dark:text-slate-300 w-16 text-right">
                      {form.submissionCount}
                    </span>
                    <span className="text-xs text-slate-400 w-24 text-right">
                      {new Date(form.updatedAt).toLocaleDateString()}
                    </span>
                    <div className="flex items-center gap-1">
                      <Link to={`/builder/${form.id}`}>
                        <Button size="sm" variant="outline" className="text-xs h-7 px-2">
                          Edit
                        </Button>
                      </Link>
                      <Link to={`/forms/${form.id}/responses`}>
                        <Button size="sm" variant="ghost" className="text-xs h-7 px-2">
                          Responses
                        </Button>
                      </Link>
                      <button
                        onClick={() => handleDuplicate(form.id)}
                        className="p-1.5 text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 rounded-md cursor-pointer"
                        title="Duplicate"
                      >
                        <Copy className="w-3.5 h-3.5" />
                      </button>
                      <button
                        onClick={() => handleDelete(form.id)}
                        className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md cursor-pointer"
                        title="Delete"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        )}
      </div>
    </DashboardLayout>
  );
};
