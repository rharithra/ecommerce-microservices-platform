# E-Commerce Microservices Platform

A comprehensive, production-ready e-commerce platform built using microservices architecture with Spring Boot, Spring Cloud, and modern technologies.

## 🏗️ Architecture Overview

This platform implements a complete microservices-based e-commerce solution with the following core components:

### Infrastructure Services
- **Discovery Service** (Eureka Server) - Service registry and discovery
- **Config Service** (Spring Cloud Config) - Centralized configuration management
- **API Gateway** (Spring Cloud Gateway) - Request routing and filtering

### Business Services
- **User Service** - User management and authentication
- **Product Catalog Service** - Product management with caching and search
- **Order Service** - Order processing and management
- **Payment Service** - Payment processing with Razorpay integration
- **Notification Service** - Multi-channel notifications
- **Email Service** - Event-driven email processing
- **Search Service** - Advanced product search with Elasticsearch

### Technology Stack

| Component | Technology |
|-----------|------------|
| **Framework** | Spring Boot 3.2.0, Spring Cloud 2023.0.0 |
| **Database** | MySQL 8.0 |
| **Caching** | Redis 7.0 |
| **Message Broker** | Apache Kafka |
| **Search Engine** | Elasticsearch 8.11.0 |
| **Payment Gateway** | Razorpay |
| **Service Discovery** | Netflix Eureka |
| **API Gateway** | Spring Cloud Gateway |
| **Configuration** | Spring Cloud Config |
| **Monitoring** | Prometheus, Grafana |
| **Containerization** | Docker, Docker Compose |

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Node.js (for frontend, if needed)

### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/ecommerce-microservices-platform.git
cd ecommerce-microservices-platform
```

### 2. Start Infrastructure Services
```bash
cd docker-compose
docker-compose up -d
```

This will start:
- MySQL (Port: 3306)
- Redis (Port: 6379)
- Kafka & Zookeeper (Ports: 9092, 2181)
- Elasticsearch (Port: 9200)
- Kibana (Port: 5601)
- RabbitMQ (Ports: 5672, 15672)
- Prometheus (Port: 9090)
- Grafana (Port: 3000)

### 3. Build and Run Services
```bash
# Build all services
mvn clean install

# Start services in order
cd discovery-service && mvn spring-boot:run &
cd config-service && mvn spring-boot:run &
cd api-gateway && mvn spring-boot:run &
cd user-service && mvn spring-boot:run &
cd product-catalog-service && mvn spring-boot:run &
cd order-service && mvn spring-boot:run &
cd payment-service && mvn spring-boot:run &
cd notification-service && mvn spring-boot:run &
cd email-service && mvn spring-boot:run &
cd search-service && mvn spring-boot:run &
```

### 4. Service Ports
| Service | Port | URL |
|---------|------|-----|
| Discovery Service | 8761 | http://localhost:8761 |
| Config Service | 8888 | http://localhost:8888 |
| API Gateway | 8080 | http://localhost:8080 |
| User Service | 8081 | http://localhost:8081 |
| Product Catalog | 8082 | http://localhost:8082 |
| Order Service | 8083 | http://localhost:8083 |
| Payment Service | 8084 | http://localhost:8084 |
| Notification Service | 8085 | http://localhost:8085 |
| Email Service | 8086 | http://localhost:8086 |
| Search Service | 8087 | http://localhost:8087 |

## 📋 Features

### Core E-Commerce Features
- ✅ User Registration & Authentication
- ✅ Product Catalog Management
- ✅ Advanced Product Search & Filtering
- ✅ Shopping Cart & Checkout
- ✅ Order Management
- ✅ Payment Processing (Razorpay)
- ✅ Email Notifications
- ✅ Real-time Notifications

### Technical Features
- ✅ Microservices Architecture
- ✅ Service Discovery & Load Balancing
- ✅ Centralized Configuration
- ✅ API Gateway with Rate Limiting
- ✅ Circuit Breaker Pattern
- ✅ Event-Driven Architecture (Kafka)
- ✅ Redis Caching
- ✅ Elasticsearch Integration
- ✅ Monitoring & Observability
- ✅ Containerized Deployment

## 🔧 Configuration

### Environment Variables
Create `.env` files for each service or set the following environment variables:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=password

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Elasticsearch Configuration
ES_HOST=localhost
ES_PORT=9200

# Razorpay Configuration
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 📊 API Documentation

### User Service APIs
```bash
# Register User
POST /api/users/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}

# Get User by ID
GET /api/users/{id}

