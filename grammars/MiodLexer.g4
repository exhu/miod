lexer grammar Miod;

NEWLINE: '\r'? '\n';
WS: (' ' | '\t')+ -> skip;

JOIN_LINE: '\\' NEWLINE -> skip;

// comments

DOC_COMMENT: '##' -> pushMode(DOC_COMMENTS);
COMMENT: '#' .*? NEWLINE -> skip;

// keywords

// literals

// IDs

//////

ALL: . ;

mode DOC_COMMENTS;

DOC_LINE: .*? NEWLINE -> popMode;


