# Test Note Creation to Reproduce Error
Write-Host "Testing Note Creation to Reproduce Error" -ForegroundColor Yellow

Start-Sleep -Seconds 6

try {
    Write-Host "Creating a test note..." -ForegroundColor Cyan
    
    # Test with the exact structure your frontend is sending
    $noteJson = @{
        title = "Test Note from PowerShell"
        content = "This is a test note to reproduce the error"
        category = $null
        tags = @()
    } | ConvertTo-Json
    
    Write-Host "Sending payload:" -ForegroundColor Gray
    Write-Host $noteJson -ForegroundColor Gray
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Post -Body $noteJson -ContentType "application/json" -TimeoutSec 10
    
    Write-Host "✅ Note created successfully!" -ForegroundColor Green
    Write-Host "Created note: $($response.title) with ID: $($response.id)" -ForegroundColor Green
    
} catch {
    Write-Host "❌ Error creating note:" -ForegroundColor Red
    Write-Host "Error Message: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
        
        # Try to get the response body for more details
        try {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
            Write-Host "Response Body: $responseBody" -ForegroundColor Red
        } catch {
            Write-Host "Could not read response body" -ForegroundColor Red
        }
    }
}