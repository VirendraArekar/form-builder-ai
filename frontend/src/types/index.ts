export type SystemRole = 'SUPER_ADMIN' | 'WORKSPACE_OWNER' | 'ADMIN' | 'EDITOR' | 'VIEWER';
export type PlanTier = 'FREE' | 'PRO' | 'BUSINESS';
export type FormStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export type QuestionType =
  | 'SHORT_TEXT'
  | 'LONG_TEXT'
  | 'EMAIL'
  | 'PHONE'
  | 'NUMBER'
  | 'URL'
  | 'DATE'
  | 'TIME'
  | 'DATETIME'
  | 'SINGLE_CHOICE'
  | 'MULTIPLE_CHOICE'
  | 'DROPDOWN'
  | 'YES_NO'
  | 'RATING'
  | 'STAR_RATING'
  | 'LINEAR_SCALE'
  | 'SLIDER'
  | 'CHECKBOX'
  | 'RADIO'
  | 'FILE_UPLOAD'
  | 'IMAGE_UPLOAD'
  | 'SIGNATURE'
  | 'ADDRESS'
  | 'SECTION_HEADING'
  | 'PARAGRAPH'
  | 'HIDDEN_FIELD'
  | 'BUTTON';

export type FormButtonAction = 'SUBMIT' | 'NEXT_SECTION' | 'PREV_SECTION' | 'LINK' | 'RESET';
export type FormButtonVariant = 'primary' | 'secondary' | 'outline' | 'danger';
export type FormButtonAlign = 'left' | 'center' | 'right' | 'full';

export interface FormButtonConfig {
  buttonAction: FormButtonAction;
  buttonVariant: FormButtonVariant;
  buttonAlign: FormButtonAlign;
  buttonText: string;
  buttonUrl?: string;
  openInNewTab?: boolean;
}

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  systemRole?: SystemRole;
  role?: string;
  avatarUrl?: string;
  jobTitle?: string;
  companyName?: string;
  phoneNumber?: string;
  timezone?: string;
  preferredTheme?: string;
}

export interface WorkspaceSummary {
  id: string;
  name: string;
  slug: string;
  role: string;
  planTier: PlanTier;
  description?: string;
  logoUrl?: string;
}

export interface Workspace {
  id: string;
  name: string;
  slug: string;
  description?: string;
  logoUrl?: string;
  ownerId: string;
  ownerName: string;
  planTier: PlanTier;
  maxForms: number;
  maxResponsesPerMonth: number;
  aiGenerationsQuota: number;
  aiGenerationsUsed: number;
  memberCount: number;
  formCount: number;
  userRole: string;
  createdAt: string;
}

export interface WorkspaceMember {
  id: string;
  userId: string;
  email: string;
  name?: string;
  firstName?: string;
  lastName?: string;
  avatarUrl?: string;
  role: string;
  joinedAt: string;
}

export interface FormOption {
  id?: string;
  label: string;
  value: string;
  orderIndex: number;
  isDefault?: boolean;
}

export interface FormQuestion {
  id: string;
  sectionId?: string;
  fieldKey: string;
  title: string;
  description?: string;
  questionType: QuestionType;
  type: QuestionType;
  placeholder?: string;
  isRequired: boolean;
  defaultValue?: string;
  minLength?: number;
  maxLength?: number;
  minValue?: number;
  maxValue?: number;
  regexPattern?: string;
  allowedFileTypes?: string;
  maxFileSizeMb?: number;
  orderIndex: number;
  configJson?: string;
  options: FormOption[];
  validationRules?: Record<string, any>;
}

export interface FormSection {
  id: string;
  title: string;
  description?: string;
  orderIndex: number;
  questions?: FormQuestion[];
}

export type ConditionOperator =
  | 'EQUALS'
  | 'NOT_EQUALS'
  | 'CONTAINS'
  | 'GREATER_THAN'
  | 'LESS_THAN'
  | 'IS_EMPTY'
  | 'IS_NOT_EMPTY';

