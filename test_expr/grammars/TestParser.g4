parser grammar TestParser;
options {tokenVocab = TestLexer;}


compUnit: statement+;

bareName: ID | SETTER | GETTER;
qualifName: NAMESPACE_SEP? qualifNameTxt;
qualifNameOnly: NAMESPACE_SEP? qualifNameOnlyTxt;

qualifNameTxt: bareName (NAMESPACE_SEP bareName)*;
qualifNameOnlyTxt: bareName (NAMESPACE_SEP bareName)+;

commaExpr: (COMMA expr)+;

exprList: expr commaExpr?;

expr: OPEN_PAREN expr CLOSE_PAREN #exprParen
    | expr OPEN_PAREN exprList? CLOSE_PAREN #exprCall
    | literal #exprLiteral
    | qualifName #exprQualifName
    | BASE #exprBase
    ;

statement: expr;

literal: NULL   #literalNull
    | INTEGER   #literalInteger
    | INT_OCTAL #literalIntOctal
    | INT_HEX   #literalIntHex
    | INT_BIN   #literalIntBin
    | FLOAT     #literalFloat
    | STRING    #literalString
    | RAW_STRING#literalRawString
    | CHAR_STR  #literalCharStr
    | TRUE      #literalTrue
    | FALSE     #literalFalse
    ;



