param()
$patterns = @('DB_PASSWORD=','API_TOKEN=','HMAC_SECRET=')
Get-ChildItem -Recurse -File | ForEach-Object {
  $t = Get-Content $_.FullName -ErrorAction SilentlyContinue
  foreach($p in $patterns){ if($t -match $p){ Write-Output "警告: $($_.FullName) 包含敏感键" } }
}
