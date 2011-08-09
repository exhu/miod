grammar miod1;
options {
     language = Java;
     output = AST;
}

tokens {
	FIELD_DECL;
	VAR_TYPED; // variable with specified type name
	VAR_ANY;
	UNSIZED_ARRAY; // array size must be taken from initialization expression
	SIZED_ARRAY;
	NEGATION = '-';
}


@header {
     package org.miod.antlr;
}



module	
	:	module_part+
	;


// lexer part

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

fragment
INT_N :	'0'..'9'+ 
    ;


INT :	INT_N 
    ;
    
UINT :	INT_N ('u'|'U') 
    ;

LONG :	INT_N ('l'|'L')
    ;

ULONG :	INT_N ('ul'|'UL') 
    ;

LLONG :	INT_N ('ll'|'LL')
    ;

ULLONG	:	INT_N ('ull'|'ULL') 
    ;


FLOAT	:	FLOAT_N ('f'|'F')
	;

DOUBLE	:	FLOAT_N
	;

fragment
FLOAT_N
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING	:	STRING_V
	;

CHAR	:	CHAR_V
	;
	
WSTRING	:	'L' STRING_V
	;
	
WCHAR	:	'L' CHAR_V
	;


fragment
STRING_V
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment
CHAR_V:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;


// grammar
	
module_part 
	:	var_decl
	|	type_decl
	;
	
var_decl
	:	'var' var_init (',' var_init)*	-> var_init+
	;		
	
var_init
	:	typename ID ('=' expr)?	-> ^(VAR_TYPED ID typename ^('=' expr)?)
	|	ID '=' expr		-> ^(VAR_ANY ID ^('=' expr))
	;
	
var_or_type_dot
	:	(ID -> ID) (('.' n=ID) -> ^('.' $var_or_type_dot $n))*
	;

typename
	:	var_or_type_dot
	|	'strong' '[' typename ']' -> ^('strong' typename)
	|	'weak' '[' typename ']'  -> ^('weak' typename)
	|	'ptr' '[' typename ']'  -> ^('ptr' typename)
	|	'array' '[' typename {boolean fixed = false;} (',' expr {fixed  = true;})? ']'
		-> {fixed}? ^(SIZED_ARRAY typename expr)
		-> ^(UNSIZED_ARRAY typename)
	|	'int' | 'uint' | 'uintptr' | 'int8' | 'int32' | 'uint32' | 'int16' | 'uint16' | 'int64' | 'uint64' | 'uint8'
	|	'bool'
	;
	
	
literal	
	:	FLOAT | DOUBLE | INT | UINT | LONG | ULONG | LLONG | ULLONG | STRING | CHAR
	|	'true' | 'false'
	;	

expr	:	sum_expr
	;

sum_expr
	:	mulexpr ('+'^ mulexpr | '-'^ mulexpr)*
//	|	'(' expr ')'
	;
	
mulexpr	:	('~'^ |'not'^ | NEGATION^)? primexpr ('*'^ mulexpr | '/'^ mulexpr 
		| '%'^ mulexpr | '<<'^ mulexpr | '>>'^ mulexpr | '|'^ mulexpr | '&'^ mulexpr | '^'^ mulexpr
		| 'or'^ mulexpr | 'and'^ mulexpr | 'xor'^ mulexpr)?
	|	'('! expr ')'!
	;
	
primexpr
	:	literal
	|	postf_op
	|	'sizeof' '(' typename ')' -> ^('sizeof' typename)
	|	'ptr' '(' postf_op ')' -> ^('ptr' postf_op)
	|	'cast' '[' typename ']' '(' expr ')' -> ^('cast' expr typename)
	;
	
postf_op
	:	(ID -> ID) ('.' nm=ID -> ^('.' $postf_op $nm)
	|	'[' e=expr ']' -> ^('[' $postf_op $e))*
	;	

type_decl
	:	'type' ID '=' typename (',' ID '=' typename)* -> ^('type' ID typename)+ 
	|	'struct' ID '{' field_decl* '}' -> ^('struct' ID field_decl*)
	;
	
field_decl
	:	typename ID (',' ID)* -> ^(FIELD_DECL typename ID)+
	;