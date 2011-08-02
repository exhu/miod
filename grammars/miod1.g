grammar miod1;
options {
     language = Java;
     output = AST;
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

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
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
	;
	
var_decl
	:	'var' var_init (',' var_init)* -> ^('var' var_init)+	
	;		
	
var_init
	:	typename ID ('=' expr)?	-> ^(ID typename ^('=' expr)?)
	|	ID '=' expr		-> ^(ID ^('=' expr))
	;
	
var_or_type_dot
	:	ID^ ('.'! var_or_type_dot)?
	;

typename
	:	var_or_type_dot
	|	'strong' '[' typename ']'
	|	'weak' '[' typename ']'
	|	'ptr' '[' typename ']'
	|	'array' '[' typename (',' expr)? ']'
	;
	
	
literal	
	:	FLOAT | DOUBLE | INT | UINT | LONG | ULONG | LLONG | ULLONG | STRING | CHAR
	;	

expr	:	sum_expr
	;

sum_expr
	:	mulexpr ('+'^ mulexpr | '-'^ mulexpr)*
//	|	'(' expr ')'
	;
	
mulexpr	:	primexpr ('*'^ mulexpr | '/'^ mulexpr)?
	|	'('! expr ')'!
	;
	
primexpr
	:	literal
	|	postf_op
	;
	
postf_op
	:	(ID -> ID) ('.' nm=ID -> ^('.' $postf_op $nm)
	|	'[' e=expr ']' -> ^('[' $postf_op $e))*
	;	

	
	