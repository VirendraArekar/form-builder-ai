# FormGenius AI – Architecture Documentation

## 1. System Overview

**FormGenius AI** is an enterprise-ready, full-stack AI-powered SaaS form builder platform. It enables users to describe any form requirement in natural language and instantly receive a fully structured, multi-section form schema complete with appropriate input types, conditional branching, validation rules, and themes.

```mermaid
graph TD
    User["End User / Form Respondent"]
    Admin["Workspace Creator / Editor"]

    subgraph "Frontend Layer (React 19 + TypeScript + Vite)"
        WebSPA["React SPA (Tailwind CSS v4)"]
        DnDBuilder["Visual 3-Panel Form Builder (dnd-kit)"]
        PublicRenderer["Public Form Renderer Engine (/f/:slug)"]
        ChartsEngine["Analytics Visualizer (Recharts)"]
    end

    subgraph "Backend Layer (Spring Boot 3.4 / Java 21+)"
        APIGateway["REST API Gateway & Security Filter"]
        JWTAuth["JWT Authentication & RBAC"]
        FormEngine["Form Service & Schema Engine"]
        ValidationPipeline["Strict Server-Side Validator"]
        AnalyticsAggregator["PostgreSQL SQL Aggregator"]
        AiService["Gemini AI Integration Layer"]
        WebhookDispatcher["Outgoing Webhook Dispatcher"]
    end

    subgraph "Data & External AI"
        PG[(PostgreSQL 16 Database)]
        Flyway["Flyway Database Migrations"]
        GeminiAPI["Google Gemini 1.5 Flash API"]
    end

    User --> PublicRenderer
    Admin --> WebSPA
    WebSPA --> DnDBuilder
    WebSPA --> ChartsEngine

    PublicRenderer --> APIGateway
    DnDBuilder --> APIGateway
    ChartsEngine --> APIGateway

    APIGateway --> JWTAuth
    JWTAuth --> FormEngine
    FormEngine --> ValidationPipeline
    FormEngine --> AiService
    AiService --> GeminiAPI
    ValidationPipeline --> PG
    AnalyticsAggregator --> PG
    Flyway --> PG
    FormEngine --> WebhookDispatcher
```

---

## 2. Frontend Architecture (React 19 + TypeScript)

The frontend is structured around clear separation of concerns:
- **`src/pages/`**: 22 dedicated page views covering public marketing, authentication, workspace dashboard, AI generator, 3-panel visual form builder, live form preview, form settings, public submissions (`/f/:slug`), submission success, responses spreadsheet, response detail, analytics, AI insights, template gallery, team RBAC, notifications, profile, workspace settings, and billing.
- **`src/components/builder/`**:
  - `QuestionPalette.tsx`: Draggable component palette with 24 question types grouped into Text, Choice, Rating, DateTime, Media, and Layout categories.
  - `CanvasQuestionCard.tsx`: Sortable question item with dnd-kit integration, inline editing, and live dummy field preview.
  - `PropertiesEditor.tsx`: Right panel property editor with AI question improver, choice options manager, and validation constraints.
  - `LogicEditor.tsx`: Conditional branching engine (IF question X equals Y THEN show/hide question Z).
  - `ThemeEditor.tsx`: Styling engine (brand accent colors, background, typography, border radius, header banner, logo).
  - `PublishModal.tsx`: Sharing modal with public link copy, QR code generator, and iFrame embed snippet.
- **`src/store/`**:
  - `authStore.ts`: Global authentication state, JWT tokens, active workspace, user profile.
  - `builderStore.ts`: Form builder state, history snapshot undo/redo stack, selected question/section, auto-save status.
  - `themeStore.ts`: Dark/light mode theme toggle.
- **`src/services/`**:
  - Axios HTTP client with JWT request interceptor and automatic 401 token refresh.
  - Dedicated service modules for auth, forms, public submissions, responses, analytics, AI, templates, members, notifications, and webhooks.

---

## 3. Backend Architecture (Spring Boot 3.4 / Java 21+)

The backend follows an idiomatic, modular layered design:
1. **Controller Layer**: REST endpoints documented with OpenAPI 3 (Swagger).
2. **Security & Filter Layer**:
   - `JwtAuthenticationFilter`: Parses and verifies `Bearer` tokens on incoming requests.
   - `SecurityConfig`: Stateless session management, BCrypt password hashing, and role-based route authorizations.
