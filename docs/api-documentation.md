# FormGenius AI – API Documentation

All REST API endpoints are prefixed with `/api/v1`. Interactive Swagger documentation is available at `http://localhost:8080/swagger-ui.html`.

---

## 1. Authentication & Security

All private endpoints require a valid JWT Bearer token passed in the `Authorization` header:

```http
Authorization: Bearer <your_jwt_access_token>
```

### Standard Response Envelope
All API endpoints return responses in a standardized JSON envelope:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2026-11-14T10:00:00Z"
}
```

---

## 2. API Endpoints Reference

### 2.1 Authentication (`/api/v1/auth`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Register a new user account & workspace | No |
| `POST` | `/api/v1/auth/login` | Authenticate with email & password | No |
| `GET` | `/api/v1/auth/me` | Fetch currently authenticated user | Yes |
| `POST` | `/api/v1/auth/refresh` | Refresh expired access token | No |
| `POST` | `/api/v1/auth/forgot-password` | Send password reset email | No |
| `POST` | `/api/v1/auth/reset-password` | Set new password with reset token | No |

### 2.2 Workspaces (`/api/v1/workspaces`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/workspaces` | List workspaces user has access to | Yes |
| `POST` | `/api/v1/workspaces` | Create a new workspace | Yes |
| `GET` | `/api/v1/workspaces/{id}` | Get workspace details & quotas | Yes |
| `PUT` | `/api/v1/workspaces/{id}` | Update workspace name/branding | Yes |
| `GET` | `/api/v1/workspaces/{id}/members` | List workspace team members | Yes |
| `POST` | `/api/v1/workspaces/{id}/members` | Invite a collaborator | Yes |
| `PUT` | `/api/v1/workspaces/{id}/members/{mId}` | Update member role | Yes |
| `DELETE` | `/api/v1/workspaces/{id}/members/{mId}` | Remove member from workspace | Yes |
| `GET` | `/api/v1/workspaces/{id}/analytics` | Get aggregate workspace metrics | Yes |

### 2.3 Forms Management (`/api/v1/forms`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/forms` | List workspace forms (filter/sort/search) | Yes |
| `POST` | `/api/v1/forms` | Create a blank form | Yes |
| `GET` | `/api/v1/forms/{id}` | Get full form detail with questions & rules | Yes |
| `PUT` | `/api/v1/forms/{id}` | Update form settings & metadata | Yes |
| `PUT` | `/api/v1/forms/{id}/schema` | Save complete form schema & questions | Yes |
| `POST` | `/api/v1/forms/{id}/duplicate` | Clone form as a draft copy | Yes |
| `POST` | `/api/v1/forms/{id}/publish` | Publish form with public URL slug | Yes |
| `POST` | `/api/v1/forms/{id}/unpublish` | Take published form offline | Yes |
| `POST` | `/api/v1/forms/{id}/archive` | Archive form | Yes |
| `POST` | `/api/v1/forms/{id}/restore` | Restore archived form to draft | Yes |
| `DELETE` | `/api/v1/forms/{id}` | Delete form permanently | Yes |

### 2.4 Public Form Submission Engine (`/api/v1/public/forms`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/public/forms/{slug}` | Fetch published form schema for rendering | No |
| `POST` | `/api/v1/public/forms/{slug}/submissions` | Submit form answers with strict validation | No |

### 2.5 Responses Management (`/api/v1/forms/{id}/responses`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/forms/{id}/responses` | List paginated form submissions | Yes |
| `GET` | `/api/v1/forms/{id}/responses/{rId}` | Get single submission detail with answers | Yes |
| `DELETE` | `/api/v1/forms/{id}/responses/{rId}` | Delete a submission | Yes |
| `POST` | `/api/v1/forms/{id}/responses/bulk-delete` | Bulk delete multiple submissions | Yes |
| `PATCH` | `/api/v1/forms/{id}/responses/{rId}/review` | Toggle reviewed status | Yes |
| `POST` | `/api/v1/forms/{id}/responses/{rId}/notes` | Add internal team note | Yes |
| `POST` | `/api/v1/forms/{id}/responses/{rId}/tags` | Attach tag to submission | Yes |
| `DELETE` | `/api/v1/forms/{id}/responses/{rId}/tags/{tId}` | Remove tag from submission | Yes |
| `GET` | `/api/v1/forms/{id}/responses/export` | Download submissions as CSV | Yes |

### 2.6 Analytics (`/api/v1/forms/{id}/analytics`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/forms/{id}/analytics/overview` | Overview KPIs & daily submission trends | Yes |
| `GET` | `/api/v1/forms/{id}/analytics/questions` | Per-question response breakdown | Yes |

### 2.7 AI Form Generation & Insights (`/api/v1/ai`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/v1/ai/forms/generate` | Generate complete form schema via Gemini | Yes |
| `POST` | `/api/v1/ai/questions/improve` | Generate wording improvements for question | Yes |
| `POST` | `/api/v1/ai/forms/translate` | Translate form to target language | Yes |
| `POST` | `/api/v1/ai/insights/generate` | Generate response sentiment & complaints | Yes |

### 2.8 Templates (`/api/v1/templates`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/templates` | List pre-built form templates | Yes |
| `GET` | `/api/v1/templates/{id}` | Get template schema detail | Yes |
| `POST` | `/api/v1/templates/{id}/use` | Clone template into workspace | Yes |

### 2.9 Notifications (`/api/v1/notifications`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/notifications` | List user notification alerts | Yes |
| `PATCH` | `/api/v1/notifications/{id}/read` | Mark single notification as read | Yes |
| `PATCH` | `/api/v1/notifications/read-all` | Mark all notifications as read | Yes |

### 2.10 Webhooks (`/api/v1/workspaces/{id}/webhooks`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/workspaces/{id}/webhooks` | List configured webhooks | Yes |
| `POST` | `/api/v1/workspaces/{id}/webhooks` | Register new outgoing webhook | Yes |
| `DELETE` | `/api/v1/workspaces/{id}/webhooks/{wId}` | Delete webhook | Yes |
| `POST` | `/api/v1/workspaces/{id}/webhooks/{wId}/test` | Trigger test payload delivery | Yes |
