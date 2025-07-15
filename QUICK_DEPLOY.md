# 🚀 Quick Deploy to Free Cloud

**Get your E-Commerce Microservices Platform online in 15 minutes!**

## 🎯 **Recommended Free Option: Railway**

Railway offers the easiest deployment for microservices with **$5/month free credits**.

### 1. **Prerequisites (2 minutes)**
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login
```

### 2. **One-Click Deployment (10 minutes)**
```bash
# Make deployment script executable
chmod +x deployment/scripts/deploy-railway.sh

# Run automated deployment
./deployment/scripts/deploy-railway.sh
```

### 3. **Set Up External Services (3 minutes)**
Add these free services to your Railway project:

**Database:** Railway PostgreSQL (included)
```bash
railway add postgresql
```

**Redis Cache:** Railway Redis (included)
```bash
railway add redis
```

**Your APIs will be available at:**
- API Gateway: `https://api-gateway.railway.app`
- User Service: `https://user-service.railway.app`
- Product Service: `https://product-service.railway.app`

---

## 🌩️ **Alternative: Google Cloud Platform**
**Best for production** - $300 free credits for 90 days

### Quick GCP Setup
```bash
# 1. Create GCP project
gcloud projects create ecommerce-platform-$(date +%s)

# 2. Enable services
gcloud services enable run.googleapis.com sql-component.googleapis.com

# 3. Deploy to Cloud Run
cd discovery-service
gcloud run deploy --source . --platform managed --allow-unauthenticated

# Repeat for each service...
```

---

## 🔧 **Manual Free Setup (Advanced)**

If you prefer setting up each service individually:

### 1. **Free Database Options**
Choose one:
- **PlanetScale** (MySQL) - 1 database, 1GB storage
- **Supabase** (PostgreSQL) - 500MB, 2 projects  
- **Railway** (PostgreSQL) - Included in free tier

### 2. **Free Cache & Search**
- **Upstash Redis** - 10,000 requests/day
- **Bonsai Elasticsearch** - 125MB storage

### 3. **Free Message Broker**
- **Upstash Kafka** - 10,000 messages/day

### 4. **Manual Service Deployment**
Deploy each service to:
- **Railway** (easiest)
- **Render** (750 hours/month)
- **Railway** (best for microservices)

---

## 📋 **Essential Environment Variables**

After deployment, set these in your Railway dashboard:

### Required for All Services
```bash
SPRING_PROFILES_ACTIVE=cloud
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://discovery-service.railway.app/eureka/
```

### Payment Service
```bash
RAZORPAY_KEY_ID=your_test_key_id
RAZORPAY_KEY_SECRET=your_test_secret
```
*Get free test keys from [Razorpay Dashboard](https://dashboard.razorpay.com/)*

### Email Service
```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=your_app_password
```
*Create App Password in [Google Account Settings](https://myaccount.google.com/apppasswords)*

---

## 🎯 **Testing Your Deployment**

### 1. **Health Check**
```bash
curl https://api-gateway.railway.app/actuator/health
```

### 2. **Create a User**
```bash
curl -X POST https://api-gateway.railway.app/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### 3. **Add a Product**
```bash
curl -X POST https://api-gateway.railway.app/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "A test product",
    "price": 99.99,
    "stockQuantity": 100,
    "category": "Electronics",
    "brand": "TestBrand"
  }'
```

---

## 🆓 **100% Free Deployment Stack**

**Total Cost: $0/month** (within free tiers)

| Service | Provider | Free Limit |
|---------|----------|------------|
| **Hosting** | Railway | $5/month credits |
| **Database** | Railway PostgreSQL | Included |
| **Cache** | Railway Redis | Included |
| **Search** | Bonsai Elasticsearch | 125MB |
| **Message Queue** | Upstash Kafka | 10K msgs/day |
| **Email** | Gmail SMTP | Free |
| **Payments** | Razorpay | Test mode |
| **Monitoring** | Railway Analytics | Included |

---

## 🔧 **Troubleshooting**

### Service Won't Start?
```bash
# Check logs
railway logs

# Check environment variables
railway variables
```

### Database Connection Issues?
```bash
# Verify database URL is set
railway variables | grep DATABASE_URL

# Test connection
railway run bash
mysql -h $DB_HOST -u $DB_USER -p$DB_PASSWORD
```

### Service Discovery Problems?
1. Ensure Discovery Service is deployed first
2. Check all services point to correct Eureka URL
3. Wait 2-3 minutes for service registration

---

## 🎉 **Next Steps**

Once deployed:

1. **📱 Add a Frontend**
   - Deploy React/Angular app to Vercel/Netlify
   - Point to your API Gateway URL

2. **🔒 Enable HTTPS**
   - Railway provides automatic HTTPS
   - Configure CORS for your frontend domain

3. **📊 Set Up Monitoring**
   - Enable Railway analytics
   - Add New Relic (free tier)

4. **🚀 Scale Up**
   - Upgrade Railway plan for more resources
   - Move to GCP/AWS for production

---

## 💡 **Pro Tips**

### Minimize Costs
- Use Railway's sleep feature for dev environments
- Implement service-level caching to reduce database calls
- Use connection pooling to optimize database connections

### Performance
- Enable Redis caching for frequently accessed data
- Use Elasticsearch for all product searches
- Implement API rate limiting to prevent abuse

### Security
- Use environment variables for all secrets
- Enable CORS only for your frontend domains
- Implement JWT token expiration

---

## 📞 **Need Help?**

- **Railway Issues**: Check [Railway Docs](https://docs.railway.app/)
- **GCP Issues**: See [Cloud Run Docs](https://cloud.google.com/run/docs)
- **Application Issues**: Check service logs and health endpoints

**Start deploying now!** Choose Railway for the quickest setup or GCP for production-ready deployment.

🚀 **Happy Deploying!** 