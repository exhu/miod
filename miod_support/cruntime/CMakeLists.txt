cmake_minimum_required (VERSION 2.6)
project (cruntime)
add_library(cruntime 
m_class.c
m_smartptr.c
)

add_subdirectory(tests)

