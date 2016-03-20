parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

// TODO remove global/local const/var rules
// todo fix associativity
// TODO


compUnit: unitHeader unitBody?;
unitHeader: annotation* UNIT QUALIF_NAME;

qualifName: QUALIF_NAME | BARE_NAME;

annotation: ANNOTATE qualifName annotationDict?;
annotationDict: OPEN_CURLY BARE_NAME COLON expr (COMMA BARE_NAME COLON expr)* CLOSE_CURLY;

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


typeArgsOpen: TYPE_ARGS_OPEN;
typeArgsClose: GREATER;

// TODO reqursive rules are to be here
expr: literal #exprLiteral
    | NEW OPEN_PAREN typeSpec CLOSE_PAREN #exprNew
    | CAST typeArgsOpen typeSpec typeArgsClose OPEN_PAREN expr CLOSE_PAREN #exprCast
    | qualifName (typeArgsOpen qualifName (COMMA qualifName)* typeArgsClose)? #exprQualifName
    | expr MEMBER_ACCESS BARE_NAME #exprMemberAccess
    | expr OPEN_BRACKET expr CLOSE_BRACKET #exprIndex
    | expr OPEN_PAREN (expr (COMMA expr)*)? CLOSE_PAREN #exprCall
    | OPEN_CURLY expr COLON expr (COMMA expr COLON expr)* CLOSE_CURLY #exprDictStruct
    | OPEN_BRACKET expr (COMMA expr)* CLOSE_BRACKET #exprArray
    | MINUS expr #exprNeg
    | BNOT expr #exprBNot
    | expr MUL expr #exprMul
    | expr DIV expr #exprDiv
    | expr MOD expr #exprMod
    | expr PLUS expr #exprPlus
    | expr MINUS expr #exprMinus
    | expr BOR expr #exprBOr
    | expr BAND expr #exprBAnd
    | expr XOR expr #exprXor
    | expr SHL expr #exprShl
    | expr SHR expr #exprShr
    | NOT expr #exprNot
    | expr GREATER expr #exprGreater
    | expr LESS expr #exprLess
    | expr GREATER_EQ expr #exprGreaterEq
    | expr LESS_EQ expr #exprLessEq
    | expr EQUALS expr #exprEquals
    | expr NOT_EQ expr #exprNotEq
    | expr AND expr #exprAnd
    | expr OR expr #exprOr
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

typeSpec: QUALIF_NAME
    | arrayType;

// generic type: Map$<String,Map$<Integer,String>>

// array type part
arrayType: ARRAY ARRAY_BEG arrayVariant ARRAY_END;
arrayVariant: type = QUALIF_NAME    # unknownSizeArray
    | type = QUALIF_NAME COMMA size = expr # sizedArray
    ;


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



