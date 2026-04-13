# FTMS - Foreign Trade Management System

A complete **three-tier foreign trade management system** with multi-role approval workflows, real-time exchange rates, SWIFT code integration, and secure account activation before transaction access.

**📊 Project Status:** Final Year Project ✅ | **🔓 License:** Apache 2.0 | **💰 Cost:** 100% FREE

**[Latest Updates](#implementation-status---april-2026)** | **[Quick Start](#quick-start)** | **[Features](#completed-features)** | **[License](#license--attribution)**

---

## Implementation Status - April 2026

### ✅ **COMPLETED FEATURES** (Ready for Production)

| Feature | Status | Details |
|---------|--------|---------|
| **Account Registration** | ✅ Complete | Email, bank details, SWIFT code (6-11 chars) |
| **Two-Tier Access Control** | ✅ Complete | Account Status + KYC Status (separate flows) |
| **JWT Authentication** | ✅ Complete | Spring Security + BCrypt password hashing |
| **6 User Roles** | ✅ Complete | Admin, Central Bank, Commercial Bank, Importer, Exporter, Exchanger |
| **KYC Verification** | ✅ Complete | Passport document upload + admin approval |
| **SWIFT Code Integration** | ✅ Complete | Mandatory format validation (6-11 alphanumeric) |
| **Forex Transactions** | ✅ Complete | Multi-step approval workflow |
| **USD Bridge Currency** | ✅ Complete | Source → USD → Destination conversion |
| **Invoice Generation** | ✅ Complete | PDF-ready with conversion breakdown & SWIFT codes |
| **Multi-Step Approval** | ✅ Complete | Central Bank → Commercial Bank verification |
| **Rejection Tracking** | ✅ Complete | Users see rejection reasons |
| **Audit Trail** | ✅ Complete | Timestamps, approver details, status history |
| **Error Handling** | ✅ Complete | Global exception handler, validation messages |
| **Input Validation** | ✅ Complete | SWIFT format, amount ranges, email formats |
| **Responsive UI** | ✅ Complete | HTML5/CSS3 with professional banking theme |
| **Database Schema** | ✅ Complete | H2 + PostgreSQL support |
| **REST API** | ✅ Complete | 20+ endpoints with proper HTTP methods |
| **CORS Security** | ✅ Complete | Configurable allowed origins |

### 🔄 **PLANNED ENHANCEMENTS** (For A+ Grade)

| Feature | Priority | Effort | Value |
|---------|----------|--------|-------|
| **Unit Tests (JUnit + Mockito)** | 🔴 CRITICAL | 2-3 days | Tests for Service/Controller/Repo layers |
| **API Documentation (Swagger/OpenAPI)** | 🔴 CRITICAL | 1-2 days | Auto-generated /swagger-ui endpoint |
| **Database Migrations (Flyway)** | 🟡 HIGH | 1 day | Version control for schema changes |
| **Transaction Audit Log** | 🟡 HIGH | 1 day | Full history of all transaction changes |
| **Email Notifications** | 🟡 HIGH | 1 day | Approval/Rejection emails (JavaMailSender) |
| **Advanced Search/Filter** | 🟡 HIGH | 2 days | Date range, status, currency filters |
| **Rate Limiting** | 🟡 MEDIUM | 1 day | API abuse protection (Bucket4j) |
| **PDF Invoice Export** | 🟢 NICE | 1 day | Download invoice as PDF (Apache PDFBox) |
| **Dashboard Analytics** | 🟢 NICE | 2 days | Charts & graphs (ApexCharts) |
| **Real-time Notifications** | 🔴 ADVANCED | 3 days | WebSocket for live updates |

---

## Key Real-World Banking Features

- **SWIFT/BIC Code Support** — International bank identifier (6-11 alphanumeric chars, mandatory)
- **USD Bridge Currency** — All conversions: Source → USD → Destination (industry standard)
- **Two-Tier Access Control** — Account Activation + KYC Verification (regulatory compliance)
- **Full Audit Trail** — Rejection reasons, exchange rate sources, banker approval
- **Bank Charges** — SWIFT processing fees shown on invoice ($15 default USD)
- **Multi-Role Approvals** — Admin → Central Bank → Commercial Bank (regulatory hierarchy)
- **Invoice System** — Professional documents with conversion breakdown & SWIFT codes

## Project Structure

```
Mini-project/
├── backend/                    # Spring Boot 3.2.0 REST API
│   ├── src/
│   │   ├── main/java/com/ftms/
│   │   │   ├── FtmsApplication.java
│   │   │   ├── config/        # Spring Security, CORS, Default Users
│   │   │   ├── controller/    # REST endpoints (6 controllers)
│   │   │   ├── dto/           # Data transfer objects
│   │   │   ├── filter/        # JWT authentication filter
│   │   │   ├── model/         # JPA entities (User, Transaction, ExchangeRate)
│   │   │   ├── repository/    # Spring Data JPA interfaces
│   │   │   └── service/       # Business logic (Forex, Invoice, User, JWT, Transaction)
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-prod.properties
│   ├── pom.xml                # Maven dependencies (all opensource)
│   └── Dockerfile             # Docker build file
├── frontend/                   # HTML5/CSS3/JavaScript (No Framework)
│   ├── index.html             # Home page
│   ├── login.html             # Login screen
│   ├── register.html          # Registration with SWIFT code
│   ├── invoice.html           # Invoice viewer
│   ├── role-selection.html    # User role selection
│   ├── css/styles.css         # Professional banking theme
│   ├── js/
│   │   ├── config.js          # API configuration
│   │   └── auth.js            # JWT authentication handler
│   ├── admin/dashboard.html   # Account & KYC approval
│   ├── central-bank/dashboard.html    # Transaction approval
│   ├── bank/dashboard.html    # Transaction verification
│   └── user/
│       ├── dashboard.html     # User home
│       ├── importer-dashboard.html    # Import transactions
│       ├── exporter-dashboard.html    # Export transactions
│       └── exchanger-dashboard.html   # Currency exchange
└── database/
    └── schema.sql             # PostgreSQL schema (Supabase)
```

## Tech Stack (All Completely FREE & Open Source)
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

# Set Environment Variables (Required for production):
export DB_URL=jdbc:postgresql://db.PROJECT_ID.supabase.co:5432/postgres?sslmode=require
export DB_USERNAME=postgres
export DB_PASSWORD=your_supabase_password
export JWT_SECRET=your_secret_key_min_32_chars
export ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5500,https://your-frontend.netlify.app
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

All test accounts pre-loaded with password: **Admin@123**

| Email | Role | Account Status | KYC Status | Purpose |
|-------|------|---|---|---------|
| admin@ftms.com | ADMIN | APPROVED | APPROVED | User & KYC management |
| centralbank@ftms.com | CENTRAL_BANK | APPROVED | APPROVED | Transaction approval & rates |
| bank@ftms.com | COMMERCIAL_BANK | APPROVED | APPROVED | Transaction verification |
| importer@ftms.com | IMPORTER | APPROVED | APPROVED | Import transactions |
| exporter@ftms.com | EXPORTER | APPROVED | APPROVED | Export transactions |
| exchanger@ftms.com | EXCHANGER | APPROVED | APPROVED | Currency exchange |

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

### Phase 1: Account Registration & Activation
1. User registers at `/register.html` with personal & bank details (SWIFT code mandatory)
2. Account created with `account_status = PENDING` — user cannot login yet
3. User sees: "Your account is pending admin approval"
4. Admin logs in → Admin Dashboard → Account Approval section
5. Admin reviews bank details, SWIFT code, documents
6. Admin clicks "Approve Account" → `account_status = APPROVED`
7. User can now login successfully

### Phase 2: KYC Verification
1. User logs in → Dashboard shows "Complete KYC verification to initiate transactions"
2. Admin sees user in KYC Approval section
3. Admin reviews KYC documents → Clicks "Approve KYC" → `kyc_status = APPROVED`
4. User can now initiate forex transactions

### Phase 3: Forex Transaction & Multi-Step Approval
1. User places transaction:
   - Enters amount & currency pair
   - Requires **Beneficiary SWIFT code** (format: HDFCINBB or HDFCINBBXXX)
   - System auto-fills sender's SWIFT code from profile
   - Shows live USD bridge conversion with bank charges
2. Transaction created with `status = PENDING_CENTRAL_BANK`
3. Central Bank officer logs in → Reviews transaction including sender/beneficiary SWIFT codes
4. Central Bank clicks "Approve" → `status = APPROVED_BY_CENTRAL_BANK`
5. Commercial Bank officer logs in → Verifies transaction details
6. Bank clicks "Verify & Complete" → `status = COMPLETED`
7. User downloads invoice showing:
   - Both SWIFT codes
   - Two-step conversion: [Amount] → USD → [Final]
   - Both exchange rates
   - Bank charges ($15 default)
   - Transaction status stamp

## API Endpoints

### Public (No Authentication)
- `POST /api/auth/register` - Register new user (account_status = PENDING)
- `POST /api/auth/login` - Login and get JWT token (checks account_status & kyc_status)
- `GET /api/forex/rates` - Get live exchange rates

### User Endpoints (JWT Required)
- `POST /api/forex/transaction` - Create transaction (**requires kyc_status = APPROVED**)
- `GET /api/forex/my-transactions` - User's transaction history
- `GET /api/forex/invoice/{id}` - Download transaction invoice

### Admin Endpoints (ADMIN role)
- `GET /api/admin/pending-account` - Users awaiting account activation
- `GET /api/admin/pending-kyc` - Users awaiting KYC approval
- `PUT /api/admin/approve-account/{id}` - Activate user account
- `PUT /api/admin/reject-account/{id}` - Reject user account
- `PUT /api/admin/approve-kyc/{id}` - Approve KYC
- `PUT /api/admin/reject-kyc/{id}` - Reject KYC

### Central Bank Endpoints (CENTRAL_BANK role)
- `GET /api/central-bank/pending` - Pending transactions
- `GET /api/central-bank/approved` - Approved transactions
- `GET /api/central-bank/rejected` - Rejected transactions
- `PUT /api/central-bank/approve/{id}` - Approve transaction
- `PUT /api/central-bank/reject/{id}` - Reject with reason

### Commercial Bank Endpoints (COMMERCIAL_BANK role)
- `GET /api/bank/pending-verification` - Awaiting verification
- `GET /api/bank/completed` - Completed transactions
- `PUT /api/bank/verify/{id}` - Verify & complete transaction

## Features

✅ **Two-Tier Access Control** — Account activation + KYC approval (separate flows)
✅ **SWIFT Code Integration** — Bank identifier validation (8 or 11 characters)
✅ **USD Bridge Currency** — All conversions flow through USD
✅ **Bank Charges** — Automatic SWIFT processing fees on invoices
✅ **Full Invoice** — Two-step conversion breakdown, both SWIFT codes, rejection reasons
✅ **Session Security** — JWT token with auto-logout on 401 Unauthorized 
✅ **Role-Based Auto Redirect** — Immediate dashboard navigation after login
✅ **Rejection Tracking** — User sees Central Bank rejection reasons
✅ **Exchange Rate Transparency** — Timestamp & source displayed (API or Manual)
✅ **Environment Variables** — Secure configuration for production
✅ **Three-tier architecture** (Frontend, Backend, Database)
✅ **Multi-role access control** (Admin, Central Bank, Commercial Bank, Users)
✅ **Real-time exchange rates** from free API
✅ **Forex transaction lifecycle management** with audit trail
✅ **JWT-based authentication** with stateless sessions
✅ **BCrypt password hashing** for security
✅ **Docker containerization** for easy deployment
✅ **CORS security** with configurable allowed origins

---

## 🔧 Tech Stack Details (All Open Source & FREE)

### Backend: Spring Boot 3.2.0 (Java 17)
```
✅ spring-boot-starter-web         - REST API, Tomcat embedded
✅ spring-boot-starter-data-jpa    - ORM with Hibernate
✅ spring-boot-starter-security    - Authentication & Authorization
✅ spring-boot-starter-validation  - Input validation framework
✅ jjwt (0.11.5)                   - JWT token generation & validation
✅ h2 (2.2.224)                    - In-memory database (dev/test)
✅ postgresql (42.6.1)             - PostgreSQL JDBC driver (production)
✅ lombok (1.18.30)                - Reduce boilerplate code
✅ gson (2.10.1)                   - JSON serialization
✅ spring-boot-starter-mail        - Ready for email (future enhancement)
```

### Frontend: Pure Web Standards
```
✅ HTML5                           - Semantic structure
✅ CSS3                            - Responsive grid, flexbox, CSS variables
✅ JavaScript (ES6+)               - No dependencies, pure vanilla JS
✅ Dark mode theme                 - Professional banking colors
```

### Database: PostgreSQL (Supabase)
```
✅ PostgreSQL 14+                  - Production-grade relational DB
✅ H2 2.2.224                      - Embedded for testing
✅ Spring Data JPA                 - ORM abstraction layer
```

### DevOps (All FREE Tiers)
```
✅ Maven 3.9+                      - Build automation
✅ Docker                          - Containerization
✅ Render.com                      - Backend hosting (FREE tier)
✅ Netlify                         - Frontend hosting (FREE tier)
✅ Supabase                        - PostgreSQL hosting (FREE tier)
```

**Total Cost of Ownership: $0 per month** 💰

---

## 📊 Database Schema

### Users Table (ftms_users)
```sql
- id (BIGINT, PK, Auto-increment)
- full_name, email (UNIQUE), password (BCrypt hash)
- role (ENUM: ADMIN, CENTRAL_BANK, COMMERCIAL_BANK, IMPORTER, EXPORTER, EXCHANGER)
- account_status (ENUM: PENDING, APPROVED, REJECTED)
- kyc_status (ENUM: PENDING, APPROVED, REJECTED)
- bank_name, account_number, ifsc_code, swift_code (6-11 chars)
- passport_data (Base64 encoded image)
- created_at, updated_at (Timestamps)
```

### Transactions Table (ftms_transactions)
```sql
- id (BIGINT, PK)
- user_id (FK to users)
- transaction_type (IMPORT, EXPORT, EXCHANGE)
- from_currency, to_currency, from_amount, to_amount
- bridge_currency (always USD), bridge_amount
- exchange_rate, bank_charges (USD 15.00 default)
- status (PENDING_CENTRAL_BANK → APPROVED_BY_CENTRAL_BANK → VERIFIED_BY_BANK → COMPLETED)
- beneficiary_name, beneficiary_bank, beneficiary_swift
- rejection_reason, purpose
- central_bank_approved_by, bank_verified_by (FK to users)
- created_at, updated_at (Timestamps)
```

### Exchange Rates Table (ftms_exchange_rates)
```sql
- id (BIGINT, PK)
- base_currency, target_currency
- rate (DECIMAL 15,6)
- source (API or CENTRAL_BANK_MANUAL)
- fetched_at (Timestamp)
```

---

## 📞 Support

For issues:
1. **Testing locally?** Kill Java processes: `Get-Process java | Stop-Process -Force`
2. **Database locked?** Delete H2 files: `Remove-Item ftms_db* -Force`
3. **API errors?** Check `/swagger-ui.html` (after Swagger integration)
4. **Integration issues?** Verify `frontend/js/config.js` API_BASE_URL

---

## 📄 License & Attribution

### Project License: **Apache License 2.0**

This project is licensed under the Apache License 2.0 - see the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0) for details.

