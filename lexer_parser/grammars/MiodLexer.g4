lexer grammar MiodLexer;

fragment NL: ('\r'? '\n');
// comments

DOC_COMMENT: '##' .*? (NEWLINE|EOF) -> channel(2);
COMMENT: '#' .*? (NEWLINE|EOF) -> channel(HIDDEN);

NEWLINE: NL -> channel(HIDDEN);

//
WS: (' ' | '\t')+ -> skip;

JOIN_LINE: '\\' NEWLINE -> skip;


// keywords
CONST: 'const';
//FINAL: 'final';
VAR: 'var';
PROC: 'proc';
METHOD: 'method';
INLINE: 'inline';
RETURN: 'return';
EXTERN: 'extern';
END_PROC: 'end';
IMPORT: 'import'; // items are accessible via fully qualified name only
IMPORT_ALL: 'import_all'; // injects into global namespace
INCLUDE: 'include';
TYPE: 'type';
OPAQUE: 'opaque';
STATIC_IF: 'static_if';
IF: 'if';
THEN: 'then';
ELSE: 'else';
ELIF: 'elif';
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
OPEN_CURLY: '{';
CLOSE_CURLY: '}';
COLON: ':';
SEMICOLON: ';';
COMMA: ',';
WITH: 'with';
END_WITH: 'end_with';
OPEN_PAREN: '(';
CLOSE_PAREN: ')';
ARRAY: 'array';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';
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
END_ENUM: 'end_enum';
WEAK: 'weak';
CLASS_HELPER: 'class_helper';
STRUCT_HELPER: 'struct_helper';
GENERIC: 'generic';
IMPLEMENT: 'implement';
CLASS: 'class';
INTERFACE: 'interface';
END_INTERFACE: 'end_interface';
BASE_CLASS: 'base_class';
END_CLASS: 'end_class';
PROPERTY: 'property';
//READONLY: 'readonly';
//WRITEONLY: 'writeonly';
SETTER: 'setter';
GETTER: 'getter';
NAMESPACE_SEP: '::';
BASED_ON: 'based_on';
IMPLEMENTS: 'implements';
// Map$<String, Integer> -- integer map generic type
TYPE_ARGS_OPEN: '$<';
LITERAL: 'literal';
NSTRING: 'nstring';
NWSTRING: 'nwstring';
BASE: 'base';



// literals
fragment ESC: '\\"' | '\\\\';
fragment ESC_CHAR: '\\\'' | '\\\\';
STRING: '"' (ESC|~('\r'|'\n'))*? '"';
RAW_STRING: '"""' .*? '"""';
// only 32-127 ASCII char can be specified
CHAR_STR: '\'' (ESC_CHAR|[ -\u007F])*? '\'';

fragment HEX: [a-fA-F0-9_];
fragment DIGIT: [0-9_];
fragment OCTAL: [0-7_];
fragment BIN: [01_];
ID: [a-zA-Z_]+[0-9a-zA-Z]*;

INT_OCTAL: '-'? '0o' OCTAL+ ('_' OCTAL+)* 'U'?;
INT_HEX: '-'? '0x' HEX+ ('_' HEX+)* 'U'?;
INT_BIN: '-'? '0b' BIN+ ('_' HEX+)* 'U'?;
FLOAT: '-'? ((DIGIT+ ('_' DIGIT+)* '.' DIGIT*) | ('.' DIGIT+)) ([eE][+\-]DIGIT+)? 'f'?;
INTEGER: '-'? DIGIT+ ('_' DIGIT+)* 'U'?;


//////

//ALL: . ;


