#ifndef M_SMARTPTR_H_INCLUDED
#define M_SMARTPTR_H_INCLUDED

#include "m_types.h"

/*
    TODO

    1)

    strong reference is a pointer to m_object_header which is the
    header of the BaseObject, maintains reference counter, pointer to
    weak counter structure and finalizer.

    weak reference is a pointer to weak counter structure.

    When a weak reference is requester:
        - if weak_counter == NULL -> create new
        - return pointer to weak_counter
        - inc ref weak counter
        
    When object is finilized:
        - if weak_counter != null
            - weak_counter.object = null
            
    When weak refrence is goes out of scope:
        - dec ref weak_counter
        - if weak_counter == 0:
            - weak.object.weak_counter = null
            - free weak_counter


    2) interfaces

    A strong reference is a pointer to the interface. All reference counting
    activities via calling Interface::asObject()

    Weak reference is a pointer to weak_counter obtained via conversion
        asObject()

    3) Accessing a weak reference in the active code block:
        - allocate temporary strong refrence at the start of the block
        - access object via temprorary reference
*/


// TODO rewrite everything

/// smart pointer
struct _m_smartptr_object_header;

typedef void (*object_finalizer_proc)(struct _m_smartptr_object_header *obj);

typedef struct _m_smartptr_object_header{
    int strong_counter;
    m_weak_counter *weak_counter;    
    m_type_id m_type; /// type to which points, if M_TYPE_INSTANCE then call desctructor on release
    object_finalizer_proc finalize;
	// ... here object data comes ...
} m_smartptr_object_header;

typedef struct {
    m_smartptr_object_header *object;
} m_smart_ptr;

typedef struct {
    int weak_count;
    m_smartptr_object_header *object;
} m_weak_counter;

/// call before any other operations
void m_smart_ptr_init(m_smart_ptr *psp, bool weak,
    object_finalizer_proc finalize, m_smartptr_object_header *obj);

/// call when pointer is no longer in scope
void m_smart_ptr_done(m_smart_ptr *psp);

/// make dst point to src
void m_smart_ptr_assign(m_smart_ptr *dst, m_smart_ptr *src);

/// set the pointer to manage memory for
//void m_smart_ptr_attach(struct m_smart_ptr *dst, void * p, m_type_id m_type);

/// returns pointer value, NULL for dst == NULL etc. safe function
m_smartptr_object_header *m_smart_ptr_get(m_smart_ptr *dst);

m_weak_counter


#endif // M_SMARTPTR_H_INCLUDED

