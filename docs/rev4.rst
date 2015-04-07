====================================
Miod Programming Language Revision 4
====================================

Intro
-----

Design goals:    
    - No complex "under-the-hood" features, like C++-RAII, templates,
      exceptions etc.;
    - Cross-platform, cross-target compilation (Java, C);
    - Possible memory and code optimization hints, e.g. memory pools,
      native target types...
    - Semi-automatic memory management, i.e. controllable allocations,
      expected deallocations, yet no tedious book-keeping;
    - Zero-cost foreign function interface.


Language limited to java-friendly implementation with special annotations
for possible C optimizations.

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
a class instance. Arrays are actually classes.

A variable reference is an exception, i.e. procs have const parameters by
default, but there can be 'var' arguments, which are modifiable.

Pointer usage is allowed for C targets 'opaque' types and extern procs.

**Weak references** are of two kinds, a) watchable/checked, a proxy, can be
implemented using a class instance, and b) optimized/unchecked.

A checked reference is automatically nulled when class instance is freed (
although at a cost of a proxy object), but the unchecked one is not (although
is lightweight). All weak pointers are implemented as safe watched ones 
in debug.

Reference counter is incremented on assignment, so possible usage of *weak*
modifier for proc's local variables can be carefully used to optimize.
If unsafe, it'll trigger null pointer dereferencing in debug builds.


Reference counter
-----------------

Class instances are the only automatically reference counted objects in
the language. They are implemented as pointers for the C target.

Temporary objects like the anonymous instance in 
*callSomeProc(new(MyClass))* are automatically decRefed after the call,
the decrement can be called just after the
call or at the enclosing scope leave.

Circular references must be solved manually using the *weak* modifier.

Arguments are weak references if compiled in debug, i.e. the main policy
is for safety, so all behind-the-curtains pointers are backed by
weak references in debug in order to track class instances owning policy
breakage. Arguments are not passed as strong pointers for speed sake.

Weak pointers and references are implemented as proxy objects in C debug
and probably for Java, to detect ownership problems early when writing
both java and c targetted app.

Even with automatic reference counting there can be dangling pointers, as
illustrated below::
    
    var storage: array[MyClass, 10]
    proc main()
        storage[0] = new(MyClass)
        update(0, storage[0]) # passed as weak pointer
    end

    proc update(i: cardinal, o: MyClass)
        storage[i] = null
        storage[i] = o # 'o' is a dangling pointer now, 
        # because it's passed weak and had only one strong reference 
        # in the 'storage' array.
    end

When compiled for **release**, it will **crash**. In **debug** mode, however,
the 'o' becomes a weak (watched) reference and the null reference is
**detected early**.

Optionally one can compile program with all strong pointers, so the code
will work ok at the cost of performance (a lot of inc/dec refs for
arguments passing).

Reference counting in Java
--------------------------

Classes which depend on reference counting can be marked as such with
annotations so that when compiled for Java they maintain a reference counter
and their destructor is called not in the 'finalize' method, but when the
counter reaches zero. This can be useful for objects which hold 
some system resources. e.g. files.

But this feature is still questionable because it complicates things. Probably
it will be implemented in an only *all-or-none* principle for debugging
purpose.


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


Package definition file (mpkg)
------------------------------

Specifies version.
Enumerates the packages it depends on.
Specifies optional find_package() etc. cmake directives if uses native C
libraries, or specifies JNI libraries.
TBD

Application definition file (mapp)
----------------------------------

The same as for mpkg but also can specify executable mode for Windows,
gui or console.

TBD



'With' statement
----------------

A *with-end_with* statement block encloses a new scope, where members of
specified structure/class become available without prefix. E.g.::

    type MyStruct = struct
        x, y, z: int
        end_struct

    proc setupStruct(a: var MyStruct)
        with a
            x = 3
            y = x+5
            z = x/y
        end_with
    end


Classes
-------

As exceptions are not supported by the language, the constructors are not
supported too. All fields are initialized to zeroes.

Destructors are not guaranteed to execute as well, i.e. if target language
is Java and no reference-counting is used.

'final'
-------

'final' used with a method or class forbids derived classes and/or methods.

'const'
-------

Declares a variable as immutable. For simple types it can even place
values verbatim at places where the constant is used (compiler implementation
specific).


