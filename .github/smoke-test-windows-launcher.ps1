# Starts the agent from the app folder the Windows installer build left behind, the same way the
# service does, and fails unless the launcher picks the native runtime, the agent becomes healthy
# and it exits on its own once the launcher is killed.
#
# usage: smoke-test-windows-launcher.ps1 [build-dir]
param([string] $BuildDir = 'build')

$timeoutSeconds = 180
$shutdownSeconds = 60

# javapackager stages the app folder somewhere under build. The launcher is also copied to
# build\launcher, which is not an app folder, so that one is skipped.
$app = (Get-ChildItem $BuildDir -Recurse -Filter sysapi-launcher.ps1 |
    Where-Object { $_.DirectoryName -notlike '*launcher*' } |
    Select-Object -First 1).DirectoryName
if (-not $app) {
    Write-Output "No app folder with sysapi-launcher.ps1 under $BuildDir"
    exit 1
}
Write-Output "App folder: $app"
Push-Location $app

# Fails fast with a readable list of what is missing, rather than a launcher that falls back to
# the JVM and leaves us guessing why.
& native\windows-amd64\sysapi.exe --native-self-test
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$launcherLog = Join-Path $app 'launcher.log'
$launcherErr = Join-Path $app 'launcher.err'
$launcher = Start-Process powershell `
    -ArgumentList '-NoProfile', '-ExecutionPolicy', 'Bypass', '-File', (Join-Path $app 'sysapi-launcher.ps1') `
    -WorkingDirectory $app `
    -RedirectStandardOutput $launcherLog `
    -RedirectStandardError $launcherErr `
    -PassThru -NoNewWindow

# Wait for the health check, giving up early if the launcher dies.
$deadline = (Get-Date).AddSeconds($timeoutSeconds)
$healthy = $false
while (-not $healthy -and (Get-Date) -lt $deadline -and -not $launcher.HasExited) {
    try {
        Invoke-RestMethod http://localhost:8080/actuator/health -TimeoutSec 5 | Out-Null
        $healthy = $true
    } catch {
        Start-Sleep -Seconds 2
    }
}

Get-Content $launcherLog | Select-String '\[launcher\]'
if (-not $healthy) {
    Write-Output "The agent did not become healthy within ${timeoutSeconds}s."
    Get-Content $launcherLog, $launcherErr
    exit 1
}
if (-not (Select-String -Path $launcherLog -Pattern '\[launcher\] runtime=native\b' -Quiet)) {
    Write-Output 'The launcher did not pick the native runtime.'
    exit 1
}

# The service wrapper kills the launcher outright when it stops the service, so the agent has
# to notice its launcher is gone and exit by itself instead of lingering.
Stop-Process -Id $launcher.Id -Force
$killed = Get-Date
do {
    Start-Sleep -Seconds 1
    $left = Get-Process | Where-Object { $_.Path -like "$app\*" }
} while ($left -and ((Get-Date) - $killed).TotalSeconds -lt $shutdownSeconds)

Get-Content $launcherLog -Tail 25
if ($left) {
    Write-Output "Still running ${shutdownSeconds}s after the launcher was killed: $($left.Path)"
    $left | Stop-Process -Force
    exit 1
}
Write-Output "The agent exited $([int]((Get-Date) - $killed).TotalSeconds)s after the launcher was killed."
