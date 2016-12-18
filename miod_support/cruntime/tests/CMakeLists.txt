cmake_minimum_required (VERSION 2.6)
project (tests)
add_executable(tests main.c)
target_link_libraries(tests cruntime)

