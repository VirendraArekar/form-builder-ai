import { create } from 'zustand';
import { FormDetail, FormLogicRule, FormOption, FormQuestion, FormSection, FormTheme, QuestionType } from '@/types';

interface HistorySnapshot {
  title: string;
  description?: string;
  sections: FormSection[];
  questions: FormQuestion[];
  logicRules: FormLogicRule[];
  theme: FormTheme;
}

interface BuilderState {
  form: FormDetail | null;
  selectedQuestionId: string | null;
  selectedSectionId: string | null;
  saveStatus: 'saved' | 'saving' | 'unsaved';
  viewportMode: 'desktop' | 'tablet' | 'mobile';
  undoStack: HistorySnapshot[];
  redoStack: HistorySnapshot[];

  // Actions
  setForm: (form: FormDetail) => void;
  setSelectedQuestionId: (id: string | null) => void;
  setSelectedSectionId: (id: string | null) => void;
  setViewportMode: (mode: 'desktop' | 'tablet' | 'mobile') => void;
  setSaveStatus: (status: 'saved' | 'saving' | 'unsaved') => void;

  updateFormMeta: (meta: { title?: string; description?: string }) => void;
  addQuestion: (type: QuestionType, sectionId?: string, targetIndex?: number) => void;
  updateQuestion: (id: string, updates: Partial<FormQuestion>) => void;
  deleteQuestion: (id: string) => void;
  duplicateQuestion: (id: string) => void;
  reorderQuestions: (startIndex: number, endIndex: number) => void;
  addQuestionOption: (questionId: string, label?: string) => void;
  updateQuestionOption: (questionId: string, optIndex: number, label: string, value: string) => void;
  removeQuestionOption: (questionId: string, optIndex: number) => void;

  addSection: (title?: string) => void;
  updateSection: (id: string, updates: Partial<FormSection>) => void;
  deleteSection: (id: string) => void;

  updateTheme: (updates: Partial<FormTheme>) => void;
  addLogicRule: (rule: FormLogicRule) => void;
  deleteLogicRule: (index: number) => void;

  undo: () => void;
  redo: () => void;
}

function makeSnapshot(form: FormDetail): HistorySnapshot {
  return JSON.parse(
    JSON.stringify({
      title: form.title,
      description: form.description,
      sections: form.sections,
      questions: form.questions,
      logicRules: form.logicRules,
      theme: form.theme,
    })
  );
}

export function generateUUID(): string {
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return crypto.randomUUID();
  }
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0;
    const v = c === 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

const defaultTypeLabels: Record<QuestionType, string> = {
  SHORT_TEXT: 'Single-line Text',
  LONG_TEXT: 'Paragraph Text',
  EMAIL: 'Email Address',
  PHONE: 'Phone Number',
  NUMBER: 'Number Input',
  URL: 'Website URL',
  DATE: 'Date Picker',
  TIME: 'Time Picker',
  DATETIME: 'Date & Time',
  SINGLE_CHOICE: 'Single Choice (Radio)',
  MULTIPLE_CHOICE: 'Multiple Choice (Checkboxes)',
  DROPDOWN: 'Dropdown Select',
  YES_NO: 'Yes / No Toggle',
  RATING: 'Rating Score (1-5)',
  STAR_RATING: 'Star Rating',
  LINEAR_SCALE: 'Linear Scale (0-10)',
  SLIDER: 'Range Slider',
  CHECKBOX: 'Single Confirmation Checkbox',
  RADIO: 'Radio Option Group',
  FILE_UPLOAD: 'File Attachment Upload',
  IMAGE_UPLOAD: 'Image Upload',
  SIGNATURE: 'Digital Signature Pad',
  ADDRESS: 'Full Address Fields',
  SECTION_HEADING: 'Section Heading Divider',
  PARAGRAPH: 'Descriptive Text Paragraph',
  HIDDEN_FIELD: 'Hidden Tracking Metadata',
  BUTTON: 'Action Button',
};

