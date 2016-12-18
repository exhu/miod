#include "m_smartptr.h"
#include <malloc.h>

static void dec_ref(struct m_smart_ptr *psp) {
    if (psp->ptr_desc == NULL)
        return;

    if (psp->weak)
        psp->ptr_desc->weak_counter -= 1;
    else {
        psp->ptr_desc->strong_counter -= 1;

        if (psp->ptr_desc->strong_counter == 0) {
            // TODO call desctructor

            free(psp->ptr_desc->ptr_value);
            psp->ptr_desc->ptr_value = NULL;
        }
    }

    if ((psp->ptr_desc->weak_counter == 0) && (psp->ptr_desc->strong_counter == 0)) {
        free(psp->ptr_desc);
    }

    psp->ptr_desc = NULL;
}


static void inc_ref(struct m_smart_ptr *psp) {
    if (psp->weak)
        psp->ptr_desc->weak_counter += 1;
    else
        psp->ptr_desc->strong_counter += 1;
}


void m_smart_ptr_init(struct m_smart_ptr *psp, bool weak) {
    psp->weak = weak;
    psp->ptr_desc = NULL;
}


void m_smart_ptr_done(struct m_smart_ptr *psp) {
    dec_ref(psp);
}


void m_smart_ptr_assign(struct m_smart_ptr *dst, struct m_smart_ptr *src) {
    dec_ref(dst);

    if (src == NULL) {
        dst->ptr_desc = NULL;
        return;
    }

    dst->ptr_desc = src->ptr_desc;

    if (dst->ptr_desc != NULL)
        inc_ref(dst);
}


void m_smart_ptr_attach(struct m_smart_ptr *dst, void *p, m_type_id m_type) {
    dec_ref(dst);

    dst->ptr_desc = NULL;

    if (dst->weak) {
        /// TODO report runtime error, weak pointers cannot be initialized other than from strong ones
        return;
    }


    if (p == NULL)
        return;


    dst->ptr_desc = (struct m_smartptr_desc *) malloc(sizeof(struct m_smartptr_desc));
    dst->ptr_desc->m_type = M_TYPE_UNSPECIFIED;
    dst->ptr_desc->ptr_value = p;
    dst->ptr_desc->strong_counter = 1;
    dst->ptr_desc->weak_counter = 0;
}


void * m_smart_ptr_get(struct m_smart_ptr * dst) {
    if (dst == NULL)
        return NULL;

    if (dst->ptr_desc == NULL)
        return NULL;

    return dst->ptr_desc->ptr_value;
}

