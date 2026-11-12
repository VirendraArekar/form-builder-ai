import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Search,
  Plus,
  Sparkles,
  LayoutDashboard,
  FileText,
  TrendingUp,
  Inbox,
  LayoutTemplate,
  Settings,
  Sun,
  Moon,
  ExternalLink,
  X,
  ArrowRight,
} from 'lucide-react';
import { useThemeStore } from '@/store/themeStore';
import { formService } from '@/services/formService';
import { useAuthStore } from '@/store/authStore';

interface CommandPaletteProps {
  isOpen: boolean;
  onClose: () => void;
}

export const CommandPalette: React.FC<CommandPaletteProps> = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const { isDark, toggleTheme } = useThemeStore();
  const { currentWorkspace } = useAuthStore();
  const [query, setQuery] = useState('');
  const [matchingForms, setMatchingForms] = useState<any[]>([]);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isOpen) {
      setQuery('');
      setSelectedIndex(0);
      setTimeout(() => inputRef.current?.focus(), 50);
    }
  }, [isOpen]);

  useEffect(() => {
    if (!query.trim() || !currentWorkspace?.id) {
      setMatchingForms([]);
      return;
    }

    const timer = setTimeout(async () => {
      try {
        const res = await formService.listForms({
          workspaceId: currentWorkspace.id,
          search: query.trim(),
          size: 5,
        });
        setMatchingForms(res.content || []);
      } catch (err) {
        // silent catch
      }
    }, 200);

    return () => clearTimeout(timer);
  }, [query, currentWorkspace?.id]);

  const defaultActions = [
    {
      id: 'create-ai',
      title: 'Generate Form with AI',
      subtitle: 'Describe your requirements and let Gemini AI build it',
      icon: <Sparkles className="w-4 h-4 text-emerald-500" />,
      action: () => {
        onClose();
        navigate('/generate-ai');
      },
    },
    {
      id: 'create-blank',
      title: 'Create Blank Form',
      subtitle: 'Start with an empty canvas in the drag & drop builder',
      icon: <Plus className="w-4 h-4 text-emerald-600" />,
      action: async () => {
        onClose();
        if (currentWorkspace?.id) {
          try {
            const f = await formService.createForm({
              workspaceId: currentWorkspace.id,
              title: 'Untitled Form',
            });
            navigate(`/builder/${f.id}`);
          } catch (e) {
            navigate('/forms');
          }
        }
      },
    },
    {
      id: 'nav-dashboard',
      title: 'Go to Dashboard',
      subtitle: 'View high-level overview, stats and recent activity',
      icon: <LayoutDashboard className="w-4 h-4 text-slate-500" />,
      action: () => {
        onClose();
        navigate('/dashboard');
      },
    },
    {
      id: 'nav-forms',
      title: 'Go to My Forms',
      subtitle: 'Browse, filter, and manage all your forms',
      icon: <FileText className="w-4 h-4 text-slate-500" />,
      action: () => {
        onClose();
        navigate('/forms');
      },
    },
    {
      id: 'nav-inbox',
      title: 'Go to Unified Inbox',
      subtitle: 'Review incoming submissions across all forms in real-time',
      icon: <Inbox className="w-4 h-4 text-teal-500" />,
      action: () => {
        onClose();
        navigate('/inbox');
      },
    },
    {
      id: 'nav-insights',
      title: 'Go to Workspace Insights',
      subtitle: 'Explore analytics, submission trends, and AI synthesis',
      icon: <TrendingUp className="w-4 h-4 text-emerald-500" />,
      action: () => {
        onClose();
        navigate('/insights');
      },
    },
    {
      id: 'nav-templates',
      title: 'Browse Template Gallery',
      subtitle: 'Choose from pre-built feedback, survey, and intake forms',
      icon: <LayoutTemplate className="w-4 h-4 text-slate-500" />,
      action: () => {
        onClose();
        navigate('/templates');
      },
    },
    {
      id: 'toggle-theme',
      title: isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode',
      subtitle: 'Toggle theme display appearance',
      icon: isDark ? <Sun className="w-4 h-4 text-amber-500" /> : <Moon className="w-4 h-4 text-emerald-500" />,
      action: () => {
        toggleTheme();
        onClose();
      },
    },
    {
      id: 'nav-settings',
      title: 'Open Settings',
      subtitle: 'Manage profile, security credentials, and preferences',
      icon: <Settings className="w-4 h-4 text-slate-500" />,
      action: () => {
        onClose();
        navigate('/settings');
      },
    },
  ];

  const filteredActions = query.trim()
    ? defaultActions.filter(
        (a) =>
          a.title.toLowerCase().includes(query.toLowerCase()) ||
          a.subtitle.toLowerCase().includes(query.toLowerCase())
      )
    : defaultActions;

  const totalItems = matchingForms.length + filteredActions.length;

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setSelectedIndex((prev) => (prev + 1) % Math.max(1, totalItems));
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setSelectedIndex((prev) => (prev - 1 + Math.max(1, totalItems)) % Math.max(1, totalItems));
    } else if (e.key === 'Enter') {
      e.preventDefault();
      if (selectedIndex < matchingForms.length) {
        const form = matchingForms[selectedIndex];
        onClose();
        navigate(`/builder/${form.id}`);
      } else {
        const actionIdx = selectedIndex - matchingForms.length;
        if (filteredActions[actionIdx]) {
          filteredActions[actionIdx].action();
        }
      }
    } else if (e.key === 'Escape') {
      onClose();
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-start justify-center pt-20 px-4">
      {/* Backdrop */}
      <div
        className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm transition-opacity"
        onClick={onClose}
      />

      {/* Modal Dialog */}
      <div
        className="relative w-full max-w-xl rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-2xl overflow-hidden z-10 animate-in fade-in zoom-in-95 duration-150"
        onKeyDown={handleKeyDown}
      >
        {/* Search Input Bar */}
        <div className="flex items-center px-4 py-3.5 border-b border-slate-100 dark:border-slate-800 gap-3">
          <Search className="w-5 h-5 text-emerald-500 shrink-0" />
          <input
            ref={inputRef}
            type="text"
            value={query}
            onChange={(e) => {
              setQuery(e.target.value);
              setSelectedIndex(0);
            }}
            placeholder="Type a command or search forms..."
            className="flex-1 bg-transparent text-sm text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none"
          />
          <kbd className="hidden sm:inline-flex items-center gap-0.5 px-2 py-0.5 text-[10px] font-bold text-slate-400 bg-slate-100 dark:bg-slate-800 rounded border border-slate-200 dark:border-slate-700">
            ESC
          </kbd>
        </div>

        {/* Results Body */}
        <div className="max-h-96 overflow-y-auto p-2 space-y-1">
          {/* Matching Forms Section */}
          {matchingForms.length > 0 && (
            <div className="mb-2">
              <div className="px-3 py-1 text-[11px] font-bold uppercase tracking-wider text-emerald-600 dark:text-emerald-400">
                Matching Forms
              </div>
              {matchingForms.map((form, idx) => {
                const isSelected = selectedIndex === idx;
                return (
                  <button
                    key={form.id}
                    onClick={() => {
                      onClose();
                      navigate(`/builder/${form.id}`);
                    }}
                    className={`w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-left transition-all ${
                      isSelected
                        ? 'bg-emerald-50 dark:bg-emerald-950/50 text-emerald-900 dark:text-emerald-200'
                        : 'text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800/60'
                    }`}
                  >
                    <div className="flex items-center gap-3 truncate">
                      <div className="w-7 h-7 rounded-lg bg-emerald-500/10 text-emerald-600 flex items-center justify-center shrink-0">
                        <FileText className="w-4 h-4" />
                      </div>
                      <div className="truncate">
                        <p className="text-xs font-bold truncate">{form.title}</p>
                        <p className="text-[11px] text-slate-400 truncate">
                          {form.submissionCount} submissions • {form.status}
                        </p>
                      </div>
                    </div>
                    <ArrowRight className="w-3.5 h-3.5 text-slate-400 shrink-0" />
                  </button>
                );
              })}
            </div>
          )}

          {/* Quick Actions Section */}
          <div>
            {matchingForms.length > 0 && (
              <div className="px-3 py-1 text-[11px] font-bold uppercase tracking-wider text-slate-400">
                Actions & Navigation
              </div>
            )}
            {filteredActions.length === 0 && matchingForms.length === 0 ? (
              <div className="text-center py-8 text-slate-400 text-xs">
                No commands or forms found for "{query}"
              </div>
            ) : (
              filteredActions.map((action, idx) => {
                const actualIndex = matchingForms.length + idx;
                const isSelected = selectedIndex === actualIndex;
                return (
                  <button
                    key={action.id}
                    onClick={action.action}
                    className={`w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-left transition-all ${
                      isSelected
                        ? 'bg-emerald-50 dark:bg-emerald-950/50 text-emerald-900 dark:text-emerald-200'
                        : 'text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800/60'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-7 h-7 rounded-lg bg-slate-100 dark:bg-slate-800 flex items-center justify-center shrink-0">
                        {action.icon}
                      </div>
                      <div>
                        <p className="text-xs font-bold text-slate-900 dark:text-slate-100">{action.title}</p>
                        <p className="text-[11px] text-slate-400">{action.subtitle}</p>
                      </div>
                    </div>
                    <kbd className="hidden sm:inline-flex text-[10px] text-slate-400">↵</kbd>
                  </button>
                );
              })
            )}
          </div>
        </div>

        {/* Footer shortcuts */}
        <div className="px-4 py-2 bg-slate-50 dark:bg-slate-950/60 border-t border-slate-100 dark:border-slate-800 text-[11px] text-slate-400 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <span>↑↓ Navigate</span>
            <span>↵ Select</span>
            <span>ESC Close</span>
          </div>
          <span className="text-[10px] font-semibold text-emerald-600 dark:text-emerald-400">
            Timely Forms AI
          </span>
        </div>
      </div>
    </div>
  );
};
