# GitHub Actions Workflows

This directory contains GitHub Actions workflows for automating the build, test, and deployment process for the MediTrack patient-service.

## Available Workflows

### 1. Integration and Unit Tests Pipeline (GitFlow)

**File:** `.github/workflows/test-pipeline.yml`

This workflow is designed for the development process and focuses on:
1. Running unit tests
2. Running integration tests
3. Generating test reports

**Triggers:**
- Pushes to the `develop` branch
- Pushes to any `feature/*` branch
- Pull requests to `develop`, `feature/*`, or `release/*` branches
- Manual triggers via the GitHub Actions UI

### 2. Docker Build and Publish Pipeline

**File:** `.github/workflows/docker-publish.yml`

This workflow focuses on the deployment process:
1. Building the Java application
2. Running tests
3. Building a Docker image
4. Pushing the Docker image to DockerHub

**Triggers:**
- Pushes to the `main` branch
- Pull requests to the `main` branch
- Manual triggers via the GitHub Actions UI

### 3. Makefile CI

**File:** `.github/workflows/makefile.yml`

This workflow uses the Makefile targets to:
1. Run unit tests
2. Run integration tests
3. Generate test reports
4. Build and push Docker images

**Triggers:**
- Pushes to the `main` and `develop` branches
- Pull requests to the `main` and `develop` branches
- Manual triggers via the GitHub Actions UI

## Required Setup

Before the workflows can push images to DockerHub, you need to set up the following secrets in your GitHub repository:

1. `DOCKERHUB_USERNAME`: Your DockerHub username
2. `DOCKERHUB_TOKEN`: A DockerHub access token (not your password)

### Setting Up DockerHub Secrets

1. Generate a DockerHub access token:
   - Log in to [DockerHub](https://hub.docker.com/)
   - Go to Account Settings > Security > New Access Token
   - Create a token with an appropriate name and permissions
   - Copy the generated token

2. Add secrets to your GitHub repository:
   - Go to your GitHub repository
   - Navigate to Settings > Secrets and variables > Actions
   - Click "New repository secret"
   - Add both `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` secrets