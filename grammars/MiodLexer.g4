lexer grammar MiodLexer;

fragment NL: ('\r'? '\n');
// comments

DOC_COMMENT: '##' .*? (NEWLINE|EOF) -> channel(HIDDEN);
COMMENT: '#' .*? (NEWLINE|EOF) -> channel(HIDDEN);

NEWLINE: NL -> channel(HIDDEN);

//
WS: (' ' | '\t')+ -> skip;

JOIN_LINE: '\\' NEWLINE -> skip;


// keywords
CONST: 'const';
VAR: 'var';
PROC: 'proc';
EXTERN: 'extern';
END_PROC: 'end';
IMPORT: 'import';
INCLUDE: 'include';
TYPE: 'type';
STATIC_IF: 'static_if';
IF: 'if';
THEN: 'then';
ELSE: 'else';
ENDIF: 'end_if';
UNIT: 'unit';
PLUS: '+';
MINUS: '-';
DIV: '/';
MUL: '*';
MOD: '%';
BNOT: '~';
BOR: '|';
BAND: '&';
NOT: 'not';
OR: 'or';
AND: 'and';
XOR: '^'; // only binary
EQUALS: '==';
NOT_EQ: '!=';
ASSIGN: '=';
SHL: 'shl';
SHR: 'shr';
NULL: 'null';
TRUE: 'true';
FALSE: 'false';
ALIAS: 'alias';
LET: 'let';
FINALLY: 'finally'; // code block run at leaving scope
END_FINALLY: 'end_finally';
STRUCT: 'struct';
END_STRUCT: 'end_struct';
ANNOTATE: '@';
DICT_BEG: '{';
DICT_END: '}';
COLON: ':';
SEMICOLON: ';';
COMMA: ',';
WITH: 'with';
END_WITH: 'end_with';
OPEN_BRACE: '(';
CLOSE_BRACE: ')';
MEMBER_ACCESS: '.';
FOR: 'for';
IN: 'in';
END_FOR: 'end_for';


// literals
fragment ESC: '\\"' | '\\\\';
fragment ESC_CHAR: '\\\'' | '\\\\';
STRING: '"' (ESC|~('\r'|'\n'))*? '"';
RAW_STRING: '"""' .*? '"""';
// must evaluate to single code point, i.e. 1 byte for ASCII,
// 2 byte for widechar etc.
CHAR_STR: '\'' (ESC_CHAR|~('\r'|'\n'))*? '\'';

fragment HEX: [a-fA-F0-9_];
fragment DIGIT: [0-9_];
fragment OCTAL: [0-7_];
fragment BIN: [01_];
fragment ID: [a-zA-Z_]+[0-9a-zA-Z]*;

INT_OCTAL: '-'? '0o' OCTAL+;
INT_HEX: '-'? '0x' HEX+;
INT_BIN: '-'? '0b' BIN+;
FLOAT: '-'? ((DIGIT+ '.' DIGIT*) | (DIGIT* '.' DIGIT+)) ([eE][+\-]DIGIT+)? 'f'?;
INTEGER: '-'? DIGIT+;

DOT_NAME: ID ('::' ID)*;
NON_DOT_NAME: ID;
// IDs

//////

//ALL: . ;


