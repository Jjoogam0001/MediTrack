# GitHub Actions Setup for MediTrack

## Overview

This document provides an overview of the GitHub Actions workflow that has been set up to automate the build, test, and deployment process for the MediTrack patient-service.

## What Has Been Implemented

Two GitHub Actions workflows have been created:

### 1. Integration and Unit Tests Pipeline (GitFlow)

This workflow is designed for the development process and focuses on:
1. Running unit tests
2. Running integration tests
3. Generating test reports

### 2. Docker Build and Publish Pipeline

This workflow focuses on the deployment process:
1. Building the Java application
2. Running tests
3. Building a Docker image
4. Pushing the Docker image to DockerHub

## Files Created

1. `.github/workflows/test-pipeline.yml` - The GitFlow test pipeline workflow file
2. `.github/workflows/docker-publish.yml` - The Docker build and publish workflow file
3. `.github/README.md` - Documentation for the GitHub Actions workflows

## How It Works

### Integration and Unit Tests Pipeline

This workflow is triggered by:
- Pushes to the `develop` branch
- Pushes to any `feature/*` branch
- Pull requests to `develop`, `feature/*`, or `release/*` branches
- Manual triggers via the GitHub Actions UI

When triggered, the workflow:
1. **Unit Tests Job**:
   - Determines the appropriate semantic version tag based on branch type:
     - Feature branches (`feature/*`): Increment major version (e.g., v1.0.0 → v2.0.0)
     - Develop branch: Increment minor version (e.g., v1.0.0 → v1.1.0)
     - Fix branches (`fix/*`): Increment patch version (e.g., v1.0.0 → v1.0.1)
   - Creates and pushes a Git tag with the determined version (skipped for pull requests)
   - Sets up a Java 17 environment
   - Runs unit tests using Maven
   - Uploads unit test results as artifacts

2. **Integration Tests Job**:
   - Runs after the unit tests job completes
   - Sets up a Java 17 environment
   - Runs integration tests using Maven Failsafe plugin
   - Uploads integration test results as artifacts

3. **Test Report Job**:
   - Runs after both test jobs complete
   - Generates a comprehensive test report
   - Uploads the report as an artifact

This pipeline helps ensure code quality during development by running tests early and often, before code is merged to main production branches.

### Docker Build and Publish Pipeline

This workflow is triggered by:
- Pushes to the `main` branch
- Pull requests to the `main` branch
- Manual triggers via the GitHub Actions UI

When triggered, the workflow:
1. Sets up a Java 17 environment
2. Builds and tests the application using Maven
3. Logs in to DockerHub (using secrets)
4. Builds the Docker image using the Dockerfile in the patient-service directory
5. Pushes the image to DockerHub (only for pushes to main, not for pull requests)

The Docker image is tagged with:
- `latest` - Always points to the most recent successful build
- `v{major}.{minor}.{patch}` - Semantic version tag automatically determined based on branch type:
  - Feature branches (`feature/*`): Increment major version (e.g., v1.0.0 → v2.0.0)
  - Develop branch: Increment minor version (e.g., v1.0.0 → v1.1.0)
  - Fix branches (`fix/*`): Increment patch version (e.g., v1.0.0 → v1.0.1)
- `<commit-sha>` - A unique tag for each build based on the Git commit SHA

Additionally, the workflow creates a Git tag with the same semantic version and pushes it to the repository.

## Required Setup

Before the workflow can push images to DockerHub, you need to set up the following secrets in your GitHub repository:

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

## Customization

If you need to customize the Docker image name or tag, modify the environment variables in the workflow file:

```yaml
env:
  REGISTRY: docker.io
  IMAGE_NAME: martin2020i/patient-service
  IMAGE_TAG: latest
```

## Testing the Workflows

### Testing the Integration and Unit Tests Pipeline

You can test this workflow by:
1. Creating a new feature branch: `git checkout -b feature/your-feature-name`
2. Making changes and pushing to the feature branch
3. Creating a pull request to the develop branch
4. Checking the GitHub Actions tab to see the workflow running
5. Manually triggering the workflow from the GitHub Actions UI

To test the auto-tagging functionality:
1. Push to a feature branch to see a major version bump (e.g., v1.0.0 → v2.0.0)
2. Push to the develop branch to see a minor version bump (e.g., v1.0.0 → v1.1.0)
3. Push to a fix branch to see a patch version bump (e.g., v1.0.0 → v1.0.1)
4. Check the repository tags to verify that the new tag was created

### Testing the Docker Build and Publish Pipeline

You can test this workflow by:
1. Pushing changes to the main branch
2. Creating a pull request to the main branch
3. Manually triggering the workflow from the GitHub Actions UI

To test the auto-tagging functionality:
1. Push to a feature branch to see a major version bump (e.g., v1.0.0 → v2.0.0)
2. Push to the develop branch to see a minor version bump (e.g., v1.0.0 → v1.1.0)
3. Push to a fix branch to see a patch version bump (e.g., v1.0.0 → v1.0.1)
4. Check the repository tags to verify that the new tag was created
5. Check DockerHub to verify that the Docker image was tagged with the new version

## Troubleshooting

### Integration and Unit Tests Pipeline

If the test pipeline fails:
1. Check the GitHub Actions logs for error messages
2. Examine the test results artifacts for specific test failures
3. Run the tests locally to reproduce the issue:
   ```
   cd patient-service
   mvn clean test                           # For unit tests
   mvn failsafe:integration-test            # For integration tests
   ```
4. Make sure your database configuration is correct for integration tests

If the auto-tagging fails:
1. Check if you have write permissions to the repository
2. Verify that the branch name follows the expected pattern (feature/*, develop, fix/*)
3. Check if there are existing tags that might conflict
4. Ensure the tag format is valid (v{major}.{minor}.{patch})

### Docker Build and Publish Pipeline

If the Docker pipeline fails:
1. Check the GitHub Actions logs for error messages
2. Verify that the DockerHub secrets are correctly set up
3. Ensure the Dockerfile builds successfully locally
4. Make sure the Maven build and tests pass locally

If the auto-tagging fails:
1. Check if you have write permissions to the repository
2. Verify that the branch name follows the expected pattern (feature/*, develop, fix/*)
3. Check if there are existing tags that might conflict
4. Ensure the tag format is valid (v{major}.{minor}.{patch})
5. Verify that you can push to DockerHub with the specified tag
