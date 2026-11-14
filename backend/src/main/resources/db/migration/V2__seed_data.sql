-- FormGenius AI Seed Data
-- Migration V2: Default User, Workspace, Templates, Demo Forms & Submissions

-- 1. Insert Default User (password is 'Password123!')
-- BCrypt for 'Password123!': $2a$10$J3wRuBuNnAsXnwl980eVq.3U/tVlEwZeKDTw7KTZlFdVVmZOfXdYy
INSERT INTO users (id, email, password_hash, first_name, last_name, system_role, is_email_verified, is_active, created_at, updated_at)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'alex@formgenius.ai',
    '$2a$10$J3wRuBuNnAsXnwl980eVq.3U/tVlEwZeKDTw7KTZlFdVVmZOfXdYy',
    'Alex',
    'Mercer',
    'SUPER_ADMIN',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, email, password_hash, first_name, last_name, system_role, is_email_verified, is_active, created_at, updated_at)
VALUES (
    '11111111-1111-1111-1111-222222222222',
    'demo@timelyforms.ai',
    '$2a$10$J3wRuBuNnAsXnwl980eVq.3U/tVlEwZeKDTw7KTZlFdVVmZOfXdYy',
    'Demo',
    'Admin',
    'SUPER_ADMIN',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- 2. Insert User Profile
INSERT INTO user_profiles (id, user_id, job_title, company_name, phone_number, timezone, preferred_theme)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111',
    'Product Lead',
    'Acme Innovations',
    '+1 (555) 019-2834',
    'America/New_York',
    'system'
) ON CONFLICT (id) DO NOTHING;

-- 3. Insert Default Workspace
INSERT INTO workspaces (id, name, slug, description, owner_id, plan_tier, max_forms, max_responses_per_month, ai_generations_quota, ai_generations_used)
VALUES (
    '33333333-3333-3333-3333-333333333333',
    'Acme Workspace',
    'acme-workspace',
    'Primary team workspace for customer engagement and product feedback.',
    '11111111-1111-1111-1111-111111111111',
    'PRO',
    50,
    5000,
    100,
    14
) ON CONFLICT (id) DO NOTHING;

-- 4. Insert Workspace Membership
INSERT INTO workspace_members (id, workspace_id, user_id, role)
VALUES (
    '44444444-4444-4444-4444-444444444444',
    '33333333-3333-3333-3333-333333333333',
    '11111111-1111-1111-1111-111111111111',
    'WORKSPACE_OWNER'
) ON CONFLICT (id) DO NOTHING;

