import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { formService } from '@/services/formService';
import { FormDetail } from '@/types';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Textarea } from '@/components/ui/Textarea';
import { Switch } from '@/components/ui/Switch';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  ArrowLeft,
  Save,
  Globe,
  Lock,
  Calendar,
  Mail,
  Sliders,
  CheckCircle2,
  ExternalLink,
  Code2,
} from 'lucide-react';

export const FormSettingsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { showToast } = useToast();

  const [form, setForm] = useState<FormDetail | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  // Form settings state
  const [slug, setSlug] = useState('');
  const [isPasswordProtected, setIsPasswordProtected] = useState(false);
  const [accessPassword, setAccessPassword] = useState('');
  const [allowMultipleSubmissions, setAllowMultipleSubmissions] = useState(true);
  const [maxSubmissions, setMaxSubmissions] = useState<number | undefined>(undefined);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [sendEmailNotification, setSendEmailNotification] = useState(false);
  const [notificationEmail, setNotificationEmail] = useState('');
  const [thankYouMessage, setThankYouMessage] = useState('');
  const [redirectUrl, setRedirectUrl] = useState('');

  useEffect(() => {
    if (!id) return;
    formService
      .getForm(id)
      .then((data) => {
        setForm(data);
        setSlug(data.publishedSlug || '');
        setIsPasswordProtected(data.isPasswordProtected || false);
        setAllowMultipleSubmissions(data.allowMultipleSubmissions ?? true);
        setMaxSubmissions(data.maxSubmissions);
        setStartDate(data.startDate ? data.startDate.substring(0, 10) : '');
        setEndDate(data.endDate ? data.endDate.substring(0, 10) : '');
        setSendEmailNotification(data.sendEmailNotification ?? false);
        setNotificationEmail(data.notificationEmail || '');
        setThankYouMessage(data.thankYouMessage || 'Thank you! Your response has been recorded.');
        setRedirectUrl(data.redirectUrl || '');
        setIsLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setIsLoading(false);
      });
  }, [id]);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form) return;

    setIsSaving(true);
    try {
      const updated = await formService.updateForm(form.id, {
        publishedSlug: slug.trim() || undefined,
        isPasswordProtected,
        accessPassword: isPasswordProtected ? accessPassword : '',
        allowMultipleSubmissions,
        maxSubmissions: maxSubmissions ? Number(maxSubmissions) : undefined,
        startDate: startDate ? new Date(startDate).toISOString() : undefined,
        endDate: endDate ? new Date(endDate).toISOString() : undefined,
        sendEmailNotification,
        notificationEmail: sendEmailNotification ? notificationEmail : undefined,
        thankYouMessage,
        redirectUrl: redirectUrl.trim() || undefined,
      });

      setForm(updated);
      showToast('Form settings updated successfully!', 'success');
    } catch (err: any) {
      showToast(err.message || 'Failed to update settings', 'error');
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading || !form) {
    return (
      <DashboardLayout>
        <div className="max-w-4xl mx-auto space-y-6">
          <Skeleton className="h-10 w-48" />
          <Skeleton className="h-64 w-full rounded-2xl" />
        </div>
      </DashboardLayout>
    );
  }

  const publicUrl = slug
    ? `${window.location.origin}/f/${slug}`
    : `${window.location.origin}/f/${form.id}`;

  return (
    <DashboardLayout>
      <form onSubmit={handleSave} className="max-w-4xl mx-auto space-y-8 pb-16">
        {/* Top Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <Link
              to={`/builder/${form.id}`}
              className="p-1.5 rounded-lg text-slate-500 hover:text-slate-800 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            >
              <ArrowLeft className="w-4 h-4" />
            </Link>
            <div>
              <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-white">
                Form Settings: {form.title}
              </h1>
              <p className="text-xs text-slate-500">
                Configure URL slug, access control, response limits, notifications, and redirects.
              </p>
            </div>
          </div>

          <Button type="submit" isLoading={isSaving} className="font-semibold shadow-sm">
            <Save className="w-4 h-4 mr-1.5" />
            Save Changes
          </Button>
        </div>

        {/* 1. Public URL & Slug */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <Globe className="w-4 h-4 text-indigo-500" />
              Public URL & Custom Slug
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-1.5">
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase">
                Custom URL Slug
              </label>
              <div className="flex items-center">
                <span className="text-xs font-mono px-3 py-2 bg-slate-100 dark:bg-slate-800 border border-r-0 border-slate-200 dark:border-slate-700 rounded-l-lg text-slate-500">
                  {window.location.host}/f/
                </span>
                <input
                  type="text"
                  value={slug}
                  onChange={(e) => setSlug(e.target.value.toLowerCase().replace(/[^a-z0-9-]/g, '-'))}
                  placeholder="feedback-survey"
                  className="flex-1 text-xs px-3 py-2 rounded-r-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-1 focus:ring-indigo-500"
                />
              </div>
              <p className="text-[11px] text-slate-400">
                Your form will be accessible publicly at: <strong className="text-indigo-600">{publicUrl}</strong>
              </p>
            </div>
          </CardContent>
        </Card>

        {/* 2. Access Control & Security */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <Lock className="w-4 h-4 text-indigo-500" />
              Access Control & Security
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <Switch
              label="Password Protection"
              description="Require respondents to enter an access password before viewing and submitting."
              checked={isPasswordProtected}
              onChange={setIsPasswordProtected}
            />

            {isPasswordProtected && (
              <div className="pt-2">
                <Input
                  label="Access Password"
                  type="password"
                  placeholder="Enter secret password"
                  value={accessPassword}
                  onChange={(e) => setAccessPassword(e.target.value)}
                />
              </div>
            )}

            <div className="pt-3 border-t border-slate-100 dark:border-slate-800">
              <Switch
                label="Allow Multiple Submissions"
                description="Allow the same respondent to fill out and submit the form more than once."
                checked={allowMultipleSubmissions}
                onChange={setAllowMultipleSubmissions}
              />
            </div>
          </CardContent>
        </Card>

        {/* 3. Response Limits & Schedule */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <Calendar className="w-4 h-4 text-indigo-500" />
              Scheduling & Limits
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <Input
              label="Submission Quota Limit (Optional)"
              type="number"
              placeholder="e.g. 500 (Leave blank for unlimited)"
              value={maxSubmissions ?? ''}
              onChange={(e) => setMaxSubmissions(e.target.value ? Number(e.target.value) : undefined)}
            />

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-2">
              <Input
                label="Start Date"
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
              <Input
                label="End Date (Automatic Close)"
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </div>
          </CardContent>
        </Card>

        {/* 4. Notifications */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <Mail className="w-4 h-4 text-indigo-500" />
              Email Notifications
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <Switch
              label="Email Notification on Submission"
              description="Receive an email notification every time a respondent submits this form."
              checked={sendEmailNotification}
              onChange={setSendEmailNotification}
            />

            {sendEmailNotification && (
              <Input
                label="Notification Email"
                type="email"
                placeholder="notifications@company.com"
                value={notificationEmail}
                onChange={(e) => setNotificationEmail(e.target.value)}
              />
            )}
          </CardContent>
        </Card>

        {/* 5. Confirmation & Redirection */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 text-indigo-500" />
              After Submission Experience
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <Textarea
              label="Custom Thank You Message"
              placeholder="Thank you for taking the time to share your feedback!"
              value={thankYouMessage}
              onChange={(e) => setThankYouMessage(e.target.value)}
              rows={3}
            />

            <Input
              label="Redirect URL (Optional)"
              placeholder="https://yourcompany.com/thank-you"
              value={redirectUrl}
              onChange={(e) => setRedirectUrl(e.target.value)}
            />
          </CardContent>
        </Card>
      </form>
    </DashboardLayout>
  );
};
