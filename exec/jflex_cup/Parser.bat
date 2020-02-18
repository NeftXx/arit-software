@echo off

REM Tener agregado Cup en variables de entorno

cd ..
cd ..

set PATH_PARSER=src\com\neftxx\interpreter\jflex_cup

set OPTIONS_PARSER=-destdir %PATH_PARSER% -locations -interface -parser Parser -symbols Sym
set PARSER_FILE=%PATH_PARSER%\Parser.cup

color 0A
echo "Ejecutando CUP"
call java_cup %OPTIONS_PARSER% %PARSER_FILE%
echo.
pause
exit