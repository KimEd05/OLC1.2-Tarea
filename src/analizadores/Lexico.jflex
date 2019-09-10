package analizadores;

import java_cup.runtime.Symbol;
%% 

%{
    
%}

/* Directivas */
%cupsym Simbolos
%class Scanner
%cup
%public
%full
%8bit
%unicode
%line
%column
%char
%ignorecase

/* Expresiones regulares */
blanco = [ |\t|\n|\f|\r]+
booleano = "true"|"false"
apostrofe = [\\][\']
caracter = [\']([^\']|[\\][^\']|{apostrofe})?[\']
comilla = [\\][\"""]
cadena = "\""([^"\""]|{comilla})*"\""
entero = "-"?[0-9]+
doble = "-"?[0-9]+"."[0-9]+
identificador = [A-Za-zÑñ]+["_"0-9A-Za-zÑñ]*
comentario = "//"[^\r\n]*[\r|\n|\r\n|\n\r]?
comentarioMulti = "/*" ~"*/"

%%

/* Caracteres */
"{"                 { return new Symbol(Simbolos.llaa, yyline, yycolumn, yytext()); }
"}"                 { return new Symbol(Simbolos.llac, yyline, yycolumn, yytext()); }
"("                 { return new Symbol(Simbolos.para, yyline, yycolumn, yytext()); }
")"                 { return new Symbol(Simbolos.parc, yyline, yycolumn, yytext()); }
"="                 { return new Symbol(Simbolos.igual, yyline, yycolumn, yytext()); }
";"                 { return new Symbol(Simbolos.puntoycoma, yyline, yycolumn, yytext()); }
"+"                 { return new Symbol(Simbolos.mas, yyline, yycolumn, yytext()); }
"-"                 { return new Symbol(Simbolos.menos, yyline, yycolumn, yytext()); }
"*"                 { return new Symbol(Simbolos.por, yyline, yycolumn, yytext()); }
"/"                 { return new Symbol(Simbolos.dividido, yyline, yycolumn, yytext()); }
"=="                { return new Symbol(Simbolos.igualdad, yyline, yycolumn, yytext()); }
"!="                { return new Symbol(Simbolos.desigualdad, yyline, yycolumn, yytext()); }
">"                 { return new Symbol(Simbolos.mayor, yyline, yycolumn, yytext()); }
"<"                 { return new Symbol(Simbolos.menor, yyline, yycolumn, yytext()); }
">="                { return new Symbol(Simbolos.mayorigual, yyline, yycolumn, yytext()); }
"<="                { return new Symbol(Simbolos.menorigual, yyline, yycolumn, yytext()); }
"&&"                { return new Symbol(Simbolos.tand, yyline, yycolumn, yytext()); }
"¬¬"                { return new Symbol(Simbolos.tor, yyline, yycolumn, yytext()); }
"^"                 { return new Symbol(Simbolos.xor, yyline, yycolumn, yytext()); }
"!"                 { return new Symbol(Simbolos.not, yyline, yycolumn, yytext()); }

/* Palabras reservadas */ 
"boolean"           { return new Symbol(Simbolos.tboolean, yyline, yycolumn, yytext()); }
"char"              { return new Symbol(Simbolos.tchar, yyline, yycolumn, yytext()); }
"int"               { return new Symbol(Simbolos.tint, yyline, yycolumn, yytext()); }
"double"            { return new Symbol(Simbolos.tdouble, yyline, yycolumn, yytext()); }
"string"            { return new Symbol(Simbolos.tstring, yyline, yycolumn, yytext()); }
"print"             { return new Symbol(Simbolos.print, yyline, yycolumn, yytext()); }
"repetir"           { return new Symbol(Simbolos.repetir, yyline, yycolumn, yytext()); }
"pow"               { return new Symbol(Simbolos.pow, yyline, yycolumn, yytext()); }

/* Tokens */
{booleano}          { return new Symbol(Simbolos.booleano, yyline, yycolumn, yytext()); }
{caracter}          { return new Symbol(Simbolos.caracter, yyline, yycolumn, yytext()); }
{cadena}            { return new Symbol(Simbolos.cadena, yyline, yycolumn, yytext()); }
{entero}            { return new Symbol(Simbolos.entero, yyline, yycolumn, yytext()); }
{doble}             { return new Symbol(Simbolos.doble, yyline, yycolumn, yytext()); }
{identificador}     { return new Symbol(Simbolos.identificador, yyline, yycolumn, yytext()); }

/* Comentarios */
{blanco}            {   }
{comentario}        {   }       
{comentarioMulti}   {   } 

/* Errores */
.                   { System.err.println("Elemento léxico desconocido: " + yytext() + ", Línea: " + (yyline + 1) + ", Columna: " + (yycolumn + 1)); }
