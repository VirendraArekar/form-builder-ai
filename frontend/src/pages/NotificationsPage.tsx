import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { notificationService, NotificationItem } from '@/services/notificationService';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { useToast } from '@/components/ui/Toast';
import {
  Bell,
  CheckCircle2,
  Clock,
  Sparkles,
  MessageSquare,
  Shield,
  ExternalLink,
  CheckCheck,
} from 'lucide-react';

export const NotificationsPage: React.FC = () => {
  const { showToast } = useToast();
  const [filter, setFilter] = useState<'ALL' | 'UNREAD'>('ALL');

  const {
    data: notifications,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['notifications'],
    queryFn: () => notificationService.listNotifications(),
  });

  const handleMarkAsRead = async (id: string) => {
    try {
      await notificationService.markAsRead(id);
      refetch();
    } catch (err) {
      showToast('Failed to mark notification as read', 'error');
    }
  };

  const handleMarkAllRead = async () => {
    try {
      await notificationService.markAllAsRead();
      refetch();
      showToast('All notifications marked as read', 'success');
    } catch (err) {
      showToast('Failed to mark all as read', 'error');
    }
  };

  const list = (notifications || []).filter((n) =>
    filter === 'UNREAD' ? !n.isRead : true
  );

  const unreadCount = (notifications || []).filter((n) => !n.isRead).length;

  return (
    <DashboardLayout>
      <div className="max-w-4xl mx-auto space-y-6 pb-16">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-white">
                Notifications
              </h1>
              {unreadCount > 0 && <Badge variant="indigo">{unreadCount} unread</Badge>}
            </div>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-0.5">
              Updates on new form responses, team activity, and AI insights.
            </p>
          </div>

          <div className="flex items-center gap-2">
            <div className="flex items-center border border-slate-200 dark:border-slate-800 rounded-lg p-0.5 bg-slate-100 dark:bg-slate-800 text-xs">
              <button
                onClick={() => setFilter('ALL')}
                className={`px-3 py-1.5 rounded-md font-semibold transition-colors ${
                  filter === 'ALL'
                    ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-xs'
                    : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                All
              </button>
              <button
                onClick={() => setFilter('UNREAD')}
                className={`px-3 py-1.5 rounded-md font-semibold transition-colors ${
                  filter === 'UNREAD'
                    ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-xs'
                    : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                Unread
              </button>
            </div>

            {unreadCount > 0 && (
              <Button size="sm" variant="outline" onClick={handleMarkAllRead}>
                <CheckCheck className="w-4 h-4 mr-1.5" />
                Mark all read
              </Button>
            )}
          </div>
        </div>

        {/* Notification list */}
        {isLoading ? (
          <div className="space-y-3">
            {[...Array(4)].map((_, i) => (
              <Skeleton key={i} className="h-20 rounded-xl" />
            ))}
          </div>
        ) : list.length === 0 ? (
          <EmptyState
            icon={<Bell className="w-12 h-12 text-slate-400" />}
            title="All caught up!"
            description={
              filter === 'UNREAD'
                ? 'You have zero unread notifications.'
                : 'No notification activity recorded yet.'
            }
          />
        ) : (
          <Card className="divide-y divide-slate-100 dark:divide-slate-800 overflow-hidden">
            {list.map((item) => (
              <div
                key={item.id}
                onClick={() => !item.isRead && handleMarkAsRead(item.id)}
                className={`p-4 sm:p-5 flex items-start justify-between gap-4 transition-colors cursor-pointer ${
                  !item.isRead
                    ? 'bg-indigo-50/40 dark:bg-indigo-950/20 hover:bg-indigo-50/60'
                    : 'hover:bg-slate-50 dark:hover:bg-slate-800/40'
                }`}
              >
                <div className="flex items-start gap-3.5">
                  <div
                    className={`w-9 h-9 rounded-xl flex items-center justify-center shrink-0 mt-0.5 ${
                      !item.isRead
                        ? 'bg-indigo-600 text-white shadow-sm'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-500'
                    }`}
                  >
                    <Bell className="w-4 h-4" />
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <h4 className="font-bold text-sm text-slate-900 dark:text-white">
                        {item.title}
                      </h4>
                      {!item.isRead && (
                        <span className="w-2 h-2 rounded-full bg-indigo-600" />
                      )}
                    </div>
                    <p className="text-xs text-slate-600 dark:text-slate-400 mt-1 leading-relaxed">
                      {item.message}
                    </p>
                    <span className="text-[11px] text-slate-400 flex items-center gap-1 mt-2">
                      <Clock className="w-3 h-3" />
                      {new Date(item.createdAt).toLocaleString()}
                    </span>
                  </div>
                </div>

                {!item.isRead && (
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleMarkAsRead(item.id);
                    }}
                    className="p-1.5 text-slate-400 hover:text-indigo-600 rounded-md shrink-0"
                    title="Mark as read"
                  >
                    <CheckCircle2 className="w-4 h-4" />
                  </button>
                )}
              </div>
            ))}
          </Card>
        )}
      </div>
    </DashboardLayout>
  );
};
