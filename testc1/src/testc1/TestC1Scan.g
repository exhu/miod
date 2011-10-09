grammar TestC1Scan;

options {
     language = Java;
     //ASTLabelType=MyAstNode; requires custom Tree adaptor
     output = AST;
     
}

tokens
{
	VARDECL;
	ASSIGN;

}

@header {
    package testc1;
    import java.util.HashMap;
}

@lexer::header{
    package testc1;
}

//@members {SymbolTable symtab;}


program // pass symbol table to start rule
//@init {this.symtab = symtab;}
// set the parser's field
 : stat+
;

stat
        : varDecl
        | assignment
;

varDecl
        :       t=ID n=ID -> ^(VARDECL $t $n)
        ;

assignment 
	:	ID '=' expr -> ^(ASSIGN ID expr);
	
value	:	ID | INT | FLOAT | STRING | CHAR
	;
	
expr 	:	add
	;
	
add 	:	mulexpr ('+'^ mulexpr)*
	;
	
mulexpr :	value ('*'^ value)*
	;



// tokens follow

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

FLOAT
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
	