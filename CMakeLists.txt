cmake_minimum_required(VERSION 3.15)

project(TimeChecker)

file(GLOB SOURCE_FILES ${CMAKE_CURRENT_LIST_DIR}/src/*.cpp)
file(GLOB HEADER_FILES ${CMAKE_CURRENT_LIST_DIR}/src/*.h)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

add_executable(TimeChecker ${SOURCE_FILES} ${HEADER_FILES})

set_target_properties(TimeChecker PROPERTIES CXX_STANDARD 20 CXX_STANDARD_REQUIRED ON)
