parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

compUnit: unitHeader unitBody?;
unitHeader: annotation? UNIT name=QUALIF_NAME;

annotation: ANNOTATE structInitValue?;

structFieldValue: BARE_NAME COLON expr;
structInitValue: DICT_BEG (structFieldValue (COMMA structFieldValue)*)? DICT_END;

unitBody: globalStmt+;

globalStmt: staticIf
    | globalDecl
    ;


staticIf:
    //| STATIC_IF THEN  {notifyErrorListeners("const expr expected for static_if");}
    //| 
    STATIC_IF expr THEN globalStmt* (ELSE globalStmt*)? ENDIF;

globalDecl: constDecl
    | varDecl
    | procDecl
    | typeDecl
    | importDecl
    | includeDecl
    | visibilityStmt
    ;

visibilityStmt: PRIVATE | PROTECTED | PUBLIC;

constDecl: CONST constAssign (COMMA constAssign)*;

constAssign: BARE_NAME (COLON typeSpec)? ASSIGN expr;

expr: literal
    | QUALIF_NAME
    | dictValue
    | arrayValue
    ;

keyValue: expr COLON expr;
dictValue: DICT_BEG keyValue (COMMA keyValue)* DICT_END;

arrayValue: ARRAY_BEG expr (COMMA expr)* ARRAY_END;

typeSpec: QUALIF_NAME; // add array etc

literal: NULL
    | INTEGER
    | INT_OCTAL 
    | INT_HEX
    | INT_BIN
    | FLOAT
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

importDecl: IMPORT name=QUALIF_NAME;

includeDecl: INCLUDE STRING;

//docComments: DOC_COMMENT+;

// endStmt: NEWLINE | EOF;


forEachLoop: FOR varNames IN expr blockStmts? END_FOR;
forLoop: FOR OPEN_BRACE varAssigns SEMICOLON loopActs SEMICOLON boolExpr CLOSE_BRACE blockStmts? END_FOR;

loopActs: ;
boolExpr: ;

varNames: BARE_NAME (',' BARE_NAME)*;
varAssigns: varAssign | varInitAssign (',' varAssign | varInitAssign)*;
varAssign: BARE_NAME ASSIGN expr;
varInitAssign: BARE_NAME COLON typeSpec ASSIGN expr;

blockStmts:;


