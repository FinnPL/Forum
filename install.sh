#!/bin/bash
set -euo pipefail

# Update the system and install Docker and Docker Compose
sudo apt-get update && sudo apt-get upgrade -y && curl -fsSL https://get.docker.com -o get-docker.sh | sh && sudo apt-get install -y docker-compose

# Find out the public IP address of the server
export SERVER_IP=$(curl -s http://checkip.amazonaws.com)

# Set environment variables
if [ $# -eq 0 ]; then
  echo -e "\e[33mWarning: No arguments provided. Generating random password.\e[0m"
  export DB_PASSWORD=$(openssl rand -base64 12)
  export DB_NAME="forum"
  export GF_SECURITY_ADMIN_PASSWORD=$(openssl rand -base64 12)
else
  export DB_PASSWORD=${1} DB_NAME=${2} GF_SECURITY_ADMIN_PASSWORD=${3}
fi

# Make the gradlew file executable
chmod +x ./server/gradlew

# Start Docker Compose
docker-compose up -d

echo -e "\e[32mScript completed successfully.\e[0m"

if [ $# -eq 0 ]; then
  echo -e "\e[36mGenerated password: ${DB_PASSWORD}\e[0m"
  echo -e "\e[36mGrafana admin password: ${GF_SECURITY_ADMIN_PASSWORD}\e[0m"
fi