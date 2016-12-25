// OBSOLETE

#include "m_smartptr.h"
#include <malloc.h>

#if 0

static void dec_ref(m_smart_ptr *psp) {
    if (psp->object == NULL)
        return;

    if (psp->weak)
        psp->object->weak_counter -= 1;
    else {
        psp->object->strong_counter -= 1;

        if (psp->object->strong_counter == 0) {
            // call desctructor
            psp->object->finalize(pst->object);
            free(psp->object);
            psp->ptr_desc->ptr_value = NULL;
        }
    }

    if ((psp->ptr_desc->weak_counter == 0) && (psp->ptr_desc->strong_counter == 0)) {
        free(psp->ptr_desc);
    }

    psp->ptr_desc = NULL;
}


static void inc_ref(m_smart_ptr *psp) {
    if (psp->weak)
        psp->ptr_desc->weak_counter += 1;
    else
        psp->ptr_desc->strong_counter += 1;
}


void m_smart_ptr_init(m_smart_ptr *psp, bool weak,
    object_finalizer_proc finalize, m_smartptr_object_header *obj) {
    psp->weak = weak;
    psp->finalize = finalize;
    psp->object = obj;
}


void m_smart_ptr_done(m_smart_ptr *psp) {
    dec_ref(psp);
}


void m_smart_ptr_assign(m_smart_ptr *dst, m_smart_ptr *src) {
    dec_ref(dst);

    if (src == NULL) {
        dst->ptr_desc = NULL;
        return;
    }

    dst->ptr_desc = src->ptr_desc;

    if (dst->ptr_desc != NULL)
        inc_ref(dst);
}


void m_smart_ptr_attach(m_smart_ptr *dst, void *p, m_type_id m_type) {
    dec_ref(dst);

    dst->ptr_desc = NULL;

    if (dst->weak) {
        /// TODO report runtime error, weak pointers cannot be initialized other than from strong ones
        return;
    }


    if (p == NULL)
        return;


    dst->ptr_desc = (m_smartptr_desc *) malloc(sizeof(m_smartptr_desc));
    dst->ptr_desc->m_type = M_TYPE_UNSPECIFIED;
    dst->ptr_desc->ptr_value = p;
    dst->ptr_desc->strong_counter = 1;
    dst->ptr_desc->weak_counter = 0;
}


void *m_smart_ptr_get(m_smart_ptr * dst) {
    if (dst == NULL)
        return NULL;

    if (dst->ptr_desc == NULL)
        return NULL;

    return dst->ptr_desc->ptr_value;
}

#endif

