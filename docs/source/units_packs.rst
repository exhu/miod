Units and packages
==================

A Miod application is the final product to be used as a standalone executable
or a set of executables (one per source file), a static or shared library
for using in C/C++ products, plugins etc. So it's a set of binary targets.

A Miod package is a development component that can be of two types:

    a) it is a set of binary data and source files to be used by a Miod
        application during the build process.

    b) it is a set of binary data and compiled binaries (static library or
        shared library) with preparsed sources.

In future there can also be a dynamic set feature support -- a shared library 
that contains several independent packages. This is perfect for a big runtime
library.



Directory layout
----------------

Package definition file (mpkg)
******************************

Specifies version.
Enumerates the packages it depends on.
Specifies optional find_package() etc. cmake directives if uses native C
libraries, or specifies JNI libraries.
TBD


Application definition file (mapp)
**********************************

The same as for mpkg but also can specify executable mode for Windows,
gui or console.

TBD

directory::

    - abc (DIR)
        | my1.miod
        | intern.miod
        | multipl (DIR)
            | consts.miod
        | l10n
            | en.strings
    - abc.mpkg (FILE)


abc.mpkg::

    package abc
    version 1.0
    description "some generic package"
    # all source files from abc dir are exported by default
    # hide symbols when building shared
    hide abc::intern
    # associated package data files can be accessed by application build code
    data "l10n/*"
    c_package(get_text, 1.3) # cmake/pkg-config requirement


Application definition
----------------------

my_prog.mapp::

    # application
    # directory to copy package associated data files:
    # will put to "pkgdata/abc/l10n/en.strings" etc.
    package_data "pkgdata"
    target_app(main.miod):
        # link with dynamic libraries (package sets)
        dynamic_sets:
            rtl
            gui


Dynamic precompiled packages (dynamic sets)
-------------------------------------------

rtl.mdyn::

    # name of the DLL:
    dynamic_set rtl
    packages:
        system
        os
        strings
        unicode
        buffers

    package_data "/usr/share/miod/dyn/pkgdata"

