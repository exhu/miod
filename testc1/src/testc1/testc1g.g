grammar testc1g;
options {
     language = Java;
     output = AST;
     //ASTLabelType=MyAstNode;
}

@header {
    package testc1;
    import java.util.HashMap;
}

@lexer::header{
    package testc1;
}

@members {SymbolTable symtab;}


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


program [SymbolTable symtab] // pass symbol table to start rule
@init {this.symtab = symtab;}
// set the parser's field
 : stat+
;

stat
        : varDecl
        | assignment
;

varDecl
        :       type vn=ID {symtab.defineSymbol(new VariableSymbol($vn.text, $type.t));}
        ;

assignment 
	:	ID '='^ expr;


type[Type t]    :       tn=ID {$t = (Type)symtab.resolveSymbol($tn.text);}
        ;
	
value	:	ID | INT | FLOAT | STRING | CHAR
	;
	
expr 	:	add
	;
	
add 	:	mulexpr ('+'^ mulexpr)*
	;
	
mulexpr :	value ('*'^ value)*
	;
	