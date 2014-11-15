#!/bin/bash

ABSPATH=`cd "../grammars"; pwd`


antlr4 -o ./ $ABSPATH/MiodLexer.g4 $ABSPATH/MiodParser.g4
javac *java