**What this means:**
- ✅ **Free for commercial & non-commercial use**
- ✅ **You can modify & distribute**
- ✅ **Must include license & attribution**
- ✅ **No warranty provided (as-is)**

### Open Source Dependencies Attribution

This project uses the following **FREE & Open Source** libraries:

| Library | License | Purpose |
|---------|---------|---------|
| Spring Boot 3.2.0 | Apache 2.0 | Web framework & DI |
| Spring Security | Apache 2.0 | Authentication & authorization |
| Spring Data JPA | Apache 2.0 | ORM & database abstraction |
| Hibernate ORM | LGPL 2.1 | Object-relational mapping |
| PostgreSQL JDBC | BSD 2-Clause | Database driver |
| H2 Database | MPL 2.0 / EPL 1.0 | In-memory test database |
| JJWT | Apache 2.0 | JWT token library |
| Lombok | MIT | Code generation |
| Gson | Apache 2.0 | JSON serialization |
| Maven | Apache 2.0 | Build tool |
| Docker | Apache 2.0 | Containerization |

**All dependencies are completely FREE and open source with permissive licenses.**

### How to Use This Project

1. **For Learning:** ✅ Use freely for educational purposes
2. **For College Submit:** ✅ Submit as your final year project
3. **For Commercial:** ✅ You can commercialize with proper attribution
4. **For Modification:** ✅ Fork, modify, and distribute

