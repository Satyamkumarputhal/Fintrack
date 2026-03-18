# FinTrack Backend

## Project Overview
FinTrack is a Spring Boot backend for personal expense tracking, budgeting, analytics, and admin operations. The API uses JWT authentication, role-based authorization, standardized JSON responses, validation, and optimistic locking for safer concurrent updates.

## Features
- User registration and login with JWT issuance
- Expense CRUD with ownership checks and pagination
- Budget creation and monthly budget summaries
- Category management per user
- Analytics endpoints for monthly totals, top categories, trends, overspending, and savings estimates
- Admin-only user promotion endpoints
- Standardized success and error response envelopes
- Optimistic locking for expense updates

## Tech Stack
- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- Bean Validation
- springdoc-openapi / Swagger UI
- JUnit 5 / MockMvc / Mockito

## API Endpoints Summary
### Auth
- `POST /api/user/register`
- `POST /api/user/login`

### Expenses
- `POST /api/expenses`
- `GET /api/expenses`
- `GET /api/expenses/{id}`
- `PUT /api/expenses/{id}`
- `DELETE /api/expenses/{id}`

### Budgets
- `POST /api/budgets`
- `GET /api/budgets/summary`

### Categories
- `POST /api/category/create`
- `GET /api/category`
- `DELETE /api/category/{id}`

### Analytics
- `GET /analytics/analytics/monthly-total`
- `GET /analytics/category-breakdown`
- `GET /analytics/top-category`
- `GET /analytics/monthly-trend`
- `GET /analytics/spending-health`
- `GET /analytics/overspending`
- `GET /analytics/savings-estimate`

### Admin
- `PATCH /api/admin/users/{id}/promote`
- `GET /api/admin/test`

## Security Features
- JWT bearer authentication
- RBAC with `ADMIN`-only access to `/api/admin/**`
- Method-level security using `@PreAuthorize`
- Consistent 401 responses for missing, expired, malformed, or tampered tokens
- Consistent 403 responses for authenticated but unauthorized actions

## Error Handling Design
- Error response format:
  - `status`
  - `messages`
  - `timestamp`
- Validation, type mismatch, malformed JSON, access denied, auth failures, optimistic locking, and domain-specific conflicts are mapped to stable HTTP status codes
- Internal exception details are not exposed to clients

## Concurrency Handling
- `Expense` uses JPA optimistic locking via `@Version`
- Concurrent stale updates return `409 Conflict`
- Conflict message:
  - `Resource was updated by another request. Please retry.`

## How To Run Locally
1. Configure `.env` with JWT, database, and mail values.
2. Start PostgreSQL.
3. Run:
   - `.\mvnw.cmd spring-boot:run`
4. Open Swagger UI:
   - `http://localhost:8080/swagger-ui/index.html`
5. Open OpenAPI spec:
   - `http://localhost:8080/v3/api-docs`

## Future Improvements
- Refresh token flow
- Audit logging for sensitive actions
- Request correlation IDs and structured logging
- Testcontainers-based integration tests
- Rate limiting and abuse protection
- Postman collection export and contract tests
