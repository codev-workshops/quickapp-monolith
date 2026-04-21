# QuickApp Microservices

Decomposition of the QuickApp .NET/Angular monolith into a microservices architecture using Spring Boot and React.

## Architecture

```
                    ┌─────────────────┐
                    │  React Frontend  │
                    │   (port 3000)    │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │   API Gateway    │
                    │   (port 8080)    │
                    └────────┬────────┘
               ┌─────────┬──┴──┬─────────┐
          ┌────▼───┐ ┌───▼──┐ ┌▼──────┐ ┌▼──────┐
          │  Auth  │ │Cust. │ │Product│ │ Order │
          │ :8081  │ │:8082 │ │ :8083 │ │ :8084 │
          └────────┘ └──────┘ └───────┘ └───────┘
              │         │         │         │
           SQLite    SQLite    SQLite    SQLite
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| API Gateway | 8080 | Spring Cloud Gateway — routes requests to services |
| Auth Service | 8081 | JWT authentication, user/role management |
| Customer Service | 8082 | Customer CRUD operations |
| Product Service | 8083 | Product and category management |
| Order Service | 8084 | Order management with cross-service enrichment |
| Frontend | 3000 | React + Vite + TypeScript + React-Bootstrap |

## Tech Stack

- **Backend**: Spring Boot 3.4.4, Java 17, Spring Security, Spring Data JPA
- **Database**: SQLite (per-service isolation)
- **Auth**: JWT (HS512) with shared secret
- **Frontend**: React 18, TypeScript, Vite, React-Bootstrap
- **Containerization**: Docker + Docker Compose

## Quick Start

### With Docker Compose

```bash
cd microservices
docker-compose up --build
```

### Without Docker (development)

Start each service individually:

```bash
# 1. Auth Service
cd auth-service && mvn spring-boot:run

# 2. Customer Service
cd customer-service && mvn spring-boot:run

# 3. Product Service
cd product-service && mvn spring-boot:run

# 4. Order Service
cd order-service && mvn spring-boot:run

# 5. API Gateway
cd api-gateway && mvn spring-boot:run

# 6. Frontend
cd frontend && npm install && npm run dev
```

### Default Credentials

- **Admin**: admin / tempP@ss123
- **User**: user / tempP@ss123

## API Endpoints

### Auth Service (`/api/auth/`)
- `POST /api/auth/token` — Login
- `POST /api/auth/refresh` — Refresh token
- `GET /api/auth/users/me` — Current user
- `GET/POST /api/auth/users` — List/create users
- `GET/PUT/DELETE /api/auth/users/{id}` — User CRUD
- `GET/POST /api/auth/roles` — List/create roles
- `GET/PUT/DELETE /api/auth/roles/{id}` — Role CRUD
- `GET /api/auth/permissions` — List permissions

### Customer Service (`/api/customers/`)
- `GET/POST /api/customers` — List/create customers
- `GET/PUT/DELETE /api/customers/{id}` — Customer CRUD

### Product Service (`/api/products/`, `/api/product-categories/`)
- `GET/POST /api/products` — List/create products
- `GET/PUT/DELETE /api/products/{id}` — Product CRUD
- `GET/POST /api/product-categories` — List/create categories
- `GET/PUT/DELETE /api/product-categories/{id}` — Category CRUD

### Order Service (`/api/orders/`)
- `GET/POST /api/orders` — List/create orders
- `GET/PUT/DELETE /api/orders/{id}` — Order CRUD

## Comparison Tests

Run the comparison tests to verify parity between the monolith and microservices:

```bash
cd comparison-tests
python compare.py
```

This generates `comparison-report.json` and `comparison-report.md`.

## Seed Data

Each service seeds data on first startup:

- **Auth**: 2 roles (administrator, user), 2 users (admin, user)
- **Customers**: 14 customers (4 default + 10 additional)
- **Products**: 6 categories, 12 products (2 default + 10 additional)
- **Orders**: 12 orders (2 default + 10 additional) with multiple line items
