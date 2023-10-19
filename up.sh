#!/bin/sh

# Exits immediately if a command exits with a non-zero status
set -e

# Run 'docker-compose up' for starting DB container
docker-compose -f docker/db/docker-compose.yml up -d