3. **Service Layer**:
   - `FormService`: CRUD operations on forms, sections, questions, and themes. Schema versioning and form duplication.
   - `PublicFormService`: Strict server-side validation pipeline verifying field identifiers, question types, required fields, conditional rules, regex patterns, and numeric ranges.
   - `GeminiAiService`: Integration with Google Gemini API using structured JSON output, prompt engineering, schema normalization, and offline heuristic fallbacks.
   - `AnalyticsService`: High-performance PostgreSQL SQL aggregations for daily submission trends, completion rates, and per-question distributions.
   - `WebhookService`: Asynchronous webhook delivery to external URLs on form events.
4. **Data Access Layer**: Spring Data JPA repositories with custom JPQL and native PostgreSQL queries.

---

## 4. AI Form Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Frontend as React Client
    participant Controller as AiController
    participant GeminiService as GeminiAiService
    participant Gemini as Google Gemini API
    participant FormService as FormService
    participant DB as PostgreSQL

    User->>Frontend: Enter one-sentence description
    Frontend->>Controller: POST /api/v1/ai/forms/generate
    Controller->>GeminiService: generateFormSchema(workspaceId, prompt)
    GeminiService->>Gemini: Prompt with Structured JSON Output Schema
    Gemini-->>GeminiService: JSON Form Schema
    GeminiService->>GeminiService: Validate & Normalize Schema
    GeminiService-->>Controller: AiFormSchema
    Controller-->>Frontend: 200 OK (Structured Schema)
    Frontend->>User: Display Live Schema Preview
    User->>Frontend: Click "Open in Visual Builder"
    Frontend->>FormService: POST /api/v1/forms (Create Form)
    Frontend->>FormService: PUT /api/v1/forms/{id}/schema (Save Schema)
    FormService->>DB: Persist Form, Sections, Questions, Rules, Theme
    DB-->>FormService: Saved Entities
    FormService-->>Frontend: FormDetail
    Frontend->>User: Route to /builder/:id
```

---

## 5. Public Form Submission & Validation Pipeline

Security is critical: the application enforces **real server-side validation** for every submission. Frontend validation provides smooth UX, but the backend validates all constraints before database persistence:

```mermaid
flowchart TD
    SubmitReq["POST /api/v1/public/forms/{slug}/submissions"]
    LoadSchema["1. Load Published Form Schema from PostgreSQL"]
    CheckStatus{"2. Is Form Published & Active?"}
    CheckDates{"3. Within Start & End Dates?"}
    CheckQuota{"4. Below Max Submissions Quota?"}
    CheckPassword{"5. Password Protected & Valid?"}
    ValidateFields["6. Validate Field Keys & Expected Types"]
    EvalLogic["7. Evaluate Conditional Logic (Filter Hidden Fields)"]
    ValidateRequired["8. Validate Required Fields on Visible Questions"]
    ValidateConstraints["9. Validate Regex, Lengths, Numeric Min/Max"]
    PersistTx["10. Transactionally Save FormResponse & ResponseAnswers"]
    RecordMetrics["11. Increment Form Analytics & Daily Trend Counters"]
    DispatchHooks["12. Trigger Outgoing Webhook Event (Async)"]
    SendEmail["13. Dispatch Email Notification (If Enabled)"]
    ReturnSuccess["200 OK: Submission ID & Confirmation"]

    SubmitReq --> LoadSchema --> CheckStatus
    CheckStatus -- No --> RejectError["400 Bad Request / 404 Not Found"]
    CheckStatus -- Yes --> CheckDates
    CheckDates -- No --> RejectError
    CheckDates -- Yes --> CheckQuota
    CheckQuota -- No --> RejectError
    CheckQuota -- Yes --> CheckPassword
    CheckPassword -- Invalid --> AuthError["401 Unauthorized (Password Required)"]
    CheckPassword -- Valid --> ValidateFields
    ValidateFields --> EvalLogic --> ValidateRequired --> ValidateConstraints
    ValidateConstraints -- Failure --> ValidationErrors["400 Bad Request with Field Errors"]
    ValidateConstraints -- Pass --> PersistTx
    PersistTx --> RecordMetrics --> DispatchHooks --> SendEmail --> ReturnSuccess
```
