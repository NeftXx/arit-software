@echo off

REM Tener agregado JAVACC en variables de entorno

cd ..
cd ..

set PATH_GRAMMAR=src\com\neftxx\interpreter\javacc

cd %PATH_GRAMMAR%

set GRAMMAR_FILE=Grammar.jj

color 0A
echo "Ejecutando JAVACC"
call javacc %GRAMMAR_FILE%
echo.
pause
exit