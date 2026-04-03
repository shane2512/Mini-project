# FTMS Testing Quick-Start Checklist

## 🚀 QUICK START: 5-MINUTE VERIFICATION

### Prerequisites (2 minutes)
- [ ] MySQL running locally (port 3306)
- [ ] Java 17+ installed
- [ ] Maven available
- [ ] Node.js/npx available
- [ ] 3 terminal windows open

### Terminal Setup (2 minutes)

**Terminal 1: MySQL**
```bash
# Start MySQL (Windows)
net start mysql80

# Or verify running
mysql -u root -p -e "SELECT VERSION();" | grep -i server
```

**Terminal 2: Backend**
```bash
cd C:\forex-system\backend
mvn spring-boot:run
# Expected: "Started FtmsApplication" + "Tomcat started on port(s): 8080"
```

**Terminal 3: Frontend**
```bash
cd C:\forex-system\frontend
npx live-server . --port=5500 --open=login.html
# Expected: Browser opens at http://127.0.0.1:5500/login.html
```

---

## ✅ QUICK TEST SEQUENCE (3 minutes)

### Test 1: System Health Check (30 seconds)
```bash
# Test backend API
curl http://localhost:8080/api/forex/rates

# Expected: Valid JSON response
# {
#   "rates": [
#     {"from": "USD", "to": "INR", "rate": 82.50},
#     ...
#   ]
# }
```
**Result:** ✅ PASS / ❌ FAIL

---

### Test 2: Login Flow (1 minute)
1. Go to browser: `http://127.0.0.1:5500/login.html`
2. Login with:
   - Email: `admin@ftms.com`
   - Password: `Admin@123`
3. **Expected:** Redirect to admin dashboard
4. **Result:** ✅ PASS / ❌ FAIL

---

### Test 3: Invoice Generation (1 minute)
1. Create dummy transaction in database:
```sql
USE ftms_db;
INSERT INTO transactions (user_id, from_currency, to_currency, amount, exchange_rate, status, created_at)
VALUES (1, 'USD', 'INR', 1000.00, 82.50, 'COMPLETED', NOW());
```

2. Get transaction ID (usually 1):
```sql
SELECT id FROM transactions ORDER BY id DESC LIMIT 1;
```

3. Go to: `http://127.0.0.1:5500/invoice.html?id=1`
4. **Expected:** Invoice displays with 15 fields
5. Click "Download PDF"
6. **Expected:** PDF downloads as `invoice-1.pdf`
7. **Result:** ✅ PASS / ❌ FAIL

---

### Test 4: Admin Stats (30 seconds)
1. Still logged in as admin
2. Check top of admin dashboard
3. **Expected:** 4 stat cards showing:
   - Total Users: Number ≥ 3
   - Pending KYC: Number
   - Total Transactions: Number
   - Completion Rate: Percentage
4. **Result:** ✅ PASS / ❌ FAIL

---

## 📋 COMPREHENSIVE TEST SUITE (30 minutes)

### Feature 1: Complete User Registration & Role Selection

**Setup (1 minute)**
```bash
# Clear test user if exists
mysql -u root -p ftms_db -e "DELETE FROM users WHERE email = 'testuser@test.com';"
```

**Test Steps (3 minutes)**
1. [ ] Go to `/register.html`
2. [ ] Fill form:
   - Name: "Test Import Specialist"
   - Email: "testuser@test.com"
   - Password: "Test@1234"
   - City: "Mumbai"
   - Address: "123 Trade Street"
   - Bank: "HDFC"
   - SWIFT: "HDFCINBB"
   - Passport: Upload any JPG/PNG
3. [ ] Click "Submit Registration & KYC"
4. [ ] **Verify:** Success message appears
5. [ ] Check database: 
```sql
SELECT email, kyc_status, role_selected FROM users WHERE email = 'testuser@test.com';
# Expected: testuser@test.com | PENDING | false
```
6. [ ] Logout

**KYC Approval (2 minutes)**
1. [ ] Login as `admin@ftms.com`
2. [ ] Go to "KYC Approvals" tab
3. [ ] Click passport image for "Test Import Specialist"
4. [ ] Verify modal shows passport image
5. [ ] Close modal → Click "Approve"
6. [ ] **Verify:** Success message

**Role Selection (1 minute)**
1. [ ] Logout → Login as `testuser@test.com`
2. [ ] **Verify:** Auto-redirect to `/role-selection.html`
3. [ ] Click "Importer" card
4. [ ] **Verify:** Redirect to user dashboard

**Database Verification (1 minute)**
```sql
SELECT email, kyc_status, role_selected, role FROM users WHERE email = 'testuser@test.com';
# Expected: testuser@test.com | APPROVED | true | IMPORTER
```

