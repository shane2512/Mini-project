# FTMS Selenium Test Suite - Complete Test Case Documentation

## Overview
This document provides comprehensive documentation of all 30 Selenium test cases for the FTMS (Forex Trading Management System). Each test case includes inputs, expected outcomes, and test data.

---

## Test Cases Directory

### **Authentication & Registration Tests (TC-BB-01 to TC-BB-04)**

#### TC-BB-01: User Registration & Login with Valid Credentials
- **Category:** Authentication
- **Description:** Test user login functionality with valid email and password
- **Test Method:** `testValidUserLoginAndDashboard()`
- **Inputs:**
  - Email: `importer@ftms.com`
  - Password: `Admin@123`
- **Expected Output:** User successfully logs in and dashboard is displayed
- **Assertions:** User dashboard loads and user-name element is visible
- **Preconditions:** None (assumes user account exists)

#### TC-BB-02: Login with Invalid Password
- **Category:** Authentication
- **Description:** Test login validation with incorrect password
- **Test Method:** `testInvalidPasswordLogin()`
- **Inputs:**
  - Email: `importer@ftms.com`
  - Password: `WrongPassword@123`
- **Expected Output:** Login fails with error message
- **Assertions:** Error message displayed indicating authentication failure
- **Preconditions:** User account must exist

#### TC-BB-03: Login with Empty Fields
- **Category:** Authentication
- **Description:** Test login form validation with empty credentials
- **Test Method:** `testEmptyFieldsValidation()`
- **Inputs:**
  - Email: (empty)
  - Password: (empty)
- **Expected Output:** Validation error messages for required fields
- **Assertions:** Error messages on email and/or password fields
- **Preconditions:** User is on login page

#### TC-BB-04: User Registration with Valid Data
- **Category:** Registration
- **Description:** Test new user registration with complete and valid information
- **Test Method:** `testUserRegistration()`
- **Inputs:**
  - First Name: `John`
  - Last Name: `Doe`
  - Email: `johndoe[timestamp]@ftms.com` (dynamic to avoid duplicates)
  - Password: `SecurePass@123`
  - Confirm Password: `SecurePass@123`
- **Expected Output:** New user account created and redirected to login page
- **Assertions:** URL contains "login" or "dashboard" after registration
- **Preconditions:** Registration page is accessible

---

### **Order Management Tests (TC-BB-05 to TC-BB-09)**

#### TC-BB-05: Place Valid Import Order
- **Category:** Order Management
- **Description:** Test placing a valid import order by importer user
- **Test Method:** `testPlaceValidImportOrder()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Navigate to: `/user/dashboard`
  - Order Details:
    - Product: Import order form filled
    - Quantity: Based on form fields
    - Price: Based on market data
- **Expected Output:** Import order successfully placed
- **Assertions:** Success message displayed confirming order placement
- **Preconditions:** User must be logged in as importer

#### TC-BB-06: Track Order Status
- **Category:** Order Management
- **Description:** Test tracking an existing order's status
- **Test Method:** `testTrackOrderStatus()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Order ID: First available order in system
- **Expected Output:** Order details and status displayed
- **Assertions:** Order status element visible with valid status value
- **Preconditions:** At least one order must exist in user's account

#### TC-BB-07: Export Order Placement
- **Category:** Order Management
- **Description:** Test placing a valid export order by exporter user
- **Test Method:** `testPlaceValidExportOrder()`
- **Inputs:**
  - Login: `exporter@ftms.com` / `Admin@123`
  - Navigate to: `/user/dashboard`
  - Order Details:
    - Product Name: `Textiles`
    - Quantity: `500`
    - Price: `10.50`
- **Expected Output:** Export order successfully placed
- **Assertions:** Success message displayed confirming order placement
- **Preconditions:** User must be logged in as exporter

