# FTMS - Foreign Trade Management System
## Complete Tech Stack Documentation

---

## 📱 FRONTEND

### Technologies
- **HTML5** - Markup & structure
- **CSS3** - Styling (custom styles.css)
- **JavaScript (Vanilla)** - No frameworks

### File Structure
```
frontend/
├── index.html                    (Landing page)
├── login.html                    (Authentication page)
├── register.html                 (User registration)
├── role-selection.html           (Role selection after signup)
├── invoice.html                  (Invoice generation)
├── css/
│   └── styles.css               (Global styling)
├── js/
│   ├── config.js                (Configuration & API endpoints)
│   ├── auth.js                  (Authentication logic)
│   └── api-interceptor.js       (API call wrapper with 401 handling)
├── admin/
│   └── dashboard.html           (Admin dashboard)
├── bank/
│   └── dashboard.html           (Bank officer dashboard)
├── central-bank/
│   └── dashboard.html           (Central bank dashboard)
└── user/
    ├── dashboard.html           (User main dashboard)
    ├── importer-dashboard.html  (Importer role dashboard)
    ├── exporter-dashboard.html  (Exporter role dashboard)
    └── exchanger-dashboard.html (Exchanger role dashboard)
```

### Key Configuration (config.js)
- **API_BASE_URL**: `https://mini-project-059o.onrender.com` (Production)
- **Fallback URL**: `http://localhost:8080` (Local development)
- **Exchange API**: ExchangeRate API (exchangerate-api.com)
- **Bridge Currency**: USD (all conversions use USD as intermediary)
- **Supported Currencies**: USD, EUR, GBP, JPY, AUD, CAD, CHF, INR, SGD, HKD, CNY, AED

### Authentication Flow
- JWT Tokens stored in localStorage
- Automatic 401 handling - redirects to login on session expiry
- Stored data: `token`, `role`, `userId`, `fullName`, `kycStatus`, `accountStatus`

### API Interceptor
- Global fetch wrapper in `api-interceptor.js`
- Handles 401 Unauthorized responses
- Clears session data on expiry

---

## 🔧 BACKEND

### Framework & Language
- **Spring Boot 3.2.0** (Java 17)
- **Maven** (Build tool)
- **Render.com** (Deployment)

### Dependencies & Libraries

| Dependency | Version | Purpose |
|-----------|---------|---------|
| Spring Boot Starter Web | 3.2.0 | REST API endpoints |
| Spring Data JPA | 3.2.0 | Database ORM |
| Spring Security | 3.2.0 | JWT authentication & CORS |
| PostgreSQL Driver | Latest | Database connectivity |
| JJWT (JWT Library) | 0.11.5 | JWT token creation/validation |
| H2 Database | Latest | In-memory testing database |
| Lombok | Latest | Reduces boilerplate code |
| Validation Starter | 3.2.0 | @NotBlank, @NotNull validation |
| Spring Boot Test | 3.2.0 | Unit & integration testing |

### Package Structure
```
com.ftms/
├── FtmsApplication.java                    (Main entry point)
├── controller/
│   ├── AuthController.java                 (POST /api/auth/register, /api/auth/login)
│   ├── AdminController.java                (Admin approval operations)
│   ├── UserController.java                 (User transactions & profile)
│   ├── BankController.java                 (Bank verification & approval)
│   ├── CentralBankController.java          (Central bank approval)
│   └── ForexController.java                (Exchange rate endpoints)
├── service/
│   ├── UserService.java                    (User registration, profile logic)
│   ├── JwtService.java                     (JWT token operations)
│   ├── TransactionService.java             (Transaction business logic)
│   ├── ForexService.java                   (Forex rate fetching & caching)
│   └── InvoiceService.java                 (Invoice generation)
├── model/
│   ├── User.java                           (User entity - @Entity)
│   ├── Transaction.java                    (Transaction entity - @Entity)
│   └── ExchangeRate.java                   (Exchange rate entity - @Entity)
├── repository/
│   ├── UserRepository.java                 (JPA interface - auto SQL queries)
│   ├── TransactionRepository.java          (JPA interface - auto SQL queries)
│   └── ExchangeRateRepository.java         (JPA interface - auto SQL queries)
├── dto/
│   ├── LoginRequest.java                   (Incoming login DTO)
│   ├── RegisterRequest.java                (Incoming registration DTO)
│   └── TransactionRequest.java             (Incoming transaction DTO)
├── filter/
│   └── JwtAuthFilter.java                  (JWT validation filter on every request)
└── config/
    ├── SecurityConfig.java                 (CORS, JWT, security rules)
    └── DefaultAccountInitializer.java      (Creates default users on startup)
```

