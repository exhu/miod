======================
Implementation Details
======================

Scope stack
-----------

Scope type:

    - module
    - proc
    - block (if, with, block etc.), static_if does not create a scope
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

Identifiers
~~~~~~~~~~~

*Qualified* -- dot-separated ('::') fully qualified, used as type names,
module names, in expressions.

*Clean IDs* -- identifiers without dots, used in var, const, proc, type
declarations.

Memory pooling
~~~~~~~~~~~~~~

New construct or special function and annotation to allocate arrays of
objects sequentially::

    type
        SSEFriendlYArray = @_sse array[int32]
        PoolData = @_sse array[MyClass]
    
        ClassesPool<T> = class
            proc init(count: cardinal)
            end

            proc newCls(): T
            end

        extern proc initArrayOfObjects(var o: PoolData)


Such instances can be deallocated only all at once.

TODO: invent a proper way to declare such pools.

Since it is a very low-level feature, it is probably better to leave it for
the C language.
