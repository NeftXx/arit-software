/*****************************************************************
 * Lexer.java
 *
 * Copyright ©2020 Ronald Berdúo. All Rights Reserved.
 * This software is the proprietary information of Ronald Berdúo.
 *
 *****************************************************************/
package com.neftxx.interpreter.jflex_cup;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.util.NodeInfo;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author Ronald Berdúo
 */

%%

%class Lexer
%cupsym Sym
%public
%unicode
%line
%column
%cup
%char
%ignorecase

%{
    /**
     * Guarda el texto de las cadenas
     */
    private StringBuilder string;

    /**
     * Creador de simbolos complejos
     */
    private ComplexSymbolFactory symbolFactory;

    /**
     * analizador
     */
    private AritLanguage aritLanguage;

    /**
     * Constructor del analizador lexico
     *
     * @param in Entrada que se va analizar
     * @param symbolFactory creador de simbolos complejos
     */
    public Lexer(java.io.Reader in, ComplexSymbolFactory symbolFactory, AritLanguage aritLanguage) {
	    this(in);
        string = new StringBuilder();
	    this.symbolFactory = symbolFactory;
        this.aritLanguage = aritLanguage;
    }

    /**
     * Metodo que devuelve un nuevo java_cup.runtime.Symbol
     *
     * @param name nombre que recibira el simbolo
     * @param sym numero de token
     * @param value valor que recibira el simbolo
     * @param buflength tam del valor
     * @return java_cup.runtime.Symbol
     */
    private Symbol symbol(String name, int sym, Object value, int buflength) {
        Location left = new Location(yyline + 1, yycolumn + yylength() - buflength, yychar + yylength() - buflength);
        Location right= new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name, sym, left, right, value);
    }

    /**
     * Metodo que devuelve un nuevo java_cup.runtime.Symbol
     *
     * @param name nombre que recibira el simbolo
     * @param sym numero de token
     * @return java_cup.runtime.Symbol
     */
    private Symbol symbol(String name, int sym) {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right= new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name, sym, left, right);
    }

    /**
     * Devuelve un nuevo java_cup.runtime.Symbol
     *
     * @param name nombre que recibira el simbolo
     * @param sym numero de token
     * @param val valor que recibira el simbolo
     * @return java_cup.runtime.Symbol
     */
    private Symbol symbol(String name, int sym, Object val) {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right= new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name, sym, left, right, val);
    }

    /**
     * Guarda los errores en el manejador
     *
     * @param message mensaje del error
     */
    private void error(String message) {
        aritLanguage.addLexicalError(message, new NodeInfo(yyline + 1, yycolumn + 1, aritLanguage.filename));
    }
%}

%eofval{
    return symbolFactory.newSymbol(
        "EOF", Sym.EOF,
        new Location(yyline + 1, yycolumn + 1, yychar),
        new Location(yyline + 1, yycolumn + 1, yychar + 1)
    );
%eofval}

/* Definición léxica */
Digito          = [0-9]
Letra           = [a-zA-Z]

/**
 *  Finalización de línea:
 *
 *  Carácter salto de línea (LF ASCII)
 *  Carácter retorno de carro (CR ASCII)
 *  Carácter retorno de carro (CR ASCII) seguido de carácter salto de línea (LF ASCII)
 */
Fin_linea        = \r|\n|\r\n

/**
 *  Espacios en blanco:
 *
 *  Espacio (SP ASCII)
 *  Tabulación horizontal (HT ASCII)
 *  Caracteres de finalización de línea
 */
Espacios        = {Fin_linea} | [ \t\f]

/* Identificadores */
Identificador   = ((["."]({Letra}|["_"]|["."]))|{Letra})({Letra}|{Digito}|["_"]|["."])*

/* literales */
Integer_Literal = {Digito}+
Decimal_Literal = {Digito}+["."]{Digito}+
Boolean_Literal = true|false
Null_Literal    = null

/* Estados */

/**
 *  Comentarios:
 *
 *  Los comentarios de una línea que serán delimitados al inicio con los símbolos //
 *  y al final con un carácter de finalización de línea.
 */
%state COMENTARIO_DE_FIN_DE_LINEA

/* Comentarios múltiples */
%state COMENTARIO_TRADICIONAL

/**
 * string: """ <Caracteres ASCII> """
 */
%state CADENA

%%

