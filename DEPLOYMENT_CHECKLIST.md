# Deployment Checklist ✅

## Pre-Deployment Verification

### Backend (Render)
- ✅ `Procfile` created with Java build command
- ✅ `render.yaml` configured for auto-deployment
- ✅ Environment variables template: `.env.example`
- ✅ `application.properties` uses env variables: `${DB_PASSWORD}` and `${JWT_SECRET}`
- ✅ CORS configured for production domains
- ✅ Maven `pom.xml` configured for production builds
- ✅ Git repository clean of secrets

### Frontend (Netlify)
- ✅ `netlify.toml` configured with SPA routing
- ✅ `config.js` dynamically sets API_BASE_URL based on environment
- ✅ Static site ready for deployment (no build step needed)
- ✅ All HTML files in `/frontend` directory

### Security
- ✅ No hardcoded credentials in source code
- ✅ No secrets in `.env.example` (only templates)
- ✅ `target/` folder in `.gitignore`
- ✅ Git history cleaned of exposed passwords
- ✅ Environment variables protected in deployment platforms

---

## Deployment Steps

### 1️⃣ Deploy Backend to Render

#### Create Service on Render
1. Go to https://dashboard.render.com
2. Click "**New +**" → "**Web Service**"
3. Connect GitHub repo `shane2512/Mini-project`
4. Fill in:
   - **Name**: `ftms-backend`
   - **Environment**: `Java`
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `java -jar backend/target/ftms-backend-1.0.0.jar`
   - **Instance Type**: `Free` (or Starter)

#### Set Environment Variables
In Render Dashboard → `ftms-backend` → **Environment**:
```
DB_PASSWORD = [Your Aiven password]
JWT_SECRET = [Generate secure random string]
```

#### Deploy
- Push to `main` branch or click **Deploy** in Render
- Wait for build (5-10 minutes on first deploy)
- Get your backend URL: `https://ftms-backend.onrender.com`

---

### 2️⃣ Deploy Frontend to Netlify

#### Connect GitHub Repository
1. Go to https://app.netlify.com
2. Click "**Add new site**" → "**Import an existing project**"
3. Choose **GitHub** and select `shane2512/Mini-project`
4. Configure:
   - **Base directory**: Leave empty (or set to `frontend`)
   - **Build command**: Leave empty (static files)
   - **Publish directory**: `frontend`

#### Deploy
- Netlify auto-deploys on every push to `main`
- Get your frontend URL: `https://[site-name].netlify.app`

#### Update API Configuration (if needed)
If your Render backend URL is different from the default in `config.js`:
```javascript
// In frontend/js/config.js
API_BASE_URL: 'https://your-render-backend-url'
```

---

### 3️⃣ Verify Deployment

#### Test Backend
```bash
curl https://ftms-backend.onrender.com/api/forex/rates
```
Should return: `{ "rates": { "EUR": 0.xx, ... } }`

#### Test Frontend
1. Visit `https://[site-name].netlify.app`
2. Test login/registration
3. Test role switching
4. Test transactions (verify API calls work)

---

## Common Issues & Solutions

### ❌ Backend won't start
**Check:**
- Environment variables are set in Render
- Database connectivity (Aiven is reachable)
- View logs: Dashboard → Logs

```bash
# To debug locally:
$env:DB_PASSWORD = "your-password"
$env:JWT_SECRET = "dev-secret"
mvn spring-boot:run
```

### ❌ Frontend can't reach backend
**Check:**
- Backend URL in `config.js` is correct
- CORS is enabled in `application.properties`
- Check browser console for errors

```javascript
// Test from browser console:
fetch('https://ftms-backend.onrender.com/api/forex/rates')
  .then(r => r.json())
  .then(d => console.log(d))
```

### ❌ Login fails with 401
**Check:**
- JWT_SECRET environment variable is set
- JWT token is being sent correctly
- Check Render logs for authentication errors

### ❌ Database connection timeout
**Check:**
- Aiven MySQL is running
- DB_PASSWORD is correct
- Firewall allows Render IP to access Aiven

---

## Monitoring & Maintenance

### Render Logs
Dashboard → `ftms-backend` → **Logs**
- Real-time application logs
- Error messages and stack traces

### Netlify Logs
Dashboard → **Deploys** → Click deploy → **View deploy logs**
- Build logs
- Deployment status

### Database Backups
- Aiven provides automated backups
- Access via Aiven console: https://console.aiven.io

---

## Production Configuration Checklist

Before going live:
- [ ] Change `JWT_SECRET` to a strong, random value
- [ ] Add custom domain to Netlify (optional)
- [ ] Set up monitoring alerts in Render
- [ ] Review CORS settings for production domains
- [ ] Test all user flows (register, login, transactions)
- [ ] Check error handling on both frontend and backend
- [ ] Review security settings (HTTPS, headers, etc.)

---

## Rollback & Redeploy

### Render
If deployment fails:
1. Check logs for error
2. Fix code locally
3. Commit and push to `main`
4. Render auto-redeploys or click "Deploy" manually

### Netlify
If frontend has issues:
1. Revert code locally
2. Commit and push
3. Netlify auto-deploys new version

---

## Environment Variables Reference

| Variable | Purpose | Render | Netlify |
|----------|---------|--------|---------|
| `DB_PASSWORD` | Aiven MySQL password | ✅ Required | ❌ Not needed |
| `JWT_SECRET` | JWT signing key | ✅ Required | ❌ Not needed |
| `PORT` | Server port | ✅ Auto (8080) | ❌ N/A |
| `API_BASE_URL` | Backend URL | ❌ N/A | ✅ In config.js |

---

## File Structure for Deployment

```
├── Procfile                 ← Render build config
├── render.yaml             ← Render service definition
├── netlify.toml            ← Netlify config
├── .env.example            ← Environment variables template
├── DEPLOYMENT_GUIDE.md     ← Full deployment instructions
├── backend/
│   ├── pom.xml             ← Maven config (build for Render)
│   └── src/main/resources/
│       └── application.properties  ← Uses env variables
└── frontend/
    ├── **/*.html           ← All static files
    ├── css/styles.css      ← Styling
    └── js/config.js        ← Dynamic API configuration
```

---

## Support & Troubleshooting

### Documentation
- [Render Java Deployment](https://render.com/docs/deploy-java)
- [Netlify Static Site Hosting](https://docs.netlify.com/)
- [Spring Boot Configuration](https://docs.spring.io/spring-boot/docs/3.2.0/reference/html/)

### Quick Commands

**Local testing:**
```bash
# Backend
cd backend
$env:DB_PASSWORD = "password"
$env:JWT_SECRET = "secret"
mvn spring-boot:run

# Frontend (requires Live Server extension or):
python -m http.server 5500 --directory frontend
```

**Check git status:**
```bash
git status
git log --oneline -5
```

**Push changes:**
```bash
git add .
git commit -m "your message"
git push origin main
```

---

## Success Indicators ✅

Your deployment is successful when:
1. ✅ Backend responds on `https://ftms-backend.onrender.com/api/forex/rates`
2. ✅ Frontend loads on Netlify without errors
3. ✅ Login/registration works
4. ✅ Role switching works
5. ✅ Transactions can be initiated
6. ✅ No console errors in browser DevTools
7. ✅ Render and Netlify logs are clean

---

**Last Updated**: April 5, 2026
**Status**: Ready for production deployment 🚀
