==========================
Foreign Function Interface
==========================

.. contents:: Table of Contents

Introduction
------------

FFI is defined using special annotations, 'opaque' type, extern procs,
structs and classes.

Union, pointers are not available, but can be accessed using "opaque" type.

extern proc, extern class, extern struct --- native partially "opaque"
declarations. Extern class is only available for Java target.


Difference between *alias* and *type* for 'opaque'::

    type Abc = opaque # requires direct cast to other opaq derivatives
    alias Bbc = opaque # the same as 'opaque' everywhere


FFI for C target
----------------

How to access a byte buffer for C target:

1) Miod declarations::

        type Buf = opaque
        extern proc get_byte(b: Buf, ofs: int): byte
        extern proc set_byte(b: Buf, ofs: int, v: byte)


2) C impl::

        typedef char* Buf;

        inline uchar get_byte(Buf b, int ofs) {
            return b[ofs];
        }
        inline void set_byte(Buf b, int ofs, uchar b) {
            b[ofs] = b;
        }
        


C annotations
*************

@_cattr{headers:["<stdint.h>", "a.h", "b.h", sources:["mycallbacks.c", "aa.c"]}
before *unit* statement.

@_cattr{name: "native_name"} before *type*, *extern proc* or *extern struct
field name* specifies the verbatim name in the generated C code.

A user must define a typedef for types that are used as pointers, e.g. FILE* ::

    "myfile.h":
    typedef FILE* PFILE

    "myfile.miod"
    @_cattr{headers: ["myfile.h"]}
    unit myfile

    type
        @_cattr{name: "PFILE"}
        File = opaque

    extern proc fopen(fn: cstring, mode: cstring): File


