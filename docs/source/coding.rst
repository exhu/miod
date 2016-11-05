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



Error handling convention
-------------------------

Traditional exceptions are not implemented in the language. A procedure which
can return invalid data must support returning error info via an argument.
If argument is null then program must terminate displaying appropriate message.

If a value/non-nullable type is used to pass the storage for the error info
then one should create two functions, e.g.

::

    # function sets 'valid' to true on success
    proc intFromString(s: string, valid: var bool): int

    # function aborts program execution on wrong number format in the string
    proc intFromStringAbort(s: string): int


A more common way can be so that the function returns a boolean flag:

::

    proc parseIntFromString(out: var int, s: string): bool
    proc openFile(out: var File, out: var FileError, fn: string): bool

    # convenience function, aborts program on error
    proc fileOpenAbort(fn: string): File

Error info is lightweight and thus implemented as value objects (structs):

::

    type Error = struct
        message: string
    end_struct


If there's a block of functions which can fail then consider putting them
into a separate proc:

::

    proc execSqls(a, b, c, d: SqlStatement, err: var SqlError): bool
        return a.exec(err) and 
            b.exec(err) and 
            c.exec(err) and
            d.exec(err)
    end_proc

