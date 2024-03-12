@echo off

REM Change to the Angular application directory and execute NPM
echo Compiling Angular application...
cd ./web-photofilm/
call npm install
if %errorlevel% neq 0 (
    echo npm install failed, aborting...
    exit /b 1
)

call npm run build
if %errorlevel% neq 0 (
    echo npm run build failed, aborting...
    exit /b 1
)

REM Change to the Java Maven application directory and execute Maven
echo Compiling Java Maven application...
cd ..\WebService\
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo Maven compilation failed, aborting...
    exit /b 1
)

REM Go back to the root directory and start Docker Compose services
echo Starting Docker Compose services in background...
call docker compose up -d --build

REM End of script