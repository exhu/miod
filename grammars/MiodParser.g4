parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

compUnit: unitHeader unitBody?;
unitHeader: annotation? UNIT name=QUALIF_NAME;

annotation: ANNOTATE dictValue?;

keyValue: literal COLON literal;
dictValue: DICT_BEG (keyValue (COMMA keyValue)*)? DICT_END;

unitBody: globalStmts;

globalStmts: globalStmt+;

globalStmt: staticIf
    | globalDecl
    ;


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
    | visibilityStmt
    ;

visibilityStmt: PRIVATE | PROTECTED | PUBLIC;

constDecl: CONST constAssign (COMMA constAssign)*;

constAssign: BARE_NAME (COLON typeSpec)? ASSIGN constExpr;

/// semantic pass must check for proc calls
constExpr: expr;

expr: literal
    ;

typeSpec: QUALIF_NAME; // add array etc

literal: NULL
    | INTEGER
    | INT_OCTAL 
    | INT_HEX
    | INT_BIN
    | FLOAT
    | QUALIF_NAME
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


