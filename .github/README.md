# GitHub Actions Workflows

This directory contains GitHub Actions workflows for automating various tasks in the MediTrack project.

## Available Workflows

### Integration and Unit Tests

**File:** [test-pipeline.yml](./workflows/test-pipeline.yml)

This workflow is designed for the GitFlow development process, focusing on running tests for feature branches and pull requests.

**Triggers:**
- Push to the `develop` branch
- Push to any `feature/*` branch
- Pull requests to `develop`, `feature/*`, or `release/*` branches
- Manual trigger via GitHub Actions UI

**Jobs:**
1. **Unit Tests**: Runs all unit tests and uploads test results
2. **Integration Tests**: Runs all integration tests and uploads test results
3. **Test Report**: Generates a comprehensive test report combining both test types

**Artifacts:**
- Unit test results
- Integration test results
- Combined test reports

This workflow helps ensure code quality during development by running tests early and often, before code is merged to main production branches.

### Docker Build, Test and Publish

**File:** [docker-publish.yml](./workflows/docker-publish.yml)

This workflow automates the process of building, testing, and publishing the Docker image for the patient-service to DockerHub.

**Triggers:**
- Push to the `main` branch
- Pull requests to the `main` branch
- Manual trigger via GitHub Actions UI

**Steps:**
1. Checkout the repository
2. Set up JDK 17
3. Build and test the application with Maven
4. Login to Docker Hub (skipped for pull requests)
5. Build and push the Docker image to DockerHub (only pushes for non-pull request events)

**Docker Image Tags:**
- `latest`: Always points to the most recent successful build from the main branch
- `v{major}.{minor}.{patch}`: Semantic version tag automatically determined based on branch type:
  - Feature branches (`feature/*`): Increment major version (e.g., v1.0.0 → v2.0.0)
  - Develop branch: Increment minor version (e.g., v1.0.0 → v1.1.0)
  - Fix branches (`fix/*`): Increment patch version (e.g., v1.0.0 → v1.0.1)
- `<commit-sha>`: Unique tag for each build, using the Git commit SHA

## Required Secrets

To use the Docker publishing workflow, you need to set up the following secrets in your GitHub repository:

1. `DOCKERHUB_USERNAME`: Your DockerHub username
2. `DOCKERHUB_TOKEN`: A DockerHub access token (not your password)

### How to set up DockerHub secrets:

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

## Customization

To customize the Docker image name or tag, modify the environment variables in the workflow file:

```yaml
env:
  REGISTRY: docker.io
  IMAGE_NAME: martin2020i/patient-service
  IMAGE_TAG: latest
```
