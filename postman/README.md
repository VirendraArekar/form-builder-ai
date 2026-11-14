# FormGenius AI – Postman API Collection & Environments

This directory contains the official Postman API Collection and Environment configuration files for **FormGenius AI**, covering all REST endpoints for local development and Docker deployments.

---

## 📁 Files Included

| File | Description |
|------|-------------|
| [`FormGenius_AI.postman_collection.json`](file:///Users/apple/Desktop/Antigravity/Form%20Builder%20AI/postman/FormGenius_AI.postman_collection.json) | Complete v2.1.0 Postman collection with 11 domain folders and automated test/token scripts. |
| [`FormGenius_Local.postman_environment.json`](file:///Users/apple/Desktop/Antigravity/Form%20Builder%20AI/postman/FormGenius_Local.postman_environment.json) | Environment config pointing to `http://localhost:8080` (Spring Boot running locally) with seed credentials. |
| [`FormGenius_Docker.postman_environment.json`](file:///Users/apple/Desktop/Antigravity/Form%20Builder%20AI/postman/FormGenius_Docker.postman_environment.json) | Environment config pointing to `http://localhost/api` (reverse-proxied through Nginx container). |

---

## 🚀 Getting Started

### Option 1: Import into Postman App

1. Open Postman desktop or web client.
2. Click **Import** (top left).
3. Select and drag all 3 JSON files from this `postman/` directory:
   - `FormGenius_AI.postman_collection.json`
   - `FormGenius_Local.postman_environment.json`
   - `FormGenius_Docker.postman_environment.json`
4. In the top right environment dropdown, select **FormGenius AI - Local Dev** (or Docker).

### Option 2: Automated Testing via Newman (CLI)

You can run the entire collection headlessly from your terminal using Newman:

```bash
# Install Newman if not already installed
npm install -g newman

# Run against local backend
newman run postman/FormGenius_AI.postman_collection.json \
  -e postman/FormGenius_Local.postman_environment.json \
  --reporters cli,json \
  --reporter-json-export postman-report.json
```

---

## 🔑 Automated JWT Authentication Workflow

The collection includes a pre-configured **Test script** on `POST /api/auth/login`:
- When you execute **Auth -> Login User**, the response `accessToken` and default workspace ID are **automatically captured and set in your Postman environment** (`{{accessToken}}` and `{{workspaceId}}`).
- All subsequent secured endpoints automatically use the `Bearer {{accessToken}}` header.
- No manual copy-pasting of Bearer tokens is required!

### Default Credentials (Seed Data)
- **Email:** `alex@formgenius.ai`
- **Password:** `Password123!`
- **Workspace ID:** `bbbb0000-0000-0000-0000-000000000001`
- **Demo Form ID:** `aaaa0000-0000-0000-0000-000000000001`
- **Demo Form Slug:** `customer-satisfaction-demo`

---

## 📑 Collection Structure (11 Folders)

### 1. Authentication (`/api/auth`)
- `POST /register`: Register a new SaaS account
- `POST /login`: Log in, return JWT tokens, and auto-persist `{{accessToken}}` to environment
- `POST /refresh`: Refresh an expired access token using a refresh token
- `POST /forgot-password`: Request a password reset link
- `POST /reset-password`: Reset password using token

### 2. User Profile (`/api/users/profile`)
- `GET /`: Get current authenticated user details
- `PUT /`: Update display name and avatar URL
- `PUT /password`: Change user password

### 3. Workspaces (`/api/workspaces`)
- `GET /`: List all workspaces for the current user
- `POST /`: Create a new workspace
- `GET /:id`: Get workspace details and settings
- `PUT /:id`: Update workspace name, logo, or settings
- `GET /:id/members`: List members and roles
- `POST /:id/members`: Invite team members (ADMIN, EDITOR, VIEWER)

### 4. Forms Management (`/api/forms`)
- `GET /`: List workspace forms with filtering (`search`, `status`, `page`, `size`)
- `POST /`: Create a new form schema
- `GET /:id`: Get complete form schema with questions and logic
- `PUT /:id`: Update form title, description, schema, and logic rules
- `POST /:id/publish`: Publish form with custom URL slug
- `POST /:id/unpublish`: Take form offline
- `POST /:id/duplicate`: Clone an existing form and its questions
- `DELETE /:id`: Soft delete form

### 5. Public Submissions Engine (`/api/public/forms`)
- `GET /:slug`: Fetch published form schema (unauthenticated for public respondents)
- `POST /:slug/submit`: Submit form answers (validated transactionally against schema constraints)

### 6. Responses & Submissions (`/api/forms/:id/responses`)
- `GET /`: List submissions with filtering, sorting, and pagination
- `GET /:responseId`: View detailed submission answers
- `DELETE /:responseId`: Delete individual response
- `GET /export/csv`: Stream responses as CSV file
- `POST /:responseId/notes`: Add internal team review note
- `POST /:responseId/tags`: Add categorization tag

### 7. Analytics & Insights (`/api/forms/:id/analytics`)
- `GET /summary`: Summary KPIs (total views, submissions, completion rate, avg time)
- `GET /views`: Timeseries of page views and conversions
- `GET /dropoff`: Question-level drop-off analysis
- `GET /ai-insights`: Gemini AI-generated executive summaries and actionable recommendations

### 8. AI Form Engine (`/api/ai`)
- `POST /generate`: Generate complete structured form schema from a single natural-language sentence
- `POST /regenerate-question`: Rephrase or enhance an individual question
- `POST /analyze-sentiment`: Analyze sentiment across text response submissions

### 9. Templates Library (`/api/templates`)
- `GET /`: List all 15 pre-built professional templates by category
- `POST /:id/use`: Instantiate a template into your workspace

### 10. Webhooks & Integrations (`/api/webhooks`)
- `GET /`: List configured webhooks
- `POST /`: Register endpoint (Slack, Zapier, custom URL)
- `POST /:id/test`: Trigger test webhook ping

### 11. System & Actuator (`/actuator`)
- `GET /health`: System health check (PostgreSQL connectivity, disk space)
- `GET /info`: Build metadata and Spring Boot version
