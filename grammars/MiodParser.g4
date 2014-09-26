parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

compUnit: unitHeader unitBody?;
unitHeader: docComments? UNIT name=DOT_NAME;
unitBody: globalStmts;

globalStmt: staticIf
    | globalDecl
    | docComments
    ;

globalStmts: globalStmt+;

staticIf:
    //| STATIC_IF THEN  {notifyErrorListeners("const expr expected for static_if");}
    //| 
    STATIC_IF constExpr THEN globalStmts? (ELSE globalStmts?)? ENDIF;

globalDecl: constDecl
    | varDecl
    | procDecl
    | typeDecl
    | importDecl
    | includeDecl
    ;

constDecl: 
    CONST NON_DOT_NAME (COLON typeSpec)? ASSIGN constExpr
    ;

constExpr: globalExpr;

/// no function calls
globalExpr: expr;

expr: literal
    ;

typeSpec: DOT_NAME; // add array etc

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

typeDecl: TYPE;

importDecl: IMPORT;

includeDecl: INCLUDE STRING;

docComments: DOC_COMMENT+;

// endStmt: NEWLINE | EOF;

annotation: ANNOTATE dictValue?;

keyValue: NON_DOT_NAME COLON literal;
dictValue: DICT_BEG (keyValue (COMMA keyValue)*)? DICT_END;

