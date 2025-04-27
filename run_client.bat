@echo off
REM Configurer le classpath
set CLASSPATH=out

REM Lancer le client
java -cp "out" -D"java.rmi.server.codebase=http://localhost/classes/" -D"java.security.policy=policies/client.policy" client.ClientLauncher
pause

