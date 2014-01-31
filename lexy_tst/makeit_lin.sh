#!/bin/sh

flex myjson_lex.l
gcc lex.yy.c -lfl

