=======================
Code generation details
=======================

Miod application can consist of several binary targets, i.e. it can be
an embeddable application/static/shared library, a command line executable,
a gui application or a set of them, one main miod source file for each.


Compiler framework must support custom project generation tools. The primary
one to support is CMake, but there should be possibility to implement
e.g. ninja project file generation and pkg-config facilities.


Full unit name *myprog::utils::moduleA* is translated into 
*myprog__utils__moduleA*.

Build tool is not part of the compiler.

C target mappings
-----------------

Modules/units are compiled into *full_unit_name.c/h*, where only public
@_cattr procs and types are defined as *extern* in the header file.

If no exported callbacks/types are defined for usage from C, then no header
file is created.

For classes and interfaces implementation see :doc:`c_oop`.

CMake generator
***************

Compiler can export information necessary to generate CMakeLists.txt for
compilation, linking, installing.

If a package is being built then targets, version etc. cmake files are 
generated to be used with find_package().

Code generated from source packages is put into package named subdirectories
along with a CMakeLists.txt file describing it as a static or shared library.

So the source tree looks like this:

::

    - 'myapp'
        |   main.miod
        |   myapp.mapp
        |   build <temporary dir>
            |   build_out <dir for cmake generated files>
                |   install -- <dir for makeing the final distrubution>
            |   CMakeLists.txt -- generated cmake project
            |   myapp.c
            |   miod__system <dir for miod::system package generated C sources>
                |   CMakeLists.txt -- generated file defines a static library
                |   miod__system__base.c  -- generated miod::system::base unit
                |   miod__system__io.c -- generated miod::system::io unit
                |   interop.c -- copied original C source for FFI, specified
                                 in @_cattr{sources: "interop.c"}



Entry Point and Resources
*************************

Global function **MiodInit** is generated which initializes runtime and
calls program entry proc.

There're classes PackageDataSource and PackageDataLocator which manage finding
and loading package resources. Several PackageDataSource's are created to map
local or global paths to packages.

Paths for source packages
*************************

Local builds from *source packages* (see :doc:`units_packs`) register absolute
paths, so the program is not relocateable.

Installable builds from *source packages* register relative paths (relative
to the binary executable being built).

Java target mappings
--------------------

Top level procs and globals (vars, consts) are put into
*full_package_name.Globals* java class.

Classes, structs etc. are put into *full_package_name.ClassName*.

Additional private runtime information can be put into *full_package_name.MiodRtti* class.


