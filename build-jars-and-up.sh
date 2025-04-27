#!/usr/bin/env bash
set -euo pipefail

echo "1. Budowanie wszystkich JAR-ów (bez testów)..."
./gradlew clean :cart-service:bootJar :order-service:bootJar :product-service:bootJar -x test

echo "2. Budowanie i uruchamianie kontenerów Docker Compose..."
docker-compose up --build