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


Java target mappings
--------------------

Top level procs and globals (vars, consts) are put into
*full_package_name.Globals* java class.

Classes, structs etc. are put into *full_package_name.ClassName*.

Additional private runtime information can be put into *full_package_name.MiodRtti* class.

Callbacks for java are declared using *extern* interfaces.

Static procs are declared as *extern proc* with *@_jattr{name: ""}* with fully
qualified name specified in the annotation.


C target mappings
-----------------

Modules/units are compiled into *full_package_name.c/h*, where only public
members are defined as *extern* in the header file.

Callbacks for C are declared using *@_cattr* for types and implementation.
Headers and sources can be specified in one of the cattr annotations using
*header* and optionally *source* keys.

C procs are declared using *extern proc*.


Packages
--------

There are several meanings to the word *package* in the Miod context:

    1) dot-separated paths to modules;

    2) a compiled set of data and binary modules:

        a) statically linked library and optional data files,
        copied during program linking stage.
        
        b) dynamically linked library and optional data files, stored
        in the filesystem.
    

There's always a package definition file or optional application definition
file. If there's not any application definition file with the app sources
then one must provide used packages via command line arguments or
comments/uses clause (TBD).


Application/package directory layout
------------------------------------
::

    - 'myapp' or 'mypkg' (DIR)
        |   main.miod
        |   l10n (DIR)
            |   en.strings

    - 'myapp.mapp' or 'mypkg.mpkg'


Application definition file (mapp)
----------------------------------

TBD


Package definition file (mpkg)
------------------------------

TBD


