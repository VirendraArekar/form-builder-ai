import React, { useState } from 'react';
import { QuestionType } from '@/types';
import { useBuilderStore } from '@/store/builderStore';
import {
  Type,
  AlignLeft,
  Mail,
  Phone,
  Hash,
  Globe,
  Calendar,
  Clock,
  CalendarDays,
  CheckCircle2,
  CheckSquare,
  ChevronDown,
  CircleDot,
  ToggleLeft,
  Star,
  Award,
  Sliders,
  Check,
  UploadCloud,
  Image,
  PenTool,
  MapPin,
  Heading,
  FileText,
  EyeOff,
  Plus,
  Layers,
  Sparkles,
  MousePointerClick,
} from 'lucide-react';

interface QuestionPaletteItem {
  type: QuestionType;
  label: string;
  icon: React.ReactNode;
  category: 'text' | 'choice' | 'rating' | 'datetime' | 'media' | 'layout';
}

const PALETTE_ITEMS: QuestionPaletteItem[] = [
  // Text
  { type: 'SHORT_TEXT', label: 'Short Text', icon: <Type className="w-4 h-4" />, category: 'text' },
  { type: 'LONG_TEXT', label: 'Paragraph / Long Text', icon: <AlignLeft className="w-4 h-4" />, category: 'text' },
  { type: 'EMAIL', label: 'Email Address', icon: <Mail className="w-4 h-4" />, category: 'text' },
  { type: 'PHONE', label: 'Phone Number', icon: <Phone className="w-4 h-4" />, category: 'text' },
  { type: 'NUMBER', label: 'Number', icon: <Hash className="w-4 h-4" />, category: 'text' },
  { type: 'URL', label: 'Website URL', icon: <Globe className="w-4 h-4" />, category: 'text' },

  // Choice
  { type: 'SINGLE_CHOICE', label: 'Single Choice', icon: <CircleDot className="w-4 h-4" />, category: 'choice' },
  { type: 'MULTIPLE_CHOICE', label: 'Multiple Choice', icon: <CheckSquare className="w-4 h-4" />, category: 'choice' },
  { type: 'DROPDOWN', label: 'Dropdown Select', icon: <ChevronDown className="w-4 h-4" />, category: 'choice' },
  { type: 'YES_NO', label: 'Yes / No Toggle', icon: <ToggleLeft className="w-4 h-4" />, category: 'choice' },
  { type: 'CHECKBOX', label: 'Agreement Checkbox', icon: <Check className="w-4 h-4" />, category: 'choice' },
  { type: 'RADIO', label: 'Radio Buttons', icon: <CircleDot className="w-4 h-4" />, category: 'choice' },

  // Rating
  { type: 'STAR_RATING', label: 'Star Rating', icon: <Star className="w-4 h-4" />, category: 'rating' },
  { type: 'RATING', label: 'Score Rating (1-5)', icon: <Award className="w-4 h-4" />, category: 'rating' },
  { type: 'LINEAR_SCALE', label: 'Linear Scale (0-10)', icon: <Sliders className="w-4 h-4" />, category: 'rating' },
  { type: 'SLIDER', label: 'Continuous Slider', icon: <Sliders className="w-4 h-4" />, category: 'rating' },

  // Date & Time
  { type: 'DATE', label: 'Date Picker', icon: <Calendar className="w-4 h-4" />, category: 'datetime' },
  { type: 'TIME', label: 'Time Picker', icon: <Clock className="w-4 h-4" />, category: 'datetime' },
  { type: 'DATETIME', label: 'Date & Time', icon: <CalendarDays className="w-4 h-4" />, category: 'datetime' },

  // Media & Specialized
  { type: 'FILE_UPLOAD', label: 'File Upload', icon: <UploadCloud className="w-4 h-4" />, category: 'media' },
  { type: 'IMAGE_UPLOAD', label: 'Image Upload', icon: <Image className="w-4 h-4" />, category: 'media' },
  { type: 'SIGNATURE', label: 'Digital Signature', icon: <PenTool className="w-4 h-4" />, category: 'media' },
  { type: 'ADDRESS', label: 'Full Postal Address', icon: <MapPin className="w-4 h-4" />, category: 'media' },

  // Layout & Actions
  { type: 'BUTTON', label: 'Action Button / CTA', icon: <MousePointerClick className="w-4 h-4" />, category: 'layout' },
  { type: 'SECTION_HEADING', label: 'Section Heading', icon: <Heading className="w-4 h-4" />, category: 'layout' },
  { type: 'PARAGRAPH', label: 'Descriptive Text', icon: <FileText className="w-4 h-4" />, category: 'layout' },
  { type: 'HIDDEN_FIELD', label: 'Hidden Field', icon: <EyeOff className="w-4 h-4" />, category: 'layout' },
];