#### TC-BB-08: Cancel Placed Order
- **Category:** Order Management
- **Description:** Test cancellation of an existing order
- **Test Method:** `testCancelOrder()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Order to Cancel: First available order
  - Confirmation: Yes (click confirm-cancel-btn)
- **Expected Output:** Order status changed to cancelled
- **Assertions:** Order shows "Cancelled" status
- **Preconditions:** User must have at least one order available to cancel

#### TC-BB-09: Payment Processing & Transaction Status
- **Category:** Payment Processing
- **Description:** Test payment initiation and transaction status update
- **Test Method:** `testPaymentProcessing()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Payment Transaction: First pending transaction
- **Expected Output:** Payment modal appears and transaction status updates to processing
- **Assertions:** Payment modal visible and transaction status shows "processing"
- **Preconditions:** User must have at least one pending transaction

---

### **Exchange Rate Tests (TC-BB-10, TC-BB-11, TC-BB-28, TC-BB-29)**

#### TC-BB-10: View Exchange Rates (USD/INR)
- **Category:** Exchange Rates
- **Description:** Test viewing USD to INR exchange rate
- **Test Method:** `testViewExchangeRates()`
- **Inputs:**
  - Navigate to: Exchange rates page
  - Currency Pair: USD/INR
- **Expected Output:** Current USD/INR exchange rate displayed
- **Assertions:** Exchange rate element found with numeric value
- **Preconditions:** User logged in; exchange rates API is available

#### TC-BB-11: View Live Rates Display
- **Category:** Exchange Rates
- **Description:** Test live exchange rates update functionality
- **Test Method:** `testLiveRatesDisplay()`
- **Inputs:**
  - Navigate to: Exchange rates dashboard
  - Focus on: Live rates widget
- **Expected Output:** Live exchange rates displayed with real-time values
- **Assertions:** Rate element present and non-null
- **Preconditions:** Live rates service must be operational

#### TC-BB-28: GBP/INR Exchange Rate Display
- **Category:** Exchange Rates
- **Description:** Test viewing GBP to INR exchange rate
- **Test Method:** `testGbpInrExchangeRate()`
- **Inputs:**
  - Navigate to: Exchange rates page
  - Currency Pair: GBP/INR
- **Expected Output:** Current GBP/INR exchange rate displayed with numeric value
- **Assertions:** Rate element found and matches numeric pattern
- **Preconditions:** User logged in; exchange rates API is available

#### TC-BB-29: EUR/INR Exchange Rate Display
- **Category:** Exchange Rates
- **Description:** Test viewing EUR to INR exchange rate
- **Test Method:** `testEurInrExchangeRate()`
- **Inputs:**
  - Navigate to: Exchange rates page
  - Currency Pair: EUR/INR
- **Expected Output:** Current EUR/INR exchange rate displayed with numeric value
- **Assertions:** Rate element found and matches numeric pattern
- **Preconditions:** User logged in; exchange rates API is available

---

### **Dashboard & Navigation Tests (TC-BB-12, TC-BB-13)**

#### TC-BB-12: View Transaction Dashboard
- **Category:** Dashboard
- **Description:** Test transaction dashboard displays user's transactions
- **Test Method:** `testViewTransactionDashboard()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Navigate to: `/user/dashboard`
- **Expected Output:** Dashboard loads with transaction list visible
- **Assertions:** Transaction section displays transactions
- **Preconditions:** User must be logged in

#### TC-BB-13: User Can Navigate Dashboard Sections
- **Category:** Dashboard Navigation
- **Description:** Test user navigation between different dashboard sections
- **Test Method:** `testDashboardNavigation()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Navigate through: Orders, Transactions, History sections
- **Expected Output:** All sections load without errors
- **Assertions:** All dashboard sections are accessible and display content
- **Preconditions:** User must be logged in

---

### **Invoice & History Tests (TC-BB-14, TC-BB-15)**

#### TC-BB-14: Invoice Page Access for Authenticated Users
- **Category:** Invoice Management
- **Description:** Test accessing invoice page with valid transaction
- **Test Method:** `testInvoicePageAccess()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Navigate to: `/invoice.html?id=test-transaction`
- **Expected Output:** Invoice displayed for the transaction
- **Assertions:** Invoice page loads and displays transaction details
- **Preconditions:** User must be authenticated; transaction ID must exist

#### TC-BB-15: View Transaction History Section
- **Category:** Transaction History
- **Description:** Test accessing and viewing transaction history
- **Test Method:** `testViewTransactionHistory()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Navigate to: `/user/dashboard`
  - Action: Click history tab
