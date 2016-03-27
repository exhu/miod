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

// Semantic phase must evaluate expressions to calculate static_if

globalStaticIf:
    STATIC_IF boolExpr THEN globalStmt* (ELSE globalStmt*)? END_IF
    ;

globalDecl: constDecl
    | procMethodDecl
    | typeDecl
    | visibilityStmt
    | varDecl
//    | includeDecl
    ;

//includeDecl: INCLUDE STRING;

constDecl: CONST constAssign (COMMA constAssign)*;
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


// Check for double getters/setters in semantic pass!
propertyDecl: PROPERTY bareName COLON typeSpec (EQUALS OPEN_CURLY 
    propSetterGetter (COMMA propSetterGetter)? CLOSE_CURLY)?;
propSetterGetter: (SETTER|GETTER) EQUALS bareName;


typeSpec: qualifName #typeSpecName
    | genericTypeSpec #typeSpecGenericArgs
    | (PROC|METHOD) OPEN_PAREN procArgsDecl? CLOSE_PAREN #typeSpecProcMethod
    | arrayType #typeSpecArray
    ;


// generic type: Map$<String,Map$<Integer,String>>
genericTypeSpec: qualifName typeArgsOpen typeSpec (COMMA typeSpec)* typeArgsClose ;

procArgsDecl: procArgDecl (COMMA procArgDecl)*;


procArgDecl: bareName (COMMA bareName)* COLON VAR? typeSpec;

// array type part
arrayType: ARRAY OPEN_BRACKET arrayVariant CLOSE_BRACKET;
arrayVariant: qualifName # unknownSizeArray
    | typeSpec COMMA expr # sizedArray
    ;

procMethodDecl: annotation* (PROC|METHOD) OPEN_PAREN procArgsDecl CLOSE_PAREN
    statement* END_PROC;

statement: RETURN expr? #statementReturn
    | constDecl #statementConstDecl
    | varDecl #statementVarDecl
    | staticIf #statementStaticIf
    | expr OPEN_PAREN (expr (COMMA expr)*)? CLOSE_PAREN #statementCall
    | FINALLY statement* END_FINALLY #statementFinally
    | BREAK #statementBreak
    | CONTINUE #statementContinue
    | forEachLoop #statementForEach
    | whileLoop #statementWhile
    | expr ASSIGN expr #statementAssign
    | ifStatement #statementIf
    ;

staticIf:
    STATIC_IF boolExpr THEN statement* (ELSE statement*)? END_IF
    ;

forEachLoop: FOR OPEN_PAREN varNames IN expr CLOSE_PAREN statement* END_FOR;
varNames: bareName (COMMA bareName)*;

whileLoop: WHILE OPEN_PAREN boolExpr CLOSE_PAREN statement* END_WHILE;

ifStatement: IF boolExpr THEN statement* (ELIF boolExpr THEN statement*)*
    (ELSE statement*)? END_IF;


////////// TODO rework everything below ///////////
typeDecl: TYPE;



