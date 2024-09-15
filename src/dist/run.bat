@echo off
cd /d %~dp0
call bin\sysapi --spring.config.location=file:config/application.properties
pause