@echo off

REM Tener agregado JFlex en variables de entorno

cd ..
cd ..

set PATH_LEXER=src\com\neftxx\interpreter\jflex_cup

set OPTIONS_LEXER=-d %PATH_LEXER%
set LEXER_FILE=%PATH_LEXER%\Lexer.jflex

color 0A
echo "Ejecutando JFLEX"
call jflex %OPTIONS_LEXER% %LEXER_FILE%
echo.
pause
exit