import React, { useState } from 'react';
import { useBuilderStore } from '@/store/builderStore';
import { FormLogicRule } from '@/types';
import { Button } from '@/components/ui/Button';
import { Plus, Trash2, GitBranch, ArrowRight, Check } from 'lucide-react';

export const LogicEditor: React.FC = () => {
  const { form, addLogicRule, deleteLogicRule } = useBuilderStore();

  const questions = form?.questions || [];
  const rules = form?.logicRules || [];

  const [sourceQuestionId, setSourceQuestionId] = useState<string>(questions[0]?.id || '');
  const [operator, setOperator] = useState<string>('EQUALS');
  const [expectedValue, setExpectedValue] = useState<string>('');
  const [action, setAction] = useState<string>('SHOW');
  const [targetQuestionId, setTargetQuestionId] = useState<string>(questions[1]?.id || '');

  const handleAddRule = () => {
    if (!sourceQuestionId || !targetQuestionId || !expectedValue.trim()) return;

    const newRule: FormLogicRule = {
      id: 'rule_' + Math.random().toString(36).substring(2, 9),
      sourceQuestionId,
      operator: operator as any,
      expectedValue: expectedValue.trim(),
      action: action as any,
      targetQuestionId,
      orderIndex: rules.length,
    };

    addLogicRule(newRule);
    setExpectedValue('');
  };

  const getQuestionTitle = (id: string) => {
    const q = questions.find((item) => item.id === id);
    return q ? q.title : id;
  };

  return (
    <div className="flex flex-col h-full bg-white dark:bg-slate-900 border-l border-slate-200 dark:border-slate-800 w-80 shrink-0 overflow-y-auto scrollbar-thin">
      <div className="p-4 border-b border-slate-100 dark:border-slate-800">
        <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">
          Conditional Logic
        </h3>
        <p className="text-[11px] text-slate-500 mt-0.5">
          Dynamically show, hide, or require questions based on responses.
        </p>
      </div>

      <div className="p-4 space-y-6 flex-1">
        {/* Create Rule Form */}
        <div className="p-3.5 rounded-xl border border-emerald-200 dark:border-emerald-900/60 bg-emerald-50/40 dark:bg-emerald-950/20 space-y-3">
          <span className="text-xs font-bold text-emerald-900 dark:text-emerald-300 flex items-center gap-1.5">
            <GitBranch className="w-3.5 h-3.5" />
            Create Condition
          </span>

          <div className="space-y-2 text-xs">
            <div>
              <label className="text-[10px] font-bold uppercase text-slate-500 block mb-1">
                IF Question
              </label>
              <select
                value={sourceQuestionId}
                onChange={(e) => setSourceQuestionId(e.target.value)}
                className="w-full p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white"
              >
                {questions.map((q) => (
                  <option key={q.id} value={q.id}>
                    {q.title}
                  </option>
                ))}
              </select>
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[10px] font-bold uppercase text-slate-500 block mb-1">
                  Operator
                </label>
                <select
                  value={operator}
                  onChange={(e) => setOperator(e.target.value)}
                  className="w-full p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white"
                >
                  <option value="EQUALS">Equals</option>
                  <option value="NOT_EQUALS">Does Not Equal</option>
                  <option value="CONTAINS">Contains</option>
                  <option value="GREATER_THAN">Greater Than</option>
                  <option value="LESS_THAN">Less Than</option>
                </select>
              </div>

              <div>
                <label className="text-[10px] font-bold uppercase text-slate-500 block mb-1">
                  Value
                </label>
                <input
                  type="text"
                  placeholder="e.g. Yes"
                  value={expectedValue}
                  onChange={(e) => setExpectedValue(e.target.value)}
                  className="w-full p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[10px] font-bold uppercase text-slate-500 block mb-1">
                  THEN Action
                </label>
                <select
                  value={action}
                  onChange={(e) => setAction(e.target.value)}
                  className="w-full p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white"
                >
                  <option value="SHOW">Show</option>
                  <option value="HIDE">Hide</option>
                  <option value="REQUIRE">Make Required</option>
                </select>
              </div>

              <div>
                <label className="text-[10px] font-bold uppercase text-slate-500 block mb-1">
                  Target Question
                </label>
                <select
                  value={targetQuestionId}
                  onChange={(e) => setTargetQuestionId(e.target.value)}
                  className="w-full p-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white"
                >
                  {questions
                    .filter((q) => q.id !== sourceQuestionId)
                    .map((q) => (
                      <option key={q.id} value={q.id}>
                        {q.title}
                      </option>
                    ))}
                </select>
              </div>
            </div>

            <Button
              size="sm"
              onClick={handleAddRule}
              disabled={!expectedValue.trim()}
              className="w-full mt-2"
            >
              <Plus className="w-3.5 h-3.5 mr-1" />
              Add Condition
            </Button>
          </div>
        </div>

        {/* Existing Rules List */}
        <div className="space-y-3">
          <label className="text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider block">
            Configured Rules ({rules.length})
          </label>

          {rules.length === 0 ? (
            <p className="text-xs text-slate-400 italic">No logic rules configured yet.</p>
          ) : (
            rules.map((rule, idx) => (
              <div
                key={rule.id || idx}
                className="p-3 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-800/40 text-xs space-y-1.5"
              >
                <div className="flex items-center justify-between">
                  <span className="font-semibold text-emerald-600 dark:text-emerald-400 text-[10px] uppercase">
                    Rule #{idx + 1}
                  </span>
                  <button
                    onClick={() => deleteLogicRule(idx)}
                    className="text-slate-400 hover:text-rose-600"
                    title="Delete rule"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>

                <div className="text-slate-700 dark:text-slate-300">
                  <p>
                    <strong className="text-slate-900 dark:text-white">IF:</strong> {getQuestionTitle(rule.sourceQuestionId)}
                  </p>
                  <p className="text-slate-500">
                    {rule.operator} "{rule.expectedValue}"
                  </p>
                  <p className="pt-1 text-emerald-600 dark:text-emerald-400 font-semibold flex items-center gap-1">
                    <ArrowRight className="w-3 h-3" />
                    {rule.action} {getQuestionTitle(rule.targetQuestionId || '')}
                  </p>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};
