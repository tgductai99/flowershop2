u#!/bin/bash

# FlowerShop Deployment Script for Ubuntu VPS

set -e

echo "🌸 FlowerShop Deployment Script"
echo "================================"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Installing Docker...${NC}"
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
    rm get-docker.sh
    echo -e "${GREEN}Docker installed successfully!${NC}"
    echo -e "${YELLOW}Please log out and log back in for group changes to take effect.${NC}"
    exit 0
fi

# Check if Docker Compose is installed
if ! docker compose version &> /dev/null; then
    echo -e "${RED}Docker Compose plugin is not installed. Installing...${NC}"
    sudo apt update
    sudo apt install docker-compose-plugin -y
    echo -e "${GREEN}Docker Compose installed successfully!${NC}"
fi

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo -e "${YELLOW}Creating .env file from .env.example...${NC}"
    cp .env.example .env
    echo -e "${GREEN}.env file created. Please update it with your secure passwords!${NC}"
    echo -e "${YELLOW}Edit .env file: nano .env${NC}"
    read -p "Press enter to continue after editing .env file..."
fi

# Create necessary directories
echo "Creating necessary directories..."
mkdir -p nginx/ssl
mkdir -p init-db
mkdir -p backups

# Ask deployment mode
echo ""
echo "Select deployment mode:"
echo "1) Basic (App + Database only)"
echo "2) With Nginx reverse proxy"
read -p "Enter choice [1-2]: " choice

case $choice in
    1)
        echo -e "${GREEN}Deploying in basic mode...${NC}"
        docker compose up -d --build
        ;;
    2)
        echo -e "${GREEN}Deploying with Nginx...${NC}"
        docker compose --profile with-nginx up -d --build
        ;;
    *)
        echo -e "${RED}Invalid choice. Deploying in basic mode...${NC}"
        docker compose up -d --build
        ;;
esac

# Wait for services to be healthy
echo ""
echo "Waiting for services to start..."
sleep 10

# Check service status
echo ""
echo "Service Status:"
docker compose ps

# Show logs
echo ""
echo -e "${GREEN}Deployment complete!${NC}"
echo ""
echo "Useful commands:"
echo "  View logs:        docker compose logs -f"
echo "  Stop services:    docker compose down"
echo "  Restart:          docker compose restart"
echo "  Update app:       docker compose up -d --build app"
echo ""

# Get server IP
SERVER_IP=$(hostname -I | awk '{print $1}')

if [ "$choice" == "2" ]; then
    echo -e "${GREEN}Access your application at: http://$SERVER_IP${NC}"
else
    echo -e "${GREEN}Access your application at: http://$SERVER_IP:8080${NC}"
fi

echo ""
echo -e "${YELLOW}Don't forget to:${NC}"
echo "  1. Configure firewall: sudo ufw allow 80/tcp && sudo ufw allow 443/tcp"
echo "  2. Set up SSL certificate for HTTPS (see DEPLOYMENT.md)"
echo "  3. Create database if needed (see DEPLOYMENT.md)"
echo "  4. Change default passwords in .env file"
