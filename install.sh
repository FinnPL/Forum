#!/bin/bash
set -euo pipefail

# Update the system and install Docker and Docker Compose
curl -fsSL https://get.docker.com -o get-docker.sh | sh && \
apt-get install -y docker-compose

# Find out the public IP address of the server
export SERVER_IP=$(curl -s http://checkip.amazonaws.com)

# Set environment variables
export DB_PASSWORD=$1 DB_NAME=$2 GF_SECURITY_ADMIN_PASSWORD=$3

# Make the gradlew file executable
chmod +x gradlew

# Start Docker Compose
docker-compose up -d