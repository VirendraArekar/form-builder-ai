import React from 'react';
import { useLocation, useParams, Link } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import { CheckCircle2, ArrowRight, RotateCcw } from 'lucide-react';

export const SubmissionSuccessPage: React.FC = () => {
  const { slug } = useParams<{ slug: string }>();
  const location = useLocation();

  const state = (location.state as { thankYouMessage?: string; submissionId?: string }) || {};
  const thankYouMessage = state.thankYouMessage || 'Thank you! Your response has been securely submitted.';
  const submissionId = state.submissionId;

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex items-center justify-center p-6 font-sans">
      <div className="max-w-md w-full p-8 md:p-10 rounded-3xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-center space-y-6 shadow-xl">
        <div className="w-16 h-16 bg-emerald-100 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 rounded-full flex items-center justify-center mx-auto shadow-sm">
          <CheckCircle2 className="w-10 h-10" />
        </div>

        <div className="space-y-2">
          <h1 className="text-2xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            Submission Confirmed
          </h1>
          <p className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">
            {thankYouMessage}
          </p>
        </div>

        {submissionId && (
          <div className="p-3 bg-slate-50 dark:bg-slate-800/60 border border-slate-200 dark:border-slate-700/60 rounded-xl text-xs space-y-1">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">
              Submission Reference ID
            </span>
            <span className="font-mono font-semibold text-slate-800 dark:text-slate-200">
              {submissionId}
            </span>
          </div>
        )}

        <div className="pt-2 flex flex-col gap-2.5">
          {slug && (
            <Link to={`/f/${slug}`}>
              <Button variant="outline" className="w-full">
                <RotateCcw className="w-4 h-4 mr-2" />
                Submit Another Response
              </Button>
            </Link>
          )}

          <Link to="/">
            <Button variant="ghost" size="sm" className="w-full text-xs text-slate-400 hover:text-slate-600">
              Create your own forms with Timely Forms AI
              <ArrowRight className="w-3.5 h-3.5 ml-1" />
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
};
