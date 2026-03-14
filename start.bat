@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting SmartFarm Aquaculture Backend...
echo Java Version:
java -version

echo.
echo Building project...
if not exist ".mvn\wrapper\maven-wrapper.jar" (
    echo Downloading Maven Wrapper...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
)

java -cp ".mvn\wrapper\maven-wrapper.jar" -Dmaven.home=".mvn\wrapper" -Dmaven.multiModuleProjectDirectory=. org.apache.maven.wrapper.MavenWrapperMain clean package -DskipTests

echo.
echo Starting application...
echo API: http://localhost:8081/api/v1/aquaculture
echo Swagger: http://localhost:8081/api/v1/aquaculture/swagger-ui.html
echo.

java -jar target\aquaculture-1.0.0.jar

pause