### Models & Enums

#### User Entity
**Table**: `ftms_users`
```
Fields:
- id (Long, PK)
- fullName (String)
- email (String, Unique)
- password (String, BCrypt hashed)
- phone (String, Mandatory)
- city (String)
- address (Text)
- role (Enum: ADMIN, CENTRAL_BANK, COMMERCIAL_BANK, IMPORTER, EXPORTER, EXCHANGER)
- accountStatus (Enum: PENDING, APPROVED, REJECTED) - Admin approval required
- kycStatus (Enum: PENDING, APPROVED, REJECTED) - KYC verification
- roleSelected (Boolean) - Has user chosen their role?
- bankName (String, Mandatory)
- accountNumber (String, Mandatory)
- swiftCode (String, Mandatory)
- ifscCode (String, Mandatory)
- passportData (Text, Base64 encoded)
- createdAt (LocalDateTime)
- updatedAt (LocalDateTime)
```

#### Transaction Entity
**Table**: `ftms_transactions`
```
Fields:
- id (Long, PK)
- user (User, FK) - ManyToOne relationship
- transactionType (Enum: IMPORT, EXPORT, EXCHANGE)
- fromCurrency (String) - Source currency (e.g., INR)
- toCurrency (String) - Target currency (e.g., USD)
- fromAmount (BigDecimal) - Amount user is sending
- bridgeCurrency (String) - Always "USD" (bridge currency)
- bridgeAmount (BigDecimal) - Amount in USD after step 1 conversion
- toAmount (BigDecimal) - Final amount user receives
- bankCharges (BigDecimal) - Fee in USD (default 15.00)
- exchangeRate (BigDecimal) - Rate used at transaction time
- purpose (String) - Reason for transaction
- beneficiaryName (String)
- beneficiaryBank (String)
- beneficiarySwift (String)
- status (Enum: PENDING_CENTRAL_BANK, APPROVED_BY_CENTRAL_BANK, 
           REJECTED_BY_CENTRAL_BANK, VERIFIED_BY_BANK, COMPLETED, FAILED)
- centralBankApprovedBy (Long) - User ID of central bank officer
- bankVerifiedBy (Long) - User ID of bank officer
- rejectionReason (Text)
- createdAt (LocalDateTime)
- updatedAt (LocalDateTime)
```

#### ExchangeRate Entity
**Table**: `ftms_exchange_rates`
```
Fields:
- id (Long, PK)
- baseCurrency (String) - Source currency (USD)
- targetCurrency (String) - Target currency (INR, EUR, etc.)
- rate (BigDecimal) - Exchange rate
- source (Enum: API, CENTRAL_BANK_MANUAL)
- fetchedAt (LocalDateTime)
```

### API Endpoints

#### Authentication (Public - No JWT Required)
```
POST /api/auth/register
POST /api/auth/login
```

#### Admin Dashboard (ADMIN role required)
```
GET  /api/admin/pending-approvals          (Pending user approvals)
GET  /api/admin/pending-kyc                 (Pending KYC approvals)
POST /api/admin/approve-user/{userId}       (Approve user)
POST /api/admin/reject-user/{userId}        (Reject user)
POST /api/admin/approve-kyc/{userId}        (Approve KYC)
POST /api/admin/reject-kyc/{userId}         (Reject KYC)
GET  /api/admin/stats                       (Dashboard statistics)
```

#### Central Bank Operations (CENTRAL_BANK role required)
```
GET  /api/central-bank/pending              (Pending transactions)
GET  /api/central-bank/approved             (Approved transactions)
GET  /api/central-bank/rejected             (Rejected transactions)
POST /api/central-bank/approve/{txnId}      (Approve transaction)
POST /api/central-bank/reject/{txnId}       (Reject transaction)
```

#### Bank Operations (COMMERCIAL_BANK role required)
```
GET  /api/bank/pending-verification         (Pending verification)
GET  /api/bank/completed                    (Completed transactions)
POST /api/bank/verify/{txnId}               (Verify transaction)
```

