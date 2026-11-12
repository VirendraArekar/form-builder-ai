import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { workspaceService } from '@/services/workspaceService';
import { WorkspaceMember } from '@/types';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { useAuthStore } from '@/store/authStore';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Select } from '@/components/ui/Select';
import { Badge } from '@/components/ui/Badge';
import { Modal } from '@/components/ui/Modal';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Skeleton } from '@/components/ui/Skeleton';
import { useToast } from '@/components/ui/Toast';
import {
  Users,
  UserPlus,
  Shield,
  Trash2,
  Mail,
  CheckCircle2,
  Lock,
} from 'lucide-react';

export const WorkspaceMembersPage: React.FC = () => {
  const { currentWorkspace, user } = useAuthStore();
  const { showToast } = useToast();
  const workspaceId = currentWorkspace?.id || '';

  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [inviteEmail, setInviteEmail] = useState('');
  const [inviteRole, setInviteRole] = useState('EDITOR');
  const [isInviting, setIsInviting] = useState(false);

  const {
    data: members,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['workspace-members', workspaceId],
    queryFn: () => workspaceService.listMembers(workspaceId),
    enabled: !!workspaceId,
  });

  const handleInvite = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!workspaceId || !inviteEmail.trim()) return;

    setIsInviting(true);
    try {
      await workspaceService.inviteMember(workspaceId, {
        email: inviteEmail.trim(),
        role: inviteRole,
      });
      showToast(`Invitation sent to ${inviteEmail}`, 'success');
      setInviteEmail('');
      setIsInviteModalOpen(false);
      refetch();
    } catch (err: any) {
      showToast(err.message || 'Failed to send invitation', 'error');
    } finally {
      setIsInviting(false);
    }
  };

  const handleRoleChange = async (memberId: string, newRole: string) => {
    if (!workspaceId) return;
    try {
      await workspaceService.updateMemberRole(workspaceId, memberId, newRole);
      showToast('Role updated successfully', 'success');
      refetch();
    } catch (err: any) {
      showToast('Failed to update role', 'error');
    }
  };

  const handleRemove = async (memberId: string, memberEmail: string) => {
    if (!window.confirm(`Remove ${memberEmail} from the workspace?`)) return;
    if (!workspaceId) return;
    try {
      await workspaceService.removeMember(workspaceId, memberId);
      showToast('Member removed', 'info');
      refetch();
    } catch (err: any) {
      showToast('Failed to remove member', 'error');
    }
  };

  const memberList = members || [];

  return (
    <DashboardLayout>
      <div className="max-w-5xl mx-auto space-y-8 pb-16">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
              Team & Permissions
            </h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
              Manage collaborators and assign role-based access for <strong className="text-slate-700 dark:text-slate-300">{currentWorkspace?.name}</strong>.
            </p>
          </div>

          <Button onClick={() => setIsInviteModalOpen(true)} className="shadow-sm">
            <UserPlus className="w-4 h-4 mr-1.5" />
            Invite Member
          </Button>
        </div>

        {/* Members Table Card */}
        <Card>
          <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800">
            <div className="flex items-center justify-between">
              <CardTitle className="text-base font-bold flex items-center gap-2">
                <Users className="w-4 h-4 text-indigo-500" />
                Active Collaborators ({memberList.length})
              </CardTitle>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            {isLoading ? (
              <div className="p-6 space-y-3">
                {[...Array(3)].map((_, i) => (
                  <Skeleton key={i} className="h-14 rounded-xl" />
                ))}
              </div>
            ) : (
              <div className="divide-y divide-slate-100 dark:divide-slate-800">
                {memberList.map((m) => {
                  const isCurrentUser = m.email === user?.email;
                  return (
                    <div
                      key={m.id}
                      className="p-4 sm:p-5 flex items-center justify-between gap-4 hover:bg-slate-50/50 dark:hover:bg-slate-800/30 transition-colors"
                    >
                      <div className="flex items-center gap-3.5">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-indigo-500 to-violet-600 text-white font-bold text-sm flex items-center justify-center uppercase shadow-xs">
                          {m.name ? m.name.charAt(0) : m.email.charAt(0)}
                        </div>
                        <div>
                          <div className="flex items-center gap-2">
                            <span className="font-bold text-slate-900 dark:text-white text-sm">
                              {m.name || m.email.split('@')[0]}
                            </span>
                            {isCurrentUser && (
                              <Badge variant="outline" className="text-[10px]">
                                You
                              </Badge>
                            )}
                          </div>
                          <span className="text-xs text-slate-500 block">{m.email}</span>
                        </div>
                      </div>

                      <div className="flex items-center gap-4">
                        {m.role === 'WORKSPACE_OWNER' ? (
                          <Badge variant="indigo" className="font-semibold text-xs">
                            Owner
                          </Badge>
                        ) : (
                          <select
                            value={m.role}
                            onChange={(e) => handleRoleChange(m.id, e.target.value)}
                            disabled={isCurrentUser}
                            className="text-xs p-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 font-medium text-slate-700 dark:text-slate-300"
                          >
                            <option value="ADMIN">Admin</option>
                            <option value="EDITOR">Editor</option>
                            <option value="VIEWER">Viewer</option>
                          </select>
                        )}

                        {m.role !== 'WORKSPACE_OWNER' && !isCurrentUser && (
                          <button
                            onClick={() => handleRemove(m.id, m.email)}
                            className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800"
                            title="Remove member"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>
            )}
          </CardContent>
        </Card>

        {/* Role Permissions Matrix */}
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-xs font-bold uppercase tracking-wider text-slate-500 flex items-center gap-1.5">
              <Shield className="w-3.5 h-3.5" />
              Role Permissions Overview
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 sm:grid-cols-4 gap-4 text-xs">
              <div className="p-3.5 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 space-y-1">
                <span className="font-bold text-slate-900 dark:text-white block">Workspace Owner</span>
                <p className="text-slate-500">Full control over billing, deletions, team invites, and forms.</p>
              </div>
              <div className="p-3.5 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 space-y-1">
                <span className="font-bold text-slate-900 dark:text-white block">Admin</span>
                <p className="text-slate-500">Manage all forms, integrations, settings, and invite collaborators.</p>
              </div>
              <div className="p-3.5 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 space-y-1">
                <span className="font-bold text-slate-900 dark:text-white block">Editor</span>
                <p className="text-slate-500">Create, edit, and publish forms, view responses, and export data.</p>
              </div>
              <div className="p-3.5 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 space-y-1">
                <span className="font-bold text-slate-900 dark:text-white block">Viewer</span>
                <p className="text-slate-500">Read-only access to forms, response submissions, and analytics.</p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Invite Member Modal */}
        <Modal
          isOpen={isInviteModalOpen}
          onClose={() => setIsInviteModalOpen(false)}
          title="Invite Team Member"
        >
          <form onSubmit={handleInvite} className="space-y-4">
            <Input
              label="Teammate Email Address"
              type="email"
              placeholder="colleague@company.com"
              icon={<Mail className="w-4 h-4" />}
              value={inviteEmail}
              onChange={(e) => setInviteEmail(e.target.value)}
              required
            />

            <Select
              label="Role & Access Level"
              options={[
                { value: 'ADMIN', label: 'Admin (Manage forms & members)' },
                { value: 'EDITOR', label: 'Editor (Create & edit forms)' },
                { value: 'VIEWER', label: 'Viewer (Read-only access)' },
              ]}
              value={inviteRole}
              onChange={(e) => setInviteRole(e.target.value)}
            />

            <div className="pt-2 flex justify-end gap-3">
              <Button type="button" variant="outline" onClick={() => setIsInviteModalOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" isLoading={isInviting}>
                Send Invitation
              </Button>
            </div>
          </form>
        </Modal>
      </div>
    </DashboardLayout>
  );
};
