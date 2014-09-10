Miod programming language, revision 4
=====================================
Language limited to java-friendly implementation with special annotations
for possible C optimizations.

Alter syntax (if necessary) to skip new lines and whitespace.

Union, pointers are not available, but can be accessed using "opaque" type.

extern proc, extern class, extern struct --- native partially "opaque"
declarations. Extern class is only available for Java target.

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
        


Annotations for classes:
    - *nortti* - do not store class name etc., makes dynamic checked casts
impossible (C optimization)
    - *props* - export properties as published (C+Java optimization)


Difference between *alias* and *type*::

    type Abc = opaque # requires direct cast to other opaq derivatives
    alias Bbc = opaque # the same as 'opaque' everywhere


Reference types
---------------

Classes are the only reference objects, if you need a pointer, you use
a class instance.

A variable reference is an excpetion, i.e. procs have const parameters by
default, but there can be 'var' arguments, which are modifiable.

Optimization is possible via 'opaque' types and C native code.

**Weak references** are of two kinds, a) watchable/checked, a proxy, can be implemented using a class instance, and b) optimized/unchecked.

A checked reference is automatically nulled when class instance is freed (
although at a cost of a proxy object), but the unchecked one is not (although
is lightweight).


