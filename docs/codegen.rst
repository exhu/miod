=======================
Code generation details
=======================

Full unit name *myprog::utils::moduleA* is translated into 
*myprog__utils__moduleA*.


C target mappings
-----------------

Modules/units are compiled into *full_unit_name.c/h*, where only public
@_cattr procs and types are defined as *extern* in the header file.
If no exported callbacks/types are defined for usage from C, then no header
file is created.

CMake
*****

Compiler produces CMakeLists.txt for compilation, linking, installing.

If a package is being build then targets, version etc. cmake files are 
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



Java target mappings
--------------------

Top level procs and globals (vars, consts) are put into
*full_package_name.Globals* java class.

Classes, structs etc. are put into *full_package_name.ClassName*.

Additional private runtime information can be put into *full_package_name.MiodRtti* class.


