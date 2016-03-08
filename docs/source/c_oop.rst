C codegenerator implementation of classes and interfaces
========================================================

In C++ terms one can image a BaseObject interface and other interfaces
inherit it as virtual.

Class inheritance
-----------------

There's a vtable for each class::

    struct MyClassAVTbl {
        void (*doA)(MyClassAVTbl *this);
    };
    
    struct MyClassA {
        MyClassAVTbl *vtable;
        int field1;
    };

    void MyClassA_InitVTable();

    struct MyClassB {
        MyClassA superClass;
        MyClassBVTable *vB;
        int fieldForB;
    };

    void MyClassB_InitVTable();

So this is very simple::

    - static vtable for class, reused between instances
    - vtable is duplicated for each descendant class
    - cast to/from super class requires no pointer manipulation

For every extended class there's a copy of vtables for the super classes if
there are any overriden methods of the base class, otherwise the vtable of the
super class is reused via pointer.

Interfaces
----------

Where a class instance pointer is equal for the base and extended classes
the interface pointer differs.

To solve the problem two solutions described below. The first one was chosen
for its simplicity and for being slightly faster in a benchmark.

An interface instance pointer stores not only a pointer to the vtable but
a *this* pointer for the class instance as well. Thus each interface pointer
becomes two times bigger than a usual class instance pointer::
    
    struct InterfacePtr {
        InterfaceVTable *vtable;
        void *this;
    };

Interfaces that extend/share another interfaces contain a pointer to the super
interface in their vtable::

    struct MyInterfaceVTable {
        struct BaseObjectInterface *super;
        int (*MyMethod)(void *this);
    };

So every interface has a static *vtable* which can be referenced by the
subinterfaces via their vtable.

For every class there's a vtable for every interface it implements::

    struct MyClassVTable {
        BaseObjectInterfaceVtable *vtable1;
        SuperClass1 *vtable2;
    };

The class hierarchy is known at compile time, so interface vtable pointers
are not duplicated for classes which already have such pointers in super classes'
vtables or interfaces::

    class BaseObject implements BaseObjectInterface;
    interface MyInterface extends BaseObjectInterface and ToStringInterface;
    class MyObject extends BaseObject implements BaseObjectInterface and MyInterface;

    struct BaseObjectInterfaceVtable {
        int (*hash)(void *this);
    };

    struct ToStringVtable {
        BaseObjectInterfaceVtable *super;
        char* (*ToString)(void *this);
    };

    struct BaseObjectVtable {
        BaseObjectInterfaceVtable *super;
    };

    struct MyInterfaceVtable {
        BaseObjectInterfaceVtable *super1;
        ToStringVtable *super2;
    };

    struct MyObjectVtable { // no BaseObjectInterface vtable because it's defined in BaseObject
        BaseObjectVTable *super1;
        MyInterfaceVTable *super2;
    };


The following solution was abandoned. 

When a class implements an interface the former acquires additional vtable entries
mirroring the new overriden methods and a vtable for the interface itself.
This also requires two sets of methods -- the usual class methods and the interface
methods which correct the *this* pointer::

    struct BaseObjIfaceVTbl {
        int (*hash)(BaseObjIface *this);
    };
    
    struct ToStringIfaceVTbl {
        char *(*toString)(ToStringIface *this);
    };

    struct BaseObjIfaceInst { // runtime instance of interface for object
        BaseObjIfaceVTbl *vtable;
    };

    struct TwoIfaces { // extends BaseObjIface and ToStringIface
        BaseObjIfaceVTbl *superA;
        ToStringIfaceVTbl *superB;
    };

    struct MyClassVTbl {        
        BaseObjInterface baseObjIface;
        ToStringIface toStringIface;
    };

    struct MyClass { // implements BaseObjInterface and ToStringIface
        SuperClass superClass;
        MyClassVTbl *vtable;
        char astr[200];
    };

    char *MyClass_toString_thunk(ToStringIface *_this) {
        MyClass *this = (MyClass*)((char*)_this - offsetof(MyClassVTbl, toStringIface) - offsetof(MyClass.vtable));
        return MyClass_toString(this);
    }

So there we need functions to convert from interface *this* into class instance *this* and vice versa.

This requires two vtables, the one within the class hierarchy (implemented interface methods are
virtual methods of the class) and the other one for interfaces. So the interfaces
vtable containts pointers to *thunk* proxies which convert *this* pointer and pass over to the instance's methods.


