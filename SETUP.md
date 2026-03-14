# Java Backend Setup Guide

## Prerequisites Required

### 1. Install Java 17
Since Java is not installed on your system, you need to install it first:

#### Option A: Download from Oracle (Recommended)
1. Go to: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. Download **JDK 17** for Windows x64
3. Run the installer
4. Set JAVA_HOME environment variable:
   - Windows Key + R → `sysdm.cpl` → Advanced → Environment Variables
   - Add new system variable: `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
   - Add to PATH: `%JAVA_HOME%\bin`

#### Option B: Use Chocolatey (Package Manager)
```powershell
# Install Chocolatey if not already installed
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Java 17
choco install openjdk17

# Refresh environment
refreshenv
```

#### Option C: Use SDKMAN! (Recommended for developers)
```powershell
# Install SDKMAN!
powershell -Command "iwr -useb https://get.sdkman.io | iex"
sdkman install java 17.0.9-tem
sdkman use java 17.0.9-tem
```

### 2. Install Maven (Optional - wrapper will download automatically)
```powershell
# Using Chocolatey
choco install maven

# Or download manually from: https://maven.apache.org/download.cgi
```

### 3. Install PostgreSQL (Required for database)
```powershell
# Using Chocolatey
choco install postgresql

# Or download from: https://www.postgresql.org/download/windows/
```

### 4. Install Redis (Optional - for caching)
```powershell
# Using Chocolatey
choco install redis-64

# Or download from: https://redis.io/download
```

## Quick Setup Commands

Once Java is installed, open a NEW PowerShell window and run:

```powershell
# Navigate to project directory
cd "C:\Users\User\Desktop\shamba\backend\aquaculture"

# Verify Java installation
java -version

# Build the project (Maven wrapper will auto-download)
.\mvnw.cmd clean install

# Run the application
.\mvnw.cmd spring-boot:run
```

## Database Setup

### Create PostgreSQL Database
```sql
-- Connect to PostgreSQL and create database
CREATE DATABASE smartfarm_aquaculture;
CREATE USER aquaculture_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE smartfarm_aquaculture TO aquaculture_user;
```

### Update Application Configuration
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smartfarm_aquaculture
    username: aquaculture_user
    password: your_password
```

## Running the Application

### Method 1: Using Maven Wrapper (Recommended)
```powershell
.\mvnw.cmd spring-boot:run
```

### Method 2: Using Installed Maven
```powershell
mvn spring-boot:run
```

### Method 3: Run JAR directly
```powershell
# Build first
.\mvnw.cmd clean package

# Run
java -jar target/aquaculture-1.0.0.jar
```

## Testing the Application

Once running, test these endpoints:

### Health Check
```powershell
curl http://localhost:8081/api/v1/aquaculture/actuator/health
```

### API Documentation
Open in browser: http://localhost:8081/api/v1/aquaculture/swagger-ui.html

### Sample API Call
```powershell
curl -X GET "http://localhost:8081/api/v1/aquaculture/ponds?page=0&size=10"
```

## Troubleshooting

### Common Issues

#### 1. "Java not recognized"
- Close and reopen PowerShell after Java installation
- Verify JAVA_HOME is set correctly
- Check if Java is in PATH: `echo $env:PATH`

#### 2. "Port 8081 already in use"
- Kill process using port: `netstat -ano | findstr :8081`
- Kill process: `taskkill /PID <PID> /F`
- Or change port in application.yml

#### 3. "Database connection failed"
- Verify PostgreSQL is running
- Check database name and credentials
- Ensure PostgreSQL allows local connections

#### 4. "Maven download failed"
- Check internet connection
- Try using VPN if behind corporate firewall
- Manually download Maven and set MAVEN_HOME

### Development Tips

#### Hot Reload
```powershell
# Enable devtools for automatic restart
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=development
```

#### Debug Mode
```powershell
# Run with remote debugging
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

#### Logs
```powershell
# View application logs
Get-Content -Path "target/spring-boot-app.log" -Wait
```

## Next Steps

Once the backend is running:

1. **Test API endpoints** using Swagger UI
2. **Connect React frontend** to `http://localhost:8081/api/v1/aquaculture`
3. **Set up database** with sample data
4. **Configure Redis** for caching (optional)
5. **Set up monitoring** with Prometheus (optional)

## Production Deployment

For production deployment:

1. **Build JAR**: `.\mvnw.cmd clean package`
2. **Use Docker**: `docker build -t smartfarm/aquaculture .`
3. **Deploy to Kubernetes**: `kubectl apply -f k8s/`
4. **Set up load balancer** and **monitoring**

## Support

If you encounter issues:

1. Check the logs in `target/` directory
2. Verify all prerequisites are installed
3. Ensure ports 8081, 5432, 6379 are available
4. Check firewall and antivirus settings

---

**Ready to scale to 500,000+ concurrent users! 🚀**
