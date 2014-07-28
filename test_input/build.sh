#!/bin/bash

antlr4 -o ./ `readlink -f ../grammars/MiodLexer.g4` `readlink -f ../grammars/MiodParser.g4`
javac *java
