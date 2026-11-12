import React, { useState, useEffect } from 'react';
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard,
  FileText,
  TrendingUp,
  Inbox,
  LayoutTemplate,
  Settings,
  Bell,
  Sun,
  Moon,
  LogOut,
  ChevronDown,
  Menu,
  X,
  Search,
  Sparkles,
  Command,
  User as UserIcon,
  Check,
  PanelLeftClose,
  PanelLeftOpen,
  Plus,
  CheckCircle2,
  FileCode,
  Layers,
  ArrowRight,
  Globe,
  Clock,
} from 'lucide-react';
import { useAuthStore } from '@/store/authStore';
import { useThemeStore } from '@/store/themeStore';
import { formService } from '@/services/formService';
import { workspaceService } from '@/services/workspaceService';
import { CommandPalette } from '@/components/CommandPalette';
import { Modal } from '@/components/ui/Modal';
import { Button } from '@/components/ui/Button';
import { useToast } from '@/components/ui/Toast';

export interface DashboardLayoutProps {
  children?: React.ReactNode;
}

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, currentWorkspace, setCurrentWorkspace, logout } = useAuthStore();
  const { isDark, toggleTheme } = useThemeStore();
  const { showToast } = useToast();

  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [sidebarExpanded, setSidebarExpanded] = useState(false);
  const [sidebarHovered, setSidebarHovered] = useState(false);
  const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);
  const [commandPaletteOpen, setCommandPaletteOpen] = useState(false);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [isCreatingForm, setIsCreatingForm] = useState(false);

  // Global keyboard shortcut for Cmd+K / Ctrl+K
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key.toLowerCase() === 'k') {
        e.preventDefault();
        setCommandPaletteOpen((prev) => !prev);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  const handleCreateScratch = async () => {
    try {
      setIsCreatingForm(true);
      let wsId = currentWorkspace?.id;
      if (!wsId) {
        const workspaces = await workspaceService.listWorkspaces();
        if (workspaces && workspaces.length > 0) {
          wsId = workspaces[0].id;
          setCurrentWorkspace(workspaces[0]);
        }
      }
      if (!wsId) {
        showToast('Please select or create a workspace first.', 'error');
        return;
      }
      const newForm = await formService.createForm({
        workspaceId: wsId,
        title: 'Untitled Form',
        description: 'Collect responses effortlessly',
      });
      setCreateModalOpen(false);
      showToast('Form created! Opening visual builder...', 'success');
      navigate(`/builder/${newForm.id}`);
    } catch (err: any) {
      showToast(err.message || 'Failed to create form', 'error');
    } finally {
      setIsCreatingForm(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isExpanded = sidebarExpanded || sidebarHovered;

  // Navigation Items with categories
  const navSections = [
    {
      title: 'Overview',
      items: [
        {
          label: 'Dashboard',
          path: '/dashboard',
          icon: <LayoutDashboard className="w-4 h-4" />,
          isActive: location.pathname === '/dashboard' || location.pathname === '/',
        },
      ],
    },
    {
      title: 'Forms',
      items: [
        {
          label: 'All Forms',
          path: '/forms',
          icon: <FileText className="w-4 h-4" />,
          isActive: location.pathname === '/forms' && (!location.search || location.search.includes('status=ALL')),
        },
        {
          label: 'Published',
          path: '/forms?status=PUBLISHED',
          icon: <Globe className="w-4 h-4" />,
          badge: 'Live',
          badgeColor: 'bg-emerald-100 text-emerald-700 dark:bg-emerald-950 dark:text-emerald-300',
          isActive: location.pathname === '/forms' && location.search.includes('status=PUBLISHED'),
        },
        {
          label: 'Draft',
          path: '/forms?status=DRAFT',
          icon: <Clock className="w-4 h-4" />,
          isActive: location.pathname === '/forms' && location.search.includes('status=DRAFT'),
        },
      ],
    },
    {
      title: 'AI & Templates',
      items: [
        {
          label: 'AI Generator',
          path: '/generate-ai',
          icon: <Sparkles className="w-4 h-4" />,
          badge: 'AI',
          badgeColor: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border border-emerald-500/20',
          isActive: location.pathname.startsWith('/generate-ai'),
        },
        {
          label: 'Templates',
          path: '/templates',
          icon: <LayoutTemplate className="w-4 h-4" />,
          isActive: location.pathname.startsWith('/templates'),
        },
      ],
    },
    {
      title: 'Analytics & Submissions',
      items: [
        {
          label: 'Inbox',
          path: '/inbox',
          icon: <Inbox className="w-4 h-4" />,
          isActive: location.pathname.startsWith('/inbox'),
        },
        {
          label: 'Insights',
          path: '/insights',
          icon: <TrendingUp className="w-4 h-4" />,
          isActive: location.pathname.startsWith('/insights'),
        },
      ],
    },
    {
      title: 'Preferences',
      items: [
        {
          label: 'Settings',
          path: '/settings',
          icon: <Settings className="w-4 h-4" />,
          isActive: location.pathname.startsWith('/settings'),
        },
      ],
    },
  ];

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex flex-col md:flex-row text-slate-900 dark:text-slate-100 font-sans selection:bg-emerald-500 selection:text-white">
      {/* Command Palette Modal */}
      <CommandPalette
        isOpen={commandPaletteOpen}
        onClose={() => setCommandPaletteOpen(false)}
      />

      {/* Create Form Modal */}
      <Modal
        isOpen={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        title="Create New Form"
        description="Choose how you would like to build your new form"
        maxWidth="lg"
      >
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-2">
          {/* Option 1: Scratch */}
          <button
            onClick={handleCreateScratch}
            disabled={isCreatingForm}
            className="flex flex-col items-start p-5 rounded-2xl border-2 border-slate-200 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 bg-white dark:bg-slate-900 text-left transition-all hover:shadow-md group cursor-pointer"
          >
            <div className="w-12 h-12 rounded-xl bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-200 group-hover:bg-emerald-100 dark:group-hover:bg-emerald-950/60 group-hover:text-emerald-600 dark:group-hover:text-emerald-400 flex items-center justify-center mb-4 transition-colors">
              <FileCode className="w-6 h-6" />
            </div>
            <h4 className="font-bold text-sm text-slate-900 dark:text-white mb-1">
              Start from Scratch
            </h4>
            <p className="text-xs text-slate-500 dark:text-slate-400 leading-relaxed flex-1">
              Build a custom blank form question by question using our visual drag-and-drop canvas.
            </p>
            <div className="mt-4 pt-3 border-t border-slate-100 dark:border-slate-800/80 w-full flex items-center justify-between text-xs font-semibold text-emerald-600 dark:text-emerald-400">
              <span>{isCreatingForm ? 'Creating...' : 'Create Blank'}</span>
              <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
            </div>
          </button>

          {/* Option 2: AI Generator */}
          <button
            onClick={() => {
              setCreateModalOpen(false);
              navigate('/generate-ai');
            }}
            className="flex flex-col items-start p-5 rounded-2xl border-2 border-emerald-500/40 bg-gradient-to-b from-emerald-500/5 via-teal-500/5 to-transparent hover:border-emerald-500 dark:hover:border-emerald-400 text-left transition-all hover:shadow-md group cursor-pointer relative overflow-hidden"
          >
            <span className="absolute top-3 right-3 text-[10px] font-extrabold uppercase px-2 py-0.5 rounded-full bg-gradient-to-r from-emerald-500 to-teal-500 text-white shadow-xs">
              AI Power
            </span>
            <div className="w-12 h-12 rounded-xl bg-gradient-to-tr from-emerald-500 to-teal-600 text-white flex items-center justify-center mb-4 shadow-sm shadow-emerald-500/20">
              <Sparkles className="w-6 h-6 animate-pulse" />
            </div>
            <h4 className="font-bold text-sm text-slate-900 dark:text-white mb-1">
              Generate with AI
            </h4>
            <p className="text-xs text-slate-500 dark:text-slate-400 leading-relaxed flex-1">
              Describe your form in plain English and let Google Gemini AI architect fields, rules, and themes.
            </p>
            <div className="mt-4 pt-3 border-t border-emerald-500/20 w-full flex items-center justify-between text-xs font-semibold text-emerald-600 dark:text-emerald-400">
              <span>Prompt Builder</span>
              <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
            </div>
          </button>

          {/* Option 3: Templates */}
          <button
            onClick={() => {
              setCreateModalOpen(false);
              navigate('/templates');
            }}
            className="flex flex-col items-start p-5 rounded-2xl border-2 border-slate-200 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 bg-white dark:bg-slate-900 text-left transition-all hover:shadow-md group cursor-pointer"
          >
            <div className="w-12 h-12 rounded-xl bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-200 group-hover:bg-teal-100 dark:group-hover:bg-teal-950/60 group-hover:text-teal-600 dark:group-hover:text-teal-400 flex items-center justify-center mb-4 transition-colors">
              <LayoutTemplate className="w-6 h-6" />
            </div>
            <h4 className="font-bold text-sm text-slate-900 dark:text-white mb-1">
              Use a Template
            </h4>
            <p className="text-xs text-slate-500 dark:text-slate-400 leading-relaxed flex-1">
              Pick from curated SaaS templates for customer feedback, surveys, registrations, and job intake.
            </p>
            <div className="mt-4 pt-3 border-t border-slate-100 dark:border-slate-800/80 w-full flex items-center justify-between text-xs font-semibold text-teal-600 dark:text-teal-400">
              <span>Browse 20+</span>
              <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
            </div>
          </button>
        </div>
      </Modal>

      {/* Mobile Top Header */}
      <div className="md:hidden flex items-center justify-between px-4 py-3 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 sticky top-0 z-50">
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-xl bg-gradient-to-tr from-emerald-500 via-teal-500 to-green-600 flex items-center justify-center text-white font-extrabold text-sm shadow-md shadow-emerald-500/20">
            T
          </div>
          <span className="font-extrabold text-base bg-gradient-to-r from-emerald-600 via-teal-600 to-green-600 dark:from-emerald-400 dark:to-teal-300 bg-clip-text text-transparent">
            Timely Forms AI
          </span>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={() => setCreateModalOpen(true)}
            className="p-2 text-white bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 rounded-lg shadow-sm"
            title="Create Form"
          >
            <Plus className="w-4 h-4" />
          </button>
          <button
            onClick={() => setCommandPaletteOpen(true)}
            className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-lg"
          >
            <Search className="w-4 h-4" />
          </button>
          <button
            onClick={toggleTheme}
            className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-lg"
          >
            {isDark ? <Sun className="w-4 h-4 text-amber-400" /> : <Moon className="w-4 h-4" />}
          </button>
          <button
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            className="p-2 text-slate-600 dark:text-slate-300 rounded-lg"
          >
            {mobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
          </button>
        </div>
      </div>

      {/* Desktop Collapsible Left Sidebar */}
      <aside
        onMouseEnter={() => setSidebarHovered(true)}
        onMouseLeave={() => setSidebarHovered(false)}
        className={`${
          mobileMenuOpen ? 'block' : 'hidden'
        } md:flex flex-col bg-white dark:bg-slate-900 border-r border-slate-200/80 dark:border-slate-800/80 shrink-0 z-40 h-auto md:h-screen md:sticky md:top-0 transition-all duration-300 ease-in-out ${
          isExpanded ? 'md:w-64' : 'md:w-20'
        }`}
      >
        {/* Brand / Logo */}
        <div className="h-16 px-4 border-b border-slate-100 dark:border-slate-800/60 flex items-center justify-between">
          <Link to="/dashboard" className="flex items-center gap-3 overflow-hidden">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-emerald-500 via-teal-500 to-green-600 flex items-center justify-center text-white font-extrabold text-lg shadow-md shadow-emerald-500/25 shrink-0">
              T
            </div>
            {isExpanded && (
              <div className="truncate transition-opacity duration-200">
                <div className="font-extrabold text-sm tracking-tight flex items-center gap-1.5">
                  <span className="text-slate-900 dark:text-white">Timely Forms</span>
                  <span className="text-[10px] font-bold px-1.5 py-0.2 rounded bg-emerald-100 dark:bg-emerald-950 text-emerald-600 dark:text-emerald-400 border border-emerald-200 dark:border-emerald-800">
                    AI
                  </span>
                </div>
                <p className="text-[10px] text-slate-400 font-medium truncate">Smart Form Platform</p>
              </div>
            )}
          </Link>

          {isExpanded && (
            <button
              onClick={() => setSidebarExpanded(!sidebarExpanded)}
              className="p-1.5 rounded-lg text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors hidden md:block"
              title={sidebarExpanded ? 'Collapse Sidebar' : 'Pin Expanded'}
            >
              {sidebarExpanded ? <PanelLeftClose className="w-4 h-4" /> : <PanelLeftOpen className="w-4 h-4" />}
            </button>
          )}
        </div>

        {/* Primary Action Button: Create Form */}
        <div className="p-3 border-b border-slate-100 dark:border-slate-800/60">
          <button
            onClick={() => setCreateModalOpen(true)}
            className={`w-full flex items-center justify-center rounded-xl bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 hover:from-emerald-600 hover:to-teal-600 text-white font-bold shadow-md shadow-emerald-500/20 active:scale-[0.98] transition-all cursor-pointer ${
              isExpanded ? 'py-2.5 px-3.5 gap-2 text-xs' : 'py-2.5 px-2'
            }`}
            title="Create a Form"
          >
            <Plus className="w-4 h-4 shrink-0" />
            {isExpanded && <span className="truncate">Create a Form</span>}
          </button>
        </div>

        {/* Navigation Sections */}
        <nav className="flex-1 px-3 py-3 space-y-4 overflow-y-auto overflow-x-hidden scrollbar-thin">
          {navSections.map((section, sIdx) => (
            <div key={sIdx} className="space-y-1">
              {isExpanded && (
                <div className="px-3 pt-1 pb-1 text-[10px] font-bold uppercase tracking-wider text-slate-400 dark:text-slate-500">
                  {section.title}
                </div>
              )}
              {section.items.map((item) => (
                <Link
                  key={item.path}
                  to={item.path}
                  onClick={() => setMobileMenuOpen(false)}
                  title={!isExpanded ? item.label : undefined}
                  className={`flex items-center rounded-xl text-xs font-semibold transition-all duration-200 ${
                    isExpanded ? 'px-3 py-2 gap-2.5' : 'justify-center py-2.5 px-2'
                  } ${
                    item.isActive
                      ? 'bg-gradient-to-r from-emerald-500 via-teal-500 to-green-600 text-white shadow-md shadow-emerald-500/20 font-bold'
                      : 'text-slate-600 dark:text-slate-400 hover:bg-slate-100/80 dark:hover:bg-slate-800/60 hover:text-slate-900 dark:hover:text-slate-100'
                  }`}
                >
                  <div className="shrink-0">{item.icon}</div>
                  {isExpanded && (
                    <div className="flex-1 flex items-center justify-between min-w-0">
                      <span className="truncate">{item.label}</span>
                      {item.badge && (
                        <span
                          className={`text-[9px] font-bold px-1.5 py-0.2 rounded-md ${
                            item.badgeColor || 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400'
                          }`}
                        >
                          {item.badge}
                        </span>
                      )}
                    </div>
                  )}
                </Link>
              ))}
            </div>
          ))}
        </nav>

        {/* User Profile Footer */}
        <div className="p-3 border-t border-slate-100 dark:border-slate-800/60">
          <div className="relative">
            <button
              onClick={() => setProfileDropdownOpen(!profileDropdownOpen)}
              className={`w-full flex items-center rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/60 transition-colors ${
                isExpanded ? 'p-2 gap-2.5 text-left' : 'p-2 justify-center'
              }`}
            >
              <div className="w-8 h-8 rounded-xl bg-gradient-to-tr from-emerald-600 to-teal-500 flex items-center justify-center text-white font-bold text-xs uppercase shadow-sm shrink-0">
                {user?.firstName?.charAt(0) || user?.email?.charAt(0) || 'U'}
              </div>
              {isExpanded && (
                <div className="truncate flex-1">
                  <div className="text-xs font-bold text-slate-900 dark:text-slate-100 truncate">
                    {user?.firstName ? `${user.firstName} ${user.lastName || ''}` : user?.email}
                  </div>
                  <div className="text-[10px] text-slate-400 truncate">{user?.email}</div>
                </div>
              )}
            </button>

            {/* Profile Dropdown */}
            {profileDropdownOpen && (
              <>
                <div
                  className="fixed inset-0 z-30"
                  onClick={() => setProfileDropdownOpen(false)}
                />
                <div className="absolute bottom-full left-0 mb-2 w-56 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-xl p-1.5 z-40 text-xs font-semibold">
                  <div className="px-3 py-2 border-b border-slate-100 dark:border-slate-800">
                    <p className="font-bold text-slate-900 dark:text-white truncate">
                      {user?.firstName} {user?.lastName}
                    </p>
                    <p className="text-[11px] text-slate-400 truncate">{user?.email}</p>
                  </div>
                  <Link
                    to="/profile"
                    onClick={() => setProfileDropdownOpen(false)}
                    className="flex items-center gap-2 px-3 py-2 rounded-lg text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
                  >
                    <UserIcon className="w-3.5 h-3.5 text-slate-400" />
                    Profile & Account
                  </Link>
                  <Link
                    to="/settings"
                    onClick={() => setProfileDropdownOpen(false)}
                    className="flex items-center gap-2 px-3 py-2 rounded-lg text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
                  >
                    <Settings className="w-3.5 h-3.5 text-slate-400" />
                    Preferences
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center gap-2 px-3 py-2 rounded-lg text-rose-600 dark:text-rose-400 hover:bg-rose-50 dark:hover:bg-rose-950/40 text-left mt-1 border-t border-slate-100 dark:border-slate-800"
                  >
                    <LogOut className="w-3.5 h-3.5" />
                    Log Out
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Desktop Top Navigation Bar */}
        <header className="hidden md:flex h-16 items-center justify-between px-6 bg-white dark:bg-slate-900 border-b border-slate-200/80 dark:border-slate-800/80 sticky top-0 z-30">
          {/* Global Search / Command Palette trigger */}
          <div className="flex items-center gap-3">
            <button
              onClick={() => setCommandPaletteOpen(true)}
              className="flex items-center gap-3 px-3.5 py-2 rounded-xl bg-slate-100 dark:bg-slate-800/80 hover:bg-slate-200/80 dark:hover:bg-slate-800 border border-slate-200 dark:border-slate-700 text-xs text-slate-500 dark:text-slate-400 w-72 transition-all cursor-pointer group"
            >
              <Search className="w-4 h-4 text-slate-400 group-hover:text-emerald-500 transition-colors" />
              <span className="flex-1 text-left">Search forms or commands...</span>
              <kbd className="inline-flex items-center gap-0.5 px-1.5 py-0.5 text-[10px] font-bold text-slate-400 bg-white dark:bg-slate-900 rounded border border-slate-200 dark:border-slate-700">
                ⌘K
              </kbd>
            </button>
          </div>

          {/* Right Header Controls */}
          <div className="flex items-center gap-3">
            {/* Quick Create Action Button in Topbar */}
            <Button
              size="sm"
              onClick={() => setCreateModalOpen(true)}
              leftIcon={<Plus className="w-4 h-4" />}
              className="font-bold text-xs shadow-sm"
            >
              New Form
            </Button>

            {/* Notifications */}
            <Link
              to="/notifications"
              className="p-2 rounded-xl text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors relative"
              title="Notifications"
            >
              <Bell className="w-4 h-4" />
              <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-emerald-500 ring-2 ring-white dark:ring-slate-900" />
            </Link>

            {/* Theme Toggle */}
            <button
              onClick={toggleTheme}
              className="p-2 rounded-xl text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors cursor-pointer"
              title="Toggle Light/Dark Theme"
            >
              {isDark ? <Sun className="w-4 h-4 text-amber-400" /> : <Moon className="w-4 h-4" />}
            </button>

            {/* User Avatar Menu Trigger */}
            <div className="h-6 w-px bg-slate-200 dark:bg-slate-800 mx-1" />
            <Link
              to="/profile"
              className="flex items-center gap-2.5 p-1 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            >
              <div className="w-8 h-8 rounded-xl bg-gradient-to-tr from-emerald-500 to-teal-600 flex items-center justify-center text-white font-bold text-xs uppercase shadow-sm">
                {user?.firstName?.charAt(0) || user?.email?.charAt(0) || 'U'}
              </div>
              <div className="hidden lg:block text-left">
                <p className="text-xs font-bold text-slate-900 dark:text-slate-100 leading-tight">
                  {user?.firstName ? `${user.firstName} ${user.lastName || ''}` : 'My Account'}
                </p>
                <p className="text-[10px] text-emerald-600 dark:text-emerald-400 font-semibold">
                  Active
                </p>
              </div>
            </Link>
          </div>
        </header>

        {/* Content Body */}
        <main className="flex-1 p-4 md:p-8 overflow-y-auto">
          {children || <Outlet />}
        </main>
      </div>
    </div>
  );
};
