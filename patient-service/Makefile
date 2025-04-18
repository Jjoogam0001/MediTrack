# Makefile for MediTrack Patient Service Docker operations

# Variables
COMPOSE_FILE := compose.yaml
DOCKER_REGISTRY := martin2020i
IMAGE_NAME := patient-service
IMAGE_TAG := latest

# Help target
.PHONY: help
help:
	@echo "MediTrack Patient Service Operations"
	@echo ""
	@echo "Usage:"
	@echo "  Docker Operations:"
	@echo "  make up        - Start all services defined in docker-compose"
	@echo "  make down      - Stop all services defined in docker-compose"
	@echo "  make build     - Build all services defined in docker-compose"
	@echo "  make push      - Push all services to the Docker registry"
	@echo "  make logs      - View logs from all services"
	@echo "  make ps        - List running services"
	@echo ""
	@echo "  Testing Operations:"
	@echo "  make test-unit       - Run unit tests"
	@echo "  make test-integration - Run integration tests"
	@echo "  make test-all        - Run all tests (unit and integration)"
	@echo "  make test-report     - Generate test reports"
	@echo ""
	@echo "  Other Operations:"
	@echo "  make help      - Show this help message"
	@echo ""
	@echo "Environment Variables:"
	@echo "  COMPOSE_FILE   - Docker compose file (default: compose.yaml)"
	@echo "  DOCKER_REGISTRY - Docker registry URL (default: your-registry.example.com)"
	@echo "  IMAGE_NAME     - Docker image name (default: patient-service)"
	@echo "  IMAGE_TAG      - Docker image tag (default: latest)"

# Start services
.PHONY: up
up:
	@echo "Starting services..."
	docker compose -f $(COMPOSE_FILE) up -d
	@echo "Services started successfully."

up-debug:
	@echo "Starting services in debug mode..."
	docker compose -f $(COMPOSE_FILE) up -d --build --no-deps --force-recreate postgres
	@echo "Services started successfully in debug mode."
	@echo "You can now attach to the postgres container using:"
# Stop services
.PHONY: down
down:
	@echo "Stopping services..."
	docker compose -f $(COMPOSE_FILE) down
	@echo "Services stopped successfully."

# Build services
.PHONY: build
build:
	@echo "Building services..."
	docker compose -f $(COMPOSE_FILE) build
	@echo "Services built successfully."

# Push services to registry
.PHONY: push
push:
	@echo "Building image..."
	docker build -t $(IMAGE_NAME):$(IMAGE_TAG) .
	@echo "Tagging image..."
	docker tag $(IMAGE_NAME):$(IMAGE_TAG) $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(IMAGE_TAG)
	@echo "Pushing image to registry..."
	docker push $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(IMAGE_TAG)
	@echo "Image pushed successfully."

# View logs
.PHONY: logs
logs:
	@echo "Viewing logs..."
	docker compose -f $(COMPOSE_FILE) logs -f

# List running services
.PHONY: ps
ps:
	@echo "Listing services..."
	docker compose -f $(COMPOSE_FILE) ps

# Run unit tests
.PHONY: test-unit
test-unit:
	@echo "Running unit tests..."
	./mvnw clean test

# Run integration tests
.PHONY: test-integration
test-integration:
	@echo "Running integration tests..."
	./mvnw failsafe:integration-test failsafe:verify

# Run all tests (unit and integration)
.PHONY: test-all
test-all:
	@echo "Running all tests..."
	./mvnw clean verify

# Generate test reports
.PHONY: test-report
test-report:
	@echo "Generating test reports..."
	./mvnw clean test surefire-report:report
	@echo "Test report generated at target/site/surefire-report.html"

# Default target
.DEFAULT_GOAL := help
