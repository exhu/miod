#include <stdio.h>
#include <stdlib.h>

#include "miod_runtime/m_smartptr.h"

int main()
{
    struct m_smart_ptr s1;
    struct m_smart_ptr w1;

    m_smart_ptr_init(&s1, false);
    m_smart_ptr_init(&w1, true);

    m_smart_ptr_attach(&s1, malloc(12), M_TYPE_UNSPECIFIED);

    m_smart_ptr_assign(&w1, &s1);

    m_smart_ptr_done(&s1);
    printf("Hello world! s1 = %x\n", s1.ptr_desc);

    m_smart_ptr_done(&w1);


    
    



    return 0;
}
