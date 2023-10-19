#!/bin/sh

# Exits immediately if a command exits with a non-zero status
set -e

# Run 'docker-compose down' for terminating DB container
docker-compose -f docker/db/docker-compose.yml down