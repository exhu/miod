#!/bin/sh

/opt/local/bin/flex myjson.l
gcc lex.yy.c -L/opt/local/lib -lfl