-- 5. Insert Templates (15 rich templates)
INSERT INTO form_templates (id, title, description, category, icon, is_featured, usage_count, template_schema)
VALUES
(
    '55555555-0001-0000-0000-000000000001',
    'Customer Feedback & NPS Survey',
    'Measure customer satisfaction, gather NPS ratings, delivery experience, and product suggestions.',
    'Customer Feedback',
    'HeartHandshake',
    TRUE,
    1420,
    '{"title": "Customer Feedback & NPS Survey", "description": "Help us improve by sharing your recent shopping experience.", "sections": [{"title": "Personal Details", "questions": [{"title": "Full Name", "type": "SHORT_TEXT", "required": true, "key": "full_name"}, {"title": "Email Address", "type": "EMAIL", "required": true, "key": "email"}]}, {"title": "Product Experience", "questions": [{"title": "How satisfied were you with the product quality?", "type": "STAR_RATING", "required": true, "key": "product_rating"}, {"title": "How likely are you to recommend us? (NPS 0-10)", "type": "LINEAR_SCALE", "required": true, "key": "nps_score"}, {"title": "What did you enjoy most about our service?", "type": "LONG_TEXT", "required": false, "key": "positive_feedback"}]}]}'
),
(
    '55555555-0002-0000-0000-000000000002',
    'General Contact & Inquiries',
    'Clean, elegant contact form with subject selection, priority, and file attachment.',
    'Contact Forms',
    'Mail',
    TRUE,
    980,
    '{"title": "Get in Touch With Our Team", "description": "Have questions or need assistance? Fill out this form and we will get back within 24 hours.", "sections": [{"title": "Contact Information", "questions": [{"title": "Your Name", "type": "SHORT_TEXT", "required": true, "key": "name"}, {"title": "Work Email", "type": "EMAIL", "required": true, "key": "email"}, {"title": "Inquiry Department", "type": "SINGLE_CHOICE", "options": ["Sales", "Customer Support", "Partnerships", "Press"], "required": true, "key": "department"}, {"title": "Your Message", "type": "LONG_TEXT", "required": true, "key": "message"}]}]}'
),
(
    '55555555-0003-0000-0000-000000000003',
    'Software Engineer Job Application',
    'Screen top engineering talent with work history, portfolio link, coding experience, and resume upload.',
    'Job Applications',
    'Briefcase',
    TRUE,
    850,
    '{"title": "Senior Full-Stack Engineer Application", "description": "Join our fast-paced product engineering team building the next generation AI form builder.", "sections": [{"title": "Candidate Profile", "questions": [{"title": "Candidate Name", "type": "SHORT_TEXT", "required": true, "key": "name"}, {"title": "Email Address", "type": "EMAIL", "required": true, "key": "email"}, {"title": "GitHub / Portfolio URL", "type": "URL", "required": true, "key": "portfolio"}, {"title": "Years of Experience in React & Java", "type": "NUMBER", "required": true, "key": "years_exp"}, {"title": "Upload Resume (PDF)", "type": "FILE_UPLOAD", "required": true, "key": "resume"}]}]}'
),
(
    '55555555-0004-0000-0000-000000000004',
    'Employee Engagement & Culture Survey',
    'Confidential pulse check on team morale, work-life balance, leadership trust, and growth.',
    'Employee Surveys',
    'Users',
    FALSE,
    430,
    '{"title": "Q4 Employee Engagement Survey", "description": "Your honest feedback helps leadership build an inclusive and inspiring workplace.", "sections": [{"title": "Work Environment", "questions": [{"title": "I feel valued and recognized for my contributions.", "type": "RATING", "required": true, "key": "valued_rating"}, {"title": "I have the tools and resources I need to do my job well.", "type": "YES_NO", "required": true, "key": "tools_available"}, {"title": "Suggestions for team culture", "type": "LONG_TEXT", "required": false, "key": "culture_suggestions"}]}]}'
),
(
    '55555555-0005-0000-0000-000000000005',
    'Tech Summit & Conference Registration',
    'Multi-track conference registration with badge details, ticket tier, dietary preferences, and workshop picks.',
    'Event Registration',
    'Calendar',
    TRUE,
    1150,
    '{"title": "Global Tech Summit 2026 Registration", "description": "Register for keynote sessions, technical workshops, and networking receptions.", "sections": [{"title": "Attendee Details", "questions": [{"title": "Attendee Name", "type": "SHORT_TEXT", "required": true, "key": "name"}, {"title": "Company & Job Title", "type": "SHORT_TEXT", "required": true, "key": "company_title"}, {"title": "Pass Type", "type": "SINGLE_CHOICE", "options": ["General Pass", "VIP All-Access", "Virtual Pass"], "required": true, "key": "pass_type"}, {"title": "Dietary Restrictions", "type": "MULTIPLE_CHOICE", "options": ["Vegetarian", "Vegan", "Gluten-Free", "Halal", "Kosher", "None"], "required": false, "key": "diet"}]}]}'
),
(
    '55555555-0006-0000-0000-000000000006',
    'Student Course Evaluation',
    'Collect feedback on course curriculum, instructor clarity, assignment difficulty, and overall learning.',
    'Education',
    'GraduationCap',
    FALSE,
    620,
    '{"title": "End-of-Semester Course Evaluation", "description": "Help us evaluate the curriculum and instructional quality.", "sections": [{"title": "Evaluation", "questions": [{"title": "Course Name", "type": "SHORT_TEXT", "required": true, "key": "course_name"}, {"title": "Instructor Clarity", "type": "STAR_RATING", "required": true, "key": "instructor_rating"}, {"title": "Course Material Quality", "type": "RATING", "required": true, "key": "material_rating"}, {"title": "What topics would you like covered in more depth?", "type": "LONG_TEXT", "required": false, "key": "topics_depth"}]}]}'
),
(
    '55555555-0007-0000-0000-000000000007',
    'Patient Intake & Health History',
    'Digital medical intake questionnaire collecting symptoms, allergies, insurance, and medical background.',
    'Healthcare',
    'Activity',
    FALSE,
    390,
    '{"title": "New Patient Medical Intake", "description": "Please complete your health intake prior to your appointment.", "sections": [{"title": "Patient Demographics", "questions": [{"title": "Full Legal Name", "type": "SHORT_TEXT", "required": true, "key": "patient_name"}, {"title": "Date of Birth", "type": "DATE", "required": true, "key": "dob"}, {"title": "Primary Symptom or Reason for Visit", "type": "LONG_TEXT", "required": true, "key": "symptoms"}, {"title": "Known Drug Allergies", "type": "SHORT_TEXT", "required": false, "key": "allergies"}]}]}'
),
(
    '55555555-0008-0000-0000-000000000008',
    'Product Launch Waitlist & Lead Gen',
    'High-converting lead generation form capturing email, company size, use case, and early access beta interest.',
    'Lead Generation',
    'Sparkles',
    TRUE,
    1890,
    '{"title": "Early Access Beta Waitlist", "description": "Be the first to access our new AI productivity suite. Reserved spots are limited.", "sections": [{"title": "Join Waitlist", "questions": [{"title": "Work Email", "type": "EMAIL", "required": true, "key": "email"}, {"title": "Company Size", "type": "SINGLE_CHOICE", "options": ["1-10", "11-50", "51-200", "201-1000", "1000+"], "required": true, "key": "company_size"}, {"title": "What problem are you hoping to solve?", "type": "LONG_TEXT", "required": true, "key": "use_case"}]}]}'
),
(
    '55555555-0009-0000-0000-000000000009',
    'User Research & Usability Interview Screener',
    'Screen participants for moderated UX testing, prototypes, and customer discovery interviews.',
    'Product Research',
    'Search',
    FALSE,
    510,
    '{"title": "Product Research Participant Screener", "description": "We are looking for users to participate in a 30-minute paid research session.", "sections": [{"title": "Screening Questions", "questions": [{"title": "How frequently do you create online forms?", "type": "SINGLE_CHOICE", "options": ["Daily", "Weekly", "Monthly", "Rarely"], "required": true, "key": "frequency"}, {"title": "Which tools do you currently use?", "type": "MULTIPLE_CHOICE", "options": ["Google Forms", "Typeform", "Jotform", "Airtable", "Other"], "required": true, "key": "tools"}, {"title": "Are you open to a 30-minute video call for a $50 gift card?", "type": "YES_NO", "required": true, "key": "video_call_ok"}]}]}'
),
(
    '55555555-0010-0000-0000-000000000010',
    'CSAT (Customer Satisfaction Score)',
    'Fast 2-question post-support ticket satisfaction survey with one-click rating.',
    'Customer Satisfaction',
    'Smile',
    TRUE,
    1340,
    '{"title": "Support Ticket Satisfaction Survey", "description": "Tell us how our support agent handled your recent request.", "sections": [{"title": "Satisfaction", "questions": [{"title": "How satisfied are you with the resolution provided?", "type": "STAR_RATING", "required": true, "key": "csat_rating"}, {"title": "Was your issue resolved on the first interaction?", "type": "YES_NO", "required": true, "key": "first_contact_resolution"}, {"title": "Additional feedback or compliments", "type": "LONG_TEXT", "required": false, "key": "comments"}]}]}'
),
(
    '55555555-0011-0000-0000-000000000011',
    'B2B Vendor & Supplier Risk Assessment',
    'Evaluate security compliance, ISO certifications, and data handling policies of third-party vendors.',
    'Business Surveys',
    'ShieldCheck',
    FALSE,
    280,
    '{"title": "Vendor Security & Compliance Assessment", "description": "Mandatory due diligence questionnaire for all enterprise partners and vendors.", "sections": [{"title": "Compliance Information", "questions": [{"title": "Vendor Organization Name", "type": "SHORT_TEXT", "required": true, "key": "org_name"}, {"title": "Do you hold active SOC 2 Type II or ISO 27001 certifications?", "type": "YES_NO", "required": true, "key": "cert_status"}, {"title": "Describe data encryption in transit and at rest", "type": "LONG_TEXT", "required": true, "key": "encryption_desc"}]}]}'
),
(
    '55555555-0012-0000-0000-000000000012',
    'Interactive General Knowledge Quiz',
    'Gamified multiple-choice quiz with scoring rules and instant feedback.',
    'Quizzes',
    'Award',
    FALSE,
    770,
    '{"title": "Cloud Computing Fundamentals Quiz", "description": "Test your knowledge on scalable architectures and modern cloud primitives.", "sections": [{"title": "Questions", "questions": [{"title": "Which cloud service model offers the highest abstraction?", "type": "SINGLE_CHOICE", "options": ["IaaS", "PaaS", "SaaS", "BaaS"], "required": true, "key": "q1"}, {"title": "True or False: PostgreSQL supports JSONB indexing via GIN indexes.", "type": "YES_NO", "required": true, "key": "q2"}]}]}'
),
(
    '55555555-0013-0000-0000-000000000013',
    'Executive Consultation Booking',
    'Collect meeting objectives, team size, preferred dates and time slots for executive consulting.',
    'Appointment Booking',
    'Clock',
    FALSE,
    640,
    '{"title": "Strategy Consultation Request", "description": "Book a strategic advisory session with our principal solutions architect.", "sections": [{"title": "Session Details", "questions": [{"title": "Your Name & Title", "type": "SHORT_TEXT", "required": true, "key": "client_name"}, {"title": "Preferred Consultation Date", "type": "DATE", "required": true, "key": "preferred_date"}, {"title": "Primary Discussion Topics", "type": "MULTIPLE_CHOICE", "options": ["Architecture Review", "AI Integration Roadmap", "Database Performance Tuning", "Security Audit"], "required": true, "key": "topics"}]}]}'
),
(
    '55555555-0014-0000-0000-000000000014',
    'New Employee Onboarding Checklist',
    'HR checklist for new hires to submit emergency contacts, equipment preferences, and t-shirt size.',
    'HR and Recruitment',
    'UserCheck',
    FALSE,
    490,
    '{"title": "New Hire Equipment & Welcome Setup", "description": "Welcome to the team! Help us get your workstation ready before day one.", "sections": [{"title": "Workstation Preference", "questions": [{"title": "Laptop Preference", "type": "SINGLE_CHOICE", "options": ["MacBook Pro 16-inch M3", "Dell XPS 15 Linux", "ThinkPad P1 Windows"], "required": true, "key": "laptop_choice"}, {"title": "Emergency Contact Name & Phone", "type": "SHORT_TEXT", "required": true, "key": "emergency_contact"}, {"title": "Swag T-Shirt Size", "type": "SINGLE_CHOICE", "options": ["S", "M", "L", "XL", "2XL"], "required": true, "key": "tshirt_size"}]}]}'
),
(
    '55555555-0015-0000-0000-000000000015',
    'SaaS Churn & Cancellation Survey',
    'Understand why subscribers cancel, competitor choices, and feedback to win them back.',
    'Marketing',
    'TrendingDown',
    FALSE,
    810,
    '{"title": "Subscription Cancellation Survey", "description": "We are sorry to see you go. Help us understand where we fell short.", "sections": [{"title": "Cancellation Reason", "questions": [{"title": "What was the main reason for cancelling today?", "type": "SINGLE_CHOICE", "options": ["Too expensive", "Missing features", "Switched to alternative", "Project finished", "Difficult to use"], "required": true, "key": "reason"}, {"title": "What could we have done differently to keep you as a customer?", "type": "LONG_TEXT", "required": false, "key": "improvement_suggestion"}]}]}'
) ON CONFLICT (id) DO NOTHING;

