# FTMS - Foreign Trade Management System

A complete three-tier foreign forex trading management system with instant signup (no email verification), real-time exchange rates, and multi-role approval workflows.

## Project Structure

```
forex-system/
├── backend/                    # Spring Boot REST API
│   ├── src/
│   │   ├── main/java/com/ftms/
│   │   │   ├── FtmsApplication.java
│   │   │   ├── config/        # Spring Security & CORS config
│   │   │   ├── controller/    # REST endpoints
│   │   │   ├── model/         # JPA entities
│   │   │   ├── dto/           # Data transfer objects
│   │   │   ├── repository/    # Database interfaces
│   │   │   ├── service/       # Business logic
│   │   │   └── filter/        # JWT authentication filter
│   │   └── resources/
│   │       └── application.properties
│   ├── pom.xml                # Maven dependencies
│   └── Dockerfile             # Docker build file
├── frontend/                   # HTML/CSS/JavaScript UI
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── css/styles.css
│   ├── js/
│   │   ├── config.js
│   │   └── auth.js
│   ├── admin/dashboard.html
│   ├── user/dashboard.html
│   ├── central-bank/dashboard.html
│   └── bank/dashboard.html
└── database/
    └── schema.sql             # PostgreSQL schema (Supabase)

## Tech Stack

### Backend (Tier 2)
- **Java 17** with **Spring Boot 3.2.0**
- **Spring Data JPA** for database operations
- **Spring Security** with **JWT** authentication
- **PostgreSQL** driver for database
- **Maven** for build management
- **Docker** for containerization

### Database (Tier 3)
- **PostgreSQL** on **Supabase**
- Schema includes: `ftms_users`, `ftms_transactions`, `ftms_exchange_rates` tables

### Frontend (Tier 1)
- Pure **HTML5, CSS3, JavaScript** (no framework)
- **Netlify** for hosting
- Responsive design with professional banking theme

## Getting Started

### 1. Prerequisites
- Java 17 (https://adoptium.net)
- Maven 3.9+
- Supabase account + project
- Git

### 2. Local Development

#### Setup Database
```bash
# Apply the PostgreSQL schema to Supabase using MCP (recommended)
# or paste database/schema.sql in Supabase SQL Editor and run it.
```

#### Configure Backend
```bash
cd backend
# Set environment variables (example in ../.env.example):
# SPRING_DATASOURCE_URL=jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres?sslmode=require
# SPRING_DATASOURCE_USERNAME=postgres
# SPRING_DATASOURCE_PASSWORD=<your-supabase-db-password>
```

#### Run Backend
```bash
cd backend
mvn spring-boot:run
# Backend runs on http://localhost:8080
```

#### Run Frontend
```bash
# Option 1: VS Code Live Server extension
# Right-click frontend/index.html → "Open with Live Server"

# Option 2: Python simple server
cd frontend
python -m http.server 3000

# Frontend runs on http://localhost:3000 or http://localhost:5500
```

#### Update Config
Edit `frontend/js/config.js`:
```javascript
const CONFIG = {
    API_BASE_URL: 'http://localhost:8080',
    // ...
};
```

### 3. Default Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@ftms.com | Admin@123 |
| Central Bank | centralbank@ftms.com | Admin@123 |
| Commercial Bank | bank@ftms.com | Admin@123 |

### 4. Production Deployment

#### Deploy Backend to Render
1. Sign up at https://render.com
2. Create new Web Service
3. Connect GitHub repository (backend folder)
4. Set environment variables:
    - `SPRING_DATASOURCE_URL`: Supabase PostgreSQL JDBC URL
    - `SPRING_DATASOURCE_USERNAME`: Database username
    - `SPRING_DATASOURCE_PASSWORD`: Database password
    - `JWT_SECRET`: Your secret key

#### Deploy Frontend to Netlify
1. Sign up at https://netlify.com
2. Drag and drop `frontend` folder OR connect GitHub
3. Update `frontend/js/config.js`:
   ```javascript
   API_BASE_URL: 'https://your-render-backend.onrender.com'
   ```

#### Setup Database on Supabase
1. Sign up at https://supabase.com
2. Create a project and copy Postgres connection details from the Connect panel
3. Apply `database/schema.sql` through Supabase SQL Editor or Supabase MCP migration
4. Use the connection details in Render environment variables

## User Flows

### Registration
1. User registers at `/register.html` with login details
2. Account is created immediately (no email verification)
3. User can login and continue role selection

### Forex Transaction
1. User logs in and places order at `/user/dashboard.html`
2. Transaction status: PENDING_CENTRAL_BANK
3. Central Bank reviews at `/central-bank/dashboard.html` and clicks APPROVE
4. Commercial Bank verifies at `/bank/dashboard.html` and marks COMPLETED
5. User sees completed transaction in history

### Admin Functions
- View pending KYC requests
- Approve/Reject user registrations
- View all users
- View all transactions

## API Endpoints

### Authentication (Public)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Forex (Public/Private)
- `GET /api/forex/rates` - Get live exchange rates (public)
- `POST /api/forex/transaction` - Place forex order (authenticated)
- `GET /api/forex/my-transactions` - Get user's transactions (authenticated)

### Admin (ADMIN role only)
- `GET /api/admin/pending-kyc` - Get pending KYC users
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/kyc/{userId}/approve` - Approve KYC
- `PUT /api/admin/kyc/{userId}/reject` - Reject KYC
- `GET /api/admin/transactions` - Get all transactions

### Central Bank (CENTRAL_BANK role only)
- `GET /api/central-bank/pending` - Get pending transactions
- `PUT /api/central-bank/approve/{transactionId}` - Approve transaction
- `PUT /api/central-bank/reject/{transactionId}` - Reject transaction

### Commercial Bank (COMMERCIAL_BANK role only)
- `GET /api/bank/pending-verification` - Get transactions awaiting verification
- `PUT /api/bank/verify/{transactionId}` - Complete transaction

## Features

✅ Three-tier architecture (Frontend, Backend, Database)
✅ Multi-role access control (Admin, Central Bank, Commercial Bank, Users)
✅ Instant signup without email verification
✅ Real-time exchange rates (free API)
✅ Forex transaction lifecycle management
✅ JWT-based authentication
✅ BCrypt password hashing
✅ Responsive UI with professional design
✅ Docker containerization
✅ Database schema with relationships

## Troubleshooting

### Backend won't start
- Check Java version: `java -version`
- Verify Supabase project is active
- Check datasource environment variables
- Review logs in console

### Frontend can't connect to backend
- Verify backend API_BASE_URL in `config.js`
- Check CORS configuration in `SecurityConfig.java`
- Ensure backend is running on port 8080

### Database errors
- Run schema.sql to create tables
- Verify Supabase credentials and host
- Ensure SSL is enabled in the connection string (`sslmode=require`)

## Support & Documentation

Refer to the FTMS-COMPLETE-GUIDE.md for:
- Detailed tech stack explanation
- Complete user flow diagrams
- Database schema explanation
- Three test scenarios for Selenium/TestNG
- Step-by-step deployment guide
