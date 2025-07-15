# Environment Configuration Guide

This guide provides environment variable configurations for different cloud deployment scenarios.

## 🔧 **Base Environment Variables**

### Database Configuration
```bash
# Database Credentials
DB_USERNAME=ecommerce_user
DB_PASSWORD=your_secure_password

# Individual Service Database URLs
USER_DB_URL=jdbc:mysql://host:3306/ecommerce_users
PRODUCT_DB_URL=jdbc:mysql://host:3306/ecommerce_products
ORDER_DB_URL=jdbc:mysql://host:3306/ecommerce_orders
PAYMENT_DB_URL=jdbc:mysql://host:3306/ecommerce_payments
EMAIL_DB_URL=jdbc:mysql://host:3306/ecommerce_emails
```

### Cache & Search Configuration
```bash
# Redis Configuration
REDIS_HOST=your-redis-host
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password

# Elasticsearch Configuration
ELASTICSEARCH_URL=https://your-elasticsearch-url:9200
```

### Message Broker Configuration
```bash
# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=your-kafka-brokers:9092
```

### External Services
```bash
# Razorpay Payment Gateway
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 🌐 **Cloud-Specific Configurations**

### Railway Deployment
```bash
# Automatically provided by Railway
DATABASE_URL=postgresql://username:password@host:port/database
REDIS_URL=redis://default:password@host:port

# Service Discovery
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://discovery-service.railway.app/eureka/

# Spring Profile
SPRING_PROFILES_ACTIVE=cloud
```

### Google Cloud Platform
```bash
# Cloud SQL Connection
USER_DB_URL=jdbc:mysql://google/ecommerce_users?cloudSqlInstance=project:region:instance&socketFactory=com.google.cloud.sql.mysql.SocketFactory
PRODUCT_DB_URL=jdbc:mysql://google/ecommerce_products?cloudSqlInstance=project:region:instance&socketFactory=com.google.cloud.sql.mysql.SocketFactory

# Cloud Redis
REDIS_HOST=10.x.x.x
REDIS_PORT=6379

# Cloud Endpoints
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=https://discovery-service-hash-uc.a.run.app/eureka/
```

### AWS Deployment
```bash
# RDS Connection
USER_DB_URL=jdbc:mysql://your-rds-endpoint:3306/ecommerce_users
PRODUCT_DB_URL=jdbc:mysql://your-rds-endpoint:3306/ecommerce_products

# ElastiCache Redis
REDIS_HOST=your-elasticache-endpoint
REDIS_PORT=6379

# MSK Kafka
KAFKA_BOOTSTRAP_SERVERS=your-msk-bootstrap-servers:9092
```

### Azure Deployment
```bash
# Azure Database for MySQL
USER_DB_URL=jdbc:mysql://your-server.mysql.database.azure.com:3306/ecommerce_users?useSSL=true&requireSSL=false
PRODUCT_DB_URL=jdbc:mysql://your-server.mysql.database.azure.com:3306/ecommerce_products?useSSL=true&requireSSL=false

# Azure Cache for Redis
REDIS_HOST=your-cache.redis.cache.windows.net
REDIS_PORT=6380
REDIS_PASSWORD=your-access-key
```

## 🌱 **Free Service Alternatives**

### Free Database Options
```bash
# PlanetScale (MySQL)
DATABASE_URL=mysql://username:password@host/database?sslaccept=strict

# Supabase (PostgreSQL)
DATABASE_URL=postgresql://username:password@host:5432/database

# Railway PostgreSQL
DATABASE_URL=postgresql://username:password@host:port/database
```

### Free Redis Options
```bash
# Upstash Redis
REDIS_URL=redis://default:password@host:port

# Redis Cloud
REDIS_HOST=redis-host.redislabs.com
REDIS_PORT=port
REDIS_PASSWORD=password
```

### Free Elasticsearch Options
```bash
# Elastic Cloud (trial)
ELASTICSEARCH_URL=https://deployment-id.es.region.cloud.es.io:9243
ELASTICSEARCH_USERNAME=elastic
ELASTICSEARCH_PASSWORD=password

