cmake_minimum_required(VERSION 3.11)

project(Imbalance_monitoring CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS_DEBUG "-g")
set(CMAKE_CXX_FLAGS_RELEASE "-O3")

set(SOURCE_DIR "src")

set(SOURCE_FILES
    "${SOURCE_DIR}/main.cpp"
    "${SOURCE_DIR}/tools.cpp"
    "${SOURCE_DIR}/tools.h"
)

add_executable(${PROJECT_NAME} ${SOURCE_FILES})
