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

# find out the public IP address of the server
export SERVER_IP=$(curl -s http://checkip.amazonaws.com)

# Set environment variables
export DB_PASSWORD=$1
export DB_NAME=$2
export GF_SECURITY_ADMIN_PASSWORD=$3

# Run Docker Compose
docker-compose up -d
