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



