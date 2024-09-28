@echo off
cd /d %~dp0
echo "Installing sys-API service"
call sys-API-servicew.exe install
echo "Starting sys-API service"
call sys-API-servicew.exe start
echo "Done. You can close this window. Check .log files."
pause