-- 6. Insert Demo Form 1: Customer Feedback (Published)
INSERT INTO forms (
    id, workspace_id, created_by, title, description, status, published_slug,
    is_password_protected, submission_count, view_count, custom_thank_you_title,
    custom_thank_you_message, created_at, updated_at
) VALUES (
    '66666666-1111-1111-1111-111111111111',
    '33333333-3333-3333-3333-333333333333',
    '11111111-1111-1111-1111-111111111111',
    'Customer Feedback & Delivery Survey',
    'Tell us about your recent online purchase, shipping delivery, and satisfaction.',
    'PUBLISHED',
    'customer-feedback-demo',
    FALSE,
    18,
    42,
    'Thank You For Your Valuable Feedback!',
    'Your responses directly help our product and logistics teams optimize the customer experience.',
    CURRENT_TIMESTAMP - INTERVAL '5 days',
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

-- Theme for Demo Form 1
INSERT INTO form_themes (
    id, form_id, primary_color, background_color, text_color, font_family, border_radius, layout_mode, show_progress_bar
) VALUES (
    '77777777-1111-1111-1111-111111111111',
    '66666666-1111-1111-1111-111111111111',
    '#10B981',
    '#FFFFFF',
    '#0F172A',
    'Inter',
    'md',
    'single_page',
    TRUE
) ON CONFLICT (id) DO NOTHING;

-- Sections for Demo Form 1
INSERT INTO form_sections (id, form_id, title, description, order_index)
VALUES
('88888888-1001-0000-0000-000000000001', '66666666-1111-1111-1111-111111111111', 'Contact & Order Details', 'Basic information about your order', 0),
('88888888-1002-0000-0000-000000000002', '66666666-1111-1111-1111-111111111111', 'Delivery & Service Experience', 'Help us evaluate shipping and fulfillment', 1),
('88888888-1003-0000-0000-000000000003', '66666666-1111-1111-1111-111111111111', 'Overall Sentiment & Suggestions', 'Your voice helps us improve', 2)
ON CONFLICT (id) DO NOTHING;

-- Questions for Demo Form 1
INSERT INTO form_questions (
    id, form_id, section_id, field_key, title, description, question_type,
    placeholder, is_required, order_index
) VALUES
('99999999-1001-0000-0000-000000000001', '66666666-1111-1111-1111-111111111111', '88888888-1001-0000-0000-000000000001', 'customer_name', 'What is your full name?', 'So we can identify your account', 'SHORT_TEXT', 'Jane Doe', TRUE, 0),
('99999999-1002-0000-0000-000000000002', '66666666-1111-1111-1111-111111111111', '88888888-1001-0000-0000-000000000001', 'customer_email', 'Your email address', 'We will send a 15% discount coupon for your feedback', 'EMAIL', 'jane@example.com', TRUE, 1),
('99999999-1003-0000-0000-000000000003', '66666666-1111-1111-1111-111111111111', '88888888-1002-0000-0000-000000000002', 'delivery_on_time', 'Did your package arrive on or before the estimated delivery date?', 'Check tracking details if unsure', 'YES_NO', NULL, TRUE, 2),
('99999999-1004-0000-0000-000000000004', '66666666-1111-1111-1111-111111111111', '88888888-1002-0000-0000-000000000002', 'delay_explanation', 'Please describe the delivery delay issue', 'Tell us what happened so we can address it with the carrier', 'LONG_TEXT', 'Carrier delivered 3 days late...', FALSE, 3),
('99999999-1005-0000-0000-000000000005', '66666666-1111-1111-1111-111111111111', '88888888-1002-0000-0000-000000000002', 'packaging_quality', 'How would you rate the packaging quality?', '1 (Poor) to 5 (Excellent)', 'STAR_RATING', NULL, TRUE, 4),
('99999999-1006-0000-0000-000000000006', '66666666-1111-1111-1111-111111111111', '88888888-1003-0000-0000-000000000003', 'overall_score', 'How satisfied are you with your shopping experience?', 'Select score from 1 to 10', 'LINEAR_SCALE', NULL, TRUE, 5),
('99999999-1007-0000-0000-000000000007', '66666666-1111-1111-1111-111111111111', '88888888-1003-0000-0000-000000000003', 'suggestions', 'What features or products would you like to see next?', 'Ideas, feedback or wishlist', 'LONG_TEXT', 'Add faster same-day shipping options...', FALSE, 6)
ON CONFLICT (id) DO NOTHING;

-- Conditional Logic Rule for Demo Form 1: IF delivery_on_time == "No" THEN SHOW delay_explanation
INSERT INTO form_logic_rules (
    id, form_id, source_question_id, target_question_id, condition_operator,
    condition_value, action_type, logical_combinator, rule_group
) VALUES (
    'aaaa1111-0000-0000-0000-000000000001',
    '66666666-1111-1111-1111-111111111111',
    '99999999-1003-0000-0000-000000000003',
    '99999999-1004-0000-0000-000000000004',
    'EQUALS',
    'No',
    'SHOW',
    'AND',
    0
) ON CONFLICT (id) DO NOTHING;

-- Publication record for Demo Form 1
INSERT INTO form_publications (id, form_id, published_by, published_slug, published_at, is_active)
VALUES (
    'bbbb1111-0000-0000-0000-000000000001',
    '66666666-1111-1111-1111-111111111111',
    '11111111-1111-1111-1111-111111111111',
    'customer-feedback-demo',
    CURRENT_TIMESTAMP - INTERVAL '5 days',
    TRUE
) ON CONFLICT (id) DO NOTHING;

-- Seed 5 sample responses with answers, tags, and notes for Form 1
INSERT INTO form_responses (id, form_id, respondent_ip, user_agent, duration_seconds, status, is_reviewed, submitted_at)
VALUES
('cccc1111-0001-0000-0000-000000000001', '66666666-1111-1111-1111-111111111111', '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', 78, 'COMPLETED', TRUE, CURRENT_TIMESTAMP - INTERVAL '4 days'),
('cccc1111-0002-0000-0000-000000000002', '66666666-1111-1111-1111-111111111111', '192.168.1.102', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', 112, 'COMPLETED', TRUE, CURRENT_TIMESTAMP - INTERVAL '3 days'),
('cccc1111-0003-0000-0000-000000000003', '66666666-1111-1111-1111-111111111111', '192.168.1.103', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X)', 65, 'COMPLETED', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
('cccc1111-0004-0000-0000-000000000004', '66666666-1111-1111-1111-111111111111', '192.168.1.104', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', 95, 'COMPLETED', TRUE, CURRENT_TIMESTAMP - INTERVAL '1 day'),
('cccc1111-0005-0000-0000-000000000005', '66666666-1111-1111-1111-111111111111', '192.168.1.105', 'Mozilla/5.0 (Linux; Android 14)', 84, 'COMPLETED', FALSE, CURRENT_TIMESTAMP - INTERVAL '6 hours')
ON CONFLICT (id) DO NOTHING;

-- Answers for sample responses
INSERT INTO response_answers (id, response_id, question_id, question_key, answer_text) VALUES
('dddd1111-0001-0000-0000-000000000001', 'cccc1111-0001-0000-0000-000000000001', '99999999-1001-0000-0000-000000000001', 'customer_name', 'Sarah Jenkins'),
('dddd1111-0002-0000-0000-000000000002', 'cccc1111-0001-0000-0000-000000000001', '99999999-1002-0000-0000-000000000002', 'customer_email', 'sarah.j@example.com'),
('dddd1111-0003-0000-0000-000000000003', 'cccc1111-0001-0000-0000-000000000001', '99999999-1003-0000-0000-000000000003', 'delivery_on_time', 'Yes'),
('dddd1111-0004-0000-0000-000000000004', 'cccc1111-0001-0000-0000-000000000001', '99999999-1005-0000-0000-000000000005', 'packaging_quality', '5'),
('dddd1111-0005-0000-0000-000000000005', 'cccc1111-0001-0000-0000-000000000001', '99999999-1006-0000-0000-000000000006', 'overall_score', '9'),
('dddd1111-0006-0000-0000-000000000006', 'cccc1111-0001-0000-0000-000000000001', '99999999-1007-0000-0000-000000000007', 'suggestions', 'Seamless ordering and fast packaging! Would love an SMS tracking notification next time.'),

('dddd1111-0007-0000-0000-000000000007', 'cccc1111-0002-0000-0000-000000000002', '99999999-1001-0000-0000-000000000001', 'customer_name', 'Marcus Brody'),
('dddd1111-0008-0000-0000-000000000002', 'cccc1111-0002-0000-0000-000000000002', '99999999-1002-0000-0000-000000000002', 'customer_email', 'marcus.b@example.com'),
('dddd1111-0009-0000-0000-000000000009', 'cccc1111-0002-0000-0000-000000000002', '99999999-1003-0000-0000-000000000003', 'delivery_on_time', 'No'),
('dddd1111-0010-0000-0000-000000000010', 'cccc1111-0002-0000-0000-000000000002', '99999999-1004-0000-0000-000000000004', 'delay_explanation', 'Package was delayed at the regional distribution depot for 2 extra days.'),
('dddd1111-0011-0000-0000-000000000011', 'cccc1111-0002-0000-0000-000000000002', '99999999-1005-0000-0000-000000000005', 'packaging_quality', '4'),
('dddd1111-0012-0000-0000-000000000012', 'cccc1111-0002-0000-0000-000000000002', '99999999-1006-0000-0000-000000000006', 'overall_score', '7'),
('dddd1111-0013-0000-0000-000000000013', 'cccc1111-0002-0000-0000-000000000002', '99999999-1007-0000-0000-000000000007', 'suggestions', 'Great products, but carrier reliability needs improvement.')
ON CONFLICT (id) DO NOTHING;

-- Tags for responses
INSERT INTO response_tags (id, response_id, tag_name, color) VALUES
('eeee1111-0001-0000-0000-000000000001', 'cccc1111-0001-0000-0000-000000000001', 'Promoter', '#10B981'),
('eeee1111-0002-0000-0000-000000000002', 'cccc1111-0001-0000-0000-000000000001', 'VIP Customer', '#6366F1'),
('eeee1111-0003-0000-0000-000000000003', 'cccc1111-0002-0000-0000-000000000002', 'Carrier Delay', '#F59E0B')
ON CONFLICT (id) DO NOTHING;

-- Notes on responses
INSERT INTO response_notes (id, response_id, author_id, note_text) VALUES
('ffff1111-0001-0000-0000-000000000001', 'cccc1111-0001-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'Sent 15% discount coupon code via email. High customer delight.'),
('ffff1111-0002-0000-0000-000000000002', 'cccc1111-0002-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'Investigated with DHL hub; delay was due to regional weather advisory.')
ON CONFLICT (id) DO NOTHING;

-- Analytics daily records
INSERT INTO form_analytics (id, form_id, date, views, starts, submissions, completions, average_duration_seconds) VALUES
('00001111-0001-0000-0000-000000000001', '66666666-1111-1111-1111-111111111111', CURRENT_DATE - 4, 12, 10, 5, 5, 82),
('00001111-0002-0000-0000-000000000002', '66666666-1111-1111-1111-111111111111', CURRENT_DATE - 3, 14, 11, 4, 4, 91),
('00001111-0003-0000-0000-000000000003', '66666666-1111-1111-1111-111111111111', CURRENT_DATE - 2, 8, 7, 3, 3, 75),
('00001111-0004-0000-0000-000000000004', '66666666-1111-1111-1111-111111111111', CURRENT_DATE - 1, 10, 8, 4, 4, 88),
('00001111-0005-0000-0000-000000000005', '66666666-1111-1111-1111-111111111111', CURRENT_DATE, 6, 5, 2, 2, 80)
ON CONFLICT (form_id, date) DO NOTHING;