- **Expected Output:** Transaction history section displayed with transaction records
- **Assertions:** History tab content visible and contains transaction data
- **Preconditions:** User must be logged in; must have transaction history

---

### **Admin & Role-Based Access Tests (TC-BB-16, TC-BB-17, TC-BB-18, TC-BB-19, TC-BB-20)**

#### TC-BB-16: Admin Page Navigation
- **Category:** Admin Access
- **Description:** Test admin dashboard accessibility with redirect handling
- **Test Method:** `testAdminDashboardAccess()`
- **Inputs:**
  - Navigate to: `/admin/dashboard`
  - Unauthenticated or non-admin user
- **Expected Output:** Page loads (may redirect to login) or shows admin dashboard
- **Assertions:** URL contains "login" or "admin"
- **Preconditions:** None

#### TC-BB-17: Bank Page Navigation
- **Category:** Bank Access
- **Description:** Test bank dashboard accessibility with redirect handling
- **Test Method:** `testBankDashboardAccess()`
- **Inputs:**
  - Navigate to: `/bank/dashboard`
  - Unauthenticated or non-bank user
- **Expected Output:** Page loads (may redirect to login) or shows bank dashboard
- **Assertions:** URL contains "login" or "bank"
- **Preconditions:** None

#### TC-BB-18: Central Bank Page Navigation
- **Category:** Central Bank Access
- **Description:** Test central bank dashboard accessibility with redirect handling
- **Test Method:** `testCentralBankDashboardAccess()`
- **Inputs:**
  - Navigate to: `/central-bank/dashboard`
  - Unauthenticated or non-central-bank user
- **Expected Output:** Page loads (may redirect to login) or shows central bank dashboard
- **Assertions:** URL contains "login" or "central-bank"
- **Preconditions:** None

#### TC-BB-19: User Role Selection Menu
- **Category:** Role Management
- **Description:** Test role switching functionality in user dashboard
- **Test Method:** `testUserRoleSelectionMenu()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Navigate to: `/user/dashboard`
- **Expected Output:** Role dropdown menu is available for selection
- **Assertions:** Role dropdown element is present and accessible
- **Preconditions:** User must be logged in with multiple roles

#### TC-BB-20: User Authentication Required Check
- **Category:** Access Control
- **Description:** Test that protected pages redirect unauthenticated users
- **Test Method:** `testAuthenticationRequired()`
- **Inputs:**
  - Navigate to: `/user/dashboard` without prior login
- **Expected Output:** Redirected to login page
- **Assertions:** Current URL contains "login"
- **Preconditions:** User is not logged in

---

### **User Profile & Settings Tests (TC-BB-21, TC-BB-22, TC-BB-23, TC-BB-24)**

#### TC-BB-21: KYC Verification Form Submission
- **Category:** KYC Compliance
- **Description:** Test KYC (Know Your Customer) form submission
- **Test Method:** `testKycVerificationSubmission()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - KYC Details:
    - PAN Number: `AAAPA5055K`
    - Aadhar Number: `123456789012`
    - Address: `123 Main Street, City, State 12345`
- **Expected Output:** KYC form submitted successfully
- **Assertions:** Success message indicating KYC submission
- **Preconditions:** User must be logged in; KYC section must be accessible

#### TC-BB-22: User Profile Update
- **Category:** Profile Management
- **Description:** Test updating user profile information
- **Test Method:** `testProfileUpdate()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Profile Updates:
    - Phone Number: `+919876543210`
    - Company Name: `ABC Trading Company`
- **Expected Output:** Profile information updated successfully
- **Assertions:** Success message displayed confirming profile update
- **Preconditions:** User must be logged in; profile page must be accessible

#### TC-BB-23: Change Password
- **Category:** Account Security
- **Description:** Test password change functionality
- **Test Method:** `testChangePassword()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Password Change:
    - Old Password: `Admin@123`
    - New Password: `NewPassword@456`
    - Confirm Password: `NewPassword@456`
