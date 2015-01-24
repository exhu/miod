parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

compUnit: unitHeader unitBody?;
unitHeader: annotation? UNIT name=QUALIF_NAME;

annotation: ANNOTATE structValue?;

unitBody: globalStmt+;

globalStmt: staticIf
    | globalDecl
    ;

constExpr: expr; // to mark semantic difference at certain places

staticIf:
    //| STATIC_IF THEN  {notifyErrorListeners("const expr expected for static_if");}
    //| 
    STATIC_IF constExpr THEN globalStmt* (ELSE globalStmt*)? ENDIF;

globalDecl: globalConstDecl
    | globalVarDecl
    | procDecl
    | typeDecl
    | importDecl
    | includeDecl
    | visibilityStmt
    ;

visibilityStmt: PRIVATE | PROTECTED | PUBLIC;

globalConstDecl: CONST constAssign (COMMA constAssign)*;
globalVarDecl: VAR constAssign (COMMA constAssign)*;
constAssign: BARE_NAME (COLON typeSpec)? ASSIGN constExpr;

expr: literal
    | QUALIF_NAME
    | dictValue
    | arrayValue
    | structValue
    ;

// dictionary initialization value part
keyValue: expr COLON expr;
dictValue: DICT_BEG keyValue (COMMA keyValue)* DICT_END;

// structure initialization value part
structFieldValue: BARE_NAME COLON expr;
structValue: DICT_BEG (structFieldValue (COMMA structFieldValue)*)? DICT_END;

// array initialization value part
arrayValue: ARRAY_BEG arrayValues  ARRAY_END;
arrayValues: value1 = expr (COMMA value2 = expr)*
;

typeSpec: QUALIF_NAME
    | arrayType;

// array type part
arrayType: ARRAY ARRAY_BEG arrayVariant ARRAY_END;
arrayVariant: type = QUALIF_NAME    # unknownSizeArray
    | type = QUALIF_NAME COMMA size = expr # sizedArray
    ;

varDecl: VAR;

procVarDecl: varDecl
    | finalDecl;

finalDecl: FINAL;

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

varNames: BARE_NAME (COMMA BARE_NAME)*;
varAssigns: varAssign | varInitAssign (COMMA varAssign | varInitAssign)*;
varAssign: BARE_NAME ASSIGN expr;
varInitAssign: BARE_NAME COLON typeSpec ASSIGN expr;

varDeclPart: BARE_NAME | varAssign | varInitAssign;

localDecl: VAR | CONST varDeclPart (COMMA varDeclPart)*;

blockStmt: localDecl;

blockStmts: blockStmt+;


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


