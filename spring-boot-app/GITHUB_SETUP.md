# GitHub Actions CI/CD Setup Guide

This guide walks you through setting up your Spring Boot application with GitHub and configuring automated CI/CD using GitHub Actions.

## What This Setup Provides

- ✅ **Automated builds** on every push and pull request
- ✅ **Automated testing** with Maven
- ✅ **Automated deployment** to Azure App Service (on master branch only)
- ✅ **Public portfolio** piece for job applications
- ✅ **Professional development workflow** using industry-standard tools

---

## Phase 1: Create GitHub Repository

### Step 1: Create Repository on GitHub

1. Go to https://github.com and sign in (create account if needed)
2. Click the **"+"** icon (top right) → **"New repository"**
3. Configure:
   - **Repository name**: `spring-boot-player-api` (or your choice)
   - **Description**: "Spring Boot REST API with JPA, Azure SQL Database, and CI/CD"
   - **Visibility**: **Public** ⭐ (important for job searching!)
   - **Do NOT** check "Initialize with README" (you already have code)
4. Click **"Create repository"**

### Step 2: Push Your Code to GitHub

After creating the repository, GitHub will show you commands. Use these:

```powershell
# Add GitHub as your remote (replace YOUR_USERNAME with your actual GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/spring-boot-player-api.git

# Verify remote was added
git remote -v

# Push your code to GitHub
git push -u origin master
```

**First-time Git push?** You may need to authenticate:
- GitHub will prompt for credentials
- Use a Personal Access Token (PAT) instead of password
- Generate PAT: GitHub Settings → Developer settings → Personal access tokens → Generate new token

---

## Phase 2: Configure Azure Deployment Credentials

To allow GitHub Actions to deploy to your Azure App Service, you need to add your Azure credentials to GitHub.

### Step 1: Get Azure Publish Profile

```powershell
# Set your app name and resource group
$webAppName = "spring-boot-player-app-rk"
$resourceGroup = "spring-boot-app-rg"

# Download the publish profile
az webapp deployment list-publishing-profiles `
  --name $webAppName `
  --resource-group $resourceGroup `
  --xml
```

This will output XML content. **Copy the entire XML output** (it contains sensitive credentials).

### Step 2: Add Publish Profile to GitHub Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **"New repository secret"**
4. Configure:
   - **Name**: `AZURE_WEBAPP_PUBLISH_PROFILE` (must match exactly!)
   - **Value**: Paste the entire XML from Step 1
5. Click **"Add secret"**

---

## Phase 3: Verify the Workflow

### Step 1: Check Workflow File

The workflow file is already in your repository at `.github/workflows/azure-deploy.yml`. This file tells GitHub Actions what to do.

### Step 2: Trigger the Workflow

The workflow will automatically run when you push to GitHub. To trigger it:

```powershell
# Make a small change to verify it works
git add .
git commit -m "Add GitHub Actions CI/CD workflow

Co-Authored-By: Warp <agent@warp.dev>"
git push origin master
```

### Step 3: Monitor the Workflow

1. Go to your GitHub repository
2. Click the **"Actions"** tab
3. You'll see your workflow running
4. Click on the workflow run to see detailed logs

**Expected behavior:**
- ✅ **Pull Requests**: Builds and tests only (does NOT deploy)
- ✅ **Master branch push**: Builds, tests, AND deploys to Azure

---

## Phase 4: Set Up Branch Protection (Recommended)

This prevents direct pushes to master and requires PRs.

1. Go to your repository → **Settings** → **Branches**
2. Click **"Add rule"** under Branch protection rules
3. Configure:
   - **Branch name pattern**: `master`
   - ✅ **Require a pull request before merging**
   - ✅ **Require status checks to pass before merging**
     - Search for and select: `Build and Test`
   - ✅ **Require branches to be up to date before merging**
4. Click **"Create"** or **"Save changes"**

Now you must create PRs to merge to master, and the build must pass!

---

## How to Use GitHub Actions Workflow

### Normal Development Flow

```powershell
# 1. Create a feature branch
git checkout -b feature/add-new-endpoint

# 2. Make your changes, then commit
git add .
git commit -m "Add new player stats endpoint"

# 3. Push to GitHub
git push origin feature/add-new-endpoint

# 4. Create Pull Request on GitHub
# - Go to your repo, click "Pull requests" → "New pull request"
# - Select your feature branch
# - GitHub Actions will automatically build and test
# - Merge after tests pass

