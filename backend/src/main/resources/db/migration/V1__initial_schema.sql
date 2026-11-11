-- FormGenius AI Database Schema
-- Flyway Migration V1: Initial Schema

-- 1. Users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    system_role VARCHAR(50) NOT NULL DEFAULT 'EDITOR',
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_password_token VARCHAR(255),
    reset_token_expiry TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 2. User Profiles
CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    avatar_url VARCHAR(500),
    job_title VARCHAR(100),
    company_name VARCHAR(100),
    phone_number VARCHAR(50),
    timezone VARCHAR(50) DEFAULT 'UTC',
    preferred_theme VARCHAR(20) DEFAULT 'system',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. Workspaces
CREATE TABLE IF NOT EXISTS workspaces (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    logo_url VARCHAR(500),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    plan_tier VARCHAR(50) NOT NULL DEFAULT 'FREE', -- FREE, PRO, BUSINESS
    max_forms INT NOT NULL DEFAULT 5,
    max_responses_per_month INT NOT NULL DEFAULT 100,
    ai_generations_quota INT NOT NULL DEFAULT 20,
    ai_generations_used INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_workspaces_owner ON workspaces(owner_id);
CREATE INDEX IF NOT EXISTS idx_workspaces_slug ON workspaces(slug);

-- 4. Workspace Members
CREATE TABLE IF NOT EXISTS workspace_members (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL DEFAULT 'EDITOR', -- SUPER_ADMIN, WORKSPACE_OWNER, ADMIN, EDITOR, VIEWER
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_workspace_user UNIQUE (workspace_id, user_id)
);
CREATE INDEX IF NOT EXISTS idx_ws_members_user ON workspace_members(user_id);

-- 5. Workspace Invitations
CREATE TABLE IF NOT EXISTS workspace_invitations (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'EDITOR',
    token VARCHAR(255) NOT NULL UNIQUE,
    invited_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, ACCEPTED, DECLINED, EXPIRED
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. Forms
CREATE TABLE IF NOT EXISTS forms (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- DRAFT, PUBLISHED, ARCHIVED
    published_slug VARCHAR(255) UNIQUE,
    is_password_protected BOOLEAN NOT NULL DEFAULT FALSE,
    access_password_hash VARCHAR(255),
    submission_limit INT,
    submission_count INT NOT NULL DEFAULT 0,
    view_count INT NOT NULL DEFAULT 0,
    starts_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    redirect_url VARCHAR(500),
    custom_thank_you_title VARCHAR(255),
    custom_thank_you_message TEXT,
    allow_multiple_submissions BOOLEAN NOT NULL DEFAULT TRUE,
    send_email_notification BOOLEAN NOT NULL DEFAULT FALSE,
    notification_email VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_forms_workspace ON forms(workspace_id);
CREATE INDEX IF NOT EXISTS idx_forms_published_slug ON forms(published_slug);

-- 7. Form Versions
CREATE TABLE IF NOT EXISTS form_versions (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    version_number INT NOT NULL,
    schema_snapshot JSONB NOT NULL,
    change_summary TEXT,
    created_by UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. Form Sections
CREATE TABLE IF NOT EXISTS form_sections (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_form_sections_form ON form_sections(form_id, order_index);

-- 9. Form Questions
CREATE TABLE IF NOT EXISTS form_questions (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    section_id UUID REFERENCES form_sections(id) ON DELETE SET NULL,
    field_key VARCHAR(100) NOT NULL,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    question_type VARCHAR(50) NOT NULL,
    placeholder VARCHAR(255),
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    default_value TEXT,
    min_length INT,
    max_length INT,
    min_value DOUBLE PRECISION,
    max_value DOUBLE PRECISION,
    regex_pattern VARCHAR(500),
    allowed_file_types VARCHAR(500),
    max_file_size_mb INT,
    order_index INT NOT NULL DEFAULT 0,
    config_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_form_questions_form ON form_questions(form_id, order_index);

-- 10. Form Question Options
CREATE TABLE IF NOT EXISTS form_question_options (
    id UUID PRIMARY KEY,
    question_id UUID NOT NULL REFERENCES form_questions(id) ON DELETE CASCADE,
    label VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    order_index INT NOT NULL DEFAULT 0,
    is_default BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE INDEX IF NOT EXISTS idx_form_options_question ON form_question_options(question_id, order_index);

-- 11. Form Logic Rules
CREATE TABLE IF NOT EXISTS form_logic_rules (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    source_question_id UUID NOT NULL REFERENCES form_questions(id) ON DELETE CASCADE,
    target_question_id UUID REFERENCES form_questions(id) ON DELETE CASCADE,
    target_section_id UUID REFERENCES form_sections(id) ON DELETE CASCADE,
    condition_operator VARCHAR(50) NOT NULL, -- EQUALS, NOT_EQUALS, CONTAINS, GREATER_THAN, LESS_THAN, IS_EMPTY, IS_NOT_EMPTY
    condition_value TEXT,
    action_type VARCHAR(50) NOT NULL, -- SHOW, HIDE, REQUIRE, JUMP_TO_SECTION
    logical_combinator VARCHAR(10) NOT NULL DEFAULT 'AND', -- AND, OR
    rule_group INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_logic_rules_form ON form_logic_rules(form_id);

-- 12. Form Themes
CREATE TABLE IF NOT EXISTS form_themes (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL UNIQUE REFERENCES forms(id) ON DELETE CASCADE,
    primary_color VARCHAR(50) DEFAULT '#4F46E5',
    background_color VARCHAR(50) DEFAULT '#FFFFFF',
    text_color VARCHAR(50) DEFAULT '#111827',
    font_family VARCHAR(100) DEFAULT 'Inter',
    border_radius VARCHAR(20) DEFAULT 'md',
    header_image_url VARCHAR(500),
    logo_url VARCHAR(500),
    button_style VARCHAR(50) DEFAULT 'rounded',
    layout_mode VARCHAR(50) DEFAULT 'single_page', -- single_page, card_per_step
    show_progress_bar BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 13. Form Publications
CREATE TABLE IF NOT EXISTS form_publications (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    published_by UUID NOT NULL REFERENCES users(id),
    published_slug VARCHAR(255) NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 14. Form Responses
CREATE TABLE IF NOT EXISTS form_responses (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    respondent_ip VARCHAR(100),
    user_agent VARCHAR(500),
    referrer VARCHAR(500),
    duration_seconds INT DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'COMPLETED', -- COMPLETED, PARTIAL
    is_reviewed BOOLEAN NOT NULL DEFAULT FALSE,
    submitted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_form_responses_form ON form_responses(form_id, submitted_at DESC);

-- 15. Response Answers
CREATE TABLE IF NOT EXISTS response_answers (
    id UUID PRIMARY KEY,
    response_id UUID NOT NULL REFERENCES form_responses(id) ON DELETE CASCADE,
    question_id UUID NOT NULL REFERENCES form_questions(id) ON DELETE CASCADE,
    question_key VARCHAR(100) NOT NULL,
    answer_text TEXT,
    answer_json JSONB
);
CREATE INDEX IF NOT EXISTS idx_resp_answers_response ON response_answers(response_id);
CREATE INDEX IF NOT EXISTS idx_resp_answers_question ON response_answers(question_id);

-- 16. Response Files
CREATE TABLE IF NOT EXISTS response_files (
    id UUID PRIMARY KEY,
    response_id UUID NOT NULL REFERENCES form_responses(id) ON DELETE CASCADE,
    question_id UUID NOT NULL REFERENCES form_questions(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    uploaded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 17. Response Notes
CREATE TABLE IF NOT EXISTS response_notes (
    id UUID PRIMARY KEY,
    response_id UUID NOT NULL REFERENCES form_responses(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    note_text TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 18. Response Tags
CREATE TABLE IF NOT EXISTS response_tags (
    id UUID PRIMARY KEY,
    response_id UUID NOT NULL REFERENCES form_responses(id) ON DELETE CASCADE,
    tag_name VARCHAR(100) NOT NULL,
    color VARCHAR(50) DEFAULT '#6366F1',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_resp_tags ON response_tags(response_id, tag_name);

-- 19. Form Analytics
CREATE TABLE IF NOT EXISTS form_analytics (
    id UUID PRIMARY KEY,
    form_id UUID NOT NULL REFERENCES forms(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    views INT NOT NULL DEFAULT 0,
    starts INT NOT NULL DEFAULT 0,
    submissions INT NOT NULL DEFAULT 0,
    completions INT NOT NULL DEFAULT 0,
    average_duration_seconds INT DEFAULT 0,
    CONSTRAINT uq_form_analytics_date UNIQUE (form_id, date)
);
CREATE INDEX IF NOT EXISTS idx_form_analytics_form ON form_analytics(form_id, date DESC);

-- 20. Workspace Analytics
CREATE TABLE IF NOT EXISTS workspace_analytics (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    total_views INT NOT NULL DEFAULT 0,
    total_submissions INT NOT NULL DEFAULT 0,
    active_forms INT NOT NULL DEFAULT 0,
    CONSTRAINT uq_workspace_analytics_date UNIQUE (workspace_id, date)
);

-- 21. Form Templates
CREATE TABLE IF NOT EXISTS form_templates (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    icon VARCHAR(50) DEFAULT 'FileText',
    template_schema JSONB NOT NULL,
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    usage_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_templates_category ON form_templates(category);

-- 22. Notifications
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    workspace_id UUID REFERENCES workspaces(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL DEFAULT 'INFO', -- INFO, SUBMISSION, ALERT, SYSTEM
    link_url VARCHAR(500),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id, is_read, created_at DESC);

-- 23. Webhooks
CREATE TABLE IF NOT EXISTS webhooks (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    form_id UUID REFERENCES forms(id) ON DELETE CASCADE,
    target_url VARCHAR(500) NOT NULL,
    secret_key VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    subscribed_events VARCHAR(500) NOT NULL, -- comma-separated: form.published,response.created,etc.
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 24. Webhook Deliveries
CREATE TABLE IF NOT EXISTS webhook_deliveries (
    id UUID PRIMARY KEY,
    webhook_id UUID NOT NULL REFERENCES webhooks(id) ON DELETE CASCADE,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    response_status INT,
    response_body TEXT,
    duration_ms INT,
    is_successful BOOLEAN NOT NULL DEFAULT FALSE,
    delivered_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 25. Audit Logs
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY,
    workspace_id UUID REFERENCES workspaces(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100) NOT NULL,
    resource_id VARCHAR(100),
    details JSONB,
    ip_address VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_audit_logs_ws ON audit_logs(workspace_id, created_at DESC);

-- 26. AI Generation History
CREATE TABLE IF NOT EXISTS ai_generation_history (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    prompt TEXT NOT NULL,
    generation_type VARCHAR(50) NOT NULL, -- FORM_GENERATION, QUESTION_IMPROVE, TRANSLATION, INSIGHTS
    model_name VARCHAR(100) NOT NULL,
    result_schema JSONB,
    tokens_used INT DEFAULT 0,
    latency_ms INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
