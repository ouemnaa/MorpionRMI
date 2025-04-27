@echo off
REM Configurer le classpath
set CLASSPATH=out\production\MorpionRMI

REM Lancer le client
java -cp %CLASSPATH% -Djava.rmi.server.codebase=http://localhost:8000/classes/ -Djava.security.policy=policies\client.policy client.ClientLauncher
pause