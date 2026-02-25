# Deployment Guide for Ubuntu VPS

This guide covers deploying the FlowerShop application on an Ubuntu VPS using Docker.

## Prerequisites

- Ubuntu 20.04+ VPS with at least 2GB RAM
- Docker and Docker Compose installed
- Domain name (optional, for HTTPS)
- Ports 80, 443, and 8080 open in firewall

## Installation Steps

### 1. Install Docker and Docker Compose

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Add user to docker group
sudo usermod -aG docker $USER
newgrp docker

# Install Docker Compose
sudo apt install docker-compose-plugin -y

# Verify installation
docker --version
docker compose version
```

### 2. Clone and Configure Application

```bash
# Clone your repository
git clone <your-repo-url>
cd <your-repo-directory>

# Create environment file
cp .env.example .env

# Edit environment variables
nano .env
```

Update `.env` with secure values:
```bash
DB_PASSWORD=YourSecurePassword123!
APP_PORT=8080
SPRING_PROFILE=prod
```

### 3. Initialize Database (Optional)

If you have SQL initialization scripts:

```bash
# Create init-db directory
mkdir -p init-db

# Copy your SQL scripts
cp TEST_ORDER_DATA.sql init-db/
```

### 4. Deploy Application

#### Basic Deployment (App + Database)

```bash
# Build and start services
docker compose up -d

# Check logs
docker compose logs -f

# Check status
docker compose ps
```

#### With Nginx Reverse Proxy

```bash
# Start with nginx profile
docker compose --profile with-nginx up -d

# Check nginx logs
docker compose logs nginx
```

### 5. Database Setup

```bash
# Access SQL Server container
docker exec -it flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourSecurePassword123!'

# Create database if not exists
1> CREATE DATABASE AsmJava5;
2> GO
3> EXIT

# Or restore from backup
docker cp backup.bak flowershop-sqlserver:/var/opt/mssql/backup/
docker exec -it flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourSecurePassword123!' -Q "RESTORE DATABASE AsmJava5 FROM DISK='/var/opt/mssql/backup/backup.bak' WITH REPLACE"
```

### 6. Configure Firewall

```bash
# Allow HTTP and HTTPS
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# If not using nginx, allow app port
sudo ufw allow 8080/tcp

# Enable firewall
sudo ufw enable
```

### 7. SSL/HTTPS Setup (Optional)

#### Using Let's Encrypt with Certbot

```bash
# Install certbot
sudo apt install certbot -y

# Generate certificate
sudo certbot certonly --standalone -d your-domain.com

# Copy certificates
sudo mkdir -p nginx/ssl
sudo cp /etc/letsencrypt/live/your-domain.com/fullchain.pem nginx/ssl/cert.pem
sudo cp /etc/letsencrypt/live/your-domain.com/privkey.pem nginx/ssl/key.pem
sudo chown -R $USER:$USER nginx/ssl

# Update nginx.conf to enable HTTPS server block
nano nginx/nginx.conf
# Uncomment the HTTPS server section and update server_name

# Restart nginx
docker compose restart nginx
```

## Management Commands

### View Logs
```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f app
docker compose logs -f sqlserver
```

### Restart Services
```bash
# Restart all
docker compose restart

# Restart specific service
docker compose restart app
```

### Update Application
```bash
# Pull latest code
git pull

# Rebuild and restart
docker compose up -d --build app
```

### Backup Database
```bash
# Create backup
docker exec flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourSecurePassword123!' -Q "BACKUP DATABASE AsmJava5 TO DISK='/var/opt/mssql/backup/AsmJava5_$(date +%Y%m%d).bak'"

# Copy backup to host
docker cp flowershop-sqlserver:/var/opt/mssql/backup/AsmJava5_$(date +%Y%m%d).bak ./backups/
```

### Stop Services
```bash
# Stop all services
docker compose down

# Stop and remove volumes (WARNING: deletes data)
docker compose down -v
```

## Monitoring

### Check Resource Usage
```bash
# Container stats
docker stats

# Disk usage
docker system df
```

### Health Checks
```bash
# Check app health
curl http://localhost:8080/actuator/health

# Check database connection
docker exec flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourSecurePassword123!' -Q "SELECT 1"
```

## Troubleshooting

### Application won't start
```bash
# Check logs
docker compose logs app

# Check if database is ready
docker compose logs sqlserver

# Restart services
docker compose restart
```

### Database connection issues
```bash
# Verify database is running
docker compose ps sqlserver

# Check database logs
docker compose logs sqlserver

# Test connection
docker exec flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourSecurePassword123!' -Q "SELECT @@VERSION"
```

### Out of memory
```bash
# Check memory usage
docker stats

# Adjust memory limits in docker-compose.yml
# Restart services
docker compose up -d
```

### Permission issues with uploads
```bash
# Fix permissions
docker exec -u root flowershop-app chown -R spring:spring /app/uploads
```

## Performance Optimization

### Enable Production Profile
Ensure `.env` has:
```bash
SPRING_PROFILE=prod
THYMELEAF_CACHE=true
SHOW_SQL=false
```

### Database Optimization
```bash
# Connect to database
docker exec -it flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourSecurePassword123!'

# Add indexes (example)
1> USE AsmJava5;
2> CREATE INDEX idx_product_category ON Products(CategoryId);
3> GO
```

## Security Checklist

- [ ] Change default database password
- [ ] Use strong passwords in `.env`
- [ ] Enable firewall (ufw)
- [ ] Set up SSL/HTTPS
- [ ] Regular backups
- [ ] Keep Docker images updated
- [ ] Monitor logs for suspicious activity
- [ ] Limit database port exposure (don't expose 1433 publicly)

## Access Application

- Without Nginx: `http://your-vps-ip:8080`
- With Nginx: `http://your-vps-ip` or `http://your-domain.com`
- With HTTPS: `https://your-domain.com`

## Support

For issues, check:
1. Application logs: `docker compose logs app`
2. Database logs: `docker compose logs sqlserver`
3. Nginx logs: `docker compose logs nginx`
4. System resources: `docker stats`
