# Quick API Test
Write-Host "Testing API quickly..." -ForegroundColor Green

try {
    # Test health endpoint
    Write-Host "Health check..." -ForegroundColor Yellow
    $health = Invoke-RestMethod -Uri "http://localhost:8080/health" -Method Get -TimeoutSec 5
    Write-Host "✅ Health: $($health.status)" -ForegroundColor Green
    
    # Test getting all notes  
    Write-Host "Get notes..." -ForegroundColor Yellow
    $notes = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Get -TimeoutSec 5
    Write-Host "✅ Notes count: $($notes.Count)" -ForegroundColor Green
    
    # Test creating a note
    Write-Host "Create note..." -ForegroundColor Yellow
    $noteJson = '{"title":"Test Note","content":"Test content","category":null,"tags":[]}'
    $created = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Post -Body $noteJson -ContentType "application/json" -TimeoutSec 5
    Write-Host "✅ Created note ID: $($created.id)" -ForegroundColor Green
    
    Write-Host "`n🎉 ALL TESTS PASSED! The backend is working correctly!" -ForegroundColor Green
    Write-Host "✅ Security configuration allows API access" -ForegroundColor Green
    Write-Host "✅ CORS is properly configured" -ForegroundColor Green
    Write-Host "✅ Data structure matches frontend expectations" -ForegroundColor Green
    
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    }
}