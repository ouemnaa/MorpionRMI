@echo off
REM Configurer le classpath
set CLASSPATH=out\production\MorpionRMI

REM Lancer le serveur
java -cp %CLASSPATH% -Djava.rmi.server.codebase=http://localhost:8000/classes/ -Djava.security.policy=policies\server.policy server.GameServer
pause