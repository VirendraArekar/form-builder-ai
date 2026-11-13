import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { formService } from '@/services/formService';
import { FormDetail, FormQuestion } from '@/types';
import { Button } from '@/components/ui/Button';
import { Skeleton } from '@/components/ui/Skeleton';
import {
  ArrowLeft,
  Monitor,
  Tablet,
  Smartphone,
  CheckCircle2,
  AlertCircle,
  Star,
  UploadCloud,
  PenTool,
  Clock,
  Calendar,
  ArrowRight,
  ExternalLink,
  RotateCcw,
  Send,
} from 'lucide-react';

export const FormPreviewPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [form, setForm] = useState<FormDetail | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [viewport, setViewport] = useState<'desktop' | 'tablet' | 'mobile'>('desktop');
  const [answers, setAnswers] = useState<Record<string, any>>({});
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isSubmitted, setIsSubmitted] = useState(false);

  useEffect(() => {
    if (!id) return;
    formService
      .getForm(id)
      .then((data) => {
        setForm(data);
        setIsLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setIsLoading(false);
      });
  }, [id]);

  if (isLoading || !form) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-950 p-8 flex justify-center items-center">
        <Skeleton className="h-96 w-full max-w-2xl rounded-2xl" />
      </div>
    );
  }

  // Evaluate conditional visibility for question
  const isQuestionVisible = (q: FormQuestion) => {
    const rules = (form.logicRules || []).filter((r) => r.targetQuestionId === q.id);
    if (rules.length === 0) return true;

    for (const rule of rules) {
      const srcAnswer = String(answers[rule.sourceQuestionId] ?? '');
      const expected = String(rule.expectedValue ?? '');

      let matches = false;
      if (rule.operator === 'EQUALS') matches = srcAnswer.toLowerCase() === expected.toLowerCase();
      else if (rule.operator === 'NOT_EQUALS') matches = srcAnswer.toLowerCase() !== expected.toLowerCase();
      else if (rule.operator === 'CONTAINS') matches = srcAnswer.toLowerCase().includes(expected.toLowerCase());
      else if (rule.operator === 'GREATER_THAN') matches = Number(srcAnswer) > Number(expected);
      else if (rule.operator === 'LESS_THAN') matches = Number(srcAnswer) < Number(expected);

      if (rule.action === 'SHOW' && !matches) return false;
      if (rule.action === 'HIDE' && matches) return false;
    }
    return true;
  };

  const handleAnswerChange = (qId: string, val: any) => {
    setAnswers((prev) => ({ ...prev, [qId]: val }));
    if (errors[qId]) {
      setErrors((prev) => {
        const next = { ...prev };
        delete next[qId];
        return next;
      });
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors: Record<string, string> = {};

    form.questions.forEach((q) => {
      if (!isQuestionVisible(q)) return;
      if (q.isRequired && (answers[q.id] === undefined || answers[q.id] === '')) {
        newErrors[q.id] = 'This field is required';
      }
    });

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setIsSubmitted(true);
  };

  const getViewportWidth = () => {
    switch (viewport) {
      case 'mobile':
        return 'max-w-[390px]';
      case 'tablet':
        return 'max-w-[720px]';
      default:
        return 'max-w-[760px]';
    }
  };

  const theme = form.theme || {
    primaryColor: '#10B981',
    backgroundColor: '#ffffff',
    fontFamily: 'Inter',
    borderRadius: 'rounded-xl',
  };

  return (
    <div className="min-h-screen bg-slate-100 dark:bg-slate-950 flex flex-col font-sans selection:bg-emerald-500 selection:text-white">
      {/* Top Test Header */}
      <header className="h-14 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 px-6 flex items-center justify-between z-10 shrink-0">
        <div className="flex items-center gap-3">
          <Link
            to={`/builder/${form.id}`}
            className="p-1.5 rounded-lg text-slate-500 hover:text-slate-800 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </Link>
          <div>
            <h2 className="font-bold text-sm text-slate-900 dark:text-white">
              Preview Mode: {form.title}
            </h2>
            <span className="text-[11px] text-amber-600 dark:text-amber-400 font-semibold">
              Responses in preview are not saved to the database.
            </span>
          </div>
        </div>

        {/* Viewport Toggles */}
        <div className="flex items-center border border-slate-200 dark:border-slate-800 rounded-lg p-0.5 bg-slate-100 dark:bg-slate-800">
          <button
            onClick={() => setViewport('desktop')}
            className={`p-1.5 rounded-md ${
              viewport === 'desktop'
                ? 'bg-white dark:bg-slate-700 text-emerald-600 dark:text-emerald-400 shadow-xs'
                : 'text-slate-400 hover:text-slate-600'
            }`}
          >
            <Monitor className="w-4 h-4" />
          </button>
          <button
            onClick={() => setViewport('tablet')}
            className={`p-1.5 rounded-md ${
              viewport === 'tablet'
                ? 'bg-white dark:bg-slate-700 text-emerald-600 dark:text-emerald-400 shadow-xs'
                : 'text-slate-400 hover:text-slate-600'
            }`}
          >
            <Tablet className="w-4 h-4" />
          </button>
          <button
            onClick={() => setViewport('mobile')}
            className={`p-1.5 rounded-md ${
              viewport === 'mobile'
                ? 'bg-white dark:bg-slate-700 text-emerald-600 dark:text-emerald-400 shadow-xs'
                : 'text-slate-400 hover:text-slate-600'
            }`}
          >
            <Smartphone className="w-4 h-4" />
          </button>
        </div>
      </header>

      {/* Main Preview Container */}
      <div className="flex-1 overflow-y-auto p-6 md:p-12 flex justify-center items-start">
        <div className={`w-full ${getViewportWidth()} transition-all duration-300`}>
          {isSubmitted ? (
            <div className="p-8 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-center space-y-4 shadow-sm">
              <div className="w-14 h-14 bg-emerald-100 dark:bg-emerald-950/60 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
                <CheckCircle2 className="w-8 h-8" />
              </div>
              <h3 className="text-xl font-bold text-slate-900 dark:text-white">
                {form.thankYouMessage || 'Thank you! Your response has been recorded.'}
              </h3>
              <p className="text-xs text-slate-500">
                This is a preview test run. In production, this submission would be validated and saved.
              </p>
              <Button onClick={() => setIsSubmitted(false)} variant="outline" size="sm">
                Restart Preview
              </Button>
            </div>
          ) : (
            <form
              onSubmit={handleSubmit}
              className="rounded-2xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 p-6 md:p-10 shadow-sm space-y-8"
              style={{ fontFamily: theme.fontFamily || 'Inter' }}
            >
              {theme.coverImageUrl && (
                <div className="h-36 -mx-6 -mt-6 md:-mx-10 md:-mt-10 mb-6 overflow-hidden rounded-t-2xl">
                  <img
                    src={theme.coverImageUrl}
                    alt="Header Banner"
                    className="w-full h-full object-cover"
                  />
                </div>
              )}

              {theme.logoUrl && (
                <img
                  src={theme.logoUrl}
                  alt="Logo"
                  className="w-14 h-14 object-contain rounded-xl mb-4"
                />
              )}

              <div>
                <h1 className="text-2xl md:text-3xl font-extrabold text-slate-900 dark:text-white">
                  {form.title}
                </h1>
                {form.description && (
                  <p className="text-sm text-slate-500 dark:text-slate-400 mt-2 leading-relaxed">
                    {form.description}
                  </p>
                )}
              </div>

              {/* Questions */}
              <div className="space-y-6">
                {form.questions.map((question, idx) => {
                  if (!isQuestionVisible(question)) return null;

                  if (question.type === 'BUTTON') {
                    let btnConfig: any = {};
                    try {
                      btnConfig = question.configJson
                        ? JSON.parse(question.configJson)
                        : (question.defaultValue ? JSON.parse(question.defaultValue) : {});
                    } catch {
                      btnConfig = {};
                    }
                    const action = btnConfig.buttonAction || 'SUBMIT';
                    const variant = btnConfig.buttonVariant || 'primary';
                    const align = btnConfig.buttonAlign || 'left';
                    const label = btnConfig.buttonText || question.title || 'Submit Response';

                    const alignClasses: Record<string, string> = {
                      left: 'justify-start',
                      center: 'justify-center',
                      right: 'justify-end',
                      full: 'justify-stretch',
                    };

                    const handleButtonClick = (e: React.MouseEvent) => {
                      e.preventDefault();
                      if (action === 'SUBMIT') {
                        setIsSubmitted(true);
                      } else if (action === 'LINK') {
                        if (btnConfig.buttonUrl) {
                          window.open(btnConfig.buttonUrl, btnConfig.openInNewTab !== false ? '_blank' : '_self');
                        }
                      } else if (action === 'RESET') {
                        setAnswers({});
                        setErrors({});
                      }
                    };

                    return (
                      <div key={question.id} className="py-2">
                        <div className={`flex ${alignClasses[align] || 'justify-start'}`}>
                          <Button
                            type="button"
                            onClick={handleButtonClick}
                            variant={variant as any}
                            className={`${align === 'full' ? 'w-full' : ''} text-sm font-bold shadow-md`}
                            rightIcon={
                              action === 'LINK' ? (
                                <ExternalLink className="w-4 h-4" />
                              ) : action === 'RESET' ? (
                                <RotateCcw className="w-4 h-4" />
                              ) : action === 'SUBMIT' ? (
                                <Send className="w-4 h-4" />
                              ) : undefined
                            }
                          >
                            {label}
                          </Button>
                        </div>
                        {question.description && (
                          <p className="text-xs text-slate-400 mt-1.5">{question.description}</p>
                        )}
                      </div>
                    );
                  }

                  const hasError = !!errors[question.id];
                  const currentVal = answers[question.id] ?? '';

                  return (
                    <div
                      key={question.id}
                      className="space-y-2 p-4 rounded-xl bg-slate-50/50 dark:bg-slate-800/30 border border-slate-100 dark:border-slate-800/80 transition-all"
                    >
                      <label className="block text-sm font-semibold text-slate-900 dark:text-white">
                        {idx + 1}. {question.title}
                        {question.isRequired && (
                          <span className="text-rose-500 ml-1 font-bold">*</span>
                        )}
                      </label>

                      {question.description && (
                        <p className="text-xs text-slate-500 dark:text-slate-400">
                          {question.description}
                        </p>
                      )}

                      {/* Interactive Form Controls */}
                      {['SHORT_TEXT', 'EMAIL', 'PHONE', 'NUMBER', 'URL'].includes(question.type) && (
                        <input
                          type={
                            question.type === 'EMAIL'
                              ? 'email'
                              : question.type === 'NUMBER'
                              ? 'number'
                              : question.type === 'PHONE'
                              ? 'tel'
                              : 'text'
                          }
                          value={currentVal}
                          onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                          placeholder={question.placeholder || 'Your answer'}
                          className={`w-full text-sm px-3.5 py-2.5 rounded-xl border bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 ${
                            hasError ? 'border-rose-400 ring-1 ring-rose-400' : 'border-slate-200 dark:border-slate-700'
                          }`}
                        />
                      )}

                      {question.type === 'LONG_TEXT' && (
                        <textarea
                          rows={3}
                          value={currentVal}
                          onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                          placeholder={question.placeholder || 'Type your message...'}
                          className={`w-full text-sm p-3.5 rounded-xl border bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 ${
                            hasError ? 'border-rose-400 ring-1 ring-rose-400' : 'border-slate-200 dark:border-slate-700'
                          }`}
                        />
                      )}

                      {['SINGLE_CHOICE', 'RADIO'].includes(question.type) && (
                        <div className="space-y-2 pt-1">
                          {(question.options || []).map((opt, i) => (
                            <label
                              key={i}
                              className={`flex items-center gap-3 p-3 rounded-xl border cursor-pointer text-xs font-medium transition-all ${
                                currentVal === opt.value
                                  ? 'border-emerald-600 bg-emerald-50/70 dark:bg-emerald-950/40 text-emerald-900 dark:text-emerald-200'
                                  : 'border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 hover:border-slate-300'
                              }`}
                            >
                              <input
                                type="radio"
                                name={question.id}
                                value={opt.value}
                                checked={currentVal === opt.value}
                                onChange={() => handleAnswerChange(question.id, opt.value)}
                                className="text-emerald-600 focus:ring-emerald-500"
                              />
                              <span>{opt.label}</span>
                            </label>
                          ))}
                        </div>
                      )}

                      {question.type === 'MULTIPLE_CHOICE' && (
                        <div className="space-y-2 pt-1">
                          {(question.options || []).map((opt, i) => {
                            const isChecked = Array.isArray(currentVal) && currentVal.includes(opt.value);
                            return (
                              <label
                                key={i}
                                className={`flex items-center gap-3 p-3 rounded-xl border cursor-pointer text-xs font-medium transition-all ${
                                  isChecked
                                    ? 'border-emerald-600 bg-emerald-50/70 dark:bg-emerald-950/40 text-emerald-900 dark:text-emerald-200'
                                    : 'border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 hover:border-slate-300'
                                }`}
                              >
                                <input
                                  type="checkbox"
                                  checked={isChecked}
                                  onChange={(e) => {
                                    const prevArr: string[] = Array.isArray(currentVal) ? currentVal : [];
                                    const next = e.target.checked
                                      ? [...prevArr, opt.value]
                                      : prevArr.filter((item) => item !== opt.value);
                                    handleAnswerChange(question.id, next);
                                  }}
                                  className="rounded text-emerald-600 focus:ring-emerald-500"
                                />
                                <span>{opt.label}</span>
                              </label>
                            );
                          })}
                        </div>
                      )}

                      {question.type === 'DROPDOWN' && (
                        <select
                          value={currentVal}
                          onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                          className="w-full text-sm px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500"
                        >
                          <option value="">Select an option...</option>
                          {(question.options || []).map((opt, i) => (
                            <option key={i} value={opt.value}>
                              {opt.label}
                            </option>
                          ))}
                        </select>
                      )}

                      {question.type === 'YES_NO' && (
                        <div className="flex gap-3 pt-1">
                          {['Yes', 'No'].map((val) => (
                            <button
                              key={val}
                              type="button"
                              onClick={() => handleAnswerChange(question.id, val)}
                              className={`flex-1 py-2.5 px-4 rounded-xl text-xs font-bold border transition-all cursor-pointer ${
                                currentVal === val
                                  ? 'border-emerald-600 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white'
                                  : 'border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 hover:border-slate-300'
                              }`}
                            >
                              {val}
                            </button>
                          ))}
                        </div>
                      )}

                      {question.type === 'STAR_RATING' && (
                        <div className="flex gap-2 pt-1 text-slate-300">
                          {[1, 2, 3, 4, 5].map((star) => (
                            <button
                              key={star}
                              type="button"
                              onClick={() => handleAnswerChange(question.id, star)}
                              className="focus:outline-none transition-transform hover:scale-110 cursor-pointer"
                            >
                              <Star
                                className={`w-8 h-8 ${
                                  Number(currentVal) >= star
                                    ? 'text-amber-400 fill-amber-400'
                                    : 'text-slate-300 dark:text-slate-700'
                                }`}
                              />
                            </button>
                          ))}
                        </div>
                      )}

                      {['RATING', 'LINEAR_SCALE'].includes(question.type) && (
                        <div className="flex gap-1.5 overflow-x-auto pt-1 pb-1">
                          {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((num) => (
                            <button
                              key={num}
                              type="button"
                              onClick={() => handleAnswerChange(question.id, num)}
                              className={`w-9 h-9 rounded-xl border text-xs font-bold transition-all shrink-0 cursor-pointer ${
                                Number(currentVal) === num
                                  ? 'border-emerald-600 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-sm'
                                  : 'border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 hover:border-slate-300'
                              }`}
                            >
                              {num}
                            </button>
                          ))}
                        </div>
                      )}

                      {question.type === 'SLIDER' && (
                        <div className="space-y-2 pt-1">
                          <input
                            type="range"
                            min="0"
                            max="100"
                            value={currentVal || 50}
                            onChange={(e) => handleAnswerChange(question.id, Number(e.target.value))}
                            className="w-full accent-emerald-600"
                          />
                          <div className="flex justify-between text-xs text-slate-500 font-semibold">
                            <span>0</span>
                            <span className="text-emerald-600 font-bold">{currentVal || 50}</span>
                            <span>100</span>
                          </div>
                        </div>
                      )}

                      {question.type === 'CHECKBOX' && (
                        <label className="flex items-center gap-3 cursor-pointer pt-1 text-xs text-slate-700 dark:text-slate-300">
                          <input
                            type="checkbox"
                            checked={!!currentVal}
                            onChange={(e) => handleAnswerChange(question.id, e.target.checked)}
                            className="rounded text-emerald-600 focus:ring-emerald-500"
                          />
                          <span>I acknowledge and confirm this statement</span>
                        </label>
                      )}

                      {['DATE', 'TIME', 'DATETIME'].includes(question.type) && (
                        <input
                          type={
                            question.type === 'DATE'
                              ? 'date'
                              : question.type === 'TIME'
                              ? 'time'
                              : 'datetime-local'
                          }
                          value={currentVal}
                          onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                          className="w-full text-sm px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500"
                        />
                      )}

                      {hasError && (
                        <p className="text-xs text-rose-500 flex items-center gap-1 font-medium mt-1">
                          <AlertCircle className="w-3.5 h-3.5" />
                          {errors[question.id]}
                        </p>
                      )}
                    </div>
                  );
                })}
              </div>

              {/* Submit button */}
              <div className="pt-4 border-t border-slate-100 dark:border-slate-800">
                <Button
                  type="submit"
                  size="lg"
                  className="w-full text-white font-bold shadow-md shadow-emerald-500/25 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 hover:from-emerald-600 hover:to-teal-600"
                >
                  Submit Form
                </Button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};
