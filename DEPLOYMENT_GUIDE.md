# FTMS Deployment Guide

## Backend Deployment to Render

### Prerequisites
- Render account (https://render.com)
- GitHub repository (already done)

### Step 1: Connect GitHub Repository to Render
1. Go to https://dashboard.render.com
2. Click "New +" → "Web Service"
3. Connect your GitHub repository `shane2512/Mini-project`
4. Configure:
   - **Name**: `ftms-backend`
   - **Environment**: `Java`
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `java -jar backend/target/ftms-backend-1.0.0.jar`
   - **Instance Type**: Free (or Starter for production)

### Step 2: Set Environment Variables in Render
Go to Dashboard → ftms-backend → Environment:
```
DB_PASSWORD = <your-aiven-password>
JWT_SECRET = <change-to-secure-value>
```

### Step 3: Deploy
Push to main branch or click "Deploy" in Render dashboard.
Your backend will be live at: **https://ftms-backend.onrender.com**

---

## Frontend Deployment to Netlify

### Prerequisites
- Netlify account (https://netlify.com)
- GitHub repository connected

### Step 1: Connect GitHub to Netlify
1. Go to https://app.netlify.com
2. Click "Add new site" → "Import an existing project"
3. Choose GitHub and select `shane2512/Mini-project`
4. Configure:
   - **Base directory**: `frontend`
   - **Build command**: Leave empty (static site)
   - **Publish directory**: `frontend`

### Step 2: Deploy
Netlify will auto-deploy on every push to main.
Your frontend will be live at: **https://your-site-name.netlify.app**

### Step 3: Custom Domain (Optional)
In Netlify settings, add your custom domain.

---

## API Configuration for Production

The `config.js` automatically detects your environment:
- **Local**: Uses `http://localhost:8080`
- **Production**: Uses `https://ftms-backend.onrender.com`

### Update Netlify Site Name (if different)
If your Netlify site name differs from the default, update in config.js:
```javascript
API_BASE_URL: 'https://your-render-backend-url'
```

---

## Database Configuration

Database is hosted on Aiven MySQL:
- **Host**: mysql-1f074a17-ooseproject123.a.aivencloud.com
- **Port**: 17457
- **Database**: ftms_db
- **Username**: avnadmin
- **Password**: Set in Render Environment Variables (secure)

---

## Monitoring

### Render Logs
Dashboard → ftms-backend → Logs

### Netlify Logs
Dashboard → Deploys → View deploy logs

### Common Issues

**Backend won't start:**
- Check if DB_PASSWORD and JWT_SECRET are set
- View Render logs for error messages
- Verify Aiven database is accessible

**Frontend can't connect to backend:**
- Check API_BASE_URL in config.js
- Verify CORS is enabled in backend
- Check Netlify environment variables

**Initial deploy takes long:**
- First Maven build downloads all dependencies (~3-5 minutes)
- Subsequent builds are faster

---

## Local Development

1. Set environment variables:
```bash
$env:DB_PASSWORD = "<your-aiven-password>"
$env:JWT_SECRET = "dev-secret-key"
```

2. Run backend:
```bash
cd backend
mvn spring-boot:run
```

3. Run frontend:
```bash
cd frontend
# Use a local server or Live Server in VS Code
```

---

## Files for Deployment

✅ **Procfile** - Render build configuration
✅ **netlify.toml** - Netlify configuration
✅ **render.yaml** - Alternative Render config (optional)
✅ **.env.example** - Environment variables documentation
✅ **config.js** - Dynamic API URL configuration
✅ **application.properties** - Updated for environment variables

---

## Next Steps

1. **Backend**: Push code → Render auto-deploys → Get URL
2. **Frontend**: Get Render URL → Update config.js if needed → Netlify auto-deploys
3. **Test**: Visit Netlify URL and test login/role switching with Render backend
4. **Monitor**: Check logs if issues
5. **Secure**: Change JWT_SECRET to a strong random value for production

