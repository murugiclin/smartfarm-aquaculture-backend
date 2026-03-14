@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting Java Backend...
echo Java Version:
java -version

echo.
echo Starting Spring Boot Application...
echo API will be available at: http://localhost:8081/api/v1/aquaculture
echo Swagger UI: http://localhost:8081/api/v1/aquaculture/swagger-ui.html
echo.

java -jar target\aquaculture-1.0.0.jar

pause
