======================
Implementation Details
======================

Scope stack
-----------

Scope type:

    - module
    - proc
    - block (if, block etc.), static_if does not create a scope
    - struct
    - enum (?)
    - class

Dotted name resolution:
    
    1) split by dot
    2) descend from top scope by name


Scope declaration when compiling to target language:
    
    - define at function start all vars from all blocks inside the proc
    - proc vars are named with prefixes, see `Name Mangling`_.

Name Mangling
-------------

Globals are prefixed with:
    a) "md" for vars, procs
    b) "Md" for types, structs, classes, interfaces, enums etc.

C target
~~~~~~~~
The globals are prefixed with full package
name with dots replaced with underscores and a generic prefix, e.g.:
    
    - pkgname_subpkg_MdTypeName

For @_cattr procs the name is given as in the annotation.

Java target
~~~~~~~~~~~

Class names are prefixed with upper case, methods with lower case.
All generated code is put into "md" package.

