import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { templateService } from '@/services/templateService';
import { FormTemplate } from '@/types';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { useAuthStore } from '@/store/authStore';
import { Button } from '@/components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Modal } from '@/components/ui/Modal';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  Sparkles,
  Search,
  Eye,
  ArrowRight,
  Layers,
  Copy,
  Tag,
  Check,
} from 'lucide-react';

const CATEGORIES = [
  'All',
  'Feedback',
  'Registration',
  'HR & Recruiting',
  'Product',
  'Healthcare',
  'Sales & Marketing',
  'Events',
  'Support',
  'Education',
];

export const TemplatesPage: React.FC = () => {
  const navigate = useNavigate();
  const { currentWorkspace } = useAuthStore();
  const { showToast } = useToast();
  const workspaceId = currentWorkspace?.id || '';

  const [activeCategory, setActiveCategory] = useState('All');
  const [search, setSearch] = useState('');
  const [previewTemplate, setPreviewTemplate] = useState<FormTemplate | null>(null);
  const [isCloning, setIsCloning] = useState(false);

  const { data: templates, isLoading } = useQuery({
    queryKey: ['templates', activeCategory, search],
    queryFn: () =>
      templateService.listTemplates({
        category: activeCategory === 'All' ? undefined : activeCategory,
        search: search || undefined,
      }),
  });

  const handleUseTemplate = async (templateId: string) => {
    if (!workspaceId) {
      showToast('No active workspace selected', 'error');
      return;
    }

    setIsCloning(true);
    try {
      const cloned = await templateService.useTemplate(templateId, workspaceId);
      showToast('Template cloned into workspace!', 'success');
      navigate(`/builder/${cloned.id}`);
    } catch (err: any) {
      showToast(err.message || 'Failed to use template', 'error');
    } finally {
      setIsCloning(false);
    }
  };

  const list = templates || [];

  return (
    <DashboardLayout>
      <div className="max-w-7xl mx-auto space-y-8 pb-16">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
              Template Gallery
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
              Select from professionally architected form templates or clone and customize.
            </p>
          </div>

          {/* Search bar */}
          <div className="relative w-full sm:w-72">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search templates..."
              className="w-full pl-9 pr-3 py-2 text-xs bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl focus:outline-none focus:ring-1 focus:ring-emerald-500 text-slate-900 dark:text-white"
            />
          </div>
        </div>

        {/* Category Filter Pills */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-2 scrollbar-none">
          {CATEGORIES.map((cat) => (
            <button
              key={cat}
              onClick={() => setActiveCategory(cat)}
              className={`px-3 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-colors cursor-pointer ${
                activeCategory === cat
                  ? 'bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-xs font-bold'
                  : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-slate-200 dark:hover:bg-slate-700'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Templates Grid */}
        {isLoading ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <Skeleton key={i} className="h-56 rounded-2xl" />
            ))}
          </div>
        ) : list.length === 0 ? (
          <div className="text-center py-16 text-slate-400 space-y-2">
            <p className="text-base font-semibold">No templates found</p>
            <p className="text-xs">Try selecting another category or clearing your search term.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {list.map((tpl) => (
              <Card
                key={tpl.id}
                className="hover:border-emerald-400 dark:hover:border-emerald-700 transition-all group flex flex-col justify-between"
              >
                <CardContent className="p-6 flex flex-col justify-between h-full">
                  <div>
                    <div className="flex items-center justify-between gap-2 mb-3">
                      <Badge variant="secondary" className="text-[10px]">
                        {tpl.category}
                      </Badge>
                      <span className="text-[11px] text-slate-400 font-mono">
                        {tpl.questionsCount || 6} questions
                      </span>
                    </div>

                    <h3 className="font-bold text-slate-900 dark:text-white group-hover:text-emerald-600 text-base line-clamp-1">
                      {tpl.title}
                    </h3>
                    <p className="text-xs text-slate-500 dark:text-slate-400 line-clamp-3 mt-2 leading-relaxed">
                      {tpl.description}
                    </p>
                  </div>

                  <div className="pt-6 mt-4 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      className="text-xs"
                      onClick={() => setPreviewTemplate(tpl)}
                    >
                      <Eye className="w-3.5 h-3.5 mr-1" />
                      Preview
                    </Button>
                    <Button
                      size="sm"
                      className="text-xs font-bold shadow-md shadow-emerald-500/20"
                      isLoading={isCloning}
                      onClick={() => handleUseTemplate(tpl.id)}
                    >
                      Use Template
                      <ArrowRight className="w-3.5 h-3.5 ml-1" />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}

        {/* Template Preview Modal */}
        {previewTemplate && (
          <Modal
            isOpen={!!previewTemplate}
            onClose={() => setPreviewTemplate(null)}
            title={previewTemplate.title}
            maxWidth="2xl"
          >
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <Badge variant="success">{previewTemplate.category}</Badge>
                <span className="text-xs text-slate-400">{previewTemplate.questionsCount} questions configured</span>
              </div>

              <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed">
                {previewTemplate.description}
              </p>

              <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/60 border border-slate-200 dark:border-slate-700/60 text-xs space-y-2">
                <span className="font-bold text-slate-700 dark:text-slate-300 uppercase text-[10px] tracking-wider block">
                  Template Features Included:
                </span>
                <ul className="space-y-1.5 text-slate-600 dark:text-slate-300">
                  <li className="flex items-center gap-2">
                    <Check className="w-3.5 h-3.5 text-emerald-500" />
                    Pre-configured conditional logic rules
                  </li>
                  <li className="flex items-center gap-2">
                    <Check className="w-3.5 h-3.5 text-emerald-500" />
                    Strict email & field-level validation rules
                  </li>
                  <li className="flex items-center gap-2">
                    <Check className="w-3.5 h-3.5 text-emerald-500" />
                    Responsive desktop & mobile-ready layout theme
                  </li>
                </ul>
              </div>

              <div className="pt-4 flex items-center justify-end gap-3">
                <Button variant="outline" size="sm" onClick={() => setPreviewTemplate(null)}>
                  Close
                </Button>
                <Button
                  size="sm"
                  className="font-bold shadow-md shadow-emerald-500/20"
                  isLoading={isCloning}
                  onClick={() => handleUseTemplate(previewTemplate.id)}
                >
                  Clone into Workspace
                  <ArrowRight className="w-3.5 h-3.5 ml-1.5" />
                </Button>
              </div>
            </div>
          </Modal>
        )}
      </div>
    </DashboardLayout>
  );
};