#### User Operations (All authenticated users)
```
GET  /api/user/profile                      (User profile)
PUT  /api/user/update-profile               (Update profile)
POST /api/user/select-role                  (Select role after signup)
POST /api/user/place-transaction            (Create transaction)
GET  /api/user/transactions                 (User's transactions)
POST /api/user/upload-passport              (Upload passport image)
```

#### Forex Rates (All authenticated users)
```
GET  /api/forex/rates                       (Get all cached rates)
POST /api/forex/rate/{from}/{to}            (Get specific rate - fetches from API if not cached)
```

### Security Configuration
- **JWT Secret**: `ftms_jwt_secret_key_2024_very_long_string_change_this`
- **JWT Expiration**: 86400000 ms (24 hours)
- **CORS Origins**: 
  - `http://localhost:5500`
  - `http://127.0.0.1:5500`
  - `http://172.16.24.81:5500`
  - Netlify production domain
- **Password Encoding**: BCrypt (Spring Security)

### Default Test Accounts (on startup)
```
1. Admin
   Email: admin@ftms.com
   Password: Admin@123
   Role: ADMIN

2. Central Bank Officer
   Email: central@ftms.com
   Password: Admin@123
   Role: CENTRAL_BANK

3. Commercial Bank Officer
   Email: bank@ftms.com
   Password: Admin@123
   Role: COMMERCIAL_BANK

4. Importer
   Email: importer@ftms.com
   Password: Admin@123
   Role: IMPORTER

5. Exporter
   Email: exporter@ftms.com
   Password: Admin@123
   Role: EXPORTER

6. Exchanger
   Email: exchanger@ftms.com
   Password: Admin@123
   Role: EXCHANGER
```

---

## 🗄️ DATABASE

### Provider
- **Supabase PostgreSQL** (managed PostgreSQL)
- **Host**: `aws-1-ap-northeast-1.pooler.supabase.com:5432`
- **Database**: `postgres`
- **Connection Pooling**: Enabled
- **SSL Mode**: Enabled (required)

### Tables

#### 1. ftms_users
```sql
CREATE TABLE ftms_users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL CHECK (role IN ('ADMIN', 'CENTRAL_BANK', 'COMMERCIAL_BANK', 'IMPORTER', 'EXPORTER', 'EXCHANGER')),
    account_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (account_status IN ('PENDING', 'APPROVED', 'REJECTED')),
    kyc_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (kyc_status IN ('PENDING', 'APPROVED', 'REJECTED')),
    role_selected BOOLEAN NOT NULL DEFAULT FALSE,
    city VARCHAR(100),
    address TEXT,
    phone VARCHAR(20) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    swift_code VARCHAR(20) NOT NULL,
    ifsc_code VARCHAR(20) NOT NULL,
    passport_data TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### 2. ftms_transactions
```sql
CREATE TABLE ftms_transactions (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES ftms_users(id) ON DELETE CASCADE,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('IMPORT', 'EXPORT', 'EXCHANGE')),
    from_currency VARCHAR(10) NOT NULL,
    to_currency VARCHAR(10) NOT NULL,
    from_amount NUMERIC(15, 2) NOT NULL,
    bridge_currency VARCHAR(10) DEFAULT 'USD',
    bridge_amount NUMERIC(15, 2),
    to_amount NUMERIC(15, 2),
    bank_charges NUMERIC(15, 2) DEFAULT 15.00,
    exchange_rate NUMERIC(15, 6),
    purpose TEXT,
    beneficiary_name VARCHAR(100),
    beneficiary_bank VARCHAR(100),
    beneficiary_swift VARCHAR(20),
    status VARCHAR(40) NOT NULL DEFAULT 'PENDING_CENTRAL_BANK' CHECK (
        status IN ('PENDING_CENTRAL_BANK', 'APPROVED_BY_CENTRAL_BANK', 'REJECTED_BY_CENTRAL_BANK', 
                   'VERIFIED_BY_BANK', 'COMPLETED', 'FAILED')
    ),
    central_bank_approved_by BIGINT,
    bank_verified_by BIGINT,
    rejection_reason TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ftms_transactions_user_id ON ftms_transactions(user_id);
