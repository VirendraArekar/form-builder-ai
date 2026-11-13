import React, { useEffect, useState, useRef } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import {
  DndContext,
  closestCenter,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  DragEndEvent,
} from '@dnd-kit/core';
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
} from '@dnd-kit/sortable';
import { formService } from '@/services/formService';
import { useBuilderStore } from '@/store/builderStore';
import { QuestionPalette } from '@/components/builder/QuestionPalette';
import { CanvasQuestionCard } from '@/components/builder/CanvasQuestionCard';
import { PropertiesEditor } from '@/components/builder/PropertiesEditor';
import { LogicEditor } from '@/components/builder/LogicEditor';
import { ThemeEditor } from '@/components/builder/ThemeEditor';
import { PublishModal } from '@/components/builder/PublishModal';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  ArrowLeft,
  Undo2,
  Redo2,
  Monitor,
  Tablet,
  Smartphone,
  Eye,
  Settings,
  Share2,
  CheckCircle2,
  Save,
  MessageSquare,
  BarChart3,
  Sliders,
  Palette,
  GitBranch,
} from 'lucide-react';

export const FormBuilderPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { showToast } = useToast();

  const {
    form,
    setForm,
    saveStatus,
    setSaveStatus,
    viewportMode,
    setViewportMode,
    undoStack,
    redoStack,
    undo,
    redo,
    reorderQuestions,
    updateFormMeta,
    updateSection,
  } = useBuilderStore();

  const [isLoading, setIsLoading] = useState(true);
  const [rightTab, setRightTab] = useState<'properties' | 'logic' | 'theme'>('properties');
  const [isPublishModalOpen, setIsPublishModalOpen] = useState(false);

  // Drag and drop sensors
  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 5,
      },
    }),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  // Initial load
  useEffect(() => {
    if (!id) return;
    const fetchForm = async () => {
      try {
        setIsLoading(true);
        const data = await formService.getForm(id);
        setForm(data);
      } catch (err: any) {
        showToast(err.message || 'Failed to load form', 'error');
        navigate('/forms');
      } finally {
        setIsLoading(false);
      }
    };
    fetchForm();
  }, [id]);

  // Debounced Auto-save
  const isInitialMount = useRef(true);
  useEffect(() => {
    if (isInitialMount.current) {
      isInitialMount.current = false;
      return;
    }
    if (!form || saveStatus !== 'unsaved') return;

    const timer = setTimeout(async () => {
      try {
        setSaveStatus('saving');
        await formService.saveFormSchema(form.id, {
          title: form.title,
          description: form.description,
          sections: form.sections,
          questions: form.questions,
          logicRules: form.logicRules,
          theme: form.theme,
        });
        setSaveStatus('saved');
      } catch (err: any) {
        console.error('Auto-save error', err);
        setSaveStatus('unsaved');
      }
    }, 1500);

    return () => clearTimeout(timer);
  }, [form, saveStatus]);

  const handleManualSave = async () => {
    if (!form) return;
    try {
      setSaveStatus('saving');
      await formService.saveFormSchema(form.id, {
        title: form.title,
        description: form.description,
        sections: form.sections,
        questions: form.questions,
        logicRules: form.logicRules,
        theme: form.theme,
      });
      setSaveStatus('saved');
      showToast('All changes saved to cloud', 'success');
    } catch (err: any) {
      setSaveStatus('unsaved');
      showToast(err.message || 'Save failed', 'error');
    }
  };

  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;
    if (!over || active.id === over.id || !form) return;

    const oldIndex = form.questions.findIndex((q) => q.id === active.id);
    const newIndex = form.questions.findIndex((q) => q.id === over.id);

    if (oldIndex !== -1 && newIndex !== -1) {
      reorderQuestions(oldIndex, newIndex);
    }
  };

  if (isLoading || !form) {
    return (
      <div className="h-screen flex flex-col bg-slate-50 dark:bg-slate-950">
        <div className="h-14 border-b border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 px-6 flex items-center justify-between">
          <Skeleton className="h-6 w-48" />
          <Skeleton className="h-8 w-64" />
        </div>
        <div className="flex-1 flex">
          <Skeleton className="w-64 h-full" />
          <div className="flex-1 p-8 max-w-3xl mx-auto space-y-4">
            <Skeleton className="h-28 w-full rounded-2xl" />
            <Skeleton className="h-20 w-full rounded-xl" />
            <Skeleton className="h-20 w-full rounded-xl" />
          </div>
          <Skeleton className="w-80 h-full" />
        </div>
      </div>
    );
  }

  // Viewport width styling
  const getCanvasWidth = () => {
    switch (viewportMode) {
      case 'mobile':
        return 'max-w-[390px]';
      case 'tablet':
        return 'max-w-[720px]';
      default:
        return 'max-w-[760px]';
    }
  };

  return (
    <div className="h-screen flex flex-col bg-slate-100 dark:bg-slate-950 overflow-hidden font-sans">
      {/* Top Builder Navigation */}
      <header className="h-14 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 px-4 flex items-center justify-between z-20 shrink-0">
        {/* Left: Back & Title */}
        <div className="flex items-center gap-3">
          <Link
            to="/forms"
            className="p-1.5 rounded-lg text-slate-500 hover:text-slate-800 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </Link>

          <div className="flex items-center gap-2">
            <input
              type="text"
              value={form.title}
              onChange={(e) => updateFormMeta({ title: e.target.value })}
              className="font-bold text-sm text-slate-900 dark:text-white bg-transparent border-b border-transparent hover:border-slate-300 focus:border-emerald-500 focus:outline-none px-1"
            />
            <Badge
              variant={form.status === 'PUBLISHED' ? 'success' : 'secondary'}
              className="text-[10px]"
            >
              {form.status}
            </Badge>
          </div>
        </div>

        {/* Center: Tabs & Viewport Switcher */}
        <div className="flex items-center gap-6">
          {/* Main App Navigation for Form */}
          <div className="hidden md:flex items-center gap-1 text-xs font-semibold">
            <span className="px-3 py-1.5 rounded-lg bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-xs font-bold">
              Builder
            </span>
            <Link
              to={`/forms/${form.id}/preview`}
              className="px-3 py-1.5 rounded-lg text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors flex items-center gap-1.5"
            >
              <Eye className="w-3.5 h-3.5" />
              Preview
            </Link>
            <Link
              to={`/forms/${form.id}/settings`}
              className="px-3 py-1.5 rounded-lg text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors flex items-center gap-1.5"
            >
              <Settings className="w-3.5 h-3.5" />
              Settings
            </Link>
            <Link
              to={`/forms/${form.id}/responses`}
              className="px-3 py-1.5 rounded-lg text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors flex items-center gap-1.5"
            >
              <MessageSquare className="w-3.5 h-3.5" />
              Responses ({form.submissionCount || 0})
            </Link>
            <Link
              to={`/forms/${form.id}/analytics`}
              className="px-3 py-1.5 rounded-lg text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors flex items-center gap-1.5"
            >
              <BarChart3 className="w-3.5 h-3.5" />
              Analytics
            </Link>
          </div>

          {/* Viewport switch */}
          <div className="hidden lg:flex items-center border border-slate-200 dark:border-slate-800 rounded-lg p-0.5 bg-slate-100 dark:bg-slate-800">
            <button
              onClick={() => setViewportMode('desktop')}
              className={`p-1.5 rounded-md ${
                viewportMode === 'desktop'
                  ? 'bg-white dark:bg-slate-700 text-emerald-600 dark:text-emerald-400 shadow-xs'
                  : 'text-slate-400 hover:text-slate-600'
              }`}
              title="Desktop Viewport"
            >
              <Monitor className="w-3.5 h-3.5" />
            </button>
            <button
              onClick={() => setViewportMode('tablet')}
              className={`p-1.5 rounded-md ${
                viewportMode === 'tablet'
                  ? 'bg-white dark:bg-slate-700 text-emerald-600 dark:text-emerald-400 shadow-xs'
                  : 'text-slate-400 hover:text-slate-600'
              }`}
              title="Tablet Viewport"
            >
              <Tablet className="w-3.5 h-3.5" />
            </button>
            <button
              onClick={() => setViewportMode('mobile')}
              className={`p-1.5 rounded-md ${
                viewportMode === 'mobile'
                  ? 'bg-white dark:bg-slate-700 text-emerald-600 dark:text-emerald-400 shadow-xs'
                  : 'text-slate-400 hover:text-slate-600'
              }`}
              title="Mobile Viewport"
            >
              <Smartphone className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>

        {/* Right: History, Auto-save & Share */}
        <div className="flex items-center gap-2.5">
          <div className="flex items-center gap-1 mr-2 border-r border-slate-200 dark:border-slate-800 pr-2">
            <button
              onClick={undo}
              disabled={undoStack.length === 0}
              className="p-1.5 rounded-lg text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 disabled:opacity-30 disabled:cursor-not-allowed"
              title="Undo (Ctrl+Z)"
            >
              <Undo2 className="w-3.5 h-3.5" />
            </button>
            <button
              onClick={redo}
              disabled={redoStack.length === 0}
              className="p-1.5 rounded-lg text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 disabled:opacity-30 disabled:cursor-not-allowed"
              title="Redo (Ctrl+Y)"
            >
              <Redo2 className="w-3.5 h-3.5" />
            </button>
          </div>

          {/* Auto-save status */}
          <div className="text-[11px] font-medium text-slate-400 flex items-center gap-1.5 mr-2">
            {saveStatus === 'saving' && (
              <span className="flex items-center gap-1 text-amber-500">
                <span className="w-2 h-2 rounded-full bg-amber-500 animate-pulse" />
                Saving...
              </span>
            )}
            {saveStatus === 'saved' && (
              <span className="flex items-center gap-1 text-emerald-500">
                <CheckCircle2 className="w-3.5 h-3.5" />
                Saved
              </span>
            )}
            {saveStatus === 'unsaved' && (
              <button
                onClick={handleManualSave}
                className="flex items-center gap-1 text-emerald-600 hover:underline cursor-pointer"
              >
                <Save className="w-3 h-3" />
                Save
              </button>
            )}
          </div>

          <Button
            size="sm"
            onClick={() => setIsPublishModalOpen(true)}
            className="font-bold text-xs shadow-md shadow-emerald-500/20"
          >
            <Share2 className="w-3.5 h-3.5 mr-1.5" />
            Share & Publish
          </Button>
        </div>
      </header>

      {/* Main 3-Panel Layout */}
      <div className="flex-1 flex overflow-hidden">
        {/* Left: Palette */}
        <QuestionPalette />

        {/* Center: Canvas Area */}
        <div className="flex-1 overflow-y-auto p-6 md:p-8 flex justify-center scrollbar-thin">
          <div className={`w-full ${getCanvasWidth()} transition-all duration-300 space-y-6 pb-24`}>
            {/* Form Cover & Title Header Card */}
            <div className="rounded-2xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 p-6 md:p-8 shadow-sm space-y-3">
              {form.theme?.coverImageUrl && (
                <div className="h-32 -mx-6 -mt-6 md:-mx-8 md:-mt-8 mb-6 overflow-hidden rounded-t-2xl">
                  <img
                    src={form.theme.coverImageUrl}
                    alt="Cover Banner"
                    className="w-full h-full object-cover"
                  />
                </div>
              )}

              {form.theme?.logoUrl && (
                <img
                  src={form.theme.logoUrl}
                  alt="Form Logo"
                  className="w-12 h-12 object-contain rounded-lg mb-2"
                />
              )}

              <input
                type="text"
                value={form.title}
                onChange={(e) => updateFormMeta({ title: e.target.value })}
                className="w-full text-2xl font-extrabold text-slate-900 dark:text-white bg-transparent border-b border-transparent hover:border-slate-300 focus:border-emerald-500 focus:outline-none pb-1"
                placeholder="Form Title"
              />

              <textarea
                value={form.description || ''}
                onChange={(e) => updateFormMeta({ description: e.target.value })}
                rows={2}
                className="w-full text-xs text-slate-500 dark:text-slate-400 bg-transparent border-b border-transparent hover:border-slate-300 focus:border-emerald-500 focus:outline-none resize-none"
                placeholder="Write a clear description or greeting for your respondents..."
              />
            </div>

            {/* Questions Canvas with DndContext */}
            <DndContext
              sensors={sensors}
              collisionDetection={closestCenter}
              onDragEnd={handleDragEnd}
            >
              <SortableContext
                items={form.questions.map((q) => q.id)}
                strategy={verticalListSortingStrategy}
              >
                <div className="space-y-4">
                  {form.questions.length === 0 ? (
                    <div className="rounded-2xl border-2 border-dashed border-slate-300 dark:border-slate-700 p-12 text-center text-slate-400 space-y-3">
                      <p className="text-sm font-semibold text-slate-600 dark:text-slate-300">
                        This form is empty
                      </p>
                      <p className="text-xs">
                        Click any question type in the left palette to add questions to your form.
                      </p>
                    </div>
                  ) : (
                    form.questions.map((question, index) => (
                      <CanvasQuestionCard
                        key={question.id}
                        question={question}
                        index={index}
                      />
                    ))
                  )}
                </div>
              </SortableContext>
            </DndContext>
          </div>
        </div>

        {/* Right: Properties, Logic & Theme Panels */}
        <div className="flex flex-col h-full bg-white dark:bg-slate-900 border-l border-slate-200 dark:border-slate-800">
          {/* Subtabs for Right Panel */}
          <div className="flex items-center border-b border-slate-200 dark:border-slate-800 text-xs font-semibold px-2">
            <button
              onClick={() => setRightTab('properties')}
              className={`py-3 px-3 border-b-2 flex items-center gap-1.5 transition-colors cursor-pointer ${
                rightTab === 'properties'
                  ? 'border-emerald-600 text-emerald-600 dark:text-emerald-400'
                  : 'border-transparent text-slate-400 hover:text-slate-600'
              }`}
            >
              <Sliders className="w-3.5 h-3.5" />
              Properties
            </button>
            <button
              onClick={() => setRightTab('logic')}
              className={`py-3 px-3 border-b-2 flex items-center gap-1.5 transition-colors cursor-pointer ${
                rightTab === 'logic'
                  ? 'border-emerald-600 text-emerald-600 dark:text-emerald-400'
                  : 'border-transparent text-slate-400 hover:text-slate-600'
              }`}
            >
              <GitBranch className="w-3.5 h-3.5" />
              Logic ({form.logicRules?.length || 0})
            </button>
            <button
              onClick={() => setRightTab('theme')}
              className={`py-3 px-3 border-b-2 flex items-center gap-1.5 transition-colors cursor-pointer ${
                rightTab === 'theme'
                  ? 'border-emerald-600 text-emerald-600 dark:text-emerald-400'
                  : 'border-transparent text-slate-400 hover:text-slate-600'
              }`}
            >
              <Palette className="w-3.5 h-3.5" />
              Theme
            </button>
          </div>

          <div className="flex-1 overflow-hidden">
            {rightTab === 'properties' && <PropertiesEditor />}
            {rightTab === 'logic' && <LogicEditor />}
            {rightTab === 'theme' && <ThemeEditor />}
          </div>
        </div>
      </div>

      {/* Share / Publish Modal */}
      {isPublishModalOpen && (
        <PublishModal
          isOpen={isPublishModalOpen}
          onClose={() => setIsPublishModalOpen(false)}
          form={form}
          onUpdateForm={(updated) => setForm(updated)}
        />
      )}
    </div>
  );
};
