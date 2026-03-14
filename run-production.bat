@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting Production Java Backend for 500K+ Users...
echo Java Version:
java -version

echo.
echo Starting Spring Boot Application...
echo Production API: http://localhost:8081/api/v1/aquaculture
echo Swagger UI: http://localhost:8081/api/v1/aquaculture/swagger-ui.html
echo Health Check: http://localhost:8081/api/v1/aquaculture/actuator/health
echo Metrics: http://localhost:8081/api/v1/aquaculture/actuator/metrics
echo.

echo JVM Configuration for 500K Users:
java -Xms2g -Xmx4g ^
     -XX:+UseG1GC ^
     -XX:MaxGCPauseMillis=200 ^
     -XX:+UseStringDeduplication ^
     -XX:+OptimizeStringConcat ^
     -XX:+UseCompressedOops ^
     -XX:+UseCompressedClassPointers ^
     -XX:+PrintGCDetails ^
     -XX:+PrintGCTimeStamps ^
     -Xloggc:gc.log ^
     -Dspring.profiles.active=production ^
     -jar target\aquaculture-1.0.0.jar

pause