export const QuestionPalette: React.FC = () => {
  const { addQuestion, addSection } = useBuilderStore();
  const [filter, setFilter] = useState<string>('all');

  const categories = [
    { id: 'all', label: 'All Fields' },
    { id: 'text', label: 'Text' },
    { id: 'choice', label: 'Choices' },
    { id: 'rating', label: 'Ratings' },
    { id: 'datetime', label: 'Date/Time' },
    { id: 'media', label: 'Media' },
    { id: 'layout', label: 'Layout' },
  ];

  const filteredItems = filter === 'all'
    ? PALETTE_ITEMS
    : PALETTE_ITEMS.filter((item) => item.category === filter);

  return (
    <div className="flex flex-col h-full bg-white dark:bg-slate-900 border-r border-slate-200 dark:border-slate-800 w-64 shrink-0 select-none">
      <div className="p-3.5 border-b border-slate-100 dark:border-slate-800">
        <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400 mb-2">
          Add Questions
        </h3>
        <div className="flex flex-wrap gap-1">
          {categories.map((cat) => (
            <button
              key={cat.id}
              onClick={() => setFilter(cat.id)}
              className={`px-2 py-0.5 text-[11px] rounded-md font-medium transition-colors ${
                filter === cat.id
                  ? 'bg-emerald-600 text-white'
                  : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-slate-200'
              }`}
            >
              {cat.label}
            </button>
          ))}
        </div>
      </div>

      {/* Palette list */}
      <div className="flex-1 overflow-y-auto p-3 space-y-1.5 scrollbar-thin">
        {filteredItems.map((item) => (
          <button
            key={item.type}
            onClick={() => addQuestion(item.type)}
            className="w-full flex items-center justify-between p-2 rounded-lg text-left text-xs font-medium text-slate-700 dark:text-slate-300 hover:bg-emerald-50 dark:hover:bg-emerald-950/40 hover:text-emerald-600 dark:hover:text-emerald-400 border border-transparent hover:border-emerald-200 dark:hover:border-emerald-900/60 transition-all group"
          >
            <div className="flex items-center gap-2.5">
              <span className="p-1.5 rounded-md bg-slate-100 dark:bg-slate-800 text-slate-500 group-hover:bg-emerald-100 dark:group-hover:bg-emerald-900/50 group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors">
                {item.icon}
              </span>
              <span>{item.label}</span>
            </div>
            <Plus className="w-3.5 h-3.5 opacity-0 group-hover:opacity-100 text-emerald-500 transition-opacity" />
          </button>
        ))}
      </div>

      {/* Add Section Action */}
      <div className="p-3 border-t border-slate-100 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/50">
        <button
          onClick={() => addSection('New Page / Section')}
          className="w-full flex items-center justify-center gap-2 py-2 px-3 rounded-lg text-xs font-semibold text-slate-700 dark:text-slate-300 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 hover:border-emerald-500 hover:text-emerald-600 transition-all shadow-xs"
        >
          <Layers className="w-3.5 h-3.5 text-emerald-500" />
          Add Form Section
        </button>
      </div>
    </div>
  );
};
