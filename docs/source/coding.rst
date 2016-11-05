Coding style
============

Styling
-------

Unit names in lower case::

    package_name::unit_name.

Predefined simple types in lower case::
    
    int, cardinal.

Classes, structs, custom types in CamelCase::

    GuidData = Struct, String = class, Utf8Sym = struct

Methods and procs start from lower case, continue in camel::

    proc stringFromInt(a: int): String


Naming
------

Procs are named with the first word denoting the return type, e.g.::
    
    proc stringFromIntArray(a: array[int]): String

If bool return type signals success and the returned value is passed via
a *var* argument the first word denotes the action, the returned values
should be the first arguments::

    proc parseIntFromString(out: var int, s: string): bool
    proc openFile(out: var File, out: var FileError, fn: string): bool

No hungarian notation is used, i.e. interfaces are named without prefixes,
specific class implementations may have *Impl* suffix to differ from the
base interface::
    
    FileSystem = interface
        end_interface
        
    FileSystemImpl = class implements FileSystem
        end_class



