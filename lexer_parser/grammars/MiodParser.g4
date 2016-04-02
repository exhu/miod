parser grammar MiodParser;
options {tokenVocab = MiodLexer;}

compUnit: unitHeader unitBody?;
unitHeader: annotations? GENERIC? UNIT qualifNameOnly;

bareName: ID | SETTER | GETTER;
qualifName: NAMESPACE_SEP? qualifNameTxt;
qualifNameTxt: bareName (NAMESPACE_SEP bareName)*;
qualifNameOnly: NAMESPACE_SEP? qualifNameOnlyTxt;
qualifNameOnlyTxt: bareName (NAMESPACE_SEP bareName)+;

// Must check for predefined annotations like @_rtti
annotation: ANNOTATE qualifName annotationDict?;
annotationDict: OPEN_CURLY bareName COLON constExpr (COMMA bareName COLON constExpr)* CLOSE_CURLY;

annotations: annotation+;

unitBody: importDecl* globalStmt+;
// IMPORT imports units, so that public symbols can be addressed as myunit::procName
// IMPORT_ALL imports unit public symbols into global namespace
importDecl: (IMPORT|IMPORT_ALL) qualifNameOnly;
globalStmt: globalStaticIf
    | globalDecl
    | implementStmt
    ;

constExpr: expr; // to mark semantic difference at certain places
boolExpr: constExpr; // to mark expected bool type

// Semantic phase must evaluate expressions to calculate static_if

globalStaticIf:
    STATIC_IF boolExpr THEN (trueStmts = globalStatements)? (ELSE (falseStmts = globalStatements)?)? END_IF
    ;

globalStatements: globalStmt+;

implementStmt: IMPLEMENT qualifNameOnly WITH (qualifName ASSIGN typeSpec)* END_WITH;


globalDecl: constDecl
    | procMethodDecl
    | typeDecl
    | visibilityStmt
    | varDecl
    | aliasDecl
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
    | LITERAL (typeArgsOpen (NSTRING|NWSTRING) typeArgsClose)? OPEN_PAREN expr CLOSE_PAREN #exprLiteralOper
    | BASE #exprBase
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

procMethodDecl: annotations? (EXTERN|INLINE)?
    (PROC|((ABSTRACT|VIRTUAL|OVERRIDE)? METHOD))
    OPEN_PAREN procArgsDecl CLOSE_PAREN (SEMICOLON|(statement* END_PROC));

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
    // semantic check for lvalue, e.g. id, memberAccess, index
    | expr ASSIGN expr #statementAssign
    | ifStatement #statementIf
    | WITH qualifName (COMMA qualifName)* statement+ END_WITH #statementWith
    ;

staticIf:
    STATIC_IF boolExpr THEN statement* (ELSE statement*)? END_IF
    ;

forEachLoop: FOR OPEN_PAREN varNames IN expr CLOSE_PAREN statement* END_FOR;
varNames: bareName (COMMA bareName)*;

whileLoop: WHILE OPEN_PAREN boolExpr CLOSE_PAREN statement* END_WHILE;

ifStatement: IF boolExpr THEN statement* (ELIF boolExpr THEN statement*)*
    (ELSE statement*)? END_IF;


aliasDecl: ALIAS bareName ASSIGN qualifName;
typeDecl: TYPE bareName ASSIGN
    (GENERIC|qualifName|arrayType|enumDecl|structDecl|classDecl);

enumDecl: annotations? ENUM (typeArgsOpen typeSpec typeArgsClose)?
    (bareName (ASSIGN constExpr)?)+ END_ENUM
;

structDecl: annotations? EXTERN? STRUCT structBodyStmt* END_STRUCT
;

structOrClassField: bareName (COMMA bareName)* COLON typeSpec;

structBodyStmt: structOrClassField
    | STATIC_IF boolExpr THEN structBodyStmt* (ELSE structBodyStmt*)? END_IF
    ;

classDecl: annotations? EXTERN? ((ABSTRACT? BASE_CLASS)|CLASS)
    EXTENDS qualifName 
    IMPLEMENTS qualifName (COMMA qualifName)*
    classBodyStmt*
    END_CLASS
    ;

classBodyStmt: visibilityStmt
    | annotations? propertyDecl
    | structOrClassField
    | procMethodDecl
    | STATIC_IF boolExpr THEN classBodyStmt* (ELSE classBodyStmt*)? END_IF
    ;

// Check for double getters/setters in semantic pass!
propertyDecl: PROPERTY bareName COLON typeSpec (EQUALS OPEN_CURLY 
    propSetterGetter (COMMA propSetterGetter)? CLOSE_CURLY)?;
propSetterGetter: (SETTER|GETTER) EQUALS bareName;