CREATE INDEX idx_ftms_transactions_status ON ftms_transactions(status);
```

#### 3. ftms_exchange_rates
```sql
CREATE TABLE ftms_exchange_rates (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    base_currency VARCHAR(10) NOT NULL,
    target_currency VARCHAR(10) NOT NULL,
    rate NUMERIC(15, 6) NOT NULL,
    source VARCHAR(50) NOT NULL DEFAULT 'API' CHECK (source IN ('API', 'CENTRAL_BANK_MANUAL')),
    fetched_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ftms_exchange_rates_pair ON ftms_exchange_rates(base_currency, target_currency);
CREATE INDEX idx_ftms_exchange_rates_fetched_at ON ftms_exchange_rates(fetched_at);
```

### Security
- **Row Level Security (RLS)**: Enabled on all tables
- **Public Schema Access**: Revoked from anonymous and authenticated users
- **Access Control**: Strictly managed via backend API

---

## 🚀 DEPLOYMENT & Infrastructure

### Frontend
- **Hosting**: Netlify
- **Build**: Static HTML/CSS/JS
- **CORS**: Configured for Render backend
- **Config File**: `netlify.toml`

### Backend
- **Hosting**: Render.com
- **Port**: 8080
- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Build**: Maven (pom.xml)
- **JAR Output**: `ftms-backend.jar`
- **Config Files**: 
  - `application.properties` (default)
  - `application-prod.properties` (production)

### Database
- **Provider**: Supabase (PostgreSQL 15)
- **Region**: AWS AP Northeast 1
- **Connection Details**: Stored in `.env`

### Environment Variables (.env)
```
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.ftbytspvsadvboiknmmy
SPRING_DATASOURCE_PASSWORD=Forex0809101112
DB_URL=jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require
DB_USERNAME=postgres.ftbytspvsadvboiknmmy
DB_PASSWORD=Forex0809101112

# JWT
JWT_SECRET=ftms_jwt_secret_key_2024_very_long_string_change_this
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:5500,http://127.0.0.1:5500,http://172.16.24.81:5500

# Forex API
FOREX_API_URL=https://api.exchangerate-api.com/v4/latest/USD

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

### Docker
- **Frontend Docker**: `frontend/Dockerfile`
- **Backend Docker**: `backend/Dockerfile`
- Both containerized for deployment

---

## 📊 Data Flow & Transaction Lifecycle

### User Registration
1. User fills form (HTML register.html)
2. Frontend sends JSON POST to `/api/auth/register`
3. Backend validates & stores user (UserService -> UserRepository)
4. User status: `PENDING` (awaits admin approval)
5. User redirected to role selection

### Transaction Flow (Import/Export/Exchange)
1. User initiates transaction (POST `/api/user/place-transaction`)
2. Status: `PENDING_CENTRAL_BANK`
3. Central Bank Officer reviews & approves (POST `/api/central-bank/approve/{txnId}`)
4. Status: `APPROVED_BY_CENTRAL_BANK`
5. Commercial Bank verifies (POST `/api/bank/verify/{txnId}`)
6. Status: `COMPLETED`

### Exchange Rate Fetching
1. Frontend requests rate for currency pair
2. Backend checks cache (ExchangeRate table)
3. If < 1 hour old: return cached rate
4. Else: fetch from api.exchangerate-api.com
5. Store new rate in database
6. Return rate to frontend

---

## 🔐 Security Features

1. **Password Security**: BCrypt hashing (Spring Security)
2. **JWT Tokens**: 24-hour expiration, stored in localStorage
3. **CORS Protection**: Whitelist of allowed origins
4. **Row Level Security**: PostgreSQL RLS on all tables
5. **HTTPS Enforcement**: Supabase SSL required
6. **Input Validation**: @NotBlank, @NotNull annotations
7. **Authentication Filter**: JwtAuthFilter validates every request
8. **Role-Based Access**: Controllers enforce role checks via Spring Security

---

## 📝 Testing

### Test File Location
- Main tests: `test_backend.py` (Python integration tests)
- Approve users script: `approve_users.py`

---

## Version Control

### Files Tracked
- `.env` - Environment configuration (secrets)
- `pom.xml` - Maven dependencies
- `package.json` - (if applicable)
- All source code in `backend/src/` and `frontend/`

---

**Last Updated**: April 15, 2026
**System**: FTMS (Foreign Trade Management System)
**Status**: Production Ready
