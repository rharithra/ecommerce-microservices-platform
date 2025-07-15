# 🚀 Cloud Deployment Guide

This guide will help you deploy the E-Commerce Microservices Platform to various free cloud providers.

## 🌐 **Free Cloud Options**

### **Option 1: Google Cloud Platform (GCP) - RECOMMENDED** 
- **Free Credits**: $300 for 90 days
- **Best for**: Complete production deployment
- **Services Used**: Cloud Run, Cloud SQL, Redis, Kafka

### **Option 2: Railway** 
- **Free Tier**: $5/month usage included
- **Best for**: Individual microservices
- **Pros**: Simple deployment, automatic HTTPS

### **Option 3: Render** 
- **Free Tier**: 750 hours/month
- **Best for**: Basic deployment
- **Limitation**: Services sleep after 15 minutes of inactivity

### **Option 4: AWS Free Tier** 
- **Free Period**: 12 months
- **Services**: EC2, RDS, ElastiCache
- **Best for**: Learning AWS ecosystem

---

## 🎯 **Quick Start - Railway (Easiest)**

### 1. Prerequisites
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login
```

### 2. Create Railway Project
```bash
# Create new project
railway new ecommerce-platform

# Link to existing project
railway link
```

### 3. Deploy Core Services

**Deploy Discovery Service:**
```bash
cd discovery-service
railway up
```

**Deploy API Gateway:**
```bash
cd api-gateway
railway up
```

**Deploy User Service:**
```bash
cd user-service
railway up
```

### 4. Add Database (PostgreSQL)
```bash
# Add PostgreSQL addon
railway add postgresql

# Get database URL
railway variables
```

### 5. Set Environment Variables
```bash
# Set for each service
railway variables set SPRING_PROFILES_ACTIVE=cloud
railway variables set SPRING_DATASOURCE_URL=$DATABASE_URL
```

---

## 🌩️ **Google Cloud Platform Deployment**

### 1. Setup GCP Project
```bash
# Install Google Cloud CLI
# Create new project
gcloud projects create ecommerce-platform-[RANDOM]

# Set project
gcloud config set project ecommerce-platform-[RANDOM]

# Enable APIs
gcloud services enable run.googleapis.com
gcloud services enable sql-component.googleapis.com
gcloud services enable redis.googleapis.com
```

### 2. Create Cloud SQL Instance
```bash
# Create MySQL instance
gcloud sql instances create ecommerce-db \
    --database-version=MYSQL_8_0 \
    --tier=db-f1-micro \
    --region=us-central1

# Create databases
gcloud sql databases create ecommerce_users --instance=ecommerce-db
gcloud sql databases create ecommerce_products --instance=ecommerce-db
gcloud sql databases create ecommerce_orders --instance=ecommerce-db
gcloud sql databases create ecommerce_payments --instance=ecommerce-db
```

### 3. Create Redis Instance
```bash
gcloud redis instances create ecommerce-cache \
    --size=1 \
    --region=us-central1 \
    --redis-version=redis_6_x
```

### 4. Deploy to Cloud Run
```bash
# Build and deploy each service
cd discovery-service
gcloud run deploy discovery-service \
    --source . \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated

cd ../user-service
gcloud run deploy user-service \
    --source . \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated

# Repeat for other services...
```

---

## 🐳 **Docker Deployment on Any Cloud**

### 1. Build All Docker Images
```bash
# Create Dockerfiles for each service (copy template)
./scripts/create-dockerfiles.sh

# Build all images
docker-compose -f deployment/docker-compose.build.yml build
```

### 2. Push to Container Registry
```bash
# Tag and push to Docker Hub
docker tag user-service yourusername/ecommerce-user-service
docker push yourusername/ecommerce-user-service

# Or use GitHub Container Registry
docker tag user-service ghcr.io/yourusername/ecommerce-user-service
docker push ghcr.io/yourusername/ecommerce-user-service
```

### 3. Deploy with Docker Compose
```bash
# Deploy to any Docker-compatible cloud
docker-compose -f deployment/gcp/docker-compose.cloud.yml up -d
```

---

## 🔧 **Environment Configuration**

### Required Environment Variables
Create `.env` file for each deployment:

```bash
# Database Configuration
DB_USERNAME=ecommerce_user
DB_PASSWORD=your_secure_password

# Individual database URLs
USER_DB_URL=jdbc:mysql://host:3306/ecommerce_users
PRODUCT_DB_URL=jdbc:mysql://host:3306/ecommerce_products
ORDER_DB_URL=jdbc:mysql://host:3306/ecommerce_orders
PAYMENT_DB_URL=jdbc:mysql://host:3306/ecommerce_payments
EMAIL_DB_URL=jdbc:mysql://host:3306/ecommerce_emails

