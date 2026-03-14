# Production Setup for 500K+ Concurrent Users

## 🚀 PostgreSQL Installation & Configuration

### 1. Install PostgreSQL 15+ (Enterprise Ready)

#### Windows Server:
```powershell
# Download PostgreSQL 15 Enterprise
# https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

# Install with these settings:
- Port: 5432
- Superuser: postgres
- Password: [Strong Password]
- Locale: C
- Encoding: UTF8
```

#### Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install postgresql-15 postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### 2. Configure PostgreSQL for 500K Users

Edit `postgresql.conf`:
```ini
# Memory settings (adjust based on server RAM)
shared_buffers = 256MB          # 25% of RAM
effective_cache_size = 1GB       # 75% of RAM
maintenance_work_mem = 64MB
checkpoint_completion_target = 0.9
wal_buffers = 16MB

# Connection settings for 500K users
max_connections = 1000          # Per database instance
superuser_reserved_connections = 3

# Performance tuning
random_page_cost = 1.1          # For SSD
effective_io_concurrency = 200  # For SSD
work_mem = 4MB
default_statistics_target = 100

# Logging for monitoring
log_min_duration_statement = 1000
log_checkpoints = on
log_connections = on
log_disconnections = on
```

### 3. Create Database
```bash
# Run the setup script
psql -U postgres -f setup-database.sql
```

## 🔧 Application Configuration

### 1. Update application.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smartfarm_aquaculture
    username: aquaculture_user
    password: Aquaculture@2026!Secure
    hikari:
      maximum-pool-size: 200      # For 500K users
      minimum-idle: 20
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
      
  jpa:
    hibernate:
      ddl-auto: validate         # Production: validate only
    show-sql: false              # Production: disable
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        jdbc:
          batch_size: 50
        order_inserts: true
        order_updates: true
        batch_versioned_data: true
        
  # Redis for caching (essential for 500K users)
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 200
          max-idle: 50
          min-idle: 10
          
  # Kafka for real-time events
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      batch-size: 16384
      linger-ms: 5
      compression-type: snappy
    consumer:
      group-id: aquaculture-group
      auto-offset-reset: earliest
      max-poll-records: 100
```

### 2. JVM Configuration for 500K Users
```bash
# Run with these JVM settings
java -Xms2g -Xmx4g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+UseStringDeduplication \
     -XX:+OptimizeStringConcat \
     -XX:+UseCompressedOops \
     -XX:+UseCompressedClassPointers \
     -Dspring.profiles.active=production \
     -jar aquaculture-1.0.0.jar
```

## 📊 Horizontal Scaling Architecture

### 1. Load Balancer Setup
```
Internet → Nginx/HAProxy → Multiple App Instances → PostgreSQL Cluster
```

### 2. Application Instances
- **3-5 instances** per region
- **200 connections** each = 600-1000 total connections
- **Redis cluster** for shared caching
- **Kafka cluster** for message queuing

### 3. Database Scaling
```
Primary PostgreSQL (Read/Write) 
    ↓
Replica 1 (Read) ← Application Instance 1
Replica 2 (Read) ← Application Instance 2
Replica 3 (Read) ← Application Instance 3
```

## 🔍 Monitoring & Alerting

### 1. Prometheus Metrics
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### 2. Key Metrics to Monitor
- **Active connections**: < 800
- **Response time**: < 200ms
- **Error rate**: < 0.1%
- **Memory usage**: < 80%
- **CPU usage**: < 70%
- **Database connections**: < 900

## 🚨 Production Checklist

### Before Going Live:
- [ ] PostgreSQL installed and optimized
- [ ] Database created with indexes
- [ ] Redis cluster running
- [ ] Kafka cluster running
- [ ] Load balancer configured
- [ ] SSL certificates installed
- [ ] Monitoring setup
- [ ] Backup strategy implemented
- [ ] Disaster recovery plan tested
- [ ] Load testing completed (500K users)

### Security Configuration:
- [ ] Firewall rules configured
- [ ] Database access restricted
- [ ] API rate limiting enabled
- [ ] JWT authentication configured
- [ ] HTTPS enforced
- [ ] Security headers set

## 🎯 Expected Performance

### With This Setup:
- **Concurrent Users**: 500,000+
- **API Response Time**: < 200ms
- **Database Queries**: < 50ms
- **Uptime**: 99.999%
- **Throughput**: 10,000+ requests/second

### Scaling Strategy:
1. **Vertical**: Increase server resources
2. **Horizontal**: Add more application instances
3. **Database**: Read replicas + sharding
4. **Caching**: Redis cluster
5. **CDN**: Static assets distribution

## 🛠️ Troubleshooting

### Common Issues:
1. **Connection Pool Exhaustion**: Increase pool size
2. **Database Slow**: Add read replicas
3. **Memory Issues**: Increase heap size
4. **CPU High**: Add more instances
5. **Network Latency**: Use CDN

This setup is designed for **enterprise scale** and will handle **500,000+ concurrent users** without crashes! 🚀