export type ActionType = 'SHOW' | 'HIDE' | 'REQUIRE' | 'JUMP_TO_SECTION';

export interface FormLogicRule {
  id?: string;
  sourceQuestionId: string;
  targetQuestionId?: string;
  targetSectionId?: string;
  conditionOperator?: ConditionOperator;
  operator?: ConditionOperator;
  conditionValue?: string;
  expectedValue?: string;
  actionType?: ActionType;
  action?: ActionType;
  logicalCombinator?: 'AND' | 'OR';
  ruleGroup?: number;
  orderIndex?: number;
}

export interface FormTheme {
  primaryColor: string;
  backgroundColor: string;
  textColor?: string;
  fontFamily: string;
  borderRadius: string;
  headerImageUrl?: string;
  coverImageUrl?: string;
  logoUrl?: string;
  buttonStyle?: string;
  layoutMode?: 'single_page' | 'card_per_step';
  showProgressBar?: boolean;
}

export interface FormSummary {
  id: string;
  workspaceId: string;
  title: string;
  description?: string;
  status: FormStatus;
  publishedSlug?: string;
  isPasswordProtected: boolean;
  isFavorite?: boolean;
  submissionCount: number;
  viewCount: number;
  questionCount?: number;
  submissionLimit?: number;
  startsAt?: string;
  expiresAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface FormDetail extends FormSummary {
  workspaceName?: string;
  redirectUrl?: string;
  thankYouMessage?: string;
  customThankYouTitle?: string;
  customThankYouMessage?: string;
  allowMultipleSubmissions: boolean;
  sendEmailNotification: boolean;
  isFavorite?: boolean;
  notificationEmail?: string;
  maxSubmissions?: number;
  startDate?: string;
  endDate?: string;
  sections: FormSection[];
  questions: FormQuestion[];
  logicRules: FormLogicRule[];
  theme: FormTheme;
}

export interface PublicFormSchema {
  id: string;
  title: string;
  description?: string;
  slug?: string;
  isPasswordProtected: boolean;
  isExpired?: boolean;
  isLimitReached?: boolean;
  startsInFuture?: boolean;
  startsAt?: string;
  expiresAt?: string;
  theme: FormTheme;
  sections: FormSection[];
  questions: FormQuestion[];
  logicRules: FormLogicRule[];
  thankYouTitle?: string;
  thankYouMessage?: string;
  redirectUrl?: string;
}

export interface ResponseTag {
  id: string;
  tagName: string;
  color: string;
  createdAt: string;
}

export interface ResponseNote {
  id: string;
  authorId: string;
  authorName: string;
  noteText: string;
  createdAt: string;
}

export interface ResponseAnswer {
  id: string;
  questionId: string;
  questionKey?: string;
  questionTitle: string;
  questionType: QuestionType;
  answerText?: string;
  answerJson?: string;
  answerValue?: any;
  fieldKey?: string;
}

export interface FormResponseSummary {
  id: string;
  formId: string;
  respondentIp?: string;
  userAgent?: string;
  durationSeconds: number;
  status: string;
  isReviewed: boolean;
  submittedAt: string;
  notesCount: number;
  tags: ResponseTag[];
  answers: Record<string, string>;
  answersSummary?: string;
}

export interface FormResponseDetail {
  id: string;
  formId: string;
  formTitle: string;
  respondentIp?: string;
  userAgent?: string;
  referrer?: string;
  durationSeconds: number;
  status: string;
  isReviewed: boolean;
  submittedAt: string;
  answers: ResponseAnswer[];
  notes: ResponseNote[];
  tags: ResponseTag[];
  ipAddress?: string;
}

export interface DailyTrend {
  date: string;
  views: number;
  submissions: number;
  completions: number;
}

export interface OptionMetric {
  label: string;
  count: number;
  percentage: number;
}

export interface NumericStats {
  average: number;
  min: number;
  max: number;
  median: number;
}

export interface QuestionAnalytics {
  questionId: string;
  questionKey: string;
  questionTitle: string;
  questionType: QuestionType;
  totalAnswers: number;
  optionMetrics: OptionMetric[];
  numericStats?: NumericStats;
  sampleAnswers: string[];
}

export interface FormAnalyticsOverview {
  totalViews: number;
  totalSubmissions: number;
  completionRate: number;
  abandonmentRate: number;
  avgDurationSeconds: number;
  dailyTrends: DailyTrend[];
  deviceDistribution: Record<string, number>;
}

export interface FormPerformance {
  formId: string;
  title: string;
  status: FormStatus;
  views: number;
  submissions: number;
  completionRate: number;
}

export interface RecentSubmission {
  responseId: string;
  formId: string;
  formTitle: string;
  respondentIp?: string;
  durationSeconds: number;
  submittedAt: string;
}

export interface WorkspaceAnalytics {
  totalForms: number;
  publishedForms: number;
  draftForms: number;
  totalResponses: number;
  totalSubmissions?: number;
  overallCompletionRate: number;
  averageCompletionRate?: number;
  avgDurationSeconds: number;
  responseTrends: DailyTrend[];
  topPerformingForms: FormPerformance[];
  recentSubmissions: RecentSubmission[];
}

export interface AiFormSchema {
  title: string;
  description: string;
  sections: FormSection[];
  questions: FormQuestion[];
  logicRules: FormLogicRule[];
  theme: FormTheme;
  customThankYouTitle?: string;
  customThankYouMessage?: string;
  estimatedCompletionTimeMinutes?: number;
}

export interface AiInsights {
  summary: string;
  overallSentimentScore?: number;
  sentimentBreakdown?: Record<string, number>;
  sentimentAnalysis?: {
    positivePercentage: number;
    neutralPercentage: number;
    negativePercentage: number;
  };
  positiveHighlights?: string[];
  commonComplaints?: string[];
  complaints?: string[];
  emergingThemes?: string[];
  actionableRecommendations?: string[];
  recommendations?: string[];
  answerToQuery?: string;
}

export interface FormTemplate {
  id: string;
  title: string;
  description: string;
  category: string;
  icon?: string;
  templateSchema?: string;
  isFeatured?: boolean;
  usageCount?: number;
  questionsCount?: number;
}

export interface NotificationItem {
  id: string;
  title: string;
  message: string;
  type: string;
  linkUrl?: string;
  isRead: boolean;
  createdAt: string;
}

export interface Webhook {
  id: string;
  workspaceId: string;
  formId?: string;
  targetUrl: string;
  secretKey: string;
  isActive: boolean;
  subscribedEvents: string;
  createdAt: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface InboxItemDto {
  responseId: string;
  formId: string;
  formTitle: string;
  formThemeColor?: string;
  respondentName?: string;
  respondentEmail?: string;
  responsePreview?: string;
  submittedAt: string;
  durationSeconds: number;
  deviceType: string;
  reviewed: boolean;
  answers: Record<string, string>;
}

export interface LeaderboardFormDto {
  formId: string;
  title: string;
  status: string;
  submissions: number;
  views: number;
  conversionRate: number;
}

export interface WorkspaceInsightsDetailDto {
  totalForms: number;
  publishedForms: number;
  totalResponses: number;
  totalViews: number;
  overallConversionRate: number;
  avgCompletionTimeSeconds: number;
  responseTimeline: Array<{ date: string; responses: number }>;
  dailyActivity: Array<{ day: string; count: number }>;
  weeklyActivity: Array<{ week: string; submissions: number }>;
  hourlyHeatmap: Array<{ hour: number; count: number }>;
  deviceDistribution: Record<string, number>;
  leaderboard: LeaderboardFormDto[];
}
