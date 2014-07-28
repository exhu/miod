parser grammar MiodParser;
options {tokenVocab = MiodLexer; }

comp_unit: unit_header unit_body;
unit_header: UNIT UNIT_NAME;
unit_body: global_stmt+;

global_stmt: static_if
    |   global_decl;

static_if: STATIC_IF const_expr THEN global_stmt (ELSE global_stmt)? ENDIF;
const_expr: ;

global_decl: const_decl
    | var_decl
    | proc_decl
    | type_decl
    | import_decl;

const_decl: CONST;

var_decl: VAR;

proc_decl: PROC;

type_decl: TYPE;

import_decl: IMPORT;


