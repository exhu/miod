Units and packages
==================


Directory layout
----------------
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
    # all files from abc dir are exported by default
    export abc.multipl.consts
    export abc.mu.*
    exclude abc.intern
    # associated package data files can be accessed by application build code
    data "l10n/*"


Application definition
----------------------

my_prog.mapp::

    application
    # directory to copy package associated data files:
    # will put to "pkgdata/abc/l10n/en.strings" etc.
    package_data "pkgdata"
    # link with dynamic libraries (package sets)
    dynamic_sets:
        rtl
        gui


Dynamic precompiled packages
----------------------------

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

