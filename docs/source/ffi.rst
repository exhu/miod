==========================
Foreign Function Interface
==========================

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


See also :doc:`codegen`

FFI for C target
----------------

Callbacks for C are declared using *@_cattr* for types and implementation.
Headers and sources can be specified in one of the cattr annotations using
*header* and optionally *source* keys.

C procs are declared using *extern proc*.


Byte buffer example
*******************

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
        


C annotations explained
***********************

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


@_assume_class{name:"mypackage.MyClass"} before interface type variable
declaration suggests that the actual interface pointer is used for class 
instance of the certain type only. This can be used to help devirtualization
preserving the clean public interface. In debug mode it checks assignment.

FFI for Java target
-------------------


Callbacks for java are declared using *extern* interfaces.

Static procs are declared as *extern proc* with *@_jattr{name: ""}* with fully
qualified name specified in the annotation.


