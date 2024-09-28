@echo off
cd /d %~dp0
echo "Stopping sys-API service"
call sys-API-servicew.exe stop
echo "Done. You can close this window. Check .log files."
pause