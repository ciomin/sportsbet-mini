# run-dev.ps1
$ErrorActionPreference = "Stop"

# path to .env next to this script
$envFile = Join-Path $PSScriptRoot ".env"

if (-not (Test-Path $envFile)) {
  throw "Missing .env file at $envFile"
}

# load key=value pairs (ignore comments/blank lines), trim quotes/CRLF
Get-Content $envFile | ForEach-Object {
  $line = $_.Trim()
  if ($line -and -not $line.StartsWith("#")) {
    $kv = $line -split "=", 2
    if ($kv.Length -eq 2) {
      $key = $kv[0].Trim()
      $val = $kv[1].Trim()            # trim spaces
      $val = $val.Trim('"')           # strip optional quotes
      $val = $val.Trim("`r","`n")     # strip CRLF just in case
      Set-Item -Path "Env:$key" -Value $val
    }
  }
}

# optional: confirm a couple of vars
Write-Host "SPRING_PROFILES_ACTIVE = $($Env:SPRING_PROFILES_ACTIVE)"
Write-Host "DB_URL = $($Env:DB_URL)"
Write-Host "KAFKA_BOOTSTRAP_SERVERS = $($Env:KAFKA_BOOTSTRAP_SERVERS)"

# Run from the betservice folder (adjust if your mvnw is elsewhere)
Push-Location (Join-Path $PSScriptRoot "betservice")
./mvnw spring-boot:run
Pop-Location
