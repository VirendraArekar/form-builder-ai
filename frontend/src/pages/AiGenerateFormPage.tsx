import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { useAuthStore } from '@/store/authStore';
import { aiService } from '@/services/aiService';
import { formService } from '@/services/formService';
import { workspaceService } from '@/services/workspaceService';
import { Button } from '@/components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { useToast } from '@/components/ui/Toast';
import { AiFormSchema } from '@/types';
import { generateUUID } from '@/store/builderStore';
import {
  Sparkles,
  ArrowRight,
  RefreshCw,
  Sliders,
  CheckCircle2,
  FileText,
  HelpCircle,
  Layers,
  Wand2,
  Lightbulb,
  Zap,
} from 'lucide-react';

const EXAMPLE_PROMPTS = [
  'Create a customer feedback form for an e-commerce platform with customer details, product rating, delivery experience, payment experience, suggestions, and an overall satisfaction score.',
  'Design an employee pulse check survey with department, psychological safety rating, workload balance, managerial support, and open suggestions.',
  'Build an event registration form for a developer tech summit including attendee personal details, track selection, dietary restrictions, and shirt size.',
  'Create a patient intake health questionnaire with personal contact details, medical history, current symptoms, allergies, and emergency contact.',
  'Generate a SaaS product onboarding questionnaire asking about company size, industry, main use-cases, team size, and integration requirements.',
  'Design a modern job application form with applicant name, email, LinkedIn URL, portfolio link, years of experience, and resume file upload.',
];

const GENERATION_STEPS = [
  'Analyzing your prompt and context...',
  'Architecting logical sections and sequence...',
  'Selecting optimal question types and placeholders...',
  'Configuring smart validation rules and conditional branching...',
  'Applying modern theme styling and submission settings...',
];