**Required Attribution:**
If you use this project, please include:
```
FTMS - Foreign Trade Management System
Licensed under Apache License 2.0
Original project: [Your Repository Link]
```

---

## Citation Format

**APA Format:**
```
Sharma, [Your Name]. (2026). FTMS - Foreign Trade Management System 
[Computer software]. Apache License 2.0. 
Retrieved from https://github.com/[your-username]/mini-project
```

**BibTeX Format:**
```bibtex
@software{ftms2026,
  author = {Sharma, [Your Name]},
  title = {FTMS - Foreign Trade Management System},
  year = {2026},
  license = {Apache License 2.0},
  url = {https://github.com/[your-username]/mini-project}
}
```

---

## 💡 Project Credits

- **Forex Data:** exchangerate-api.com (Free API)
- **Design Inspiration:** RBI (Reserve Bank of India) SWIFT protocols
- **Banking Industry Standards:** ISO 20022, SWIFT Standards
- **Compliance:** KYC/AML regulatory frameworks

---

## 🚀 Deployment Costs (Monthly)

| Component | Service | Cost | Notes |
|-----------|---------|------|-------|
| Backend API | Render.com | FREE (up to 750 hrs/month) | Auto-sleeps after 15 min inactivity |
| Frontend | Netlify | FREE (unlimited) | Excellent for static sites |
| Database | Supabase (Postgres) | FREE (500 MB storage) | Upgrade to paid if needed |
| **TOTAL** | | **$0/month** | 🎉 |

