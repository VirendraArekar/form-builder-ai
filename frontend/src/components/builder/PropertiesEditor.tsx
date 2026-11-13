import React, { useState } from 'react';
import { useBuilderStore } from '@/store/builderStore';
import { aiService } from '@/services/aiService';
import { Input } from '@/components/ui/Input';
import { Textarea } from '@/components/ui/Textarea';
import { Switch } from '@/components/ui/Switch';
import { Button } from '@/components/ui/Button';
import { useToast } from '@/components/ui/Toast';
import {
  Sparkles,
  Plus,
  Trash2,
  Sliders,
  Settings,
  MousePointerClick,
  Link as LinkIcon,
  Send,
  ChevronRight,
  ChevronLeft,
  RotateCcw,
} from 'lucide-react';

export const PropertiesEditor: React.FC = () => {
  const { showToast } = useToast();
  const {
    form,
    selectedQuestionId,
    updateQuestion,
    addQuestionOption,
    updateQuestionOption,
    removeQuestionOption,
  } = useBuilderStore();

  const [isImprovingAi, setIsImprovingAi] = useState(false);
  const [aiSuggestions, setAiSuggestions] = useState<Record<string, string> | null>(null);

  const selectedQuestion = form?.questions.find((q) => q.id === selectedQuestionId);

  if (!selectedQuestion) {
    return (
      <div className="flex flex-col items-center justify-center h-full p-6 text-center text-slate-400">
        <Settings className="w-10 h-10 mb-3 text-slate-300 dark:text-slate-700 stroke-1" />
        <h4 className="font-semibold text-slate-700 dark:text-slate-300 text-sm mb-1">
          No Element Selected
        </h4>
        <p className="text-xs text-slate-500 max-w-xs">
          Click on any question card or button element in the center canvas to configure its properties, actions, and settings.
        </p>
      </div>
    );
  }

  const isButton = selectedQuestion.type === 'BUTTON';

  // Parse button configuration if BUTTON type
  let btnConfig: any = {};
  if (isButton) {
    try {
      btnConfig = selectedQuestion.configJson
        ? JSON.parse(selectedQuestion.configJson)
        : (selectedQuestion.defaultValue ? JSON.parse(selectedQuestion.defaultValue) : {});
    } catch {
      btnConfig = {};
    }
  }

  const currentAction = btnConfig.buttonAction || 'SUBMIT';
  const currentVariant = btnConfig.buttonVariant || 'primary';
  const currentAlign = btnConfig.buttonAlign || 'left';
  const currentText = btnConfig.buttonText || selectedQuestion.title || 'Submit Response';
  const currentUrl = btnConfig.buttonUrl || '';
  const currentNewTab = btnConfig.openInNewTab !== false;

  const updateButtonConfig = (updates: Record<string, any>) => {
    const merged = {
      buttonAction: currentAction,
      buttonVariant: currentVariant,
      buttonAlign: currentAlign,
      buttonText: currentText,
      buttonUrl: currentUrl,
      openInNewTab: currentNewTab,
      ...updates,
    };
    const jsonStr = JSON.stringify(merged);
    updateQuestion(selectedQuestion.id, {
      configJson: jsonStr,
      defaultValue: jsonStr,
      title: merged.buttonText || selectedQuestion.title,
    });
  };

  const hasOptions = [
    'SINGLE_CHOICE',
    'MULTIPLE_CHOICE',
    'DROPDOWN',
    'RADIO',
  ].includes(selectedQuestion.type);

  const hasPlaceholder = [
    'SHORT_TEXT',
    'LONG_TEXT',
    'EMAIL',
    'PHONE',
    'NUMBER',
    'URL',
    'DATE',
    'TIME',
  ].includes(selectedQuestion.type);

  const hasMinMax = [
    'SHORT_TEXT',
    'LONG_TEXT',
    'NUMBER',
    'SLIDER',
  ].includes(selectedQuestion.type);

  const handleAiImprove = async () => {
    setIsImprovingAi(true);
    setAiSuggestions(null);
    try {
      const suggestions = await aiService.improveQuestion({
        questionTitle: selectedQuestion.title,
        questionType: selectedQuestion.type,
      });
      setAiSuggestions(suggestions);
      showToast('AI suggestions generated!', 'success');
    } catch {
      showToast('Failed to get AI suggestions', 'error');
    } finally {
      setIsImprovingAi(false);
    }
  };

  const applySuggestion = (newTitle: string) => {
    updateQuestion(selectedQuestion.id, { title: newTitle });
    if (isButton) {
      updateButtonConfig({ buttonText: newTitle });
    }
    setAiSuggestions(null);
    showToast('Updated with AI suggestion', 'success');
  };

  const validationRules = selectedQuestion.validationRules || {};

  const updateValidation = (key: string, value: any) => {
    updateQuestion(selectedQuestion.id, {
      validationRules: {
        ...validationRules,
        [key]: value,
      },
    });
  };

  return (
    <div className="flex flex-col h-full bg-white dark:bg-slate-900 border-l border-slate-200 dark:border-slate-800 w-80 shrink-0 overflow-y-auto scrollbar-thin">
      <div className="p-4 border-b border-slate-100 dark:border-slate-800 flex items-center justify-between">
        <div>
          <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">
            {isButton ? 'Button Properties' : 'Field Properties'}
          </h3>
          <span className="text-[11px] text-emerald-600 dark:text-emerald-400 font-semibold flex items-center gap-1">
            {isButton ? (
              <>
                <MousePointerClick className="w-3.5 h-3.5" />
                Button Element
              </>
            ) : (
              selectedQuestion.type.replace('_', ' ')
            )}
          </span>
        </div>

        {!isButton && (
          <Button
            size="sm"
            variant="outline"
            className="text-xs h-7 px-2"
            onClick={handleAiImprove}
            isLoading={isImprovingAi}
          >
            <Sparkles className="w-3.5 h-3.5 mr-1 text-emerald-500" />
            Polish AI
          </Button>
        )}
      </div>

      <div className="p-4 space-y-5 flex-1">
        {/* AI Suggestions Box */}
        {aiSuggestions && (
          <div className="p-3 bg-gradient-to-br from-emerald-50 to-teal-50 dark:from-emerald-950/40 dark:to-teal-950/30 border border-emerald-200 dark:border-emerald-900 rounded-xl space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-bold text-emerald-900 dark:text-emerald-300 flex items-center gap-1.5">
                <Sparkles className="w-3.5 h-3.5" />
                Gemini AI Suggestions:
              </span>
              <button
                onClick={() => setAiSuggestions(null)}
                className="text-[10px] text-slate-400 hover:text-slate-600 cursor-pointer"
              >
                Dismiss
              </button>
            </div>
            <div className="space-y-1.5">
              {Object.entries(aiSuggestions).map(([style, text]) => (
                <div
                  key={style}
                  className="p-2 rounded bg-white dark:bg-slate-900 border border-emerald-100 dark:border-emerald-950 text-xs flex items-center justify-between gap-2"
                >
                  <div>
                    <span className="text-[10px] font-bold uppercase text-emerald-600 dark:text-emerald-400 block">
                      {style}
                    </span>
                    <span className="text-slate-700 dark:text-slate-300 font-medium">{text}</span>
                  </div>
                  <button
                    onClick={() => applySuggestion(text)}
                    className="px-2 py-1 text-[10px] font-semibold bg-emerald-600 text-white rounded hover:bg-emerald-500 shrink-0 cursor-pointer"
                  >
                    Apply
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* BUTTON SPECIFIC PROPERTIES */}
        {isButton ? (
          <div className="space-y-4">
            <Input
              label="Button Label / Text"
              value={currentText}
              onChange={(e) => updateButtonConfig({ buttonText: e.target.value })}
              placeholder="e.g. Submit Application, Continue, Visit Site"
            />

            {/* Button Action Type */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 dark:text-slate-300 mb-1.5">
                Button Action Type
              </label>
              <select
                value={currentAction}
                onChange={(e) => updateButtonConfig({ buttonAction: e.target.value })}
                className="w-full text-xs px-3 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500"
              >
                <option value="SUBMIT">Submit Form (Send Responses)</option>
                <option value="NEXT_SECTION">Next Section / Step</option>
                <option value="PREV_SECTION">Previous Section / Step</option>
                <option value="LINK">External Link / URL</option>
                <option value="RESET">Reset / Clear Answers</option>
              </select>
              <p className="text-[11px] text-slate-400 mt-1">
                {currentAction === 'SUBMIT' && 'Validates all required fields and submits the form.'}
                {currentAction === 'NEXT_SECTION' && 'Validates the current section and advances to next page.'}
                {currentAction === 'PREV_SECTION' && 'Navigates back to the preceding form step.'}
                {currentAction === 'LINK' && 'Opens a custom web page or documentation link.'}
                {currentAction === 'RESET' && 'Clears all user inputs back to blank defaults.'}
              </p>
            </div>

            {/* External URL if action is LINK */}
            {currentAction === 'LINK' && (
              <div className="space-y-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700">
                <Input
                  label="Target Website URL"
                  value={currentUrl}
                  onChange={(e) => updateButtonConfig({ buttonUrl: e.target.value })}
                  placeholder="https://example.com/info"
                />
                <Switch
                  label="Open in new tab"
                  description="Opens destination in a separate browser tab"
                  checked={currentNewTab}
                  onChange={(checked) => updateButtonConfig({ openInNewTab: checked })}
                />
              </div>
            )}

            {/* Style Variant */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 dark:text-slate-300 mb-1.5">
                Button Style Variant
              </label>
              <div className="grid grid-cols-2 gap-2">
                {[
                  { id: 'primary', label: 'Emerald Fill' },
                  { id: 'secondary', label: 'Neutral Slate' },
                  { id: 'outline', label: 'Outline Border' },
                  { id: 'danger', label: 'Danger Rose' },
                ].map((v) => (
                  <button
                    key={v.id}
                    type="button"
                    onClick={() => updateButtonConfig({ buttonVariant: v.id })}
                    className={`px-3 py-2 text-xs font-semibold rounded-xl border text-center transition-all cursor-pointer ${
                      currentVariant === v.id
                        ? 'border-emerald-500 bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-300 shadow-xs'
                        : 'border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800'
                    }`}
                  >
                    {v.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Alignment */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 dark:text-slate-300 mb-1.5">
                Button Alignment
              </label>
              <div className="grid grid-cols-4 gap-1.5">
                {[
                  { id: 'left', label: 'Left' },
                  { id: 'center', label: 'Center' },
                  { id: 'right', label: 'Right' },
                  { id: 'full', label: 'Full Width' },
                ].map((a) => (
                  <button
                    key={a.id}
                    type="button"
                    onClick={() => updateButtonConfig({ buttonAlign: a.id })}
                    className={`py-1.5 text-xs font-semibold rounded-lg border text-center transition-all cursor-pointer ${
                      currentAlign === a.id
                        ? 'border-emerald-500 bg-emerald-600 text-white shadow-xs'
                        : 'border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800'
                    }`}
                  >
                    {a.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Optional Help Text */}
            <Textarea
              label="Optional Subtext / Help Note"
              value={selectedQuestion.description || ''}
              onChange={(e) => updateQuestion(selectedQuestion.id, { description: e.target.value })}
              placeholder="e.g. Response will be delivered immediately"
              rows={2}
            />
          </div>
        ) : (
          /* STANDARD QUESTION PROPERTIES */
          <>
            <div className="space-y-3">
              <Input
                label="Question Title"
                value={selectedQuestion.title}
                onChange={(e) => updateQuestion(selectedQuestion.id, { title: e.target.value })}
                placeholder="e.g. What is your full legal name?"
              />

              <Textarea
                label="Help / Description Text"
                value={selectedQuestion.description || ''}
                onChange={(e) => updateQuestion(selectedQuestion.id, { description: e.target.value })}
                placeholder="Add additional guidance for respondents..."
                rows={2}
              />

              {hasPlaceholder && (
                <Input
                  label="Placeholder text"
                  value={selectedQuestion.placeholder || ''}
                  onChange={(e) => updateQuestion(selectedQuestion.id, { placeholder: e.target.value })}
                  placeholder="e.g. John Doe"
                />
              )}

              <div className="pt-2">
                <Switch
                  label="Required Question"
                  description="Respondent cannot submit without answering this field."
                  checked={selectedQuestion.isRequired}
                  onChange={(checked) => updateQuestion(selectedQuestion.id, { isRequired: checked })}
                />
              </div>
            </div>

            {/* Choice Options Manager */}
            {hasOptions && (
              <div className="space-y-3 pt-3 border-t border-slate-100 dark:border-slate-800">
                <div className="flex items-center justify-between">
                  <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider">
                    Choices / Options
                  </label>
                  <button
                    onClick={() =>
                      addQuestionOption(
                        selectedQuestion.id,
                        `Option ${(selectedQuestion.options?.length || 0) + 1}`
                      )
                    }
                    className="text-xs font-semibold text-emerald-600 dark:text-emerald-400 hover:text-emerald-500 flex items-center gap-1 cursor-pointer"
                  >
                    <Plus className="w-3.5 h-3.5" />
                    Add
                  </button>
                </div>

                <div className="space-y-2">
                  {(selectedQuestion.options || []).map((opt, idx) => (
                    <div key={idx} className="flex items-center gap-2">
                      <span className="text-xs text-slate-400 w-4 font-mono">{idx + 1}.</span>
                      <input
                        type="text"
                        value={opt.label}
                        onChange={(e) =>
                          updateQuestionOption(
                            selectedQuestion.id,
                            idx,
                            e.target.value,
                            e.target.value.toLowerCase().replace(/\s+/g, '_')
                          )
                        }
                        className="flex-1 text-xs px-2.5 py-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-1 focus:ring-emerald-500"
                      />
                      <button
                        onClick={() => removeQuestionOption(selectedQuestion.id, idx)}
                        className="p-1.5 text-slate-400 hover:text-rose-600 transition-colors"
                        title="Delete option"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Validation Constraints */}
            <div className="space-y-3 pt-3 border-t border-slate-100 dark:border-slate-800">
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
                <Sliders className="w-3.5 h-3.5" />
                Validation Rules
              </label>

              {hasMinMax && (
                <div className="grid grid-cols-2 gap-2">
                  <Input
                    label="Min Length / Val"
                    type="number"
                    value={validationRules.min ?? ''}
                    onChange={(e) =>
                      updateValidation('min', e.target.value ? Number(e.target.value) : undefined)
                    }
                  />
                  <Input
                    label="Max Length / Val"
                    type="number"
                    value={validationRules.max ?? ''}
                    onChange={(e) =>
                      updateValidation('max', e.target.value ? Number(e.target.value) : undefined)
                    }
                  />
                </div>
              )}

              {['SHORT_TEXT', 'LONG_TEXT'].includes(selectedQuestion.type) && (
                <div className="space-y-2">
                  <Input
                    label="Custom Regex Pattern"
                    placeholder="^[A-Za-z0-9]+$"
                    value={validationRules.pattern ?? ''}
                    onChange={(e) => updateValidation('pattern', e.target.value || undefined)}
                  />
                  <Input
                    label="Regex Error Message"
                    placeholder="Please enter alphanumeric characters only."
                    value={validationRules.patternError ?? ''}
                    onChange={(e) => updateValidation('patternError', e.target.value || undefined)}
                  />
                </div>
              )}

              {['FILE_UPLOAD', 'IMAGE_UPLOAD'].includes(selectedQuestion.type) && (
                <div className="space-y-2">
                  <Input
                    label="Max File Size (MB)"
                    type="number"
                    placeholder="10"
                    value={validationRules.maxFileSizeMb ?? ''}
                    onChange={(e) =>
                      updateValidation(
                        'maxFileSizeMb',
                        e.target.value ? Number(e.target.value) : undefined
                      )
                    }
                  />
                  <Input
                    label="Allowed Extensions (comma-separated)"
                    placeholder=".pdf, .png, .jpg"
                    value={validationRules.allowedExtensions ?? ''}
                    onChange={(e) =>
                      updateValidation('allowedExtensions', e.target.value || undefined)
                    }
                  />
                </div>
              )}
            </div>
          </>
        )}
      </div>
    </div>
  );
};
