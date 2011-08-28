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

struct m_class_object {
    struct m_class_object * base; /// NULL for Object
    char * name;
    struct m_interfaces * ifaces;

    /// weak pointer to this
    struct m_smart_ptr safe_this;

    /// virtual methods
    void (*desctructor)(struct m_class_object_instance * p_this);
    /// user defined, compiler-generated follow...
    char * (*to_string)(struct m_class_object_instance * p_this);
};


/// instances, memory layout of any pointer to interface/object instance
struct m_class_object_instance {
    struct m_class_object_instance * p_class;
    /// user defined/compiler generated fields follow...

};
//////////////


extern struct m_class_object g_class_object_definition;

#endif // S_CLASS_H_INCLUDED
