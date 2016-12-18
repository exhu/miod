#include "m_class.h"
#include "m_types.h"

static void object_desctructor(struct m_class_object_instance * p_this) {

}


static char* object_to_string(struct m_class_object_instance * p_this) {
    return NULL;
}


static void object_constructor(struct m_class_object_instance * p_this) {

}

struct m_class_object g_class_object_definition = {
    // class header
        {
            NULL, // base
            "Object",
            NULL, // interfaces
            &object_constructor,
        },
        {}, // safe_this
        &object_desctructor,
        &object_to_string
    };
