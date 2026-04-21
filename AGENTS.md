# AGENTS.md — QuickApp Development Guide

## Repository Structure

This is a monorepo containing:
1. **Monolith** (root): .NET/Angular full-stack application
2. **Microservices** (`microservices/`): Spring Boot + React decomposition

## Monolith (.NET/Angular)

### Build & Lint
```bash
dotnet build --no-restore
cd quickapp.client && npx ng lint
```

### Database
- Uses SQLite (`Data Source=quickapp.db`)
- Migrations: `dotnet ef migrations add <Name> --project QuickApp.Server`
- Update: `dotnet ef database update --project QuickApp.Server`

### Key Patterns
- **Entity Framework Core** with Code First migrations
- **OpenIddict** for OAuth2/OIDC authentication
- **BaseEntity** pattern for audit fields (CreatedBy, UpdatedBy, CreatedDate, UpdatedDate)
- **BaseApiController** for consistent error handling
- **AutoMapper** for DTO mapping (ViewModels)

## Microservices (Spring Boot + React)

### Services
| Service | Port | Tech |
|---------|------|------|
| API Gateway | 8080 | Spring Cloud Gateway |
| Auth Service | 8081 | Spring Boot + JWT |
| Customer Service | 8082 | Spring Boot + JPA |
| Product Service | 8083 | Spring Boot + JPA |
| Order Service | 8084 | Spring Boot + JPA |
| Frontend | 3000 | React + Vite + TypeScript |

### Build (each service)
```bash
cd microservices/<service-name> && mvn clean package -DskipTests
```

### Frontend Build
```bash
cd microservices/frontend && npm install && npm run build
```

### Key Patterns
- **JWT (HS512)** shared secret across all services
- **SQLite** per-service database isolation
- **Spring Data JPA** for data access
- **CommandLineRunner DataSeeder** for initial data
- **Cross-service REST calls** via RestTemplate (order-service → customer/product)
- **React Context** for auth state management
- **Axios interceptors** for JWT token handling

### Conventions
- All microservice code lives under `microservices/`
- Each service has its own `pom.xml`, `Dockerfile`, and `application.yml`
- DTOs follow the monolith's ViewModel naming patterns
- Seed data replicates monolith's `DatabaseSeeder.cs` data
- Docker Compose orchestrates all services

### Comparison Tests
```bash
cd microservices/comparison-tests && python compare.py
```
Generates `comparison-report.json` and `comparison-report.md`.