- **Expected Output:** Password changed successfully
- **Assertions:** Success message displayed confirming password change
- **Preconditions:** User must be logged in; settings page must be accessible
- **Note:** After this test, user will need to log in with new password

#### TC-BB-24: Logout Functionality
- **Category:** Session Management
- **Description:** Test user logout and session termination
- **Test Method:** `testLogout()`
- **Inputs:**
  - Logged-in user session
  - Action: Click logout button
- **Expected Output:** User redirected to login page; session terminated
- **Assertions:** Current URL contains "login"
- **Preconditions:** User must be logged in

---

### **Search & Filter Tests (TC-BB-25, TC-BB-26)**

#### TC-BB-25: Search Transactions by Date Range
- **Category:** Transaction Search
- **Description:** Test filtering transactions within a date range
- **Test Method:** `testSearchTransactionsByDateRange()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Filter Criteria:
    - From Date: `01/01/2026`
    - To Date: `31/12/2026`
- **Expected Output:** Transactions within specified date range displayed
- **Assertions:** Transaction results section displays filtered transactions
- **Preconditions:** User must be logged in; must have transactions in the date range

#### TC-BB-26: Filter Orders by Status
- **Category:** Order Filtering
- **Description:** Test filtering orders by completion status
- **Test Method:** `testFilterOrdersByStatus()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Filter Criteria:
    - Status: `completed`
- **Expected Output:** Only completed orders displayed in list
- **Assertions:** Filtered orders list is displayed
- **Preconditions:** User must be logged in; must have orders with various statuses

---

### **Reporting & Export Tests (TC-BB-27)**

#### TC-BB-27: Generate Transaction Report
- **Category:** Reporting
- **Description:** Test generating transaction report for export/viewing
- **Test Method:** `testGenerateTransactionReport()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Report Type: Transaction Report
- **Expected Output:** Report generated and displayed
- **Assertions:** Report section visible with transaction data
- **Preconditions:** User must be logged in; reports feature must be accessible

---

### **Error Handling Tests (TC-BB-30)**

#### TC-BB-30: Error Handling - Invalid Order Amount
- **Category:** Input Validation
- **Description:** Test form validation for invalid order amount (negative value)
- **Test Method:** `testInvalidOrderAmountError()`
- **Inputs:**
  - Login: `importer@ftms.com` / `Admin@123`
  - Order Details:
    - Amount: `-1000` (invalid negative amount)
- **Expected Output:** Form validation error message displayed
- **Assertions:** Error message contains "Invalid" or "positive" text
- **Preconditions:** User must be logged in; order form must be accessible

---

## Test Data Summary

### Test Credentials
| User Type | Email | Password |
|-----------|-------|----------|
| Importer | `importer@ftms.com` | `Admin@123` |
| Exporter | `exporter@ftms.com` | `Admin@123` |
| Exchanger | `exchanger@ftms.com` | `Admin@123` |
| Admin | `admin@ftms.com` | `Admin@123` |
| Bank | `bank@ftms.com` | `Admin@123` |
| Central Bank | `central@ftms.com` | `Admin@123` |

### Configuration
- **Base URL:** `https://ftms-forex-sys.netlify.app`
- **Timeout Duration:** 15 seconds
- **Browser:** Chrome (automated via WebDriverManager v5.6.3)
- **Test Framework:** JUnit 5 (Jupiter)
- **Selenium Version:** 4.21.0

---

## Test Execution Summary

- **Total Test Cases:** 30
- **Categories Covered:** 12
  1. Authentication & Registration
  2. Order Management
  3. Payment Processing
  4. Exchange Rates
  5. Dashboard Navigation
  6. Invoice Management
  7. Admin/Role-Based Access
  8. User Profile & Settings
  9. Transaction Search & Filtering
  10. Reporting
  11. Error Handling
  12. Session Management

---

## Notes
- All tests use Selenium `WebDriverWait` with 15-second timeout
- Tests are designed for black-box functional testing
- Each test is independent and can run in any order
- Tests use dynamic data (timestamps) to avoid duplicate entry conflicts
- Tests follow AAA pattern: Arrange (setup), Act (perform action), Assert (verify result)