# Bonsai (free tier)
ELASTICSEARCH_URL=https://user:pass@host-url.bonsaisearch.net
```

### Free Kafka Options
```bash
# Upstash Kafka
KAFKA_BOOTSTRAP_SERVERS=server.upstash.io:9092
KAFKA_USERNAME=username
KAFKA_PASSWORD=password

# Confluent Cloud (free tier)
KAFKA_BOOTSTRAP_SERVERS=server.confluent.cloud:9092
KAFKA_USERNAME=api-key
KAFKA_PASSWORD=secret
```

## 🔒 **Security Configuration**

### JWT & Authentication
```bash
# JWT Secret (generate a strong key)
JWT_SECRET=your-256-bit-secret-key

# Token expiration
JWT_EXPIRATION=86400000

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://yourdomain.com
```

### API Keys & Secrets
```bash
# Never commit these to version control!
RAZORPAY_KEY_ID=rzp_test_your_key_id
RAZORPAY_KEY_SECRET=your_secret_key

# Email service credentials
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_specific_password
```

## 📊 **Monitoring Configuration**

### Application Monitoring
```bash
# New Relic (free tier)
NEW_RELIC_LICENSE_KEY=your_license_key
NEW_RELIC_APP_NAME=ecommerce-platform

# Application Insights (Azure)
APPLICATIONINSIGHTS_CONNECTION_STRING=your_connection_string
```

### Health Check Configuration
```bash
# Health check endpoints
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics

# Health check details
MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS=always
```

## 🚀 **Performance Optimization**

### JVM Configuration
```bash
# Memory settings for containers
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport

# For development (lower memory)
JAVA_OPTS=-Xms128m -Xmx256m -XX:+UseG1GC
```

### Database Connection Pooling
```bash
# HikariCP settings
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=10
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5
SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT=300000
```

### Cache Configuration
```bash
# Redis cache settings
SPRING_CACHE_REDIS_TIME_TO_LIVE=600000
SPRING_CACHE_REDIS_CACHE_NULL_VALUES=false
```

## 🔧 **Development vs Production**

### Development Environment
```bash
SPRING_PROFILES_ACTIVE=dev
LOGGING_LEVEL_ROOT=DEBUG
LOGGING_LEVEL_COM_ECOMMERCE=TRACE

# Mock external services for testing
MOCK_PAYMENT_GATEWAY=true
MOCK_EMAIL_SERVICE=true
```

### Production Environment
```bash
SPRING_PROFILES_ACTIVE=prod
LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_COM_ECOMMERCE=INFO

# Enable all features
FEATURE_EMAIL_ENABLED=true
FEATURE_PAYMENT_ENABLED=true
FEATURE_SEARCH_ENABLED=true
```

## 📝 **Setting Environment Variables**

### Railway
```bash
# Set variables for current service
railway variables set VARIABLE_NAME=value

# Set multiple variables
railway variables set VAR1=value1 VAR2=value2
```

### Google Cloud Run
```bash
# Set environment variables during deployment
gcloud run deploy service-name \
    --set-env-vars="VAR1=value1,VAR2=value2"

# Update existing service
gcloud run services update service-name \
    --set-env-vars="VAR1=value1"
```

### Docker Compose
```yaml
environment:
  - SPRING_PROFILES_ACTIVE=cloud
  - DATABASE_URL=${DATABASE_URL}
  - REDIS_URL=${REDIS_URL}
```

### Kubernetes
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  SPRING_PROFILES_ACTIVE: "cloud"
  DATABASE_URL: "your-database-url"
```

---

## ⚠️ **Important Security Notes**

1. **Never commit sensitive data** to version control
2. **Use cloud provider secret management** for production
3. **Rotate API keys and passwords** regularly
4. **Use strong, unique passwords** for all services
5. **Enable SSL/TLS** for all database connections
6. **Restrict database access** by IP when possible

## 🎯 **Quick Setup Commands**

### Copy Environment Template
```bash
# Copy and edit the environment file
cp deployment/environment-config.md .env
# Edit the .env file with your actual values
```

### Validate Configuration
```bash
# Check if all required variables are set
./deployment/scripts/validate-env.sh
```

---

This configuration guide covers all the major cloud providers and free service alternatives. Choose the configuration that matches your deployment target! 