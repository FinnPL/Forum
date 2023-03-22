#!/bin/bash

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Check if Docker is installed
if ! [ -x "$(command -v docker)" ]; then
  echo 'Error: Docker is not installed.' >&2
  exit 1
fi

# Install Docker Compose
sudo apt-get update
sudo apt-get install -y docker-compose

# Check if Docker Compose is installed
if ! [ -x "$(command -v docker-compose)" ]; then
  echo 'Error: Docker Compose is not installed.' >&2
  exit 1
fi

# Set environment variables
export DB_PASSWORD=$1
export DB_NAME=$2
export GF_SECURITY_ADMIN_PASSWORD=$3
