import React from 'react';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { FormQuestion } from '@/types';
import { useBuilderStore } from '@/store/builderStore';
import { Badge } from '@/components/ui/Badge';
import {
  GripVertical,
  Trash2,
  Copy,
  Settings,
  Star,
  Check,
  UploadCloud,
  PenTool,
  Calendar,
  Clock,
  Sparkles,
  Send,
  ChevronRight,
  ChevronLeft,
  ExternalLink,
  RotateCcw,
  MousePointerClick,
} from 'lucide-react';

interface CanvasQuestionCardProps {
  question: FormQuestion;
  index: number;
}

export const CanvasQuestionCard: React.FC<CanvasQuestionCardProps> = ({ question, index }) => {
  const {
    selectedQuestionId,
    setSelectedQuestionId,
    updateQuestion,
    duplicateQuestion,
    deleteQuestion,
  } = useBuilderStore();

  const isSelected = selectedQuestionId === question.id;

  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: question.id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: isDragging ? 0.4 : 1,
  };

  const renderFieldPreview = () => {
    switch (question.type) {
      case 'SHORT_TEXT':
      case 'EMAIL':
      case 'PHONE':
      case 'NUMBER':
      case 'URL':
        return (
          <input
            disabled
            placeholder={question.placeholder || 'Your answer here...'}
            className="w-full text-xs px-3.5 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/60 text-slate-400 cursor-not-allowed"
          />
        );

      case 'LONG_TEXT':
        return (
          <textarea
            disabled
            rows={3}
            placeholder={question.placeholder || 'Type your detailed response here...'}
            className="w-full text-xs p-3 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/60 text-slate-400 cursor-not-allowed resize-none"
          />
        );

      case 'SINGLE_CHOICE':
      case 'RADIO':
        return (
          <div className="space-y-2">
            {(question.options || []).map((opt, i) => (
              <label key={i} className="flex items-center gap-2.5 text-xs text-slate-700 dark:text-slate-300">
                <input type="radio" disabled className="text-emerald-600 border-slate-300" />
                <span>{opt.label}</span>
              </label>
            ))}
          </div>
        );

      case 'MULTIPLE_CHOICE':
        return (
          <div className="space-y-2">
            {(question.options || []).map((opt, i) => (
              <label key={i} className="flex items-center gap-2.5 text-xs text-slate-700 dark:text-slate-300">
                <input type="checkbox" disabled className="rounded text-emerald-600 border-slate-300" />
                <span>{opt.label}</span>
              </label>
            ))}
          </div>
        );

      case 'DROPDOWN':
        return (
          <select disabled className="w-full text-xs px-3.5 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-400">
            <option>Select an option...</option>
            {(question.options || []).map((opt, i) => (
              <option key={i}>{opt.label}</option>
            ))}
          </select>
        );

      case 'YES_NO':
        return (
          <div className="flex gap-2">
            <button disabled className="px-4 py-2 text-xs font-semibold rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-600">
              Yes
            </button>
            <button disabled className="px-4 py-2 text-xs font-semibold rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-600">
              No
            </button>
          </div>
        );

      case 'STAR_RATING':
        return (
          <div className="flex gap-2 text-amber-400">
            {[1, 2, 3, 4, 5].map((s) => (
              <Star key={s} className="w-6 h-6 fill-amber-100 dark:fill-amber-950/40 text-amber-400 stroke-1" />
            ))}
          </div>
        );

      case 'RATING':
      case 'LINEAR_SCALE':
        return (
          <div className="flex gap-1.5 overflow-x-auto pb-1">
            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((num) => (
              <div
                key={num}
                className="w-8 h-8 rounded-lg border border-slate-200 dark:border-slate-700 flex items-center justify-center text-xs font-bold text-slate-600 dark:text-slate-400 bg-slate-50 dark:bg-slate-800 shrink-0"
              >
                {num}
              </div>
            ))}
          </div>
        );

      case 'SLIDER':
        return (
          <div className="space-y-1.5">
            <input type="range" disabled className="w-full accent-emerald-600" />
            <div className="flex justify-between text-[10px] text-slate-400">
              <span>0</span>
              <span>50</span>
              <span>100</span>
            </div>
          </div>
        );

      case 'CHECKBOX':
        return (
          <label className="flex items-center gap-2 text-xs text-slate-700 dark:text-slate-300">
            <input type="checkbox" disabled className="rounded text-emerald-600" />
            <span>I agree to the stated terms and conditions</span>
          </label>
        );

      case 'DATE':
      case 'TIME':
      case 'DATETIME':
        return (
          <div className="relative">
            <input
              type="text"
              disabled
              placeholder={question.type === 'TIME' ? 'HH:MM AM/PM' : 'YYYY-MM-DD'}
              className="w-full text-xs px-3.5 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-400"
            />
            {question.type === 'TIME' ? (
              <Clock className="w-4 h-4 absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
            ) : (
              <Calendar className="w-4 h-4 absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
            )}
          </div>
        );

      case 'FILE_UPLOAD':
      case 'IMAGE_UPLOAD':
        return (
          <div className="border-2 border-dashed border-slate-200 dark:border-slate-700 rounded-xl p-4 text-center space-y-1 bg-slate-50/50 dark:bg-slate-800/30">
            <UploadCloud className="w-6 h-6 mx-auto text-slate-400" />
            <p className="text-xs text-slate-500 font-medium">Click or drag files here to upload</p>
            <p className="text-[10px] text-slate-400">Max size: 10MB</p>
          </div>
        );

      case 'SIGNATURE':
        return (
          <div className="border border-slate-200 dark:border-slate-700 rounded-xl h-24 bg-slate-50/50 dark:bg-slate-800/30 flex items-end justify-between p-3">
            <span className="text-[10px] text-slate-400 flex items-center gap-1">
              <PenTool className="w-3.5 h-3.5" />
              Sign above this line
            </span>
            <div className="w-24 border-b border-slate-300 dark:border-slate-600" />
          </div>
        );

      case 'ADDRESS':
        return (
          <div className="space-y-2 text-xs">
            <input disabled placeholder="Street Address" className="w-full p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-400" />
            <div className="grid grid-cols-2 gap-2">
              <input disabled placeholder="City" className="p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-400" />
              <input disabled placeholder="Postal Code" className="p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-400" />
            </div>
          </div>
        );

      case 'SECTION_HEADING':
        return (
          <div className="py-1 border-b border-slate-200 dark:border-slate-700">
            <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Section Break</span>
          </div>
        );

      case 'PARAGRAPH':
        return (
          <p className="text-xs text-slate-500 leading-relaxed italic">
            {question.description || 'Descriptive informative text block for respondents.'}
          </p>
        );

      case 'HIDDEN_FIELD':
        return (
          <div className="p-2 rounded-lg bg-amber-50 dark:bg-amber-950/30 border border-amber-200 dark:border-amber-900 text-[11px] text-amber-700 dark:text-amber-300">
            Hidden tracking field (Passes URL query params into submission metadata)
          </div>
        );

      case 'BUTTON': {
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

        const variantClasses: Record<string, string> = {
          primary:
            'bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-md shadow-emerald-500/20',
          secondary: 'bg-slate-100 text-slate-800 dark:bg-slate-800 dark:text-slate-200',
          outline:
            'border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 bg-white dark:bg-slate-900',
          danger: 'bg-rose-600 text-white shadow-sm shadow-rose-500/20',
        };

        const actionDescriptions: Record<string, string> = {
          SUBMIT: 'Submits form responses',
          NEXT_SECTION: 'Jumps to next form section',
          PREV_SECTION: 'Returns to previous section',
          LINK: btnConfig.buttonUrl ? `Opens ${btnConfig.buttonUrl}` : 'Opens external URL',
          RESET: 'Resets all answers to empty',
        };

        return (
          <div className="space-y-2 py-1">
            <div className={`flex ${alignClasses[align] || 'justify-start'}`}>
              <div
                className={`inline-flex items-center justify-center gap-2 px-5 py-2.5 rounded-xl font-semibold text-xs select-none shadow-xs transition-all ${
                  align === 'full' ? 'w-full' : ''
                } ${variantClasses[variant] || variantClasses.primary}`}
              >
                <span>{label}</span>
                {action === 'NEXT_SECTION' && <ChevronRight className="w-3.5 h-3.5 shrink-0" />}
                {action === 'PREV_SECTION' && <ChevronLeft className="w-3.5 h-3.5 shrink-0" />}
                {action === 'LINK' && <ExternalLink className="w-3.5 h-3.5 shrink-0" />}
                {action === 'RESET' && <RotateCcw className="w-3.5 h-3.5 shrink-0" />}
                {action === 'SUBMIT' && <Send className="w-3.5 h-3.5 shrink-0" />}
              </div>
            </div>
            <div className="flex items-center gap-2 text-[11px] text-slate-400">
              <span className="font-mono bg-slate-100 dark:bg-slate-800 px-1.5 py-0.5 rounded text-[10px] font-semibold text-slate-600 dark:text-slate-300">
                Action: {action}
              </span>
              <span>•</span>
              <span>{actionDescriptions[action] || action}</span>
            </div>
          </div>
        );
      }

      default:
        return null;
    }
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      onClick={() => setSelectedQuestionId(question.id)}
      className={`group relative rounded-xl border bg-white dark:bg-slate-900 p-4 transition-all cursor-pointer shadow-xs ${
        isSelected
          ? 'border-emerald-500 ring-2 ring-emerald-500/20 dark:ring-emerald-500/40'
          : 'border-slate-200 dark:border-slate-800 hover:border-slate-300 dark:hover:border-slate-700'
      }`}
    >
      {/* Top Bar: Drag Handle, Number, Title, Actions */}
      <div className="flex items-start justify-between gap-3 mb-3">
        <div className="flex items-start gap-2.5 flex-1">
          {/* Drag Handle */}
          <button
            {...attributes}
            {...listeners}
            className="cursor-grab active:cursor-grabbing p-1 rounded-md text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors mt-0.5"
            title="Drag to reorder"
          >
            <GripVertical className="w-4 h-4" />
          </button>

          {/* Question Index & Editable Title */}
          <div className="flex-1">
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-slate-400">{index + 1}.</span>
              <input
                type="text"
                value={question.title}
                onChange={(e) => updateQuestion(question.id, { title: e.target.value })}
                className="font-semibold text-sm text-slate-900 dark:text-white bg-transparent border-b border-transparent hover:border-slate-300 focus:border-emerald-500 focus:outline-none w-full"
                placeholder="Question title..."
              />
              {question.isRequired && question.type !== 'BUTTON' && (
                <span className="text-rose-500 text-sm font-bold" title="Required field">*</span>
              )}
            </div>

            {question.description && (
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5 pl-4">
                {question.description}
              </p>
            )}
          </div>
        </div>

        {/* Right Type Badge & Actions */}
        <div className="flex items-center gap-1 shrink-0">
          <Badge variant="secondary" className="text-[10px]">
            {question.type.replace('_', ' ')}
          </Badge>

          <button
            onClick={(e) => {
              e.stopPropagation();
              duplicateQuestion(question.id);
            }}
            className="p-1 rounded-md text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            title="Duplicate question"
          >
            <Copy className="w-3.5 h-3.5" />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              deleteQuestion(question.id);
            }}
            className="p-1 rounded-md text-slate-400 hover:text-rose-600 hover:bg-rose-50 dark:hover:bg-rose-950/40 transition-colors"
            title="Delete question"
          >
            <Trash2 className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* Field Input Dummy Preview */}
      <div className="pl-7">{renderFieldPreview()}</div>
    </div>
  );
};
