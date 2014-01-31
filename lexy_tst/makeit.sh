#!/bin/sh

/opt/local/bin/flex myjson_lex.l
gcc lex.yy.c -L/opt/local/lib -lfl

