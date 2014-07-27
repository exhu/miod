lexer grammar MiodLexer;

fragment NL: ('\r'? '\n');
NEWLINE: NL;
WS: (' ' | '\t')+ -> skip;

JOIN_LINE: '\\' NEWLINE -> skip;

// comments

DOC_COMMENT: '##' .*? (NEWLINE|EOF);
COMMENT: '#' .*? (NEWLINE|EOF) -> skip;

// keywords

// literals
fragment ESC: '\\"' | '\\\\';
STRING: '"' (ESC|~('\r'|'\n'))*? '"';


// IDs

//////

ALL: . ;