'finally' without a 'try'?
--------------------------

Although there's no *try* keyword and exceptions support, there is a
convenient *finally* keyword to mark a code block, executed at the end of the
enclosing scope::

    proc readData(fileName: nstring): bool
        var f = fileOpen(fileName, "rb")
        finally
            f.close()
        end_finally

        # do some work with file which can cause read error etc.

        # *return* leaves the scope so the finally block gets executed here:
        if error then return false end_if
        
        return true
    end


'private', 'protected', 'public'
--------------------------------

*Private* symbols are accessible only from the same compilation unit.
*Protected* symbols are visible to the units within the same level (dot-path).
*Public* symbols can be used from everywhere. 

Standard library
----------------

There will be a set of packages. The standard one is minimal and contains
only compiler-dependent features like memory allocation, strings, RTTI and
reflection, base object class, strong/weak references, debug features...

However collections, Unicode, Network, Threads will be in separate packages.
Along with standard collections there will be *intrusive lists* etc.



Basic types
-----------

=============================  ==============================================
Type name                       Comments
=============================  ==============================================
int8
int16
int32
int64
uint8
uint16
uint32
uint64 (unsupported?)
float32
float64
nchar                          only first 127 ASCII codes as
                               literals are allowed
nstring                         Immutable!
nwchar                         only first 127 ASCII codes as
                               literals are allowed
nwstring                        Immutable!
cardinal
range type
enum
enum<nstring>
enum<nwstring>

opaque                          Used to simplify bindings, e.g.
                                to describe type that is available
                                in target language only. Requires
                                annotations.
int
long
array<int>                      Passed by reference to functions
array<int, 120>
String                          Immutable string class with
                                hash code support
proc()
proc(), class                   Instance pointer is guarded as
                                weakref in debug builds!

float                           alias for float32
double                          alias for float64
literal<nstring|nwstring>(l)    Returns identifier (unit, class, var,
                                type, enum etc.) name as string
weak<typename>                  Plain object instance pointer type in release
weak_ref<typename>              WeakRef<> in debug/release
cast<typename>(a)               Converts type, if typename == class, then
                                returns null in debug mode if 'a' is not a
                                descendant of 'typename'. May crash in release.
cast_inst<class>(a)             Checks if 'a' is 'class' descendant, returns
                                null otherwise.

var int                         Argument passed by reference, plain pointer in
                                C, cannot be stored as class/struct field or
                                variable, constant.
=============================  ==============================================



Type/expression mappings to Java
--------------------------------

=============================  =============================================
Miod                            Java
=============================  =============================================
int8                            byte
int16                           short
int32                           int
int64                           long
uint8                           byte, extended to int in arithmetic 
uint16                          short, extended to int in arithmetic
uint32                          int, extended to long in arithmetic
uint64 (unsupported?)           long, displays warning
float32                         float
float64                         double
nchar                           char
nstring                         String
nwchar                          char
nwstring                        String
cardinal                        int, checked for under/overflow
range type                      int, bounds are checked
enum                            int
enum<nstring>                   String
enum<nwstring>                  String
opaque                          class instance or plain type
int                             int
long                            long
array<int>                      array object, cloned or set on assignment
array<int, 120>                 array object, cloned or set on assignment
String                          maps to String object, plus additional
                                functions, like fromUtf8, toUtf8 etc.
proc()                          Interface instance, which calls appropriate 
                                proc.
proc(), class                   Interface instance with instance field,
                                which calls appropriate proc on instance.
literal<nstring|nwstring>(l)     String "l"
weak<typename>                  WeakRef<> in debug
weak_ref<typename>              WeakRef<> in debug/release
var int                         Argument passed by reference, 
                                boxed value (anonymous class)
=============================  =============================================




Type/expression mappings to C
-----------------------------

