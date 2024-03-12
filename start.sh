#!/bin/bash

# Change to the Angular application directory and execute NPM
echo "Compiling Angular application..."
cd ./web-photofilm/
npm install

# Check if NPM install executed successfully
if [ $? -ne 0 ]; then
    echo "npm install failed, aborting..."
    exit 1
fi

npm run build

# Check if NPM run build executed successfully
if [ $? -ne 0 ]; then
    echo "npm run build failed, aborting..."
    exit 1
fi

# Change to the Java Maven application directory and execute Maven
echo "Compiling Java Maven application..."
cd ../WebService/
mvn clean install -DskipTests

# Check if Maven executed successfully
if [ $? -ne 0 ]; then
    echo "Maven compilation failed, aborting..."
    exit 1
fi

# Go back to the root directory and start Docker Compose services
echo "Starting Docker Compose services in background..."
docker-compose up --build -d

# End of script
