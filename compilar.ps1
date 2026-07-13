$ErrorActionPreference = "Stop"
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force out | Out-Null
$archivos = Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $archivos
Write-Host "Compilación completada. Ejecuta: java -cp out controlador.Main"
