Miod programming language, revision 4
=====================================
Language limited to java-friendly implementation with special annotations
for possible C optimizations.

Alter syntax (if necessary) to skip new lines and whitespace.

Union, pointers are not available, but can be accessed using "opaque" type.

extern proc, extern class, extern struct --- native partially "opaque"
declarations. Extern class is only available for Java target.

How to access a byte buffer for C target:

1) Miod declarations
::
        type Buf = opaque
        extern proc get_byte(b: Buf, ofs: int): byte
        extern proc set_byte(b: Buf, ofs: int, v: byte)


2) C impl
::
        typedef char* Buf;

        inline uchar get_byte(Buf b, int ofs) {
            return b[ofs];
        }
        inline void set_byte(Buf b, int ofs, uchar b) {
            b[ofs] = b;
        }
        


Annotations for classes:
    - *nortti* - do not store class name etc., makes dynamic checked casts
impossible (C optimization)
    - *props* - export properties as published (C+Java optimization)


Difference between *alias* and *type*::
    type Abc = opaque # requires direct cast to other opaq derivatives
    alias Bbc = opaque # the same as 'opaque' everywhere



