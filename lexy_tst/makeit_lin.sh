#!/bin/sh

flex myjson.l
gcc lex.yy.c -lfl

