@echo off
REM Configurer le classpath
set CLASSPATH=out\production\MorpionRMI

REM Lancer le serveur
java -cp "out" -D"java.rmi.server.codebase=http://localhost/classes/" -D"java.security.policy=policies/server.policy" server.GameServer
pause