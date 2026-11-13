import React from 'react';
import { useBuilderStore } from '@/store/builderStore';
import { Input } from '@/components/ui/Input';
import { Palette, Type, LayoutTemplate, Image } from 'lucide-react';

const PRESET_COLORS = [
  '#4f46e5', // Indigo
  '#7c3aed', // Violet
  '#2563eb', // Electric Blue
  '#0d9488', // Teal
  '#059669', // Emerald
  '#e11d48', // Rose
  '#d97706', // Amber
  '#0f172a', // Slate/Dark
];

const FONTS = [
  { id: 'Inter', label: 'Inter (Modern Sans)' },
  { id: 'Plus Jakarta Sans', label: 'Plus Jakarta Sans' },
  { id: 'Outfit', label: 'Outfit (Geometric)' },
  { id: 'Poppins', label: 'Poppins (Friendly)' },
  { id: 'Roboto', label: 'Roboto' },
  { id: 'Playfair Display', label: 'Playfair Display (Editorial Serif)' },
];

export const ThemeEditor: React.FC = () => {
  const { form, updateTheme } = useBuilderStore();

  const theme = form?.theme || {
    primaryColor: '#4f46e5',
    backgroundColor: '#ffffff',
    fontFamily: 'Inter',
    borderRadius: 'rounded-xl',
  };

  return (
    <div className="flex flex-col h-full bg-white dark:bg-slate-900 border-l border-slate-200 dark:border-slate-800 w-80 shrink-0 overflow-y-auto scrollbar-thin">
      <div className="p-4 border-b border-slate-100 dark:border-slate-800">
        <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">
          Theme & Styling
        </h3>
        <p className="text-[11px] text-slate-500 mt-0.5">
          Customize the aesthetic, brand colors, and typography of your form.
        </p>
      </div>

      <div className="p-4 space-y-6 flex-1">
        {/* Accent Color */}
        <div className="space-y-2.5">
          <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <Palette className="w-3.5 h-3.5" />
            Brand Accent Color
          </label>
          <div className="flex items-center gap-2">
            <input
              type="color"
              value={theme.primaryColor || '#4f46e5'}
              onChange={(e) => updateTheme({ primaryColor: e.target.value })}
              className="w-9 h-9 rounded-lg cursor-pointer border border-slate-200 dark:border-slate-700 bg-transparent"
            />
            <input
              type="text"
              value={theme.primaryColor || '#4f46e5'}
              onChange={(e) => updateTheme({ primaryColor: e.target.value })}
              className="flex-1 text-xs px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 font-mono text-slate-900 dark:text-white"
            />
          </div>

          <div className="flex items-center gap-1.5 pt-1">
            {PRESET_COLORS.map((color) => (
              <button
                key={color}
                onClick={() => updateTheme({ primaryColor: color })}
                style={{ backgroundColor: color }}
                className={`w-6 h-6 rounded-full border-2 transition-transform hover:scale-110 ${
                  theme.primaryColor === color ? 'border-emerald-600 dark:border-white scale-110 shadow-sm' : 'border-transparent'
                }`}
              />
            ))}
          </div>
        </div>

        {/* Font Family */}
        <div className="space-y-2 pt-2 border-t border-slate-100 dark:border-slate-800">
          <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <Type className="w-3.5 h-3.5" />
            Typography
          </label>
          <select
            value={theme.fontFamily || 'Inter'}
            onChange={(e) => updateTheme({ fontFamily: e.target.value })}
            className="w-full text-xs p-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white"
          >
            {FONTS.map((f) => (
              <option key={f.id} value={f.id}>
                {f.label}
              </option>
            ))}
          </select>
        </div>

        {/* Corner Radius */}
        <div className="space-y-2 pt-2 border-t border-slate-100 dark:border-slate-800">
          <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <LayoutTemplate className="w-3.5 h-3.5" />
            Corner Radius
          </label>
          <div className="grid grid-cols-2 gap-2 text-xs">
            {[
              { id: 'rounded-none', label: 'Sharp' },
              { id: 'rounded-md', label: 'Subtle (6px)' },
              { id: 'rounded-xl', label: 'Standard (12px)' },
              { id: 'rounded-2xl', label: 'Smooth (16px)' },
            ].map((r) => (
              <button
                key={r.id}
                onClick={() => updateTheme({ borderRadius: r.id })}
                className={`p-2 rounded-lg border text-center font-medium transition-all ${
                  theme.borderRadius === r.id
                    ? 'border-emerald-600 bg-emerald-50 dark:bg-emerald-950/40 text-emerald-600 dark:text-emerald-400 font-bold'
                    : 'border-slate-200 dark:border-slate-700 hover:border-slate-300'
                }`}
              >
                {r.label}
              </button>
            ))}
          </div>
        </div>

        {/* Logo / Header Image */}
        <div className="space-y-3 pt-2 border-t border-slate-100 dark:border-slate-800">
          <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <Image className="w-3.5 h-3.5" />
            Branding Images
          </label>
          <Input
            label="Logo URL"
            placeholder="https://example.com/logo.png"
            value={theme.logoUrl || ''}
            onChange={(e) => updateTheme({ logoUrl: e.target.value })}
          />
          <Input
            label="Cover Header Banner URL"
            placeholder="https://images.unsplash.com/..."
            value={theme.coverImageUrl || ''}
            onChange={(e) => updateTheme({ coverImageUrl: e.target.value })}
          />
        </div>
      </div>
    </div>
  );
};
