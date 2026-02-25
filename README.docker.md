# Docker Deployment Guide

Quick reference for deploying FlowerShop application using Docker on Ubuntu VPS.

## Quick Start

```bash
# 1. Clone repository
git clone <your-repo-url>
cd <your-repo-directory>

# 2. Run deployment script
chmod +x deploy.sh
./deploy.sh
```

The script will:
- Install Docker and Docker Compose if needed
- Create `.env` file from template
- Deploy the application
- Show access URL

## Manual Deployment

### Basic Setup (App + Database)

```bash
# Create environment file
cp .env.example .env
nano .env  # Update with secure passwords

# Start services
docker compose up -d

# View logs
docker compose logs -f
```

### With Nginx Reverse Proxy

```bash
# Start with nginx profile
docker compose --profile with-nginx up -d
```

## Environment Variables

Edit `.env` file to configure:

```bash
# Database
DB_PASSWORD=YourSecurePassword123!  # Change this!
DB_NAME=AsmJava5
DB_PORT=1433

# Application
APP_PORT=8080
SPRING_PROFILE=prod

# Nginx (if using)
NGINX_HTTP_PORT=80
NGINX_HTTPS_PORT=443
```

## Common Commands

```bash
# View logs
docker compose logs -f app
docker compose logs -f sqlserver

# Restart services
docker compose restart

# Stop services
docker compose down

# Update application
git pull
docker compose up -d --build app

# Backup database
docker exec flowershop-sqlserver /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P 'YourPassword' \
  -Q "BACKUP DATABASE AsmJava5 TO DISK='/var/opt/mssql/backup/backup.bak'"
```

## Architecture

```
┌─────────────────┐
│  Nginx (80/443) │  ← Optional reverse proxy
└────────┬────────┘
         │
┌────────▼────────┐
│   App (8080)    │  ← Spring Boot application
└────────┬────────┘
         │
┌────────▼────────┐
│ SQL Server      │  ← Database
│   (1433)        │
└─────────────────┘
```

## Volumes

- `sqlserver_data`: Database files (persistent)
- `app_uploads`: Uploaded product images (persistent)
- `app_logs`: Application logs (persistent)

## Security Notes

1. Change default passwords in `.env`
2. Don't expose database port (1433) publicly
3. Use HTTPS in production (see DEPLOYMENT.md)
4. Enable firewall: `sudo ufw enable`
5. Regular backups

## Troubleshooting

### Application won't start
```bash
docker compose logs app
```

### Database connection failed
```bash
docker compose logs sqlserver
docker compose restart sqlserver
```

### Out of memory
Adjust memory limits in `docker-compose.yml`:
```yaml
deploy:
  resources:
    limits:
      memory: 2G
```

## Full Documentation

See [DEPLOYMENT.md](DEPLOYMENT.md) for complete deployment guide including:
- SSL/HTTPS setup
- Database initialization
- Monitoring and maintenance
- Performance optimization
- Security checklist

## Support

Check logs first:
```bash
docker compose logs -f
```

Common issues are documented in DEPLOYMENT.md troubleshooting section.
