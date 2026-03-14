@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting Java Backend...
echo Enter PostgreSQL password when prompted:
echo.

java -jar target\aquaculture-1.0.0.jar

pause