=============================  =============================================
Miod                            C
=============================  =============================================
int8                            char
int16                           short
int32                           int
int64                           int64
uint8                           unsigned char
uint16                          unsigned short
uint32                          unsigned int
uint64 (unsupported?)           unsigned int64
float32                         float
float64                         double
nchar                           char
nstring                         char*
nwchar                          wchar_t
nwstring                        wchat_t*
cardinal                        int, checked for under/overflow
range type                      int, bounds checked
enum                            int
enum<nstring>                   char*
enum<nwstring>                  wchar_t*
opaque                          pointer to struct instance or plain type
int                             int
long                            long long
array<int>                      Struct { int * ptr, int sz }
array<int, 120>                 int arr[120], const int arr_sz = 120
String                          maps to char*, plus lengths, utf8 functions.
proc()                          plain function pointer
proc(), class                   Struct { void (\*proc)(), void * inst }
literal<nstring|nwstring>(l)    char* | wchar_t*
weak<typename>                  weak reference with checks in debug
weak_ref<typename>              weak reference with checks in debug/release
var int                         Argument passed by reference, plain pointer in
                                C.
=============================  =============================================


Debug/Release differences
-------------------------

+-------------------------+--------------------+----------------------------+
|Options                  |   Release          |      Debug                 |
+=========================+====================+============================+
|guard weak pointers      |                    |       +                    |
|treating as weakref      |                    |                            |
+-------------------------+--------------------+----------------------------+
|guard weak pointers to   |                    |       +                    |
|method callbacks         |                    |                            |
|(as weakref)             |                    |                            |
+-------------------------+--------------------+----------------------------+
|generate rtti type info  |                    |       +                    |
|for “nortti” types for   |                    |                            |
|dynamic typecasts        |                    |                            |
+-------------------------+--------------------+----------------------------+
|pass arguments as weak   |       +            |       +                    |
|pointers                 |                    |                            |
+-------------------------+--------------------+----------------------------+
|pass arguments as        |   Non-default      |       Non-default          |
|strong pointers          |                    |                            |
+-------------------------+--------------------+----------------------------+




Type promotions, inference, convertions
---------------------------------------

Floats and integers cannot be mixed in arithmetic expressions, explicit
type casts are required.

Type declarations like 'type mytype = int' introduce a distinct type, which
emits warnings if automatic conversion from 'int' is used, e.g.
::

    type IOMask = int32
    IOMaskFlags = enum<IOMask>
        read = 0x1,
        write = 0x2,
        append = 0x4,
    end_enum

    var m: IOMask = 3  # generated warning


Loops and Iterators
-------------------

'for' '(' item[, index]? in collection ')' statements 'end_for'

For general collections one should use the following *iterator pattern*.
An iterator is a class with a proc 'next' of boolean return value,
proc 'item' of element type. Optionally there can be a 'reset' proc to
reiterate after applying modifications to the collection.

An iterator is initialized to invalid (-1) item, so that 'next' must be called
before the first element is available in 'item'.
::

    while(it.next())
        print(it.item())
    end_while


GCC -O1 perfectly optimizes struct with item(), next() when they are static,
so creating a proc which returns a structure and has next/item methods
equals to for(int a = 0; a < 16; a+=2). This way we can write:
::

    type IrangeLoop = struct
        counter, step, max: int

        proc next(): bool, inline
            var r = counter < max
            if r then 
                counter += step
            end_of
            return r
        end_struct

        proc item(): int, inline
            return counter
        end_proc

    proc irange(int from, int to, int step): IrangeLoop
        var lp: IrangeLoop
        lp.counter = from - step;
        lp.step = step
        lp.max = to
        return lp
    end_proc


'alias' keyword
---------------

Can be used with types and fully-qualified names as a shortcut. However it is
not a plain substitution, the introduced alternative name is bound to the
scope where it is defined.

::
    
    unit org::prog::consts
    alias int = int32 # every 'int' will be replaced by 'int32'
    alias consts = org::prog::consts # this makes a 'consts::usualFlag' possible
    const emptyFlag = 0, usualFlag = 3
    


Error handling convention
-------------------------

Traditional exceptions are not implemented in the language. A procedure which
can return invalid data must support returning error info via an argument.
If argument is null then program must terminate displaying appropriate message.

If a value/non-nullable type is used to pass the storage for the error info
then one should create two functions, e.g.

::

    # function sets 'valid' to true on success
    proc parseNumber(s: string, valid: var bool): int

    # function aborts program execution on wrong number format in the string
    proc parseNumberAbort(s: string): int


A more common way can be so that the function returns a boolean flag:

::

    proc parseNumber(s: string, out: var int): bool
    proc openFile(fn: string, out: var File, out: var FileError): bool

    # convenience function, aborts program on error
    proc openFileAbort(fn: string): File

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


