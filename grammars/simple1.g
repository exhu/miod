grammar simple1;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

INDENT 	:  ' '+
	;

WS  :   ( '\t'
        | '\r'
        | '\n'
        | ' '
        ) {$channel=HIDDEN;}
    ;

DEF 	:	'def'
	;

prog	:	statmnt+;

statmnt	:	funcdef;

funcdef :	DEF ID ':' expr;

expr	:	ID '=' INT;

