Units and packages
==================

A Miod application is the final product to be used as a standalone executable
or a set of executables (one per source file), a static or shared library
for using in C/C++ products, plugins etc. So it's a set of binary targets.

A Miod package is a development or runtime component that can be of the
following types:

    a) **source package**, it is a set of binary data and source
    files to be used by a Miod application during the build process,
    the data files to be copied when building the installable application.

    b) **runtime package**, it is a set of binary data and compiled binaries
    (shared libraries) -- the resources are not compiled to the application.

    c) **runtime development package** -- contains preprocessed sources,
    depends on the **runtime package**.

    d) **development embeddable package** -- binary data, static libraries,
    preprocessed sources. The data is copied when build the installable app.

In future there can also be a dynamic set feature support -- a shared library 
that contains several independent packages. This is perfect for a big runtime
library.

The package definition file specifies binary targets and dependencies. It
lists shared/common dependencies and data, and dependencies per target.

A development package usually contains a default target to build a static
or shared library and a **tests** target, which builds an executable and links
to some unit-testing framework.

First Miod versions will support source packages only.

For resources paths see :doc:`codegen`.

Build Modes
-----------

There can be

    a) local build
        The build is run in-place, package data and dependencies are not
        copied.

    b) installable build
        All dependencies are copied and the build will run correctly only
        after installation.

A resource module is generated during build which maps package data directories
to global ones.

Directory layout
----------------

Package definition file (mpkg)
******************************

Specifies version, targets.
Enumerates the packages it depends on.
Specifies optional find_package() etc. cmake directives if uses native C
libraries, or specifies JNI libraries.
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

TODO obsolete

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

TODO obsolete

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