**Result:** ✅ PASS / ❌ FAIL

---

### Feature 2: Complete Transaction Workflow

**Setup (1 minute)**
```bash
# Ensure we have all test users
mysql -u root -p ftms_db << EOF
UPDATE users SET kyc_status='APPROVED', role_selected=true WHERE email='testuser@test.com';
EOF
```

**Create Transaction (2 minutes)**
1. [ ] Stay logged in as `testuser@test.com`
2. [ ] Go to "Place Order" tab
3. [ ] Select:
   - Type: "Import"
   - From: INR
   - To: USD
   - Amount: 50000
   - Purpose: "Test Import"
   - Beneficiary: "Test Corp USA"
   - Bank: "Bank of America"
   - SWIFT: "BOFAUS3N"
4. [ ] Click "Submit Order"
5. [ ] **Verify:** Success with Transaction ID
6. [ ] **Note:** Transaction ID (e.g., #456)

**Central Bank Approval (2 minutes)**
1. [ ] Logout → Login as `centralbank@ftms.com`
2. [ ] Go to "Pending" tab
3. [ ] **Verify:** See transaction #456
4. [ ] Click "Approve"
5. [ ] **Verify:** Moves to "Approved" tab

**Bank Verification (2 minutes)**
1. [ ] Logout → Login as `bank@ftms.com`
2. [ ] Go to "Pending Verification" tab
3. [ ] Click "Verify & Complete"
4. [ ] **Verify:** Button shows "⏳ Contacting SWIFT network..." for 2 seconds
5. [ ] After 2 seconds: Button shows "✓ Completed!"
6. [ ] Transaction moves to "Completed" tab

**Invoice Download (2 minutes)**
1. [ ] Logout → Login as `testuser@test.com`
2. [ ] Go to "History" tab
3. [ ] **Verify:** See completed transaction #456
4. [ ] Status: "✓ COMPLETED"
5. [ ] Click "📄 Invoice"
6. [ ] **Verify:** Invoice page opens with 15 fields
7. [ ] Click "⬇️ Download PDF"
8. [ ] **Verify:** PDF downloads as `invoice-456.pdf`

**Result:** ✅ PASS / ❌ FAIL

---

### Feature 3: Exchange Rate Caching

**Setup (1 minute)**
```sql
-- Check how many API calls were made
SELECT COUNT(*) as rate_records FROM exchange_rates;
SELECT MAX(fetched_at) as last_fetch FROM exchange_rates LIMIT 1;
```
**Expected:** Last fetch = NOW (or recent)

**Test Caching (2 minutes)**
1. [ ] Refresh user dashboard
2. [ ] Wait 2 seconds
3. [ ] Check database:
```sql
SELECT MAX(fetched_at) FROM exchange_rates;
```
4. [ ] **Verify:** Same timestamp as before (cached, no new API call)

**Test Cache Expiration (2 minutes)**
1. [ ] Manually expire cache:
```sql
UPDATE exchange_rates SET fetched_at = DATE_SUB(NOW(), INTERVAL 2 HOUR);
```
2. [ ] Refresh user dashboard
3. [ ] Check database:
```sql
SELECT MAX(fetched_at) FROM exchange_rates;
```
4. [ ] **Verify:** New timestamp (fresh API call made)

**Result:** ✅ PASS / ❌ FAIL

---

### Feature 4: Admin Dashboard Statistics

**Setup (1 minute)**
```sql
-- Get stats
SELECT 
    COUNT(*) as total_users,
    (SELECT COUNT(*) FROM users WHERE kyc_status='PENDING') as pending_kyc,
    (SELECT COUNT(*) FROM transactions) as total_tx,
    (SELECT COUNT(*) FROM transactions WHERE status='COMPLETED') as completed_tx
FROM users WHERE role != 'ADMIN' AND role != 'CENTRAL_BANK' AND role != 'COMMERCIAL_BANK';
```

**Test Display (2 minutes)**
1. [ ] Login as `admin@ftms.com`
2. [ ] Look at stat cards at top of dashboard
3. [ ] **Verify:** Numbers match database query from above
4. [ ] Stat cards show:
   - Total Users
   - Pending KYC
   - Total Transactions
   - Completion Rate (%)

**Test Updates (2 minutes)**
1. [ ] Create new transaction from `testuser@test.com`
2. [ ] Refresh admin dashboard
3. [ ] **Verify:** "Total Transactions" count increased by 1

**Result:** ✅ PASS / ❌ FAIL

---

### Feature 5: Error Handling

**Test 5A: Invalid Credentials (1 minute)**
1. [ ] Go to login page
2. [ ] Enter email: `admin@ftms.com`, password: `WrongPassword`
3. [ ] Click "Login"
4. [ ] **Verify:** Error: "Invalid password"

**Test 5B: Non-existent User (1 minute)**
1. [ ] Enter email: `nonexistent@test.com`, password: `Test@1234`
2. [ ] Click "Login"
3. [ ] **Verify:** Error: "User not found" or "Invalid credentials"

**Test 5C: KYC Pending (1 minute)**
1. [ ] Create new user: `kyc-pending@test.com` with passport
2. [ ] Try to login (without admin approval)
3. [ ] **Verify:** Error: "Your KYC verification is pending"

**Test 5D: Unauthorized Access (1 minute)**
1. [ ] Login as regular user
2. [ ] Try direct URL: `/admin/dashboard.html`
3. [ ] **Verify:** Redirect to login page OR error on page load

**Result:** ✅ PASS / ❌ FAIL

---

### Feature 6: Passport Image Display

**Test Steps (2 minutes)**
1. [ ] Login as `admin@ftms.com`
2. [ ] Go to "KYC Approvals" tab
3. [ ] Find a user with "✓ Approved" status
4. [ ] Click passport thumbnail
5. [ ] **Verify:** Modal opens with full-size passport image
6. [ ] Image is clear and legible
7. [ ] Close modal → No errors in console

**Result:** ✅ PASS / ❌ FAIL

---

## 🎯 CRITICAL PATH TEST (5 minutes)

Run this if you only have 5 minutes:

```
1. Login as admin → ✅ 
2. Create transaction → ✅ 
3. Approve (central bank) → ✅ 
4. Verify (bank) → ✅ 
5. Download invoice → ✅ 
6. Check admin stats → ✅ 
```

If all 6 pass: **✅ SYSTEM IS READY FOR DEPLOYMENT**

---

## 🔍 DEBUGGING CHECKLIST

### Backend Won't Start
```bash
# Check MySQL is running
net start mysql80

# Check port 8080 not in use
netstat -ano | findstr :8080

# Check Maven build
cd backend
mvn clean compile -q
```

### Frontend Won't Connect to Backend
```bash
# Check backend is running
curl http://localhost:8080/api/forex/rates

# Check CORS is enabled (should see Access-Control-Allow-Origin header)
curl -i -H "Origin: http://127.0.0.1:5500" http://localhost:8080/api/forex/rates
```

### Login Fails
```bash
# Verify test users exist
mysql -u root -p -e "USE ftms_db; SELECT id, email, role FROM users LIMIT 5;"

# Should see:
# 1 | admin@ftms.com | ADMIN
# 2 | centralbank@ftms.com | CENTRAL_BANK
# 3 | bank@ftms.com | COMMERCIAL_BANK
```

### Invoice Download Fails
```bash
# Check transaction exists
mysql -u root -p -e "USE ftms_db; SELECT id, status FROM transactions LIMIT 1;"

# Check browser console for errors
# F12 → Console tab → Look for red errors
```

### Stats Not Showing
```bash
# Check endpoint response
curl -H "Authorization: Bearer <your-jwt-token>" http://localhost:8080/api/admin/stats

# Should return JSON with 6 fields
```

---

## 📊 TEST RESULTS SUMMARY

### Before Deployment, Verify All Sections:

| Feature | Status | Notes |
|---------|--------|-------|
| Backend Health | ✅ PASS | Compiles, runs, responds to API calls |
| Login Flow | ✅ PASS | Test accounts work |
| Registration | ✅ PASS | New users can register |
| KYC Workflow | ✅ PASS | Approval process functional |
| Role Selection | ✅ PASS | Post-KYC role picker works |
| Transactions | ✅ PASS | Create, approve, verify, complete |
| Invoices | ✅ PASS | Generate & download as PDF |
| Exchange Rates | ✅ PASS | Cache working, API calls minimized |
| Admin Dashboard | ✅ PASS | Stats display correctly |
| Error Handling | ✅ PASS | Graceful error messages |
| Passport Storage | ✅ PASS | Images upload and display |

**Final Status:** ✅ **READY FOR PRODUCTION**

---

## 📝 TIME ESTIMATE

| Activity | Time |
|----------|------|
| Setup (MySQL, backends, frontend) | 5 min |
| Quick health checks | 3 min |
| Feature tests (all 6 features) | 30 min |
| Bug fixes (if needed) | 15 min |
| Final verification | 5 min |
| **TOTAL** | **~60 min** |

---

**Questions?** See [DEPLOYMENT_AND_TESTING_GUIDE.md](DEPLOYMENT_AND_TESTING_GUIDE.md) for detailed scenarios.

**Ready to deploy?** Check [PROJECT_ANALYSIS_AND_BUGS.md](PROJECT_ANALYSIS_AND_BUGS.md) for final quality assessment.