export const useBuilderStore = create<BuilderState>((set, get) => ({
  form: null,
  selectedQuestionId: null,
  selectedSectionId: null,
  saveStatus: 'saved',
  viewportMode: 'desktop',
  undoStack: [],
  redoStack: [],

  setForm: (form) => set({
    form,
    selectedQuestionId: form.questions.length > 0 ? form.questions[0].id : null,
    selectedSectionId: form.sections.length > 0 ? form.sections[0].id : null,
    saveStatus: 'saved',
    undoStack: [],
    redoStack: [],
  }),

  setSelectedQuestionId: (id) => set({ selectedQuestionId: id }),
  setSelectedSectionId: (id) => set({ selectedSectionId: id }),
  setViewportMode: (mode) => set({ viewportMode: mode }),
  setSaveStatus: (status) => set({ saveStatus: status }),

  updateFormMeta: (meta) => {
    const { form, undoStack } = get();
    if (!form) return;
    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, ...meta },
    });
  },

  addQuestion: (type, sectionId, targetIndex) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newId = generateUUID();
    const newFieldKey = 'field_' + (form.questions.length + 1);
    const assignedSectionId = sectionId || (form.sections.length > 0 ? form.sections[0].id : undefined);

    const isButton = type === 'BUTTON';
    const defaultButtonConfig = isButton
      ? JSON.stringify({
          buttonAction: 'SUBMIT',
          buttonVariant: 'primary',
          buttonAlign: 'left',
          buttonText: 'Submit Response',
          buttonUrl: '',
          openInNewTab: true,
        })
      : undefined;

    const defaultOptions: FormOption[] =
      type.includes('CHOICE') || type === 'DROPDOWN' || type === 'RADIO'
        ? [
            { id: generateUUID(), label: 'Option 1', value: 'opt_1', orderIndex: 0 },
            { id: generateUUID(), label: 'Option 2', value: 'opt_2', orderIndex: 1 },
            { id: generateUUID(), label: 'Option 3', value: 'opt_3', orderIndex: 2 },
          ]
        : [];

    const newQuestion: FormQuestion = {
      id: newId,
      sectionId: assignedSectionId,
      fieldKey: newFieldKey,
      title: isButton ? 'Submit Response' : (defaultTypeLabels[type] || 'Untitled Question'),
      description: '',
      questionType: type,
      type: type,
      placeholder: isButton ? '' : 'Enter your answer...',
      defaultValue: defaultButtonConfig,
      configJson: defaultButtonConfig,
      isRequired: false,
      orderIndex: targetIndex !== undefined ? targetIndex : form.questions.length,
      options: defaultOptions,
    };

    const newQuestions = [...form.questions];
    if (targetIndex !== undefined) {
      newQuestions.splice(targetIndex, 0, newQuestion);
    } else {
      newQuestions.push(newQuestion);
    }

    newQuestions.forEach((q, idx) => {
      q.orderIndex = idx;
    });

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      selectedQuestionId: newId,
      form: { ...form, questions: newQuestions },
    });
  },

  updateQuestion: (id, updates) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newQuestions = form.questions.map((q) => (q.id === id ? { ...q, ...updates } : q));

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, questions: newQuestions },
    });
  },

  deleteQuestion: (id) => {
    const { form, undoStack, selectedQuestionId } = get();
    if (!form) return;

    const newQuestions = form.questions.filter((q) => q.id !== id);
    newQuestions.forEach((q, idx) => {
      q.orderIndex = idx;
    });

    // Also remove logic rules referencing this question
    const newRules = form.logicRules.filter(
      (r) => r.sourceQuestionId !== id && r.targetQuestionId !== id
    );

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      selectedQuestionId: selectedQuestionId === id ? (newQuestions[0]?.id || null) : selectedQuestionId,
      form: { ...form, questions: newQuestions, logicRules: newRules },
    });
  },

  duplicateQuestion: (id) => {
    const { form, undoStack } = get();
    if (!form) return;

    const original = form.questions.find((q) => q.id === id);
    if (!original) return;

    const newId = generateUUID();
    const clone: FormQuestion = {
      ...JSON.parse(JSON.stringify(original)),
      id: newId,
      fieldKey: original.fieldKey + '_copy',
      title: original.title + ' (Copy)',
      orderIndex: original.orderIndex + 1,
    };

    const newQuestions = [...form.questions];
    newQuestions.splice(original.orderIndex + 1, 0, clone);
    newQuestions.forEach((q, idx) => {
      q.orderIndex = idx;
    });

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      selectedQuestionId: newId,
      form: { ...form, questions: newQuestions },
    });
  },

  reorderQuestions: (startIndex, endIndex) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newQuestions = [...form.questions];
    const [moved] = newQuestions.splice(startIndex, 1);
    newQuestions.splice(endIndex, 0, moved);
    newQuestions.forEach((q, idx) => {
      q.orderIndex = idx;
    });

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, questions: newQuestions },
    });
  },

  addQuestionOption: (questionId, label) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newQuestions = form.questions.map((q) => {
      if (q.id === questionId) {
        const nextIdx = q.options.length;
        const optLabel = label || `Option ${nextIdx + 1}`;
        const newOpt: FormOption = {
          id: generateUUID(),
          label: optLabel,
          value: optLabel.toLowerCase().replace(/[^a-z0-9]/g, '_'),
          orderIndex: nextIdx,
        };
        return { ...q, options: [...q.options, newOpt] };
      }
      return q;
    });

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, questions: newQuestions },
    });
  },

  updateQuestionOption: (questionId, optIndex, label, value) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newQuestions = form.questions.map((q) => {
      if (q.id === questionId) {
        const updated = [...q.options];
        updated[optIndex] = { ...updated[optIndex], label, value };
        return { ...q, options: updated };
      }
      return q;
    });

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, questions: newQuestions },
    });
  },

  removeQuestionOption: (questionId, optIndex) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newQuestions = form.questions.map((q) => {
      if (q.id === questionId) {
        const updated = q.options.filter((_, idx) => idx !== optIndex);
        updated.forEach((opt, idx) => {
          opt.orderIndex = idx;
        });
        return { ...q, options: updated };
      }
      return q;
    });

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, questions: newQuestions },
    });
  },

  addSection: (title) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newId = generateUUID();
    const newSection: FormSection = {
      id: newId,
      title: title || `Section ${form.sections.length + 1}`,
      description: '',
      orderIndex: form.sections.length,
    };

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      selectedSectionId: newId,
      form: { ...form, sections: [...form.sections, newSection] },
    });
  },

  updateSection: (id, updates) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newSections = form.sections.map((s) => (s.id === id ? { ...s, ...updates } : s));

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, sections: newSections },
    });
  },

  deleteSection: (id) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newSections = form.sections.filter((s) => s.id !== id);
    newSections.forEach((s, idx) => {
      s.orderIndex = idx;
    });

    // Move orphan questions to first remaining section if any
    const fallbackSecId = newSections[0]?.id;
    const newQuestions = form.questions.map((q) => (q.sectionId === id ? { ...q, sectionId: fallbackSecId } : q));

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, sections: newSections, questions: newQuestions },
    });
  },

  updateTheme: (updates) => {
    const { form, undoStack } = get();
    if (!form) return;

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, theme: { ...form.theme, ...updates } },
    });
  },

  addLogicRule: (rule) => {
    const { form, undoStack } = get();
    if (!form) return;

    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, logicRules: [...form.logicRules, rule] },
    });
  },

  deleteLogicRule: (index) => {
    const { form, undoStack } = get();
    if (!form) return;

    const newRules = form.logicRules.filter((_, idx) => idx !== index);
    set({
      undoStack: [...undoStack, makeSnapshot(form)],
      redoStack: [],
      saveStatus: 'unsaved',
      form: { ...form, logicRules: newRules },
    });
  },

  undo: () => {
    const { form, undoStack, redoStack } = get();
    if (!form || undoStack.length === 0) return;

    const currentSnapshot = makeSnapshot(form);
    const previous = undoStack[undoStack.length - 1];
    const newUndo = undoStack.slice(0, undoStack.length - 1);

    set({
      form: {
        ...form,
        title: previous.title,
        description: previous.description,
        sections: previous.sections,
        questions: previous.questions,
        logicRules: previous.logicRules,
        theme: previous.theme,
      },
      undoStack: newUndo,
      redoStack: [...redoStack, currentSnapshot],
      saveStatus: 'unsaved',
    });
  },

  redo: () => {
    const { form, undoStack, redoStack } = get();
    if (!form || redoStack.length === 0) return;

    const currentSnapshot = makeSnapshot(form);
    const next = redoStack[redoStack.length - 1];
    const newRedo = redoStack.slice(0, redoStack.length - 1);

    set({
      form: {
        ...form,
        title: next.title,
        description: next.description,
        sections: next.sections,
        questions: next.questions,
        logicRules: next.logicRules,
        theme: next.theme,
      },
      undoStack: [...undoStack, currentSnapshot],
      redoStack: newRedo,
      saveStatus: 'unsaved',
    });
  },
}));
