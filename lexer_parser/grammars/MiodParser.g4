parser grammar MiodParser;
options {tokenVocab = MiodLexer;}

compUnit: unitHeader unitBody?;
unitHeader: annotations? GENERIC? UNIT qualifNameOnlyTxt;

bareName: ID | SETTER | GETTER;
qualifName: NAMESPACE_SEP? qualifNameTxt;
qualifNameOnly: NAMESPACE_SEP? qualifNameOnlyTxt;

qualifNameTxt: bareName (NAMESPACE_SEP bareName)*;
qualifNameOnlyTxt: bareName (NAMESPACE_SEP bareName)+;

annotations: annotation+;
// Must check for predefined annotations like @_rtti
annotation: ANNOTATE qualifName annotationDict?;
annotationDict: OPEN_CURLY bareName COLON constExpr annotationDictValues? CLOSE_CURLY;

annotationDictValues: annotationDictValue+;
annotationDictValue: COMMA bareName COLON constExpr;

unitBody: importDecl* globalStmt+;

// IMPORT imports units, so that public symbols can be addressed as myunit::procName
// IMPORT_ALL imports unit public symbols into global namespace
importDecl: (IMPORT|IMPORT_ALL) qualifNameOnlyTxt;

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
    | procDecl
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

exprList: expr (COMMA expr)*;

// Recursive rules are to be here.
// If current scope is global then fails for procedure/method calls and property access

dotMember: qualifName | literal;

primary: 
    literal
    | qualifName
    ;

call
    : OPEN_PAREN exprList? CLOSE_PAREN;

expr: primary call*
    | OPEN_PAREN expr CLOSE_PAREN
    | OPEN_BRACKET exprList? CLOSE_BRACKET
 /*
    | VAR? expr #exprVar
    | NEW OPEN_PAREN typeSpec CLOSE_PAREN #exprNew
    | CAST typeArgsOpen typeSpec typeArgsClose OPEN_PAREN expr CLOSE_PAREN #exprCast
    | OPEN_CURLY expr COLON expr (COMMA expr COLON expr)* CLOSE_CURLY #exprDictStruct
    | LITERAL (typeArgsOpen (NSTRING|NWSTRING) typeArgsClose)? OPEN_PAREN expr CLOSE_PAREN #exprLiteralOper
    | BASE #exprBase
    | expr OPEN_BRACKET expr CLOSE_BRACKET #exprIndex
    | MINUS expr #exprNeg
    | BNOT expr #exprBNot
    | left=expr MUL right=expr #exprMul
    | expr DIV expr #exprDiv
    | expr MOD expr #exprMod
    | left=expr PLUS right=expr #exprPlus
    | expr MINUS expr #exprMinus
    | expr BOR expr #exprBOr
    | expr BAND expr #exprBAnd
    | expr XOR expr #exprXor
    | expr SHL expr #exprShl
    | expr SHR expr #exprShr
    | NOT expr #exprNot
    | left=expr GREATER right=expr #exprGreater
    | left=expr LESS right=expr #exprLess
    | left=expr GREATER_EQ right=expr #exprGreaterEq
    | left=expr LESS_EQ right=expr #exprLessEq
    | left=expr EQUALS right=expr #exprEquals
    | left=expr NOT_EQ right=expr #exprNotEq
    | expr AND expr #exprAnd
    | expr OR expr #exprOr
    */
    ;

literal: NULL   #literalNull
    | INTEGER   #literalInteger
    | INT_OCTAL #literalIntOctal
    | INT_HEX   #literalIntHex
    | INT_BIN   #literalIntBin
    | FLOAT     #literalFloat
    | STRING    #literalString
    | RAW_STRING#literalRawString
    | CHAR_STR  #literalCharStr
    | TRUE      #literalTrue
    | FALSE     #literalFalse
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

procDecl: annotations? (EXTERN|INLINE)? procHeader (SEMICOLON|procBody);

procBody: statement* END_PROC;

procOrMethodDecl: annotations? (EXTERN|INLINE)? (procHeader|methodHeader)
    procBody;

procHeader: PROC bareName procOrMethodArgs;
methodHeader: (ABSTRACT|VIRTUAL|OVERRIDE)? METHOD bareName procOrMethodArgs;

procOrMethodArgs: OPEN_PAREN procArgsDecl? CLOSE_PAREN (COLON typeSpec)?;

procMethodStructDecl: annotations? (EXTERN|INLINE)?
    (PROC|METHOD) bareName
    OPEN_PAREN procArgsDecl? CLOSE_PAREN (COLON typeSpec)? (SEMICOLON | procBody);

interfaceProcOrMethodDecl: procMethodStructDecl;

statementExpr
    : qualifName OPEN_PAREN exprList? CLOSE_PAREN;

statement: RETURN expr? #statementReturn
    | constDecl #statementConstDecl
    | varDecl #statementVarDecl
    | staticIf #statementStaticIf
    //| callable=expr OPEN_PAREN (expr commaExpr?)? CLOSE_PAREN #statementCall
    | FINALLY statement* END_FINALLY #statementFinally
    | BREAK #statementBreak
    | CONTINUE #statementContinue
    | forEachLoop #statementForEach
    | whileLoop #statementWhile
    // semantic check for lvalue, e.g. id, memberAccess, index
    | left=expr ASSIGN right=expr #statementAssign
    | ifStatement #statementIf
    | WITH qualifName (COMMA qualifName)* statement+ END_WITH #statementWith
    | SEMICOLON #emptyStatement
    | statementExpr #statExpr
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
typeDecl: (TYPE typeDeclAssign+)
    | (structDecl|classDecl)+;

typeDeclAssign: annotations? bareName ASSIGN (GENERIC|arrayType|enumDecl|OPAQUE);

enumDecl: annotations? ENUM (typeArgsOpen typeSpec typeArgsClose)?
    (bareName (ASSIGN constExpr)?)+ END_ENUM
;

structDecl: annotations? EXTERN? STRUCT bareName
    (IMPLEMENTS qualifName (COMMA qualifName)*)?
    structBodyStmt* END_STRUCT
;

structOrClassField: bareName (COMMA bareName)* COLON typeSpec;

structBodyStmt: structOrClassField
    | procMethodStructDecl
//    | STATIC_IF boolExpr THEN structBodyStmt* (ELSE structBodyStmt*)? END_IF
    ;

classDecl: annotations? EXTERN? ((ABSTRACT? BASE_CLASS)|CLASS) bareName
    (BASED_ON qualifName)?
    (IMPLEMENTS qualifName (COMMA qualifName)*)?
    classBodyStmt*
    END_CLASS
    ;

classBodyStmt: visibilityStmt
    | annotations? propertyDecl
    | structOrClassField
    | procOrMethodDecl
    | STATIC_IF boolExpr THEN classBodyStmt* (ELSE classBodyStmt*)? END_IF
    ;

interfaceDecl: annotations? EXTERN? INTERFACE bareName
    (BASED_ON qualifName (COMMA qualifName)*)?
    interfaceBodyStmt*
    END_INTERFACE
    ;

interfaceBodyStmt: interfaceProcOrMethodDecl
    | STATIC_IF boolExpr THEN interfaceBodyStmt* (ELSE interfaceBodyStmt*)? END_IF
    ;

// Check for double getters/setters in semantic pass!
propertyDecl: PROPERTY bareName COLON typeSpec (EQUALS OPEN_CURLY 
    propSetterGetter (COMMA propSetterGetter)? CLOSE_CURLY)?;
propSetterGetter: (SETTER|GETTER) EQUALS bareName;

