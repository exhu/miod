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
    | boolExpr
    | arithmExpr
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


forEachLoop: FOR OPEN_BRACE varNames IN expr CLOSE_BRACE blockStmts? END_FOR;
varNames: BARE_NAME (COMMA BARE_NAME)*;

whileLoop: WHILE OPEN_BRACE boolExpr CLOSE_BRACE blockStmts? END_WHILE;

boolExpr: QUALIF_NAME
    | TRUE
    | FALSE
    | procCall
    | NOT boolExpr
    | AND boolExpr
    | OR boolExpr
    | EQUALS // TODO rewrite as boolExpr EQUALS boolExpr
    | NOT_EQ
    | LESS
    | GREATER
    | LESS_EQ
    | GREATER_EQ;

procCall: ;

arithmExpr: ;

// subrules for variable/const decl:
varAssign: BARE_NAME ASSIGN expr;
varInitAssign: BARE_NAME COLON typeSpec ASSIGN expr;

varDeclPart: BARE_NAME | varAssign | varInitAssign;

// varialbe/const declaration:
localDecl: VAR | CONST varDeclPart (COMMA varDeclPart)*;

blockStmts: blockStmt+;
blockStmt: localDecl
    | BREAK
    | CONTINUE
    | RETURN;



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