export const AiGenerateFormPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { currentWorkspace, setCurrentWorkspace } = useAuthStore();
  const { showToast } = useToast();

  const [prompt, setPrompt] = useState(searchParams.get('prompt') || '');
  const [isGenerating, setIsGenerating] = useState(false);
  const [currentStepIndex, setCurrentStepIndex] = useState(0);
  const [generatedSchema, setGeneratedSchema] = useState<AiFormSchema | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  // Auto trigger if prompt passed from query string
  useEffect(() => {
    const initialPrompt = searchParams.get('prompt');
    if (initialPrompt && !generatedSchema && !isGenerating) {
      handleGenerate(initialPrompt);
    }
  }, []);

  const resolveWorkspaceId = async (): Promise<string | undefined> => {
    if (currentWorkspace?.id) return currentWorkspace.id;
    try {
      const list = await workspaceService.listWorkspaces();
      if (list && list.length > 0) {
        setCurrentWorkspace(list[0]);
        return list[0].id;
      }
    } catch (e) {
      console.warn('Could not auto-fetch workspace', e);
    }
    return undefined;
  };

  const handleGenerate = async (promptToUse?: string) => {
    const activePrompt = (promptToUse || prompt).trim();
    if (!activePrompt) {
      showToast('Please enter a description for your form', 'error');
      return;
    }

    setIsGenerating(true);
    setCurrentStepIndex(0);

    // Simulate progress steps
    const stepInterval = setInterval(() => {
      setCurrentStepIndex((prev) => (prev < GENERATION_STEPS.length - 1 ? prev + 1 : prev));
    }, 1100);

    try {
      const wsId = await resolveWorkspaceId();
      const result = await aiService.generateForm(wsId || '', activePrompt);
      clearInterval(stepInterval);
      setGeneratedSchema(result);
      showToast('Form generated successfully by Gemini AI!', 'success');
    } catch (err: any) {
      clearInterval(stepInterval);
      showToast(err.message || 'Failed to generate form. Please try again.', 'error');
    } finally {
      setIsGenerating(false);
    }
  };

  const handleOpenInBuilder = async () => {
    if (!generatedSchema) return;
    setIsSaving(true);
    try {
      let wsId = await resolveWorkspaceId();
      if (!wsId) {
        showToast('Please create or select a workspace first', 'error');
        setIsSaving(false);
        return;
      }

      // 1. Create form
      const newForm = await formService.createForm({
        workspaceId: wsId,
        title: generatedSchema.title || 'AI Generated Form',
        description: generatedSchema.description || '',
      });

      // 2. Format sections & questions with valid UUIDs for schema update
      const formattedSections: any[] = [];
      const formattedQuestions: any[] = [];

      generatedSchema.sections.forEach((sec, sIdx) => {
        const secId = generateUUID();
        formattedSections.push({
          id: secId,
          title: sec.title,
          description: sec.description || '',
          orderIndex: sIdx,
        });

        (sec.questions || []).forEach((q, qIdx) => {
          const qId = generateUUID();
          formattedQuestions.push({
            id: qId,
            sectionId: secId,
            title: q.title,
            description: q.description || '',
            type: q.type,
            placeholder: q.placeholder || '',
            isRequired: q.isRequired ?? false,
            orderIndex: qIdx,
            options: (q.options || []).map((opt, oIdx) => ({
              id: generateUUID(),
              label: opt.label,
              value: opt.value || opt.label.toLowerCase().replace(/\s+/g, '_'),
              orderIndex: oIdx,
            })),
            validationRules: q.validationRules || {},
          });
        });
      });

      // 3. Save full schema
      await formService.saveFormSchema(newForm.id, {
        title: generatedSchema.title,
        description: generatedSchema.description,
        sections: formattedSections,
        questions: formattedQuestions,
        logicRules: generatedSchema.logicRules || [],
        theme: generatedSchema.theme || {
          primaryColor: '#10B981',
          backgroundColor: '#ffffff',
          fontFamily: 'Inter',
          borderRadius: 'rounded-xl',
        },
      });

      showToast('Form saved! Opening in Visual Builder...', 'success');
      navigate(`/builder/${newForm.id}`);
    } catch (err: any) {
      showToast(err.message || 'Failed to save form schema', 'error');
      setIsSaving(false);
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-5xl mx-auto space-y-8 pb-16 selection:bg-emerald-500 selection:text-white">
        {/* Header */}
        <div className="text-center max-w-2xl mx-auto space-y-2">
          <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 border border-emerald-200 dark:border-emerald-900">
            <Sparkles className="w-3.5 h-3.5" />
            Gemini AI Form Engine
          </div>
          <h1 className="text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            Generate an entire form with one sentence
          </h1>
          <p className="text-sm text-slate-500 dark:text-slate-400">
            Describe what information you want to collect. Google Gemini will build questions, input types, validations, and conditional logic.
          </p>
        </div>

        {/* Prompt Input Box */}
        <Card className="border-emerald-200 dark:border-emerald-900/60 shadow-xl overflow-hidden">
          <CardContent className="p-6">
            <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-2">
              Describe your form requirement
            </label>
            <div className="relative">
              <textarea
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                rows={4}
                placeholder="e.g. Create a customer satisfaction feedback form with ratings for speed, friendliness, ease of checkout, suggestions, and email..."
                className="w-full text-sm rounded-xl p-4 bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 resize-none"
              />
            </div>

            <div className="flex flex-col sm:flex-row items-center justify-between gap-3 mt-4 pt-4 border-t border-slate-100 dark:border-slate-800">
              <span className="text-xs text-slate-400 flex items-center gap-1">
                <Lightbulb className="w-3.5 h-3.5 text-amber-500" />
                Be specific about questions, ratings, or steps you need.
              </span>
              <Button
                onClick={() => handleGenerate()}
                isLoading={isGenerating}
                className="w-full sm:w-auto font-bold px-6 shadow-md shadow-emerald-500/20"
              >
                <Sparkles className="w-4 h-4 mr-2" />
                {generatedSchema ? 'Regenerate Form' : 'Generate Form'}
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Inspiring Example Prompts */}
        {!generatedSchema && !isGenerating && (
          <div className="space-y-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">
              Or start with an inspiration prompt:
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {EXAMPLE_PROMPTS.map((ex, idx) => (
                <button
                  key={idx}
                  onClick={() => {
                    setPrompt(ex);
                    handleGenerate(ex);
                  }}
                  className="text-left p-3.5 rounded-xl border border-slate-200 dark:border-slate-800 hover:border-emerald-400 dark:hover:border-emerald-700 bg-white dark:bg-slate-900/60 hover:bg-emerald-50/40 dark:hover:bg-emerald-950/20 text-xs text-slate-600 dark:text-slate-300 transition-all flex items-start gap-2.5 group cursor-pointer"
                >
                  <Zap className="w-4 h-4 text-emerald-500 shrink-0 mt-0.5 group-hover:scale-110 transition-transform" />
                  <span className="line-clamp-2 leading-relaxed">{ex}</span>
                </button>
              ))}
            </div>
          </div>
        )}

        {/* Loading Progress State */}
        {isGenerating && (
          <Card className="border-emerald-300 dark:border-emerald-800 bg-emerald-50/20 dark:bg-emerald-950/20">
            <CardContent className="p-8 text-center space-y-4">
              <div className="relative w-16 h-16 mx-auto">
                <div className="w-16 h-16 rounded-full border-4 border-emerald-200 dark:border-emerald-900 border-t-emerald-600 animate-spin" />
                <Sparkles className="w-6 h-6 text-emerald-600 dark:text-emerald-400 absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2" />
              </div>

              <div>
                <h3 className="font-bold text-slate-900 dark:text-white text-lg">
                  Gemini AI is crafting your form
                </h3>
                <p className="text-xs text-emerald-600 dark:text-emerald-400 font-medium animate-pulse mt-1">
                  {GENERATION_STEPS[currentStepIndex]}
                </p>
              </div>

              <div className="max-w-md mx-auto bg-slate-200 dark:bg-slate-800 h-1.5 rounded-full overflow-hidden">
                <div
                  className="bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 h-full transition-all duration-700 ease-out"
                  style={{ width: `${((currentStepIndex + 1) / GENERATION_STEPS.length) * 100}%` }}
                />
              </div>
            </CardContent>
          </Card>
        )}

        {/* Generated Form Preview & Schema Inspection */}
        {generatedSchema && !isGenerating && (
          <div className="space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 bg-emerald-50 dark:bg-emerald-950/40 border border-emerald-200 dark:border-emerald-900 rounded-2xl">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-emerald-100 dark:bg-emerald-900/60 text-emerald-600 dark:text-emerald-400 rounded-xl flex items-center justify-center">
                  <CheckCircle2 className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="font-bold text-slate-900 dark:text-white text-base">
                    AI Form Ready for Review
                  </h3>
                  <p className="text-xs text-slate-600 dark:text-slate-400">
                    {generatedSchema.sections.reduce((acc, s) => acc + (s.questions?.length || 0), 0)} questions generated across {generatedSchema.sections.length} sections.
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => handleGenerate()}
                >
                  <RefreshCw className="w-3.5 h-3.5 mr-1.5" />
                  Regenerate
                </Button>
                <Button
                  size="sm"
                  onClick={handleOpenInBuilder}
                  isLoading={isSaving}
                  className="font-bold shadow-md shadow-emerald-500/20"
                >
                  Open in Visual Builder
                  <ArrowRight className="w-4 h-4 ml-1.5" />
                </Button>
              </div>
            </div>

            {/* Form Meta Preview */}
            <Card>
              <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800">
                <div className="flex items-start justify-between">
                  <div>
                    <CardTitle className="text-xl font-bold">{generatedSchema.title}</CardTitle>
                    <p className="text-sm text-slate-500 mt-1">{generatedSchema.description}</p>
                  </div>
                  <Badge variant="success">AI Generated</Badge>
                </div>
              </CardHeader>
              <CardContent className="p-6 space-y-6">
                {generatedSchema.sections.map((section, sIdx) => (
                  <div key={sIdx} className="space-y-3">
                    <div className="flex items-center gap-2 pb-2 border-b border-slate-100 dark:border-slate-800">
                      <Layers className="w-4 h-4 text-emerald-500" />
                      <h4 className="text-sm font-bold text-slate-900 dark:text-white">
                        Section {sIdx + 1}: {section.title}
                      </h4>
                      {section.description && (
                        <span className="text-xs text-slate-400 font-normal">({section.description})</span>
                      )}
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3 pl-2">
                      {(section.questions || []).map((q, qIdx) => (
                        <div
                          key={qIdx}
                          className="p-3.5 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/40 space-y-1.5"
                        >
                          <div className="flex items-start justify-between gap-2">
                            <span className="text-xs font-semibold text-slate-900 dark:text-white">
                              {q.title}
                              {q.isRequired && <span className="text-rose-500 ml-1">*</span>}
                            </span>
                            <Badge variant="secondary" className="text-[10px] shrink-0">
                              {q.type.replace('_', ' ')}
                            </Badge>
                          </div>
                          {q.description && (
                            <p className="text-[11px] text-slate-500">{q.description}</p>
                          )}
                          {q.options && q.options.length > 0 && (
                            <div className="flex flex-wrap gap-1 pt-1">
                              {q.options.map((opt, oIdx) => (
                                <span
                                  key={oIdx}
                                  className="text-[10px] px-2 py-0.5 rounded bg-slate-200 dark:bg-slate-800 text-slate-700 dark:text-slate-300"
                                >
                                  {opt.label}
                                </span>
                              ))}
                            </div>
                          )}
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </CardContent>
            </Card>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
};
