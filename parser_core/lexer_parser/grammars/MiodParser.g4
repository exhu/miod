parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

// TODO remove global/local const/var rules
// todo fix associativity
// TODO


compUnit: unitHeader unitBody?;
unitHeader: annotation? UNIT qualifName;

qualifName: QUALIF_NAME | BARE_NAME;

annotation: ANNOTATE structValue?;

unitBody: globalStmt+;

globalStmt: globalStaticIf
    | globalDecl
    ;

constExpr: expr; // to mark semantic difference at certain places

globalStaticIf:
    STATIC_IF boolExpr THEN globalStmt* (ELSE globalStmt*)? END_IF
    ;

globalDecl: globalConstDecl
    | procDecl
    | typeDecl
    | visibilityStmt
    | varDecl
    | importDecl
//    | includeDecl
    ;

globalConstDecl: CONST constAssign (COMMA constAssign)*;
constAssign: BARE_NAME (COLON typeSpec)? ASSIGN constExpr;

visibilityStmt: PRIVATE | PROTECTED | PUBLIC;

varDecl: VAR varItem (COMMA varItem)*;
varItem: varsAndType
    | varTypeAssign
    ;
varsAndType: BARE_NAME (COMMA BARE_NAME)* COLON typeSpec;
varTypeAssign: BARE_NAME (COLON typeSpec)? ASSIGN expr;


// TODO reqursive rules are to be here
expr: literal
    | qualifName
    | memberAccess
    | procCall
    | boolExpr
    | arithmExpr
    | dictValue
    | arrayValue
    | structValue
    ;


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


////////// TODO rework everything below ///////////
arithmAtom: literal | memberAccess | procCall;
    
arithmExpr: MINUS arithmExpr 
    | arithmAtom ((PLUS|MINUS|MUL|DIV|MOD|BNOT|BOR|BAND|XOR|SHL|SHR)
        arithmExpr)?
    
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

importDecl: IMPORT name=qualifName;

includeDecl: INCLUDE STRING;


forEachLoop: FOR OPEN_BRACE varNames IN expr CLOSE_BRACE blockStmts? END_FOR;
varNames: BARE_NAME (COMMA BARE_NAME)*;

whileLoop: WHILE OPEN_BRACE boolExpr CLOSE_BRACE blockStmts? END_WHILE;

boolExpr: boolVal boolRest?;

boolRest: EQUALS boolExpr
    | NOT_EQ boolExpr
    | LESS boolExpr
    | GREATER boolExpr
    | LESS_EQ boolExpr
    | GREATER_EQ boolExpr;

boolVal: memberAccess
    | literal
    | procCall
    | NOT boolExpr
    | AND boolExpr
    | OR boolExpr
    ;

memberAccess: QUALIF_NAME (MEMBER_ACCESS memberAccess)?;

procCall: memberAccess OPEN_BRACE procCallArgs? CLOSE_BRACE;

procCallArgs: expr (COMMA expr)*;


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
    | RETURN
    | forEachLoop
    | whileLoop
    | procCall
    | assignment
    | finallyBlock
    | ifStatement
    | staticIfStatement
    ;

assignment: memberAccess ASSIGN expr;

finallyBlock: FINALLY blockStmts END_FINALLY;

ifStatement: IF boolExpr THEN blockStmts? (ELSE blockStmts?)? END_IF;
staticIfStatement: STATIC_IF boolExpr THEN blockStmts? (ELSE blockStmts?)? END_IF;



