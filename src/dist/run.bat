@echo off
cd /d %~dp0
call bin\sysapi --spring.config.location=classpath:/config/application.properties,optional:file:config/application.properties
pause