# 5. After merging, master will automatically deploy to Azure
```

### Direct Push to Master (if no branch protection)

```powershell
git add .
git commit -m "Fix bug in player controller"
git push origin master
# Automatically builds, tests, and deploys
```

---

## Monitoring and Troubleshooting

### View Workflow Runs

1. Repository → **Actions** tab
2. Click on any workflow run to see:
   - Build logs
   - Test results
   - Deployment status

### Common Issues

**Issue 1: Workflow fails with "Secret not found"**
- Verify you created the secret with exact name: `AZURE_WEBAPP_PUBLISH_PROFILE`
- Check Settings → Secrets and variables → Actions

**Issue 2: Deployment succeeds but app doesn't work**
- Verify environment variables in Azure App Service
- Check they match AZURE_DEPLOYMENT.md Step 6
- View logs: `az webapp log tail --name spring-boot-player-app-rk --resource-group spring-boot-app-rg`

**Issue 3: Tests fail in GitHub Actions but pass locally**
- Check the test logs in Actions tab
- Ensure tests don't depend on local-only resources
- Verify H2 database is used for tests

**Issue 4: Can't push to GitHub - Authentication failed**
- Generate Personal Access Token (PAT)
- GitHub Settings → Developer settings → Personal access tokens
- Use PAT as password when Git prompts for credentials

---

## Workflow Features Explained

### Triggers

```yaml
on:
  push:
    branches: [master, main]
  pull_request:
    branches: [master, main]
```
- Runs on every push to master/main
- Runs on every pull request to master/main

### Jobs

**Build Job**: Always runs
- Checks out code
- Sets up Java 11
- Runs Maven build and tests
- Uploads JAR as artifact

**Deploy Job**: Only runs on master/main pushes
- Downloads the JAR artifact
- Deploys to Azure App Service
- Runs health check

### Conditional Deployment

```yaml
if: github.event_name == 'push' && (github.ref == 'refs/heads/master' || github.ref == 'refs/heads/main')
```
- PRs only build and test
- Only master/main pushes trigger deployment

---

## Improving Your GitHub Profile for Job Search

### 1. Add a Great README

Create a detailed README.md with:
- Project description
- Technologies used (Spring Boot, Azure SQL, JPA, Maven, etc.)
- Setup instructions
- API documentation
- Screenshots or demo link

### 2. Add Topics to Your Repository

1. Go to repository main page
2. Click the ⚙️ gear icon next to "About"
3. Add topics: `spring-boot`, `java`, `azure`, `rest-api`, `ci-cd`, `github-actions`

### 3. Pin the Repository

1. Go to your GitHub profile
2. Click "Customize your pins"
3. Select this repository to feature it prominently

### 4. Keep It Active

- Make regular commits (even small improvements)
- Use meaningful commit messages
- Respond to issues (if others find your project)

---

## Next Steps

### 1. Add More Tests

```powershell
# Add JaCoCo for code coverage reporting
```

Add to `pom.xml`:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 2. Add Status Badges to README

After your first successful workflow run:

```markdown
![Build Status](https://github.com/YOUR_USERNAME/spring-boot-player-api/workflows/Build%20and%20Deploy%20to%20Azure/badge.svg)
```

### 3. Add Multiple Environments

Create separate workflows for:
- Development environment (auto-deploy on develop branch)
- Staging environment (auto-deploy on staging branch)
- Production environment (manual approval required)

### 4. Add Deployment Slots

Use Azure App Service deployment slots for zero-downtime deployments:
```powershell
# Create a staging slot
az webapp deployment slot create `
  --name spring-boot-player-app-rk `
  --resource-group spring-boot-app-rg `
  --slot staging
```

---

## Cost

- **GitHub**: Free for public repositories
- **GitHub Actions**: 2,000 minutes/month free for private repos, unlimited for public
- **This workflow**: ~3-5 minutes per run

---

## Useful Commands

```powershell
# View recent workflow runs (requires GitHub CLI)
gh run list

# View logs for latest run
gh run view --log

# Manually trigger workflow
gh workflow run azure-deploy.yml

# Check workflow status
gh run watch
```

---

## Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Azure Web Apps Deploy Action](https://github.com/Azure/webapps-deploy)
- [Building Java with Maven](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven)
- [GitHub Actions Best Practices](https://docs.github.com/en/actions/security-guides/security-hardening-for-github-actions)

---

## Security Notes

- ✅ Publish profile is stored as encrypted GitHub Secret
- ✅ Secrets are never exposed in logs
- ✅ Publish profile can be regenerated if compromised
- ⚠️ Consider rotating publish profiles periodically
- ⚠️ Never commit credentials to your repository

---

## Workflow vs Azure DevOps

You chose GitHub Actions - here's why that's great:

| Feature | GitHub Actions | Azure DevOps |
|---------|---------------|--------------|
| **Visibility to recruiters** | ⭐⭐⭐⭐⭐ | ⭐⭐ |
| **Industry adoption** | Very high | Microsoft shops |
| **Setup complexity** | Simple | More complex |
| **Integration** | Native to GitHub | Requires separate platform |
| **Cost (public repos)** | Free, unlimited | Free, limited minutes |
| **Resume value** | High | Medium |

You made the right choice for job searching! 🎯