For production with higher usage, budget $20-50/month.

---

## 📋 Project Checklist for A+ Grade

```
COMPLETED ✅
[✅] 3-tier architecture with proper separation
[✅] Multi-role access control system
[✅] Complex approval workflows
[✅] Real-world SWIFT code integration
[✅] Professional UI/UX design
[✅] Comprehensive error handling
[✅] Input validation
[✅] 20+ REST API endpoints
[✅] Invoice generation system
[✅] Audit trails & rejection tracking
[✅] Responsive design
[✅] 100% FREE stack
[✅] Docker containerization ready
[✅] Well-documented README

RECOMMENDED FOR A+ ⭐
[⭐] Add Unit Tests (2-3 days) → Critical for grades
[⭐] Add Swagger/OpenAPI docs (1 day) → Professional impression
[⭐] Add Database Migrations (1 day) → Best practice
[⭐] Add Email Notifications (1 day) → Feature enhancement
[⭐] Add Search/Filter (2 days) → UX improvement

NICE TO HAVE (Bonus) 🎁
[🎁] PDF Invoice Download
[🎁] Dashboard Analytics
[🎁] Rate Limiting
[🎁] WebSocket Notifications
```

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

### Account approval not working
- Verify account_status field exists in ftms_users table
- Check Admin Dashboard shows "Pending Accounts" section
- Ensure admin user has ADMIN role

### KYC block on transactions
- Verify kyc_status check in ForexController 
- User dashboard should show KYC pending message
- Transaction creation should return 403 if KYC not approved

---

## Support & Documentation

For detailed questions or issues:
1. Review server logs for Spring Boot error messages
2. Check database schema in Supabase SQL Editor
3. Verify all environment variables are set correctly
4. Test API endpoints with Postman or curl
5. Check browser console for frontend JavaScript errors
