#pragma once
#include <stddef.h>
#include <stdint.h>

/*
    Class definition produces:

    const char *MIOD_CLASS_ccccc_NAME = "ccccc";

    MIOD_IFACE_iiiii_STRUCT MIOD_CLASS_ccccc__iiiii = {&ccccc__method1, };

    m_interface_item MIOD_CLASS_ccccc__iiiii_ITEM = {
        MIOD_CLASS_ccccc_NAME, offsetof(MIOD_CLASS_ccccc, iiiii),
        &MIOD_CLASS_ccccc__iiiii};

    
    todo...


*/

typedef struct _m_base_object m_base_object;
typedef struct _m_base_interface m_base_interface;

// strong ref to interface is pointer to such member-structure,
// weak ref is pointer to m_weak_counter

typedef struct _m_base_interface {
    struct _m_object_header *(*asObject)(m_base_interface *i);
} m_base_interface;

// sample interface struct
typedef struct {
    m_base_interface base_interface;
    // new methods go here...
} m_user_interface;

// copied for each class that implements a certain interface 
typedef struct {
    const char *name;
    size_t base_object_offset; // base_object instance pointer + offset = interface instance ptr, offsetof(MyClass, interface)
    m_base_interface *interface; // methods table to fill new instance from
} m_interface_item;

// for every class:
typedef struct {
    const char *name; // class name
    const char **base_classes; // names of base classes, NULL terminates the list
    m_interface_item *interfaces; // NULL terminates
    // TODO properties etc RTTI
} m_class;

// weak reference is a pointer to m_weak_counter
typedef struct {
    int weak_count;
    m_base_object *object;
} m_weak_counter;

typedef struct _m_string_object m_string_object;

typedef struct _m_base_object {
    int strong_counter;
    m_class *pclass;
    void (*object_finalizer_proc)(struct _m_base_object *obj);
    int (*hashCode)();
    m_string_object *(*asString)();
    m_weak_counter *weak_counter;
	// ... here object data comes ...
} m_base_object;

