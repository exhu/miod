#ifndef M_CLASS_H_INCLUDED
#define M_CLASS_H_INCLUDED

#include "m_smartptr.h"

/// static definitions, i.e. each module of miod has initialized structures which describe classes

struct m_interface {
    char * name;
};

struct m_interfaces {
    struct m_interface * i;
    struct m_interfaces * next; /// NULL to terminate linked list
};

// forward decl
struct m_class_object_instance;
struct m_class_object;

struct m_class_header {
    struct m_class_object * base; /// NULL for Object
    char * name;
    struct m_interfaces * ifaces;
    void (*constructor)(struct m_class_object_instance * p_this);
};

struct m_class_object {
    struct m_class_header header;

    /// weak pointer to this
    struct m_smart_ptr safe_this;    

    /// virtual methods
    void (*desctructor)(struct m_class_object_instance * p_this);
    /// user defined, compiler-generated follow...
    char * (*to_string)(struct m_class_object_instance * p_this);
};


/// instances, memory layout of any pointer to interface/object instance
struct m_class_object_instance {
    struct m_class_object * p_class;
    /// user defined/compiler generated fields follow...

};
//////////////

/// sample derived class struct
struct m_class_derived {
    struct m_class_header header;

    /// base classes with virtual methods initialized to overriden ones
    struct m_class_object base;

    /// interfaces follow...
    /// e.g. struct m_iface_cloneable iface_cloneable;

    /// new virtual methods...
};


extern struct m_class_object g_class_object_definition;

#endif // S_CLASS_H_INCLUDED
