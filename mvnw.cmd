@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM ------------------
@REM   JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM -----------------
@REM   M2_HOME - location of maven2's installed home dir
@REM   MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM   MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off
@REM set title of command window
title %0
@REM enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%

@REM set %HOME% to the directory that was specified in the set mvn_... command
@set "HOME=%MvnProjectDir%"
@set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

@REM Execute the user specified initialization if any
IF EXIST "%USERPROFILE%\mavenrc_pre.bat" CALL "%USERPROFILE%\mavenrc_pre.bat"
IF EXIST "%USERPROFILE%\mavenrc_pre.cmd" CALL "%USERPROFILE%\mavenrc_pre.cmd"

@set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
:stripBaseDir
@IF NOT "%MAVEN_PROJECTBASEDIR:~-1%" == "\" GOTO checkSDrive
@set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"
@goto stripBaseDir

:checkSDrive
@if exist "%MAVEN_PROJECTBASEDIR:~0,3%\mvnw.cmd" goto init
@set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%\.."
@goto stripBaseDir

:init
@set MAVEN_CMD_LINE_ARGS=%*

@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.
@set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn" set "MAVEN_PROJECTBASEDIR=%CD%"

@REM Execute the user specified initialization if any
IF EXIST "%USERPROFILE%\mavenrc_post.bat" CALL "%USERPROFILE%\mavenrc_post.bat"
IF EXIST "%USERPROFILE%\mavenrc_post.cmd" CALL "%USERPROFILE%\mavenrc_post.cmd"

@IF NOT "%MAVEN_SKIP_RC%" == "" goto skipRcInit
@setlocal
@FOR /F "tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\maven.config") DO @set "%%A=%%B"
@endlocal & set "MAVEN_CONFIG=%MAVEN_CONFIG%"
:skipRcInit

@SET MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@REM For Cygwin, ensure paths are in UNIX format before anything is touched
@IF "%OS%"=="Windows_NT" @setlocal
@set "WRAPPER_DIR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper"
@set "WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar"
@set "WRAPPER_LAUNCHER=%WRAPPER_DIR%\MavenWrapperLauncher.jar"
@set "WRAPPER_URL_BASE=https://repo.maven.apache.org/maven2/io/takari/maven-wrapper"
@set "WRAPPER_VERSION=0.5.6"
@set "WRAPPER_SHA256_HASH=2c0a3a3bd3f6b0329c0b6b3f2d6c2a3a3bd3f6b0329c0b6b3f2d6c2a3a3bd3f6b0329"

@REM Download Maven Wrapper if it doesn't exist
@IF NOT EXIST "%WRAPPER_JAR%" (
    @echo Downloading Maven Wrapper...
    @echo Download URL: %WRAPPER_URL_BASE%/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar
    @powershell -Command "&[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%WRAPPER_URL_BASE%/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar' -OutFile '%WRAPPER_JAR%'"
    @IF EXIST "%WRAPPER_JAR%" (
        @echo Successfully downloaded Maven Wrapper
    ) ELSE (
        @echo Failed to download Maven Wrapper
        @exit /b 1
    )
)

@REM Validate Maven Wrapper
@FOR /F %%i IN ('@powershell -Command "Get-FileHash -Path '%WRAPPER_JAR%' -Algorithm SHA256 | Select-Object -ExpandProperty Hash"') DO set "WRAPPER_SHA256_ACTUAL=%%i"
@IF NOT "%WRAPPER_SHA256_ACTUAL%" == "%WRAPPER_SHA256_HASH%" (
    @echo Maven Wrapper SHA256 hash does not match expected value
    @echo Expected: %WRAPPER_SHA256_HASH%
    @echo Actual: %WRAPPER_SHA256_ACTUAL%
    @exit /b 1
)

@REM Set environment variables
@IF NOT "%MAVEN_HOME%" == "" set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
@IF NOT "%MAVEN_CMD%" == "" goto runMaven

@REM Try to find Maven in PATH
@FOR /F "delims=" %%i IN ('where mvn.cmd 2^>nul') DO set "MAVEN_CMD=%%i"
@IF "%MAVEN_CMD%" == "" goto runWrapper

:runMaven
@echo Running Maven from PATH
@call "%MAVEN_CMD%" %MAVEN_CMD_LINE_ARGS%
@goto end

:runWrapper
@echo Running Maven Wrapper
@set "WRAPPER_OPTS=-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%"
@set "WRAPPER_OPTS=%WRAPPER_OPTS% -Dmaven.home=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper"
@IF NOT "%MAVEN_OPTS%" == "" set "WRAPPER_OPTS=%WRAPPER_OPTS% %MAVEN_OPTS%"
@set "WRAPPER_OPTS=%WRAPPER_OPTS% -classpath %WRAPPER_JAR%;%WRAPPER_LAUNCHER%"
@set "WRAPPER_MAIN=org.apache.maven.wrapper.MavenWrapperMain"

@REM Execute Maven Wrapper
@"%MAVEN_JAVA_EXE%" %WRAPPER_OPTS% %WRAPPER_MAIN% %MAVEN_CMD_LINE_ARGS%

:end
@set ERROR_CODE=%ERRORLEVEL%
@IF "%MAVEN_BATCH_PAUSE%" == "on" pause
@IF "%MAVEN_BATCH_ECHO%" == "on" echo %MAVEN_BATCH_ECHO%

@REM Exit with the same error code as Maven
@exit /b %ERROR_CODE%
