@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Building Java Backend...
echo Java Version:
java -version

echo.
echo Downloading Maven Wrapper...
if not exist ".mvn\wrapper\maven-wrapper.jar" (
    echo Creating .mvn\wrapper directory...
    if not exist ".mvn\wrapper" mkdir .mvn\wrapper
    
    echo Downloading Maven Wrapper JAR...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
)

echo.
echo Building project...
java -cp ".mvn\wrapper\maven-wrapper.jar" -Dmaven.home=".mvn\wrapper" -Dmaven.multiModuleProjectDirectory=. org.apache.maven.wrapper.MavenWrapperMain clean install

echo.
echo Build completed!
pause
