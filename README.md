# FinTrack

FinTrack is a full-stack personal finance tracker for managing expenses, budgets, categories, and monthly spending insights. It includes a Spring Boot REST API with JWT security and a React dashboard for day-to-day expense tracking.

This project was built as a practical campus-placement portfolio project: the focus is on clean API design, authentication, role-based access, validation, error handling, and a usable dashboard experience.

## Highlights

- JWT-based registration and login
- Expense CRUD with category, date, and pagination filters
- Category management per user
- Monthly budget creation, editing, and budget summary tracking
- Analytics for monthly total, top category, category breakdown, monthly trend, overspending, and spending health
- Dashboard savings estimate based on user-provided monthly income
- Admin user promotion flow with role-based authorization
- Consistent API response and error response format
- Backend validation and global exception handling
- Unit and controller tests for service logic, validation, security, and API behavior

## Tech Stack

**Backend**
- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- Bean Validation
- springdoc-openapi / Swagger UI
- JUnit 5, MockMvc, Mockito

**Frontend**
- React 18
- TypeScript
- Vite
- Tailwind CSS
- shadcn/ui-style components
- Recharts
- Axios
- Vitest

## Repository Structure

```text
fintrack/
├── src/                    # Spring Boot backend
├── finflow-dashboard/       # React dashboard frontend
├── docker-compose.yml       # PostgreSQL + backend compose setup
├── pom.xml
└── README.md
```

Note: `finflow-dashboard` is maintained as its own Git repository and linked from this backend repository.

## Screens

The dashboard includes:
- Monthly spending summary
- Spending health status
- Savings estimate with editable monthly income
- Top spending category
- Category breakdown chart
- Monthly trend chart
- Expense, category, and budget management pages

## API Overview

### Auth
- `POST /api/user/register`
- `POST /api/user/login`

### Expenses
- `POST /api/expenses`
- `GET /api/expenses`
- `GET /api/expenses/{id}`
- `PUT /api/expenses/{id}`
- `DELETE /api/expenses/{id}`

### Categories
- `POST /api/category/create`
- `GET /api/category`
- `PUT /api/category/{id}`
- `DELETE /api/category/{id}`

### Budgets
- `POST /api/budgets`
- `PUT /api/budgets`
- `PUT /api/budgets/{id}`
- `GET /api/budgets/summary`

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

## Local Setup

### Prerequisites
- Java 21
- Node.js and npm
- PostgreSQL or Docker Desktop
- Git

### Backend

1. Create a `.env` file in the project root using `.env.example`.
2. Start PostgreSQL. With Docker:

```powershell
docker compose up -d postgres
```

3. Run the backend:

```powershell
.\mvnw.cmd spring-boot:run
```

4. Open:
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Frontend

```powershell
cd finflow-dashboard
npm install
npm run dev
```

The dashboard runs at `http://localhost:5173`.

## Environment Variables

Backend variables are documented in `.env.example`.

Frontend variables are documented in `finflow-dashboard/.env.example`.

## Testing

Backend:

```powershell
.\mvnw.cmd test
```

Frontend:

```powershell
cd finflow-dashboard
npm run lint
npm run test
npm run build
```

Latest local verification:
- Backend tests: `26 passed`
- Frontend lint: `0 errors`
- Frontend tests: `1 passed`
- Frontend production build: passed

## Security And Reliability Notes

- Passwords are stored using Spring Security password encoding.
- Authenticated API access uses JWT bearer tokens.
- Admin endpoints are role protected.
- API errors use a consistent `status`, `messages`, and `timestamp` format.
- Expense updates include optimistic locking support for stale update conflicts.

## Future Improvements

- Persist monthly income in backend user settings instead of browser local storage
- Add refresh token flow
- Add Docker Compose profile for one-command full-stack startup
- Add integration tests with Testcontainers
- Add GitHub Actions CI for backend and frontend checks
