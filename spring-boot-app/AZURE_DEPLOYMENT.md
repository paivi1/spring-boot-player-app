# Azure Deployment Guide

This guide walks you through deploying your Spring Boot application to Azure App Service with Azure SQL Database.

## Prerequisites

1. **Azure Account**: Sign up at https://azure.microsoft.com/free/
2. **Azure CLI**: Install from https://docs.microsoft.com/cli/azure/install-azure-cli
3. **Java 11**: Ensure Java 11 is installed
4. **Maven**: Verify Maven is installed

## Step 1: Install and Configure Azure CLI

```powershell
# Login to Azure
az login

# Set your subscription (if you have multiple)
az account list --output table
az account set --subscription "YOUR_SUBSCRIPTION_ID"
```

## Step 2: Create Azure SQL Database

```powershell
# Set variables (customize these)
$resourceGroup = "spring-boot-app-rg"
$location = "canadacentral"
$sqlServerName = "spring-boot-sql-server-SQLServerRK"  # Must be globally unique
$sqlDbName = "playerdb"
$adminUser = "sqladmin"
$adminPassword = "Elemenopee2000!!!"  # Use a strong password

# Create resource group
az group create --name $resourceGroup --location $location

# Create SQL Server
az sql server create `
  --name $sqlServerName `
  --resource-group $resourceGroup `
  --location $location `
  --admin-user $adminUser `
  --admin-password $adminPassword

# Create SQL Database
az sql db create `
  --resource-group $resourceGroup `
  --server $sqlServerName `
  --name $sqlDbName `
  --service-objective S0

# Configure firewall to allow Azure services
az sql server firewall-rule create `
  --resource-group $resourceGroup `
  --server $sqlServerName `
  --name AllowAzureServices `
  --start-ip-address 0.0.0.0 `
  --end-ip-address 0.0.0.0

# Get your SQL connection string
$connectionString = "jdbc:sqlserver://$sqlServerName.database.windows.net:1433;database=$sqlDbName;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

Write-Host "Connection String: $connectionString"
Write-Host "Username: $adminUser"
Write-Host "Password: $adminPassword"
```

## Step 3: Build Your Application

```powershell
# Build the application
mvn clean package

# Verify the JAR was created
Get-ChildItem target\*.jar
```

## Step 4: Configure Azure App Service

Update the `pom.xml` configuration if needed:
- Change `<appName>` to a unique name (currently: spring-boot-player-app)
- Change `<resourceGroup>` if you used a different name
- Change `<region>` if you prefer a different Azure region

## Step 5: Deploy to Azure App Service

```powershell
# Deploy the application
mvn azure-webapp:deploy
```

This command will:
1. Create the App Service if it doesn't exist
2. Upload your application JAR
3. Start the application

## Step 6: Configure Environment Variables in Azure

After deployment, set the database connection environment variables:

```powershell
$webAppName = "spring-boot-player-app-rk"  # Your app name from pom.xml
$resourceGroup = "spring-boot-app-rg"

# Set environment variables
az webapp config appsettings set `
  --resource-group $resourceGroup `
  --name $webAppName `
  --settings `
    AZURE_SQL_URL="jdbc:sqlserver://$sqlServerName.database.windows.net:1433;database=$sqlDbName;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" `
    AZURE_SQL_USERNAME="$adminUser" `
    AZURE_SQL_PASSWORD="$adminPassword" `
    SPRING_PROFILES_ACTIVE="prod"
```

## Step 7: Restart the Application

```powershell
az webapp restart --name $webAppName --resource-group $resourceGroup
```

## Step 8: Verify Deployment

```powershell
# Get the application URL
$appUrl = az webapp show --name $webAppName --resource-group $resourceGroup --query defaultHostName --output tsv
Write-Host "Your application is running at: https://$appUrl"

# Test the application
Start-Process "https://$appUrl"
```

Test your API endpoints:
- `https://your-app-name.azurewebsites.net/api/players` (GET all players)
- `https://your-app-name.azurewebsites.net/api/players` (POST to create)

## Step 9: Monitor Your Application

```powershell
# View logs
az webapp log tail --name $webAppName --resource-group $resourceGroup

# Enable Application Insights (optional but recommended)
az monitor app-insights component create `
  --app $webAppName `
  --location $location `
  --resource-group $resourceGroup
```

## Troubleshooting

### View Application Logs
```powershell
# Stream logs
az webapp log tail --name $webAppName --resource-group $resourceGroup

# Download logs
az webapp log download --name $webAppName --resource-group $resourceGroup
```

### Common Issues

1. **Database Connection Failed**
   - Verify firewall rules allow Azure services
   - Check environment variables are set correctly
   - Ensure SQL credentials are correct

2. **Application Won't Start**
   - Check logs: `az webapp log tail`
   - Verify Java 11 runtime is configured
   - Ensure the JAR file was built successfully

3. **Can't Access SQL Server**
   - Add your local IP to SQL Server firewall:
   ```powershell
   az sql server firewall-rule create `
     --resource-group $resourceGroup `
     --server $sqlServerName `
     --name AllowMyIP `
     --start-ip-address YOUR_IP `
     --end-ip-address YOUR_IP
   ```

## Cost Management

- **App Service B1**: ~$13/month
- **Azure SQL S0**: ~$15/month
- **Total**: ~$28/month

To stop resources when not in use:
```powershell
# Stop the web app (still billed at reduced rate)
az webapp stop --name $webAppName --resource-group $resourceGroup

# Delete everything (no charges)
az group delete --name $resourceGroup --yes
```

## Updating Your Application

To deploy updates:

```powershell
# Make your code changes
# Build the application
mvn clean package

# Deploy
mvn azure-webapp:deploy

# Restart (optional, usually automatic)
az webapp restart --name $webAppName --resource-group $resourceGroup
```

## Next Steps

1. **Custom Domain**: Configure a custom domain name
2. **SSL Certificate**: Add SSL for custom domains
3. **CI/CD**: Set up GitHub Actions or Azure DevOps for automated deployments
4. **Scaling**: Configure auto-scaling based on demand
5. **Backup**: Enable database backup and restore

## Useful Commands

```powershell
# Check app status
az webapp show --name $webAppName --resource-group $resourceGroup

# View configuration
az webapp config appsettings list --name $webAppName --resource-group $resourceGroup

# Scale up/down
az webapp update --name $webAppName --resource-group $resourceGroup --set tags.environment=production

# Get connection string
az sql db show-connection-string --client jdbc --name $sqlDbName --server $sqlServerName
```

## Resources

- [Azure App Service Documentation](https://docs.microsoft.com/azure/app-service/)
- [Azure SQL Database Documentation](https://docs.microsoft.com/azure/azure-sql/)
- [Azure Maven Plugin](https://github.com/microsoft/azure-maven-plugins)
