cmake_minimum_required(VERSION 3.1)
project(.)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES src/Lab06/A/search1.cpp)
add_executable(main ${SOURCE_FILES})