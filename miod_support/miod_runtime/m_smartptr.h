#ifndef M_SMARTPTR_H_INCLUDED
#define M_SMARTPTR_H_INCLUDED

#include "m_types.h"

/// smart pointer

struct m_smartptr_desc {
    long strong_counter;
    long weak_counter;
    void * ptr_value;
    m_type_id m_type; /// type to which points, if M_TYPE_INSTANCE then call desctructor on release
};

struct m_smart_ptr {
    struct m_smartptr_desc * ptr_desc;
    bool weak; /// if weak, then change weak_counter, else change strong_counter.
};


/// call before any other operations
void m_smart_ptr_init(struct m_smart_ptr * psp, bool weak);

/// call when pointer is no longer in scope
void m_smart_ptr_done(struct m_smart_ptr * psp);

/// make dst point to src
void m_smart_ptr_assign(struct m_smart_ptr * dst, struct m_smart_ptr * src);

/// set the pointer to manage memory for
void m_smart_ptr_attach(struct m_smart_ptr * dst, void * p, m_type_id m_type);

/// returns pointer value, NULL for dst == NULL etc. safe function
void * m_smart_ptr_get(struct m_smart_ptr * dst);


#endif // M_SMARTPTR_H_INCLUDED
