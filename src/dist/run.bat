@echo off
cd /d %~dp0
call bin\sysapi --spring.config.location=optional:file:config/application.properties,classpath:/config/application.properties
pause