# Redis Configuration
REDIS_HOST=your-redis-host
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password

# Elasticsearch Configuration
ELASTICSEARCH_URL=https://your-elasticsearch-url:9200

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=your-kafka-brokers:9092

# Razorpay Configuration
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

---

## 📱 **Free External Services Setup**

### 1. Database (Free Options)
- **PlanetScale** (MySQL) - Free tier: 1 database, 1GB storage
- **Supabase** (PostgreSQL) - Free tier: 500MB, 2 projects
- **Railway** (PostgreSQL) - Included in free tier

### 2. Redis Cache (Free Options)
- **Upstash** - Free tier: 10,000 requests/day
- **Redis Cloud** - Free tier: 30MB
- **Railway** - Redis addon

### 3. Elasticsearch (Free Options)
- **Elastic Cloud** - Free tier: 14-day trial
- **Bonsai** - Free tier: 125MB

### 4. Kafka (Free Options)
- **Upstash Kafka** - Free tier: 10,000 messages/day
- **Confluent Cloud** - Free tier: $400 credits

---

## 🚀 **One-Click Deployment Scripts**

### Railway Deployment
```bash
# Make script executable
chmod +x deployment/scripts/deploy-railway.sh

# Run deployment
./deployment/scripts/deploy-railway.sh
```

### GCP Deployment
```bash
# Make script executable
chmod +x deployment/scripts/deploy-gcp.sh

# Run deployment
./deployment/scripts/deploy-gcp.sh
```

---

## 🔍 **Service URLs After Deployment**

### Railway URLs
- **API Gateway**: `https://ecommerce-api.railway.app`
- **User Service**: `https://user-service.railway.app`
- **Product Service**: `https://product-service.railway.app`

### GCP URLs
- **API Gateway**: `https://api-gateway-[hash]-uc.a.run.app`
- **User Service**: `https://user-service-[hash]-uc.a.run.app`

---

## 📊 **Monitoring & Health Checks**

### Health Check Endpoints
```bash
# Check service health
curl https://your-api-gateway-url/actuator/health

# Check individual services
curl https://user-service-url/actuator/health
curl https://product-service-url/actuator/health
```

### Monitoring Setup
- **Google Cloud Monitoring** (for GCP)
- **Railway Analytics** (for Railway)
- **New Relic** (free tier: 100GB/month)

---

## 🔐 **Security Considerations**

### 1. Environment Variables
- Never commit sensitive data to git
- Use cloud provider's secret management
- Rotate API keys regularly

### 2. Database Security
- Use strong passwords
- Enable SSL connections
- Restrict IP access

### 3. API Security
- Enable HTTPS only
- Implement rate limiting
- Use JWT tokens

---

## 💡 **Cost Optimization Tips**

### 1. Service Optimization
- Use minimum required resources
- Implement service sleep for inactive services
- Use connection pooling

### 2. Database Optimization
- Use read replicas sparingly
- Implement proper indexing
- Archive old data

### 3. Caching Strategy
- Cache frequently accessed data
- Use CDN for static assets
- Implement application-level caching

---

## 🔧 **Troubleshooting**

### Common Issues

**Service Discovery Issues:**
```bash
# Check Eureka dashboard
curl http://discovery-service-url:8761

# Verify service registration
curl http://discovery-service-url:8761/eureka/apps
```

**Database Connection Issues:**
```bash
# Test database connectivity
mysql -h host -u username -p database_name

# Check connection pool
curl http://service-url/actuator/metrics/hikaricp.connections
```

**Memory Issues:**
```bash
# Check memory usage
curl http://service-url/actuator/metrics/jvm.memory.used

# Adjust JVM settings in Dockerfile
ENV JAVA_OPTS="-Xms128m -Xmx256m"
```

---

## 🎯 **Production Checklist**

### Before Going Live
- [ ] All services deployed and healthy
- [ ] Database connections working
- [ ] External service integrations tested
- [ ] Environment variables configured
- [ ] SSL certificates installed
- [ ] Monitoring configured
- [ ] Backup strategy implemented
- [ ] Error handling tested
- [ ] Load testing completed
- [ ] Security scan performed

### Post-Deployment
- [ ] Monitor application metrics
- [ ] Set up alerts
- [ ] Document API endpoints
- [ ] Create user documentation
- [ ] Plan scaling strategy

---

## 📞 **Support**

If you encounter issues:
1. Check the [troubleshooting section](#troubleshooting)
2. Review cloud provider documentation
3. Check service logs
4. Create an issue on GitHub

**Happy Deploying! 🚀** 