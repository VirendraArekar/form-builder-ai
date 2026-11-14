import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ToastProvider } from '@/components/ui/Toast';
import { useAuthStore } from '@/store/authStore';

// Public Pages
import { LandingPage } from '@/pages/LandingPage';
import { LoginPage } from '@/pages/LoginPage';
import { RegisterPage } from '@/pages/RegisterPage';
import { ForgotPasswordPage } from '@/pages/ForgotPasswordPage';
import { PublicFormPage } from '@/pages/PublicFormPage';
import { SubmissionSuccessPage } from '@/pages/SubmissionSuccessPage';

// Authenticated Pages
import { DashboardPage } from '@/pages/DashboardPage';
import { FormsListPage } from '@/pages/FormsListPage';
import { AiGenerateFormPage } from '@/pages/AiGenerateFormPage';
import { FormBuilderPage } from '@/pages/FormBuilderPage';
import { FormPreviewPage } from '@/pages/FormPreviewPage';
import { FormSettingsPage } from '@/pages/FormSettingsPage';
import { ResponsesPage } from '@/pages/ResponsesPage';
import { ResponseDetailPage } from '@/pages/ResponseDetailPage';
import { FormAnalyticsPage } from '@/pages/FormAnalyticsPage';
import { WorkspaceInsightsPage } from '@/pages/WorkspaceInsightsPage';
import { InboxPage } from '@/pages/InboxPage';
import { TemplatesPage } from '@/pages/TemplatesPage';
import { WorkspaceMembersPage } from '@/pages/WorkspaceMembersPage';
import { NotificationsPage } from '@/pages/NotificationsPage';
import { UserProfilePage } from '@/pages/UserProfilePage';
import { WorkspaceSettingsPage } from '@/pages/WorkspaceSettingsPage';
import { BillingPage } from '@/pages/BillingPage';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { isAuthenticated, token } = useAuthStore();
  const tokenExists = !!token || isAuthenticated;

  if (!tokenExists) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

export const App: React.FC = () => {
  const { checkAuth, token } = useAuthStore();

  React.useEffect(() => {
    if (token) {
      checkAuth();
    }
  }, [token, checkAuth]);

  return (
    <QueryClientProvider client={queryClient}>
      <ToastProvider>
        <BrowserRouter>
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/forgot-password" element={<ForgotPasswordPage />} />
            <Route path="/f/:slug" element={<PublicFormPage />} />
            <Route path="/f/:slug/success" element={<SubmissionSuccessPage />} />

            {/* Protected SaaS Routes */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <DashboardPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/forms"
              element={
                <ProtectedRoute>
                  <FormsListPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/generate-ai"
              element={
                <ProtectedRoute>
                  <AiGenerateFormPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/builder/:id"
              element={
                <ProtectedRoute>
                  <FormBuilderPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/forms/:id/preview"
              element={
                <ProtectedRoute>
                  <FormPreviewPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/forms/:id/settings"
              element={
                <ProtectedRoute>
                  <FormSettingsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/forms/:formId/responses"
              element={
                <ProtectedRoute>
                  <ResponsesPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/forms/:formId/responses/:responseId"
              element={
                <ProtectedRoute>
                  <ResponseDetailPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/forms/:formId/analytics"
              element={
                <ProtectedRoute>
                  <FormAnalyticsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/insights"
              element={
                <ProtectedRoute>
                  <WorkspaceInsightsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/inbox"
              element={
                <ProtectedRoute>
                  <InboxPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/templates"
              element={
                <ProtectedRoute>
                  <TemplatesPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/members"
              element={
                <ProtectedRoute>
                  <WorkspaceMembersPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/notifications"
              element={
                <ProtectedRoute>
                  <NotificationsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <ProtectedRoute>
                  <UserProfilePage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/settings"
              element={
                <ProtectedRoute>
                  <WorkspaceSettingsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/billing"
              element={
                <ProtectedRoute>
                  <BillingPage />
                </ProtectedRoute>
              }
            />

            {/* Catch-all */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </BrowserRouter>
      </ToastProvider>
    </QueryClientProvider>
  );
};

export default App;
