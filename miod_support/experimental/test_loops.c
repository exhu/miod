#include <ctype.h>
#include <stdbool.h>
#include <stdio.h>

typedef struct {
    int counter;
    int step;
    int max;
} loop_t;


static bool next(loop_t * l) {
    bool r = l->counter < l->max;
    if (r)
        l->counter += l->step;
    return r;
}

static int item(loop_t * l) {
    return l->counter;
}


void testloop() {
    loop_t lp = {-2, 2, 16};
    while(next(&lp)) {
        printf("item = %i\n", item(&lp));
    }   
}

void testloopC() {
    for(int i = 0; i < 16; i += 2) {
        printf("item = %i\n", i);
    }   
}

int main() {
    testloop();
    return 0;
}
