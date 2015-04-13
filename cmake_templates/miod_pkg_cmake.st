# StringTemplate format

cmake_minimum_required(VERSION 3.1)


include(CMakePackageConfigHelpers)
#include(GenerateExportHeader)

project(somelib)

set(SOMELIB_VERSION 1.0.0)
set(SOMELIB_INCLUDE_DIR_INST include/somelib)

# TODO add dependency on somelib2, so that someapp depends on somelib, and
# somelib uses somelib2 functions

add_library(somelib somelib.c somelib.h)

#generate_export_header(somelib)

install(TARGETS somelib EXPORT somelib_targets 
    ARCHIVE DESTINATION lib
    INCLUDES DESTINATION ${SOMELIB_INCLUDE_DIR_INST}
    LIBRARY DESTINATION lib
    )

install(FILES somelib.h 
#    "${CMAKE_CURRENT_BINARY_DIR}/somelib_export.h"
    DESTINATION ${SOMELIB_INCLUDE_DIR_INST})

#export(EXPORT somelib_targets 
#    NAMESPACE somelib
#    FILE "${CMAKE_CURRENT_BINARY_DIR}/somelib-targets.cmake")

set(ConfigPackageLocation lib/somelib/cmake)

configure_package_config_file(somelib-config.cmake.in
    ${CMAKE_CURRENT_BINARY_DIR}/somelib-config.cmake
    INSTALL_DESTINATION ${ConfigPackageLocation})
#    PATH_VARS INCLUDE_INSTALL_DIR)

write_basic_package_version_file(
    ${CMAKE_CURRENT_BINARY_DIR}/somelib-config-version.cmake
    VERSION ${SOMELIB_VERSION}
    COMPATIBILITY SameMajorVersion)

install(EXPORT somelib_targets FILE somelib-targets.cmake NAMESPACE somelib::
    DESTINATION ${ConfigPackageLocation})

install(FILES 
${CMAKE_CURRENT_BINARY_DIR}/somelib-config.cmake
    ${CMAKE_CURRENT_BINARY_DIR}/somelib-config-version.cmake
    DESTINATION ${ConfigPackageLocation} )



