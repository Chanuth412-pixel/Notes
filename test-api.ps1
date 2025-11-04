# Test script for Notes API
Write-Host "Testing Notes API..." -ForegroundColor Green

# Test health endpoint
Write-Host "`nTesting /health endpoint..." -ForegroundColor Yellow
try {
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8080/health" -Method Get
    Write-Host "Health check successful:" -ForegroundColor Green
    $healthResponse | ConvertTo-Json
} catch {
    Write-Host "Health check failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test getting all notes
Write-Host "`nTesting GET /api/notes..." -ForegroundColor Yellow
try {
    $notesResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Get
    Write-Host "GET /api/notes successful:" -ForegroundColor Green
    $notesResponse | ConvertTo-Json
} catch {
    Write-Host "GET /api/notes failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test creating a note
Write-Host "`nTesting POST /api/notes..." -ForegroundColor Yellow
$noteData = @{
    title = "Test Note"
    content = "This is a test note from PowerShell"
    category = $null
    tags = @()
} | ConvertTo-Json

try {
    $createResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Post -Body $noteData -ContentType "application/json"
    Write-Host "POST /api/notes successful:" -ForegroundColor Green
    $createResponse | ConvertTo-Json
} catch {
    Write-Host "POST /api/notes failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
}

Write-Host "`nAPI testing completed!" -ForegroundColor Green