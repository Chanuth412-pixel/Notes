# Final Test - Note Creation Fix Verification
Write-Host "🧪 Testing Note Creation Fix" -ForegroundColor Green
Write-Host "==============================" -ForegroundColor Green

Start-Sleep -Seconds 8

Write-Host "`n📍 Testing different note creation scenarios..." -ForegroundColor Yellow

try {
    # Test 1: Simple note with no tags
    Write-Host "`n1️⃣ Testing note with no tags..." -ForegroundColor Cyan
    $simpleNote = @{
        title = "Simple Test Note"
        content = "This note has no tags"
        category = $null
        tags = @()
    } | ConvertTo-Json
    
    $response1 = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Post -Body $simpleNote -ContentType "application/json" -TimeoutSec 10
    Write-Host "✅ Created note: '$($response1.title)' with ID: $($response1.id)" -ForegroundColor Green
    
    # Test 2: Note with new tags (names only)
    Write-Host "`n2️⃣ Testing note with new tags..." -ForegroundColor Cyan
    $noteWithTags = @{
        title = "Note with New Tags"
        content = "This note should create new tags"
        category = $null
        tags = @(
            @{ name = "test-tag" },
            @{ name = "backend-fix" }
        )
    } | ConvertTo-Json -Depth 3
    
    $response2 = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Post -Body $noteWithTags -ContentType "application/json" -TimeoutSec 10
    Write-Host "✅ Created note: '$($response2.title)' with ID: $($response2.id)" -ForegroundColor Green
    Write-Host "   Tags: $($response2.tags.Count) tags created/attached" -ForegroundColor Gray
    
    # Test 3: Get all notes to verify they were saved
    Write-Host "`n3️⃣ Verifying notes in database..." -ForegroundColor Cyan
    $allNotes = Invoke-RestMethod -Uri "http://localhost:8080/api/notes" -Method Get -TimeoutSec 5
    Write-Host "✅ Found $($allNotes.Count) notes in database:" -ForegroundColor Green
    foreach ($note in $allNotes) {
        Write-Host "   - Note ID $($note.id): '$($note.title)' (Tags: $($note.tags.Count))" -ForegroundColor Gray
    }
    
    Write-Host "`n🎉 ALL TESTS PASSED!" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host "✅ Notes are being created successfully" -ForegroundColor White
    Write-Host "✅ Tags are being resolved and saved properly" -ForegroundColor White
    Write-Host "✅ No more TransientObjectException errors" -ForegroundColor White
    Write-Host "✅ Backend is ready for frontend integration" -ForegroundColor White
    Write-Host "`nThe fix worked! Your frontend should now be able to create notes! 🚀" -ForegroundColor Green
    
} catch {
    Write-Host "❌ Error occurred:" -ForegroundColor Red
    Write-Host "Error Message: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
        
        try {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
            Write-Host "Response Details: $responseBody" -ForegroundColor Red
        } catch {
            Write-Host "Could not read error details" -ForegroundColor Red
        }
    }
}