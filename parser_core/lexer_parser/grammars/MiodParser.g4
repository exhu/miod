parser grammar MiodParser;
options {tokenVocab = MiodLexer;}

compUnit: unitHeader unitBody?;
unitHeader: annotation* UNIT qualifNameOnly;

bareName: ID | SETTER | GETTER;
qualifName: bareName (NAMESPACE_SEP bareName)*;
qualifNameOnly: bareName (NAMESPACE_SEP bareName)+;

// Must check for predefined annotations like @_rtti
annotation: ANNOTATE qualifName annotationDict?;
annotationDict: OPEN_CURLY bareName COLON constExpr (COMMA bareName COLON constExpr)* CLOSE_CURLY;

unitBody: importDecl* globalStmt+;
importDecl: (IMPORT|IMPORT_ALL) qualifNameOnly;
globalStmt: globalStaticIf
    | globalDecl
    ;

constExpr: expr; // to mark semantic difference at certain places
boolExpr: constExpr; // to mark expected bool type

// TODO semantic phase must evaluate expressions to calculate static_if

globalStaticIf:
    STATIC_IF boolExpr THEN globalStmt* (ELSE globalStmt*)? END_IF
    ;

globalDecl: globalConstDecl
    | procDecl
    | typeDecl
    | visibilityStmt
    | varDecl
//    | includeDecl
    ;

//includeDecl: INCLUDE STRING;

globalConstDecl: CONST constAssign (COMMA constAssign)*;
constAssign: bareName (COLON typeSpec)? ASSIGN constExpr;

visibilityStmt: PRIVATE | PROTECTED | PUBLIC;

varDecl: VAR varItem (COMMA varItem)*;
varItem: varsAndType
    | varTypeAssign
    ;
varsAndType: bareName (COMMA bareName)* COLON typeSpec;
varTypeAssign: bareName (COLON typeSpec)? ASSIGN expr;

typeArgsOpen: TYPE_ARGS_OPEN;
typeArgsClose: GREATER;

// Recursive rules are to be here.
// If current scope is global then fails for procedure/method calls and property access
expr: literal #exprLiteral
    | NEW OPEN_PAREN typeSpec CLOSE_PAREN #exprNew
    | CAST typeArgsOpen typeSpec typeArgsClose OPEN_PAREN expr CLOSE_PAREN #exprCast
    | qualifName (typeArgsOpen qualifName (COMMA qualifName)* typeArgsClose)? #exprQualifName
    | expr MEMBER_ACCESS bareName #exprMemberAccess
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


// Check for double getters/setters
propertyDecl: PROPERTY bareName COLON typeSpec (EQUALS OPEN_CURLY 
    propSetterGetter (COMMA propSetterGetter)? CLOSE_CURLY)?;
propSetterGetter: (SETTER|GETTER) EQUALS bareName;

////////// TODO rework everything below ///////////

typeSpec: qualifName
    | arrayType;

// generic type: Map$<String,Map$<Integer,String>>

// array type part
arrayType: ARRAY OPEN_BRACKET arrayVariant CLOSE_BRACKET;
arrayVariant: type = qualifName    # unknownSizeArray
    | type = qualifName COMMA size = expr # sizedArray
    ;


procVarDecl: varDecl;

procDecl: annotation* PROC;

typeDecl: TYPE;



forEachLoop: FOR OPEN_PAREN varNames IN expr CLOSE_PAREN blockStmts? END_FOR;
varNames: bareName (COMMA bareName)*;

whileLoop: WHILE OPEN_PAREN boolExpr CLOSE_PAREN blockStmts? END_WHILE;

memberAccess: qualifName (MEMBER_ACCESS memberAccess)?;

procCall: memberAccess OPEN_PAREN procCallArgs? CLOSE_PAREN;

procCallArgs: expr (COMMA expr)*;


// subrules for variable/const decl:
varAssign: bareName ASSIGN expr;
varInitAssign: bareName COLON typeSpec ASSIGN expr;

varDeclPart: bareName | varAssign | varInitAssign;

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


