=======================
Miod build process
=======================

    - miod_build tool
    - package repository tool
    - compiler


miod_build tool
---------------

Generator mode::
    - reads program definition
    - runs package repository tool to grab package dependencies
    - collects source paths and definitions to the compiler
    - runs Miod compiler to generate C sources
    - collects resources paths from source packages
    - writes CMake rules to execute C compiler, copy
    resources, find_package declarations


Independent mode::
    - reads program/package definition
    - runs package repository tool to grab package dependencies
    - passes source paths and definitions to the compiler
    - copies resources from source packages to the build directory
    - runs C compiler with pkg-config
    - generates runtime and development packages if necessary



Standard library is written in C and Miod. C sources are put side by side
with the unit which uses them, and specified with @_compile annotation.
This C sources are copied to application build directory during the compilation
process. So for every application the system library is recompiled. This
is necessary for the first versions of the language until it gets stabilized,
and it is essential for platforms with complicated build systems and cross-
compiling like Android.

