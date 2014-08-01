parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

comp_unit: unit_header unit_body?;
unit_header: doc_comments? UNIT DOT_NAME end_stmt;
unit_body: global_stmts;

global_stmt: static_if
    | global_decl
    | NEWLINE
    | doc_comments
    ;

global_stmts: global_stmt (NEWLINE global_stmt)*;

static_if: STATIC_IF const_expr THEN global_stmts? (ELSE global_stmts?)? ENDIF;

global_decl: const_decl
    | var_decl
    | proc_decl
    | type_decl
    | import_decl
    | include_decl
    ;

const_decl: CONST NEWLINE #ConstSection
    | CONST NON_DOT_NAME TYPE_DECL? ASSIGN const_expr #ConstDecl
    ;

const_expr: global_expr;

/// no function calls
global_expr: expr;

expr: literal
    ;

literal: NULL
    | INTEGER
    | INT_OCTAL 
    | INT_HEX
    | INT_BIN
    | FLOAT
    | DOT_NAME
    | STRING
    | RAW_STRING
    | CHAR_STR
    | TRUE
    | FALSE
    ;


var_decl: VAR;

proc_decl: PROC;

type_decl: TYPE;

import_decl: IMPORT;

include_decl: INCLUDE STRING;

doc_comments: DOC_COMMENT+;

end_stmt: NEWLINE | EOF;
