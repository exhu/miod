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
FINAL: 'final';
VAR: 'var';
PROC: 'proc';
RETURN: 'return';
EXTERN: 'extern';
END_PROC: 'end';
IMPORT: 'import'; // items are accessible via fully qualified name only
IMPORT_ALL: 'import_all'; // injects into global namespace
INCLUDE: 'include';
TYPE: 'type';
STATIC_IF: 'static_if';
IF: 'if';
THEN: 'then';
ELSE: 'else';
END_IF: 'end_if';
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
LESS: '<';
GREATER: '>';
LESS_EQ: '<=';
GREATER_EQ: '>=';
ASSIGN: '=';
SHL: 'shl';
SHR: 'shr';
NULL: 'null';
TRUE: 'true';
FALSE: 'false';
ALIAS: 'alias';
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
ARRAY: 'array';
ARRAY_BEG: '[';
ARRAY_END: ']';
MEMBER_ACCESS: '.';
FOR: 'for';
IN: 'in';
END_FOR: 'end_for';
WHILE: 'while';
END_WHILE: 'end_while';
BREAK: 'break';
CONTINUE: 'continue';
CAST: 'cast'; // checked only in debug
CAST_INSTANCE: 'cast_inst'; // checked cast, null if not that class
PRIVATE: 'private';
PROTECTED: 'protected';
PUBLIC: 'public';
NEW: 'new';
OVERRIDE: 'override';
VIRTUAL: 'virtual';
ABSTRACT: 'abstract';
ENUM: 'enum';
WEAK: 'weak';
CLASS_HELPER: 'class_helper';
STRUCT_HELPER: 'struct_helper';


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

QUALIF_NAME: ID ('::' ID)*;
BARE_NAME: ID;
// IDs

//////

//ALL: . ;


