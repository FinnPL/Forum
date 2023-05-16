#!/bin/bash
set -euo pipefail

# Update the system and install Docker and Docker Compose
curl -fsSL https://get.docker.com -o get-docker.sh | sh && sudo apt-get install -y docker-compose

# Find out the public IP address of the server
export SERVER_IP=$(curl -s http://checkip.amazonaws.com)

# Set environment variables
if [[ "${@}" =~ "--rebuild" ]]; then
  # Check if the rebuild argument is provided
  # Stop and remove the Docker containers
  docker-compose down

  # Build and start the Docker containers
  docker-compose up -d --build
else
  # Set default values for environment variables
  DB_PASSWORD=$(openssl rand -base64 12)
  DB_NAME="forum"
  GF_SECURITY_ADMIN_PASSWORD=$(openssl rand -base64 12)

  # Override default values with command line arguments
  while [[ $# -gt 0 ]]; do
    case "$1" in
      --db-password) DB_PASSWORD="$2"; shift 2;;
      --db-name) DB_NAME="$2"; shift 2;;
      --admin-password) GF_SECURITY_ADMIN_PASSWORD="$2"; shift 2;;
      *) echo "Invalid argument: $1"; exit 1;;
    esac
  done

  # Make the gradlew file executable
  chmod +x gradlew

  # Start the Docker containers
  docker-compose up -d
fi

echo -e "\e[32mScript completed successfully.\e[0m"

# Print generated passwords if no arguments were provided
if [ $# -eq 0 ]; then
  echo -e "\e[36mGenerated password: ${DB_PASSWORD}\e[0m"
  echo -e "\e[36mGrafana admin password: ${GF_SECURITY_ADMIN_PASSWORD}\e[0m"
fi