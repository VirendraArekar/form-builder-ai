import React, { useState, useEffect } from 'react';
import QRCode from 'qrcode';
import { FormDetail } from '@/types';
import { formService } from '@/services/formService';
import { Modal } from '@/components/ui/Modal';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Switch } from '@/components/ui/Switch';
import { useToast } from '@/components/ui/Toast';
import {
  Globe,
  Copy,
  ExternalLink,
  Code2,
  QrCode,
  Lock,
  CheckCircle2,
  Share2,
} from 'lucide-react';

interface PublishModalProps {
  isOpen: boolean;
  onClose: () => void;
  form: FormDetail;
  onUpdateForm: (updated: FormDetail) => void;
}

export const PublishModal: React.FC<PublishModalProps> = ({
  isOpen,
  onClose,
  form,
  onUpdateForm,
}) => {
  const { showToast } = useToast();
  const [slug, setSlug] = useState(form.publishedSlug || '');
  const [isPublishing, setIsPublishing] = useState(false);
  const [qrDataUrl, setQrDataUrl] = useState<string>('');
  const [activeTab, setActiveTab] = useState<'link' | 'embed' | 'qr'>('link');

  const isPublished = form.status === 'PUBLISHED';
  const publicUrl = slug
    ? `${window.location.origin}/f/${slug}`
    : `${window.location.origin}/f/${form.id}`;

  const embedCode = `<iframe src="${publicUrl}" width="100%" height="650px" frameborder="0" marginheight="0" marginwidth="0">Loading Form...</iframe>`;

  useEffect(() => {
    if (publicUrl) {
      QRCode.toDataURL(publicUrl, { width: 200, margin: 2 }, (err, url) => {
        if (!err && url) setQrDataUrl(url);
      });
    }
  }, [publicUrl]);

  const handleTogglePublish = async () => {
    setIsPublishing(true);
    try {
      if (isPublished) {
        const updated = await formService.unpublishForm(form.id);
        onUpdateForm(updated);
        showToast('Form is now unpublished (offline)', 'info');
      } else {
        // Guarantee all dynamically added questions, sections, and buttons are committed to database
        await formService.saveFormSchema(form.id, {
          title: form.title,
          description: form.description,
          sections: form.sections,
          questions: form.questions,
          logicRules: form.logicRules,
          theme: form.theme,
        });

        const updated = await formService.publishForm(form.id, slug.trim() || undefined);
        onUpdateForm(updated);
        showToast('Form is live! Public link generated.', 'success');
      }
    } catch (err: any) {
      showToast(err.message || 'Failed to update publication status', 'error');
    } finally {
      setIsPublishing(false);
    }
  };

  const copyToClipboard = (text: string, msg: string) => {
    navigator.clipboard.writeText(text);
    showToast(msg, 'success');
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Publish & Share Form"
      maxWidth="xl"
    >
      <div className="space-y-6">
        {/* Status card */}
        <div
          className={`p-4 rounded-xl border flex items-center justify-between ${
            isPublished
              ? 'bg-emerald-50 dark:bg-emerald-950/30 border-emerald-200 dark:border-emerald-900 text-emerald-900 dark:text-emerald-300'
              : 'bg-slate-50 dark:bg-slate-800/40 border-slate-200 dark:border-slate-800 text-slate-700 dark:text-slate-300'
          }`}
        >
          <div className="flex items-center gap-3">
            <div
              className={`w-10 h-10 rounded-full flex items-center justify-center ${
                isPublished
                  ? 'bg-emerald-100 dark:bg-emerald-900/60 text-emerald-600'
                  : 'bg-slate-200 dark:bg-slate-700 text-slate-500'
              }`}
            >
              <Globe className="w-5 h-5" />
            </div>
            <div>
              <h4 className="font-bold text-sm">
                {isPublished ? 'This form is Published & Active' : 'Form is currently Draft (Unpublished)'}
              </h4>
              <p className="text-xs opacity-80">
                {isPublished
                  ? 'Anyone with the link can view and submit responses.'
                  : 'Submissions are disabled until you publish.'}
              </p>
            </div>
          </div>

          <Button
            size="sm"
            variant={isPublished ? 'outline' : 'primary'}
            isLoading={isPublishing}
            onClick={handleTogglePublish}
            className={isPublished ? 'border-emerald-300 dark:border-emerald-800' : ''}
          >
            {isPublished ? 'Unpublish Form' : 'Publish Now'}
          </Button>
        </div>

        {/* Custom Slug Config */}
        <div className="space-y-2">
          <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider">
            Public Form URL Slug
          </label>
          <div className="flex items-center">
            <span className="text-xs font-mono px-3 py-2.5 bg-slate-100 dark:bg-slate-800 border border-r-0 border-slate-200 dark:border-slate-700 rounded-l-lg text-slate-500">
              {window.location.host}/f/
            </span>
            <input
              type="text"
              value={slug}
              onChange={(e) => setSlug(e.target.value.toLowerCase().replace(/[^a-z0-9-]/g, '-'))}
              placeholder="customer-feedback"
              className="flex-1 text-xs px-3 py-2.5 rounded-r-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:outline-none focus:ring-1 focus:ring-emerald-500"
            />
          </div>
        </div>

        {/* Tab Selection */}
        <div className="flex border-b border-slate-200 dark:border-slate-800 text-xs font-semibold">
          <button
            onClick={() => setActiveTab('link')}
            className={`pb-2 px-3 border-b-2 flex items-center gap-1.5 transition-colors cursor-pointer ${
              activeTab === 'link'
                ? 'border-emerald-600 text-emerald-600 dark:text-emerald-400'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            }`}
          >
            <Share2 className="w-3.5 h-3.5" />
            Share Link
          </button>
          <button
            onClick={() => setActiveTab('embed')}
            className={`pb-2 px-3 border-b-2 flex items-center gap-1.5 transition-colors cursor-pointer ${
              activeTab === 'embed'
                ? 'border-emerald-600 text-emerald-600 dark:text-emerald-400'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            }`}
          >
            <Code2 className="w-3.5 h-3.5" />
            Embed Code
          </button>
          <button
            onClick={() => setActiveTab('qr')}
            className={`pb-2 px-3 border-b-2 flex items-center gap-1.5 transition-colors cursor-pointer ${
              activeTab === 'qr'
                ? 'border-emerald-600 text-emerald-600 dark:text-emerald-400'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            }`}
          >
            <QrCode className="w-3.5 h-3.5" />
            QR Code
          </button>
        </div>

        {/* Tab Content */}
        {activeTab === 'link' && (
          <div className="space-y-3">
            <div className="flex items-center gap-2">
              <input
                type="text"
                readOnly
                value={publicUrl}
                className="flex-1 text-xs px-3 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-700 dark:text-slate-300 font-mono"
              />
              <Button
                size="sm"
                variant="outline"
                onClick={() => copyToClipboard(publicUrl, 'Form link copied!')}
              >
                <Copy className="w-3.5 h-3.5 mr-1" />
                Copy
              </Button>
              <a href={publicUrl} target="_blank" rel="noreferrer">
                <Button size="sm" variant="ghost">
                  <ExternalLink className="w-3.5 h-3.5" />
                </Button>
              </a>
            </div>
          </div>
        )}

        {activeTab === 'embed' && (
          <div className="space-y-3">
            <textarea
              readOnly
              rows={3}
              value={embedCode}
              className="w-full text-xs font-mono p-3 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-700 dark:text-slate-300 resize-none"
            />
            <Button
              size="sm"
              variant="outline"
              onClick={() => copyToClipboard(embedCode, 'Embed snippet copied!')}
            >
              <Copy className="w-3.5 h-3.5 mr-1" />
              Copy iFrame HTML
            </Button>
          </div>
        )}

        {activeTab === 'qr' && (
          <div className="flex flex-col items-center justify-center p-4 bg-slate-50 dark:bg-slate-800/40 rounded-xl space-y-3">
            {qrDataUrl && (
              <img
                src={qrDataUrl}
                alt="Form QR Code"
                className="w-44 h-44 rounded-lg bg-white p-2 shadow-xs"
              />
            )}
            <p className="text-xs text-slate-500">Scan to open on any mobile camera</p>
            {qrDataUrl && (
              <a href={qrDataUrl} download={`${slug || 'form'}-qrcode.png`}>
                <Button size="sm" variant="outline">
                  Download QR Image (.PNG)
                </Button>
              </a>
            )}
          </div>
        )}
      </div>
    </Modal>
  );
};
