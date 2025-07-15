#!/bin/bash

# E-Commerce Microservices Platform - Railway Deployment Script
# This script automates the deployment of all microservices to Railway

set -e

echo "🚀 Starting Railway deployment for E-Commerce Microservices Platform"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo -e "${RED}❌ Railway CLI is not installed. Please install it first:${NC}"
    echo "npm install -g @railway/cli"
    exit 1
fi

# Check if user is logged in
if ! railway whoami &> /dev/null; then
    echo -e "${YELLOW}⚠️  You're not logged in to Railway. Please log in:${NC}"
    railway login
fi

# Function to deploy a service
deploy_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    
    echo -e "\n${BLUE}📦 Deploying ${service_name}...${NC}"
    
    cd "${service_dir}"
    
    # Create Dockerfile if it doesn't exist
    if [ ! -f "Dockerfile" ]; then
        echo -e "${YELLOW}⚠️  Creating Dockerfile for ${service_name}${NC}"
        cp ../Dockerfile.template Dockerfile
        # Replace port in Dockerfile
        sed -i "s/EXPOSE 8080/EXPOSE ${port}/g" Dockerfile
        sed -i "s/localhost:8080/localhost:${port}/g" Dockerfile
    fi
    
    # Deploy to Railway
    if railway up; then
        echo -e "${GREEN}✅ Successfully deployed ${service_name}${NC}"
    else
        echo -e "${RED}❌ Failed to deploy ${service_name}${NC}"
        return 1
    fi
    
    cd ..
}

# Function to set environment variables for a service
set_env_vars() {
    local service_name=$1
    local port=$2
    
    echo -e "\n${BLUE}🔧 Setting environment variables for ${service_name}...${NC}"
    
    # Common environment variables
    railway variables set SPRING_PROFILES_ACTIVE=cloud
    railway variables set SERVER_PORT=${port}
    
    # Service-specific variables
    case $service_name in
        "discovery-service")
            # No additional variables needed
            ;;
        "config-service")
            railway variables set EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://discovery-service.railway.app/eureka/"
            ;;
        "api-gateway")
            railway variables set EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://discovery-service.railway.app/eureka/"
            ;;
        "user-service"|"product-catalog-service"|"order-service"|"payment-service"|"email-service")
            railway variables set EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://discovery-service.railway.app/eureka/"
            # Database will be added as Railway addon
            ;;
        "search-service")
            railway variables set EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://discovery-service.railway.app/eureka/"
            ;;
    esac
}

# Create Railway project
echo -e "\n${BLUE}🏗️  Creating Railway project...${NC}"
read -p "Enter project name (default: ecommerce-platform): " project_name
project_name=${project_name:-ecommerce-platform}

if ! railway new ${project_name} --template blank; then
    echo -e "${YELLOW}⚠️  Project might already exist. Linking to existing project...${NC}"
    railway link
fi

# Services to deploy in order
declare -a services=(
    "discovery-service:8761"
    "config-service:8888"
    "api-gateway:8080"
    "user-service:8081"
    "product-catalog-service:8082"
    "order-service:8083"
    "payment-service:8084"
    "email-service:8086"
    "search-service:8087"
)

# Deploy services
echo -e "\n${BLUE}🚀 Starting service deployment...${NC}"

for service in "${services[@]}"; do
    IFS=':' read -ra ADDR <<< "$service"
    service_name="${ADDR[0]}"
    port="${ADDR[1]}"
    
    if [ -d "$service_name" ]; then
        deploy_service "$service_name" "$service_name" "$port"
        set_env_vars "$service_name" "$port"
        
        # Wait a bit between deployments
        echo -e "${YELLOW}⏳ Waiting 30 seconds before next deployment...${NC}"
        sleep 30
    else
        echo -e "${RED}❌ Directory $service_name not found. Skipping...${NC}"
    fi
done

# Add database addon
echo -e "\n${BLUE}🗄️  Adding PostgreSQL database...${NC}"
if railway add postgresql; then
    echo -e "${GREEN}✅ PostgreSQL database added successfully${NC}"
else
    echo -e "${YELLOW}⚠️  Database might already exist or failed to add${NC}"
fi

# Add Redis addon
echo -e "\n${BLUE}🔴 Adding Redis cache...${NC}"
if railway add redis; then
    echo -e "${GREEN}✅ Redis cache added successfully${NC}"
else
    echo -e "${YELLOW}⚠️  Redis might already exist or failed to add${NC}"
fi

# Get service URLs
echo -e "\n${BLUE}🌐 Getting service URLs...${NC}"
echo -e "${GREEN}📋 Service URLs:${NC}"
echo "Discovery Service: https://discovery-service.railway.app"
echo "Config Service: https://config-service.railway.app"
echo "API Gateway: https://api-gateway.railway.app"
echo "User Service: https://user-service.railway.app"
echo "Product Service: https://product-catalog-service.railway.app"
echo "Order Service: https://order-service.railway.app"
echo "Payment Service: https://payment-service.railway.app"
echo "Email Service: https://email-service.railway.app"
echo "Search Service: https://search-service.railway.app"

# Final instructions
echo -e "\n${GREEN}🎉 Deployment completed!${NC}"
echo -e "\n${BLUE}📝 Next steps:${NC}"
echo "1. Set up your environment variables in Railway dashboard"
echo "2. Configure your database URLs for each service"
echo "3. Set up your external service API keys (Razorpay, Email, etc.)"
echo "4. Test your APIs using the provided URLs"

echo -e "\n${BLUE}💡 Important Environment Variables to Set:${NC}"
echo "- RAZORPAY_KEY_ID and RAZORPAY_KEY_SECRET"
echo "- MAIL_USERNAME and MAIL_PASSWORD"
echo "- Database URLs (will be auto-configured with Railway addons)"

echo -e "\n${BLUE}🔍 Monitor your deployment:${NC}"
echo "Railway Dashboard: https://railway.app/dashboard"
echo "Check logs: railway logs"
echo "Check variables: railway variables"

echo -e "\n${GREEN}Happy deploying! 🚀${NC}" 