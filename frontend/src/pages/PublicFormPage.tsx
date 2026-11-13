import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { publicFormService } from '@/services/publicFormService';
import { PublicFormSchema, FormQuestion } from '@/types';
import { Button } from '@/components/ui/Button';
import { Skeleton } from '@/components/ui/Skeleton';
import {
  Lock,
  ArrowRight,
  ArrowLeft,
  CheckCircle2,
  AlertCircle,
  Star,
  UploadCloud,
  PenTool,
  Calendar,
  Clock,
  Sparkles,
  ExternalLink,
  RotateCcw,
  Send,
} from 'lucide-react';

export const PublicFormPage: React.FC = () => {
  const { slug } = useParams<{ slug: string }>();
  const navigate = useNavigate();

  const [form, setForm] = useState<PublicFormSchema | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);

  // Password protection
  const [isUnlocked, setIsUnlocked] = useState(false);
  const [enteredPassword, setEnteredPassword] = useState('');
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [isVerifyingPassword, setIsVerifyingPassword] = useState(false);

  // Form answer state
  const [answers, setAnswers] = useState<Record<string, any>>({});
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Multi-step pagination
  const [currentSectionIndex, setCurrentSectionIndex] = useState(0);

  // Duration timer
  const startTimeRef = useRef<number>(Date.now());

  useEffect(() => {
    if (!slug) return;
    setIsLoading(true);
    setLoadError(null);
    publicFormService
      .getPublicForm(slug)
      .then((data) => {
        setForm(data);
        if (!data.isPasswordProtected) {
          setIsUnlocked(true);
        }
        setIsLoading(false);
        startTimeRef.current = Date.now();
      })
      .catch((err) => {
        setLoadError(err.response?.data?.message || 'Form not found or is no longer accepting responses.');
        setIsLoading(false);
      });
  }, [slug]);

  const handlePasswordSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!slug || !enteredPassword.trim()) return;

    setIsVerifyingPassword(true);
    setPasswordError(null);
    try {
      const res = await publicFormService.verifyPassword(slug, enteredPassword);
      if (res.valid) {
        setIsUnlocked(true);
      } else {
        setPasswordError('Invalid access password. Please try again.');
      }
    } catch (err: any) {
      setPasswordError(err.response?.data?.message || 'Verification failed');
    } finally {
      setIsVerifyingPassword(false);
    }
  };

  // Conditional Logic Visibility Evaluator
  const isQuestionVisible = (q: FormQuestion) => {
    if (!form) return true;
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
    if (fieldErrors[qId]) {
      setFieldErrors((prev) => {
        const next = { ...prev };
        delete next[qId];
        return next;
      });
    }
    if (submitError) {
      setSubmitError(null);
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex items-center justify-center p-6">
        <div className="w-full max-w-xl space-y-4">
          <Skeleton className="h-10 w-3/4 mx-auto" />
          <Skeleton className="h-4 w-1/2 mx-auto" />
          <Skeleton className="h-64 w-full rounded-2xl mt-6" />
        </div>
      </div>
    );
  }

  if (loadError || !form) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex items-center justify-center p-6">
        <div className="max-w-md w-full p-8 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-center space-y-4 shadow-sm">
          <div className="w-12 h-12 bg-rose-50 dark:bg-rose-950/50 text-rose-600 rounded-full flex items-center justify-center mx-auto">
            <AlertCircle className="w-6 h-6" />
          </div>
          <h2 className="text-xl font-bold text-slate-900 dark:text-white">Form Unavailable</h2>
          <p className="text-xs text-slate-500">{loadError}</p>
        </div>
      </div>
    );
  }

  // Password Unlock Gate
  if (!isUnlocked) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex items-center justify-center p-6">
        <div className="max-w-md w-full p-8 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 space-y-6 shadow-sm">
          <div className="w-12 h-12 bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
            <Lock className="w-6 h-6" />
          </div>
          <div className="text-center">
            <h2 className="text-xl font-bold text-slate-900 dark:text-white">Password Protected</h2>
            <p className="text-xs text-slate-500 mt-1">
              This form requires an access code or password to view and submit.
            </p>
          </div>

          <form onSubmit={handlePasswordSubmit} className="space-y-4">
            {passwordError && (
              <p className="text-xs text-rose-600 text-center bg-rose-50 dark:bg-rose-950/50 p-2.5 rounded-lg border border-rose-200 dark:border-rose-900">
                {passwordError}
              </p>
            )}
            <input
              type="password"
              placeholder="Enter password"
              value={enteredPassword}
              onChange={(e) => setEnteredPassword(e.target.value)}
              className="w-full text-sm px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500"
              autoFocus
            />
            <Button type="submit" className="w-full font-bold" isLoading={isVerifyingPassword}>
              Unlock Form
            </Button>
          </form>
        </div>
      </div>
    );
  }

  const currentSection = form.sections[currentSectionIndex] || form.sections[0];
  const totalSections = Math.max(1, form.sections.length);
  const isLastSection = currentSectionIndex >= totalSections - 1;

  // Filter visible questions in current section
  const sectionQuestions = form.questions
    .filter((q) => q.sectionId === currentSection?.id)
    .sort((a, b) => a.orderIndex - b.orderIndex);

  const validateCurrentSection = () => {
    const errors: Record<string, string> = {};
    sectionQuestions.forEach((q) => {
      if (!isQuestionVisible(q)) return;
      const val = answers[q.id];

      // Required check
      if (q.isRequired && (val === undefined || val === null || val === '' || (Array.isArray(val) && val.length === 0))) {
        errors[q.id] = 'This question is required.';
        return;
      }

      // Email check
      if (q.type === 'EMAIL' && val) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(String(val))) {
          errors[q.id] = 'Please enter a valid email address.';
        }
      }

      // Regex pattern check
      if (q.validationRules?.pattern && val) {
        try {
          const reg = new RegExp(q.validationRules.pattern);
          if (!reg.test(String(val))) {
            errors[q.id] = q.validationRules.patternError || 'Invalid format.';
          }
        } catch (e) {}
      }
    });

    setFieldErrors((prev) => ({ ...prev, ...errors }));
    return Object.keys(errors).length === 0;
  };

  const validateAllSections = (): boolean => {
    const errors: Record<string, string> = {};
    let firstErrorSectionIdx = -1;

    form.sections.forEach((section, sIdx) => {
      const qInSec = form.questions
        .filter((q) => q.sectionId === section.id)
        .sort((a, b) => a.orderIndex - b.orderIndex);

      qInSec.forEach((q) => {
        if (!isQuestionVisible(q)) return;
        const val = answers[q.id];

        if (q.isRequired && (val === undefined || val === null || val === '' || (Array.isArray(val) && val.length === 0))) {
          errors[q.id] = 'This question is required.';
          if (firstErrorSectionIdx === -1) firstErrorSectionIdx = sIdx;
          return;
        }

        if (q.type === 'EMAIL' && val) {
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
          if (!emailRegex.test(String(val))) {
            errors[q.id] = 'Please enter a valid email address.';
            if (firstErrorSectionIdx === -1) firstErrorSectionIdx = sIdx;
          }
        }

        if (q.validationRules?.pattern && val) {
          try {
            const reg = new RegExp(q.validationRules.pattern);
            if (!reg.test(String(val))) {
              errors[q.id] = q.validationRules.patternError || 'Invalid format.';
              if (firstErrorSectionIdx === -1) firstErrorSectionIdx = sIdx;
            }
          } catch (e) {}
        }
      });
    });

    setFieldErrors(errors);

    if (Object.keys(errors).length > 0) {
      if (firstErrorSectionIdx !== -1 && firstErrorSectionIdx !== currentSectionIndex) {
        setCurrentSectionIndex(firstErrorSectionIdx);
      }
      setSubmitError('Please complete all required fields before submitting.');
      window.scrollTo({ top: 0, behavior: 'smooth' });
      return false;
    }

    setSubmitError(null);
    return true;
  };

  const handleNextSection = () => {
    if (!validateCurrentSection()) {
      setSubmitError('Please complete all required questions on this step.');
      return;
    }
    setSubmitError(null);
    setCurrentSectionIndex((prev) => Math.min(prev + 1, totalSections - 1));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handlePrevSection = () => {
    setSubmitError(null);
    setCurrentSectionIndex((prev) => Math.max(prev - 1, 0));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!slug) return;
    setSubmitError(null);

    // Validate all sections across the form
    if (!validateAllSections()) return;

    setIsSubmitting(true);
    try {
      const durationSeconds = Math.max(1, Math.round((Date.now() - startTimeRef.current) / 1000));

      // Map answers to request format
      const formattedAnswers = Object.entries(answers).map(([questionId, answerValue]) => {
        const question = form.questions.find((q) => q.id === questionId);
        return {
          questionId,
          fieldKey: question?.fieldKey || questionId,
          answerValue,
        };
      });

      const result = await publicFormService.submitForm(slug, {
        answers: formattedAnswers,
        durationSeconds,
        metadata: {
          userAgent: navigator.userAgent,
          screenResolution: `${window.screen.width}x${window.screen.height}`,
          language: navigator.language,
        },
      });

      // Redirection or thank you page
      if (form.redirectUrl) {
        window.location.href = form.redirectUrl;
      } else {
        navigate(`/f/${slug}/success`, {
          state: {
            thankYouMessage: form.thankYouMessage,
            submissionId: result.submissionId,
          },
        });
      }
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Submission failed. Please check your answers.';
      setSubmitError(msg);
      if (err.response?.data?.errors) {
        setFieldErrors(err.response.data.errors);
      }
      window.scrollTo({ top: 0, behavior: 'smooth' });
    } finally {
      setIsSubmitting(false);
    }
  };

  const theme = form.theme || {
    primaryColor: '#10B981',
    backgroundColor: '#ffffff',
    fontFamily: 'Inter',
    borderRadius: 'rounded-xl',
  };

  const progressPercentage = Math.round(((currentSectionIndex + 1) / totalSections) * 100);

  return (
    <div
      className="min-h-screen bg-slate-50 dark:bg-slate-950 flex flex-col items-center justify-start p-4 sm:p-8 font-sans selection:bg-emerald-500 selection:text-white"
      style={{ fontFamily: theme.fontFamily || 'Inter' }}
    >
      <div className="w-full max-w-2xl space-y-6">
        {/* Progress bar if multi-step */}
        {totalSections > 1 && (
          <div className="space-y-1.5">
            <div className="flex items-center justify-between text-xs font-semibold text-slate-500">
              <span>
                Step {currentSectionIndex + 1} of {totalSections}
              </span>
              <span>{progressPercentage}% Completed</span>
            </div>
            <div className="w-full h-1.5 bg-slate-200 dark:bg-slate-800 rounded-full overflow-hidden">
              <div
                className="h-full bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 transition-all duration-500 ease-out"
                style={{ width: `${progressPercentage}%` }}
              />
            </div>
          </div>
        )}

        {/* Main Card */}
        <form
          onSubmit={handleSubmit}
          className="rounded-2xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 p-6 md:p-10 shadow-lg space-y-8"
        >
          {theme.coverImageUrl && (
            <div className="h-40 -mx-6 -mt-6 md:-mx-10 md:-mt-10 mb-6 overflow-hidden rounded-t-2xl">
              <img
                src={theme.coverImageUrl}
                alt="Form Banner"
                className="w-full h-full object-cover"
              />
            </div>
          )}

          {theme.logoUrl && (
            <img
              src={theme.logoUrl}
              alt="Brand Logo"
              className="w-14 h-14 object-contain rounded-xl mb-3"
            />
          )}

          {/* Form Header (on step 0) */}
          {currentSectionIndex === 0 && (
            <div>
              <h1 className="text-2xl sm:text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">
                {form.title}
              </h1>
              {form.description && (
                <p className="text-sm text-slate-600 dark:text-slate-400 mt-2 leading-relaxed">
                  {form.description}
                </p>
              )}
            </div>
          )}

          {/* Current Section Title */}
          {currentSection && totalSections > 1 && (
            <div className="pb-3 border-b border-slate-100 dark:border-slate-800">
              <h2 className="text-lg font-bold text-slate-900 dark:text-white">
                {currentSection.title}
              </h2>
              {currentSection.description && (
                <p className="text-xs text-slate-500 mt-1">{currentSection.description}</p>
              )}
            </div>
          )}

          {/* Inline Form Submission Error Banner */}
          {submitError && (
            <div className="p-4 rounded-xl bg-rose-50 dark:bg-rose-950/60 border border-rose-200 dark:border-rose-900/60 text-rose-700 dark:text-rose-300 text-xs font-semibold flex items-center gap-2.5 shadow-sm">
              <AlertCircle className="w-4 h-4 text-rose-500 shrink-0" />
              <span>{submitError}</span>
            </div>
          )}

          {/* Questions */}
          <div className="space-y-6">
            {sectionQuestions.map((question, idx) => {
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
                    const fakeEvent = { preventDefault: () => {} } as React.FormEvent;
                    handleSubmit(fakeEvent);
                  } else if (action === 'NEXT_SECTION') {
                    handleNextSection();
                  } else if (action === 'PREV_SECTION') {
                    handlePrevSection();
                  } else if (action === 'LINK') {
                    if (btnConfig.buttonUrl) {
                      window.open(btnConfig.buttonUrl, btnConfig.openInNewTab !== false ? '_blank' : '_self');
                    }
                  } else if (action === 'RESET') {
                    setAnswers({});
                    setCurrentSectionIndex(0);
                    setSubmitError(null);
                    setFieldErrors({});
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
                          action === 'NEXT_SECTION' ? (
                            <ArrowRight className="w-4 h-4" />
                          ) : action === 'PREV_SECTION' ? (
                            <ArrowLeft className="w-4 h-4" />
                          ) : action === 'LINK' ? (
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

              const hasError = !!fieldErrors[question.id];
              const currentVal = answers[question.id] ?? '';

              return (
                <div
                  key={question.id}
                  className="space-y-2.5 p-4 rounded-xl bg-slate-50/50 dark:bg-slate-800/20 border border-slate-100 dark:border-slate-800/70 transition-all"
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

                  {/* Input Rendering */}
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
                      className={`w-full text-sm px-4 py-2.5 rounded-xl border bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 ${
                        hasError ? 'border-rose-500 ring-1 ring-rose-500' : 'border-slate-200 dark:border-slate-700'
                      }`}
                    />
                  )}

                  {question.type === 'LONG_TEXT' && (
                    <textarea
                      rows={4}
                      value={currentVal}
                      onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                      placeholder={question.placeholder || 'Type your message...'}
                      className={`w-full text-sm p-3.5 rounded-xl border bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 resize-none ${
                        hasError ? 'border-rose-500 ring-1 ring-rose-500' : 'border-slate-200 dark:border-slate-700'
                      }`}
                    />
                  )}

                  {['SINGLE_CHOICE', 'RADIO'].includes(question.type) && (
                    <div className="space-y-2 pt-1">
                      {(question.options || []).map((opt, i) => (
                        <label
                          key={i}
                          className={`flex items-center gap-3 p-3.5 rounded-xl border cursor-pointer text-xs font-semibold transition-all ${
                            currentVal === opt.value
                              ? 'border-emerald-600 bg-emerald-50/70 dark:bg-emerald-950/40 text-emerald-950 dark:text-emerald-200 shadow-xs'
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
                            className={`flex items-center gap-3 p-3.5 rounded-xl border cursor-pointer text-xs font-semibold transition-all ${
                              isChecked
                                ? 'border-emerald-600 bg-emerald-50/70 dark:bg-emerald-950/40 text-emerald-950 dark:text-emerald-200 shadow-xs'
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
                      className={`w-full text-sm px-4 py-2.5 rounded-xl border bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 ${
                        hasError ? 'border-rose-500' : 'border-slate-200 dark:border-slate-700'
                      }`}
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
                          className={`flex-1 py-3 px-4 rounded-xl text-xs font-bold border transition-all cursor-pointer ${
                            currentVal === val
                              ? 'border-emerald-600 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-md'
                              : 'border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 hover:border-slate-300'
                          }`}
                        >
                          {val}
                        </button>
                      ))}
                    </div>
                  )}

                  {question.type === 'STAR_RATING' && (
                    <div className="flex gap-2.5 pt-1">
                      {[1, 2, 3, 4, 5].map((star) => (
                        <button
                          key={star}
                          type="button"
                          onClick={() => handleAnswerChange(question.id, star)}
                          className="focus:outline-none transition-transform hover:scale-115 cursor-pointer"
                        >
                          <Star
                            className={`w-9 h-9 ${
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
                    <div className="flex gap-2 overflow-x-auto pt-1 pb-1">
                      {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((num) => (
                        <button
                          key={num}
                          type="button"
                          onClick={() => handleAnswerChange(question.id, num)}
                          className={`w-10 h-10 rounded-xl border text-xs font-bold transition-all shrink-0 cursor-pointer ${
                            Number(currentVal) === num
                              ? 'border-emerald-600 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-md'
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
                    <label className="flex items-center gap-3 cursor-pointer pt-1 text-xs text-slate-700 dark:text-slate-300 font-medium">
                      <input
                        type="checkbox"
                        checked={!!currentVal}
                        onChange={(e) => handleAnswerChange(question.id, e.target.checked)}
                        className="rounded text-emerald-600 focus:ring-emerald-500"
                      />
                      <span>I agree and confirm this answer</span>
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
                      className={`w-full text-sm px-4 py-2.5 rounded-xl border bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 ${
                        hasError ? 'border-rose-500' : 'border-slate-200 dark:border-slate-700'
                      }`}
                    />
                  )}

                  {hasError && (
                    <p className="text-xs text-rose-500 flex items-center gap-1 font-medium mt-1">
                      <AlertCircle className="w-3.5 h-3.5" />
                      {fieldErrors[question.id]}
                    </p>
                  )}
                </div>
              );
            })}
          </div>

          {/* Navigation Controls */}
          <div className="pt-6 border-t border-slate-100 dark:border-slate-800 flex flex-col-reverse sm:flex-row items-center justify-between gap-4">
            {currentSectionIndex > 0 ? (
              <Button type="button" variant="outline" onClick={handlePrevSection} className="w-full sm:w-auto">
                <ArrowLeft className="w-4 h-4 mr-1.5" />
                Previous Step
              </Button>
            ) : (
              <div className="hidden sm:block" />
            )}

            {!isLastSection ? (
              <Button
                type="button"
                onClick={handleNextSection}
                className="w-full sm:w-auto font-bold text-white shadow-md shadow-emerald-500/20 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 hover:from-emerald-600 hover:to-teal-600"
              >
                Next Step
                <ArrowRight className="w-4 h-4 ml-1.5" />
              </Button>
            ) : (
              <Button
                type="submit"
                size="lg"
                isLoading={isSubmitting}
                className="w-full sm:w-auto font-bold text-white shadow-lg shadow-emerald-500/25 px-8 bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 hover:from-emerald-600 hover:to-teal-600"
              >
                Submit Response
              </Button>
            )}
          </div>
        </form>

        {/* Footer Brand */}
        <div className="text-center pt-2">
          <p className="text-[11px] text-slate-400 flex items-center justify-center gap-1">
            Powered by{' '}
            <strong className="text-slate-600 dark:text-slate-300 font-semibold">
              Timely Forms AI
            </strong>
          </p>
        </div>
      </div>
    </div>
  );
};