<YYINITIAL> {
    /* palabras claves */
    "continue"          { return symbol("`continue`", Sym.CONTINUE); }
    "function"          { return symbol("`function`", Sym.FUNCTION); }
    "default"           { return symbol("`default`", Sym.DEFAULT); }
    "return"            { return symbol("`return`", Sym.RETURN); }
    "switch"            { return symbol("`switch`", Sym.SWITCH); }
    "break"             { return symbol("`break`", Sym.BREAK); }
    "while"             { return symbol("`while`", Sym.WHILE); }
    "case"              { return symbol("`case`", Sym.CASE); }
    "else"              { return symbol("`else`", Sym.ELSE); }
    "for"               { return symbol("`for`", Sym.FOR); }
    "in"                { return symbol("`in`", Sym.IN); }
    "do"                { return symbol("`do`", Sym.DO); }
    "if"                { return symbol("`if`", Sym.IF); }


    /* literales */
    {Integer_Literal}   {
                            int valor = 0;
                            try {
                                valor = Integer.parseInt(yytext());
                            } catch (NumberFormatException ex) {
                                error("Error: Numero "+ yytext () +" fuera del rango de un integer.");
                            }
                            return symbol("`Integer Literal`", Sym.LIT_ENTERO, valor);
                        }

    {Decimal_Literal}   {
                            double valor = 0;
                            try {
                                valor = Double.parseDouble(yytext());
                            } catch (NumberFormatException ex) {
                                error("Error: "+ yytext () +" fuera del rango de un decimal.");
                            }
                            return symbol("`Decimal Literal`", Sym.LIT_DECIMAL, valor);
                        }

    {Boolean_Literal}   { return symbol(yytext(), Sym.LIT_BOOLEANO, Boolean.parseBoolean(yytext())); }

    {Null_Literal}      { return symbol("null", Sym.NULL); }

    \"                  { string.setLength(0); yybegin(CADENA); }

    /* nombres */
    {Identificador}     { return symbol("`Identifier: '" + yytext() + "'`", Sym.ID, yytext().toLowerCase()); }

    /* separadores */
    "=>"                { return symbol("`=>`", Sym.LAMBDA);              }
    "=="                { return symbol("`==`", Sym.IGUAL_QUE);           }
    "!="                { return symbol("`!=`", Sym.DIFERENTE_QUE);       }
    ">="                { return symbol("`>=`", Sym.MAYOR_IGUAL_QUE);     }
    "<="                { return symbol("`<=`", Sym.MENOR_IGUAL_QUE);     }
    "%%"                { return symbol("`%`",  Sym.MODULO);              }
    "+"                 { return symbol("`+`",  Sym.MAS);                 }
    "-"                 { return symbol("`-`",  Sym.MENOS);               }
    "*"                 { return symbol("`*`",  Sym.MULT);                }
    "/"                 { return symbol("`/`",  Sym.DIV);                 }
    "^"                 { return symbol("`^`",  Sym.POTENCIA);            }
    "="                 { return symbol("`=`",  Sym.IGUAL);               }
    ">"                 { return symbol("`>`",  Sym.MAYOR_QUE);           }
    "<"                 { return symbol("`<`",  Sym.MENOR_QUE);           }
    "?"                 { return symbol("`?`",  Sym.INTERROGANTE);        }
    ":"                 { return symbol("`:`",  Sym.DOS_PUNTOS);          }
    "|"                 { return symbol("`|`",  Sym.OR);                  }
    "&"                 { return symbol("`&`",  Sym.AND);                 }
    "!"                 { return symbol("`!`",  Sym.NOT);                 }
    "("                 { return symbol("`(`",  Sym.PAR_IZQ);             }
    ")"                 { return symbol("`)`",  Sym.PAR_DER);             }
    "["                 { return symbol("`[`",  Sym.COR_IZQ);             }
    "]"                 { return symbol("`]`",  Sym.COR_DER);             }
    ";"                 { return symbol("`;`",  Sym.PUNTO_COMA);          }
    ","                 { return symbol("`,`",  Sym.COMA);                }
    "{"                 { return symbol("`{`",  Sym.LLAVE_IZQ);           }
    "}"                 { return symbol("`}`",  Sym.LLAVE_DER);           }

    /* espacios en blanco */
    {Espacios}          { /* IGNORAR ESPACIOS */ }

    /* comentarios */
    "#*"                { yybegin(COMENTARIO_TRADICIONAL); }
    "#"                 { yybegin(COMENTARIO_DE_FIN_DE_LINEA); }
}

<COMENTARIO_TRADICIONAL> {
    "*#"                { yybegin(YYINITIAL); }
    <<EOF>>             { yybegin(YYINITIAL); }
    [^]                 { /* IGNORAR CUALQUIER COSA */ }
}

<COMENTARIO_DE_FIN_DE_LINEA> {
    {Fin_linea}             { yybegin(YYINITIAL); }
    .                       { /* IGNORAR */  }
}

<CADENA> {
    /* Fin de cadena */
    \"                  {
                            yybegin(YYINITIAL);
                            return symbol("`String Literal`", Sym.LIT_STRING, string.toString(), string.length());
                        }

    /* Secuencias de escape */
    "\\\""              { string.append('\"'); }
    "\\\\"              { string.append('\\'); }
    "\\n"               { string.append('\n'); }
    "\\r"               { string.append('\r'); }
    "\\t"               { string.append('\t'); }
    \\.                 { error("Error: no se esperaba el escape \\." + yytext()); }

    {Fin_linea}         {
                            yybegin(YYINITIAL);
                            error("Error: final de linea inesperado.");
                        }

    [^\r\n\"\\]+        { string.append(yytext()); }
}

/* Cualquier cosa que no coincida con lo de arriba es un error. */
[^] {
	error("Error: Carácter no valido '"+ yytext () +"'.");
}