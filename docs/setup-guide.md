# FormGenius AI – Setup & Deployment Guide

This guide covers setting up and running FormGenius AI locally for development or deploying using Docker Compose.

---

## 1. Prerequisites

Ensure the following tools are installed on your machine:
- **Java**: JDK 21 or higher (Eclipse Temurin or Oracle OpenJDK)
- **Node.js**: v20.x or higher with `npm` v10+
- **Maven**: 3.9+ (or use `./mvnw`)
- **Docker & Docker Compose**: v24+ (Optional for containerized run)
- **PostgreSQL**: 15+ (Optional if using Docker)

---

## 2. Option A: Quickstart with Docker Compose (Recommended)

To run the entire full-stack application (PostgreSQL + Spring Boot Backend + React Frontend + Nginx):

```bash
# 1. Clone repository
git clone https://github.com/your-org/formgenius-ai.git
cd "Form Builder AI"

# 2. Configure environment
cp .env.example .env
# Optional: Add your Google Gemini API key into .env:
# GEMINI_API_KEY=your_gemini_api_key

# 3. Build and launch all services
docker compose up --build -d

# 4. Verify containers are healthy
docker compose ps
```

Services will be available at:
- **Frontend SPA**: `http://localhost:3000`
- **Backend API**: `http://localhost:8080`
- **Swagger / OpenAPI Documentation**: `http://localhost:8080/swagger-ui.html`
- **PostgreSQL Database**: `localhost:5432` (`formgenius` / `formgenius_secret`)

---

## 3. Option B: Local Development Setup

### 3.1 Start PostgreSQL
Start a local PostgreSQL instance or launch one via Docker:

```bash
docker run -d --name pg-formgenius \
  -e POSTGRES_DB=formgenius \
  -e POSTGRES_USER=formgenius \
  -e POSTGRES_PASSWORD=formgenius_secret \
  -p 5432:5432 \
  postgres:16-alpine
```

### 3.2 Run Backend (Spring Boot 3.4)
Flyway migrations will run automatically on startup to create all 26 tables and populate seed data:

```bash
cd backend

# Compile and run unit/integration tests
mvn clean test

# Start the Spring Boot application (Default port: 8080)
mvn spring-boot:run
```

### 3.3 Run Frontend (React 19 + Vite)
In a separate terminal:

```bash
cd frontend

# Install dependencies (cached in package-lock.json)
npm install

# Start development server with HMR (Default port: 5173)
npm run dev
```

Navigate to `http://localhost:5173`.

---

## 4. Default Seed Credentials

Flyway migration `V2__seed_data.sql` preconfigures an administrator account and sample workspace:
- **Email**: `alex@formgenius.ai`
- **Password**: `password123`
- **Workspace**: Acme Workspace (`acme-workspace`)
- **Preloaded Data**: 15 form templates across all categories, sample published forms, response submissions, tags, notes, and 30-day analytics history.

---

## 5. Google Gemini AI Configuration

To enable live AI form generation:
1. Obtain an API key from [Google AI Studio](https://aistudio.google.com/).
2. Set the `GEMINI_API_KEY` environment variable:
   ```bash
   export GEMINI_API_KEY="your-actual-api-key"
   ```
   Or set it in your `.env` file or `application-prod.yml`.

> [!NOTE]
> If no Gemini API key is configured or network is offline, FormGenius AI automatically utilizes its internal intelligent heuristic generator fallback, ensuring all form generation features continue to work seamlessly during development and evaluations.

---

## 6. Running Production Builds

```bash
# Backend JAR build
mvn -f backend/pom.xml clean package -DskipTests
# Output: backend/target/formgenius-backend-1.0.0.jar

# Frontend production build
npm --prefix frontend run build
# Output: frontend/dist/
```
