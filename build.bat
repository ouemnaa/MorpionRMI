@echo off
REM Créer le répertoire de sortie
mkdir "out\production\MorpionRMI" 2>nul

REM Compiler les sources
javac -d "out\production\MorpionRMI" src\shared\*.java src\server\*.java src\client\*.java

REM Copier les classes partagées pour le chargement dynamique
mkdir "web\classes" 2>nul
copy "out\production\MorpionRMI\shared\*.class" "web\classes\" >nul
copy "out\production\MorpionRMI\client\GameClient.class" "web\classes\" >nul
copy "out\production\MorpionRMI\client\GameClientImpl.class" "web\classes\" >nul

echo Compilation terminée. Les classes sont prêtes dans out\production\MorpionRMI
pause