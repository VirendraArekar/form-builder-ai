import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/store/authStore';
import { workspaceService } from '@/services/workspaceService';
import { webhookService, WebhookDto } from '@/services/webhookService';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Textarea } from '@/components/ui/Textarea';
import { Badge } from '@/components/ui/Badge';
import { Modal } from '@/components/ui/Modal';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  Building2,
  Save,
  Webhook,
  Plus,
  Trash2,
  Send,
  AlertTriangle,
  Globe,
  Key,
} from 'lucide-react';

export const WorkspaceSettingsPage: React.FC = () => {
  const { currentWorkspace, setCurrentWorkspace } = useAuthStore();
  const { showToast } = useToast();
  const workspaceId = currentWorkspace?.id || '';

  // Workspace form state
  const [name, setName] = useState(currentWorkspace?.name || '');
  const [description, setDescription] = useState(currentWorkspace?.description || '');
  const [logoUrl, setLogoUrl] = useState(currentWorkspace?.logoUrl || '');
  const [isSaving, setIsSaving] = useState(false);

  // Webhook state
  const [isAddWebhookOpen, setIsAddWebhookOpen] = useState(false);
  const [hookName, setHookName] = useState('');
  const [hookUrl, setHookUrl] = useState('');
  const [hookSecret, setHookSecret] = useState('');
  const [isAddingHook, setIsAddingHook] = useState(false);

  const {
    data: webhooks,
    isLoading: webhooksLoading,
    refetch: refetchWebhooks,
  } = useQuery({
    queryKey: ['workspace-webhooks', workspaceId],
    queryFn: () => webhookService.listWebhooks(workspaceId),
    enabled: !!workspaceId,
  });

  const handleSaveWorkspace = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!workspaceId) return;

    setIsSaving(true);
    try {
      const updated = await workspaceService.updateWorkspace(workspaceId, {
        name: name.trim(),
        description: description.trim() || undefined,
        logoUrl: logoUrl.trim() || undefined,
      });
      setCurrentWorkspace(updated);
      showToast('Workspace settings updated successfully!', 'success');
    } catch (err: any) {
      showToast(err.message || 'Failed to update workspace', 'error');
    } finally {
      setIsSaving(false);
    }
  };

  const handleCreateWebhook = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!workspaceId || !hookName.trim() || !hookUrl.trim()) return;

    setIsAddingHook(true);
    try {
      await webhookService.createWebhook(workspaceId, {
        name: hookName.trim(),
        targetUrl: hookUrl.trim(),
        secretKey: hookSecret.trim() || undefined,
        events: ['FORM_SUBMITTED', 'FORM_PUBLISHED'],
      });
      showToast('Webhook registered successfully', 'success');
      setHookName('');
      setHookUrl('');
      setHookSecret('');
      setIsAddWebhookOpen(false);
      refetchWebhooks();
    } catch (err: any) {
      showToast(err.message || 'Failed to create webhook', 'error');
    } finally {
      setIsAddingHook(false);
    }
  };

  const handleDeleteWebhook = async (id: string) => {
    if (!window.confirm('Delete this webhook?')) return;
    try {
      await webhookService.deleteWebhook(workspaceId, id);
      showToast('Webhook deleted', 'info');
      refetchWebhooks();
    } catch (err: any) {
      showToast('Failed to delete webhook', 'error');
    }
  };

  const handleTestWebhook = async (id: string) => {
    try {
      await webhookService.testWebhook(workspaceId, id);
      showToast('Test payload dispatched successfully (200 OK)', 'success');
    } catch (err: any) {
      showToast('Webhook test delivery failed or timed out', 'error');
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-4xl mx-auto space-y-8 pb-16">
        <div>
          <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
            Workspace Settings
          </h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
            Configure organization branding, integrations, and webhooks for <strong className="text-slate-700 dark:text-slate-300">{currentWorkspace?.name}</strong>.
          </p>
        </div>

        {/* 1. General Profile */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-bold flex items-center gap-2">
              <Building2 className="w-4 h-4 text-indigo-500" />
              Organization & Workspace Profile
            </CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSaveWorkspace} className="space-y-4">
              <Input
                label="Workspace Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="e.g. Acme Corp"
                required
              />

              <Textarea
                label="Workspace Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Brief description of this team or product line..."
                rows={2}
              />

              <Input
                label="Organization Logo URL"
                placeholder="https://example.com/logo.svg"
                value={logoUrl}
                onChange={(e) => setLogoUrl(e.target.value)}
              />

              <div className="pt-2 flex justify-end">
                <Button type="submit" isLoading={isSaving}>
                  <Save className="w-4 h-4 mr-1.5" />
                  Save Workspace
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>

        {/* 2. Webhooks & Integrations */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-3">
            <div>
              <CardTitle className="text-base font-bold flex items-center gap-2">
                <Webhook className="w-4 h-4 text-indigo-500" />
                Outgoing Webhooks
              </CardTitle>
              <p className="text-xs text-slate-500 mt-0.5">
                Send real-time JSON payloads to your servers or Zapier/Make when forms are submitted.
              </p>
            </div>
            <Button size="sm" onClick={() => setIsAddWebhookOpen(true)}>
              <Plus className="w-3.5 h-3.5 mr-1" />
              Add Webhook
            </Button>
          </CardHeader>
          <CardContent>
            {webhooksLoading ? (
              <div className="space-y-2">
                <Skeleton className="h-12 w-full" />
                <Skeleton className="h-12 w-full" />
              </div>
            ) : !webhooks || webhooks.length === 0 ? (
              <p className="text-xs text-slate-400 italic py-4 text-center">
                No webhooks configured yet. Connect Slack, Zapier, or your backend API.
              </p>
            ) : (
              <div className="divide-y divide-slate-100 dark:divide-slate-800">
                {webhooks.map((hook) => (
                  <div
                    key={hook.id}
                    className="py-3 flex items-center justify-between gap-4 text-xs"
                  >
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-bold text-slate-900 dark:text-white">
                          {hook.name}
                        </span>
                        <Badge variant="success" className="text-[10px]">
                          Active
                        </Badge>
                      </div>
                      <span className="font-mono text-slate-500 text-[11px] block mt-0.5">
                        {hook.targetUrl}
                      </span>
                    </div>

                    <div className="flex items-center gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        className="text-xs h-7 px-2"
                        onClick={() => handleTestWebhook(hook.id)}
                      >
                        <Send className="w-3 h-3 mr-1" />
                        Test
                      </Button>
                      <button
                        onClick={() => handleDeleteWebhook(hook.id)}
                        className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md"
                        title="Delete Webhook"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>

        {/* Add Webhook Modal */}
        <Modal
          isOpen={isAddWebhookOpen}
          onClose={() => setIsAddWebhookOpen(false)}
          title="Configure Outgoing Webhook"
        >
          <form onSubmit={handleCreateWebhook} className="space-y-4">
            <Input
              label="Webhook Name / Label"
              placeholder="e.g. Slack Submissions Bot"
              value={hookName}
              onChange={(e) => setHookName(e.target.value)}
              required
            />

            <Input
              label="Endpoint Target URL"
              type="url"
              placeholder="https://api.yourdomain.com/webhooks/formgenius"
              icon={<Globe className="w-4 h-4" />}
              value={hookUrl}
              onChange={(e) => setHookUrl(e.target.value)}
              required
            />

            <Input
              label="Secret Key (Optional HMAC signing)"
              placeholder="whsec_..."
              icon={<Key className="w-4 h-4" />}
              value={hookSecret}
              onChange={(e) => setHookSecret(e.target.value)}
            />

            <div className="pt-2 flex justify-end gap-3">
              <Button type="button" variant="outline" onClick={() => setIsAddWebhookOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" isLoading={isAddingHook}>
                Create Webhook
              </Button>
            </div>
          </form>
        </Modal>
      </div>
    </DashboardLayout>
  );
};
