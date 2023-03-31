#!/bin/bash

# Update the system
sudo apt-get update
sudo apt-get upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install Docker Compose
sudo apt-get update
sudo apt-get install -y docker-compose

# Set environment variables
export DB_PASSWORD=$1
export DB_NAME=$2
export GF_SECURITY_ADMIN_PASSWORD=$3
export SERVER_IP=$4

# Make the mvnw file executable
chmod +x mvnw

# Run Docker Compose
docker-compose up -d
