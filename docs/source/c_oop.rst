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

Interfaces
----------

Where a class instance pointer is equal for the base and extended classes
the interface pointer differs.

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

Another approach -- an interface instance stores not only a pointer to the vtable but a *this* pointer as well.

