parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

compUnit: unitHeader unitBody?;
unitHeader: docComments? UNIT DOT_NAME endStmt;
unitBody: globalStmts;

globalStmt: staticIf
    | globalDecl
    | NEWLINE
    | docComments
    ;

globalStmts: globalStmt (NEWLINE globalStmt)*;

staticIf: STATIC_IF constExpr THEN globalStmts? (ELSE globalStmts?)? ENDIF;

globalDecl: constDecl
    | varDecl
    | procDecl
    | cprocDecl
    | typeDecl
    | importDecl
    | includeDecl
    ;

constDecl: CONST NEWLINE #ConstSection
    | CONST NON_DOT_NAME TYPE_DECL? ASSIGN constExpr #ConstAssign
    ;

constExpr: globalExpr;

/// no function calls
globalExpr: expr;

expr: literal
    ;

literal: NULL
    | INTEGER
    | INT_OCTAL 
    | INT_HEX
    | INT_BIN
    | FLOAT
    | DOT_NAME
    | STRING
    | RAW_STRING
    | CHAR_STR
    | TRUE
    | FALSE
    ;


varDecl: VAR;

procVarDecl: varDecl
    | letDecl;

letDecl: LET;

procDecl: PROC;

cprocDecl: CPROC;

typeDecl: TYPE;

importDecl: IMPORT;

includeDecl: INCLUDE STRING;

docComments: DOC_COMMENT+;

endStmt: NEWLINE | EOF;
