lexer grammar MiodLexer;

fragment NL: ('\r'? '\n');
NEWLINE: NL;
WS: (' ' | '\t')+ -> skip;

JOIN_LINE: '\\' NEWLINE -> skip;

// comments

DOC_COMMENT: '##' .*? (NEWLINE|EOF);
COMMENT: '#' .*? (NEWLINE|EOF) -> skip;

// keywords
CONST: 'const';
VAR: 'var';
PROC: 'proc';
IMPORT: 'import';
INCLUDE: 'include';
TYPE: 'type';
STATIC_IF: 'static_if';
THEN: 'then';
ELSE: 'else';
ENDIF: 'endif';
UNIT: 'unit';
PLUS: '+';
MINUS: '-';
DIV: '/';
MUL: '*';
MOD: '%';
NOT: 'not';
BNOT: '~';
BOR: '|';
BAND: '&';
OR: 'or';
AND: 'and';
XOR: 'xor'; // only binary
EQUALS: '==';
NOT_EQ: '!=';
ASSIGN: '=';
SHL: 'shl';
SHR: 'shr';


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

DOT_NAME: ID ('.' ID)*;
NON_DOT_NAME: ID;
TYPE_DECL: ':' DOT_NAME;
// IDs

//////

//ALL: . ;