# Search Users
GET /api/users/search?keyword=john
```

### Product Catalog APIs
```bash
# Create Product
POST /api/products
{
  "name": "iPhone 15",
  "description": "Latest iPhone model",
  "price": 79999.00,
  "stockQuantity": 100,
  "category": "Electronics",
  "brand": "Apple"
}

# Get Products by Category
GET /api/products/category/{category}

# Search Products
GET /api/products/search?q=iphone&category=Electronics&minPrice=50000&maxPrice=100000
```

### Order Service APIs
```bash
# Create Order
POST /api/orders
{
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 79999.00
    }
  ]
}

# Get Orders by User
GET /api/orders/user/{userId}
```

### Payment Service APIs
```bash
# Create Payment
POST /api/payments/create
{
  "orderId": 1,
  "amount": 159998.00,
  "currency": "INR"
}

# Verify Payment
POST /api/payments/verify
{
  "razorpayOrderId": "order_xxx",
  "razorpayPaymentId": "pay_xxx",
  "razorpaySignature": "signature_xxx"
}
```

## 🔍 Search & Filtering

The platform provides powerful search capabilities through Elasticsearch:

### Product Search Features
- **Full-text search** across product name, description, and tags
- **Category filtering** with hierarchical support
- **Price range filtering** with min/max bounds
- **Brand filtering** with multiple brand selection
- **Sorting** by price, rating, popularity, newest
- **Faceted search** with aggregations
- **Auto-complete** suggestions
- **Search result highlighting**

### Search API Examples
```bash
# Basic text search
GET /api/search/products?q=smartphone

# Advanced filtering
GET /api/search/products?q=smartphone&category=Electronics&brand=Samsung&minPrice=20000&maxPrice=50000&sortBy=price&sortOrder=asc

# Search with facets
GET /api/search/products/facets?q=laptop
```

## 📧 Email Service

Event-driven email service that automatically sends emails for:
- User registration confirmation
- Order confirmation
- Payment success/failure
- Order status updates
- Password reset
- Newsletter subscriptions

### Supported Email Templates
- Welcome emails
- Order confirmations
- Payment receipts
- Shipping notifications
- Marketing campaigns

## 📱 Notification Service

Real-time notification system supporting:
- WebSocket notifications
- Push notifications
- SMS notifications (configurable)
- In-app notifications

## 🔐 Security

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- API rate limiting
- CORS configuration
- Input validation & sanitization

### Security Headers
- HTTPS enforcement
- CSRF protection
- XSS protection
- Security headers implementation

## 📈 Monitoring & Observability

### Health Checks
All services expose health endpoints:
```bash
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

### Monitoring Stack
- **Prometheus** - Metrics collection
- **Grafana** - Dashboards and visualization
- **Kafka UI** - Kafka cluster monitoring
- **Spring Boot Actuator** - Application metrics

### Key Metrics Monitored
- Request/response times
- Throughput (requests per second)
- Error rates
- Database connection pools
- Cache hit/miss ratios
- Queue depths
- Memory and CPU usage

## 🚢 Deployment

### Docker Deployment
```bash
# Build all services
mvn clean package

# Build Docker images
docker-compose -f docker-compose.services.yml build

# Deploy entire stack
docker-compose -f docker-compose.yml -f docker-compose.services.yml up -d
```

### Kubernetes Deployment
Kubernetes manifests are available in the `k8s/` directory:
```bash
kubectl apply -f k8s/
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Load Testing
Use tools like JMeter or Gatling to test performance:
- Target: 1000+ concurrent users
- Response time: < 500ms for 95th percentile
- Throughput: 10,000+ requests per minute

## 🔧 Development

### Adding a New Service
1. Create new Maven module
2. Add service dependencies
3. Implement business logic
4. Register with Eureka
5. Add to API Gateway routes
6. Update Docker Compose

### Code Quality
- Checkstyle for code formatting
- SpotBugs for static analysis
- JaCoCo for test coverage
- SonarQube integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Implement your changes
4. Add tests
5. Update documentation
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Email: support@ecommerce-platform.com
- Documentation: [Wiki](https://github.com/your-repo/ecommerce-microservices-platform/wiki)

## 🗺️ Roadmap

### Phase 1 (Current)
- ✅ Core microservices implementation
- ✅ Basic e-commerce features
- ✅ Payment integration
- ✅ Search functionality

### Phase 2 (Upcoming)
- 🔄 Machine learning recommendations
- 🔄 Advanced analytics
- 🔄 Mobile app support
- 🔄 Multi-vendor marketplace

### Phase 3 (Future)
- 🔄 AI-powered chatbot
- 🔄 Blockchain integration
- 🔄 IoT device support
- 🔄 Global deployment

---

**Built with ❤️ using Spring Boot and Modern Microservices Architecture** 