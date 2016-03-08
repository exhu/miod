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


Interfaces
----------

Implementing interfaces requires correcting *this* offsets for the methods::

    struct BaseObjInterface {
        int (*hash)(BaseObjInterface *this);
    };
    
    struct ToStringIface {
        char *toString(ToStringIface *this);
    };

    struct MyClassVTbl {        
        BaseObjInterface baseObjInterface;
        ToStringIface toStringIface;
    };

    struct MyClass {
        SuperClass superClass;
        MyClassVTbl vtable;
        char astr[200];
    };

    char *MyClass_toString(ToStringIface *_this) {
        MyClass *this = (MyClass*)((char*)_this - offsetof(MyClassVTbl, toStringIface) - offsetof(MyClass.vtable));
    }

So there we need functions to convert from interface *this* into class instance *this* and vice versa.

