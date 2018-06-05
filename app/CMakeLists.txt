# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

#ffmpeg版本3.3.6,3.4集成失败 之后再试下
#头文件路径
include_directories(src/main/cpp/include)
#库路径 ANDROID_ABI = gradle中设置的abiFilters
set(FF ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI})

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#添加文件
add_library( # Sets the name of the library.
             JDVideoEditor

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/Jni_FFmpegCmd.c
             src/main/cpp/cmdutils.c
             src/main/cpp/ffmpeg.c
             src/main/cpp/ffmpeg_filter.c
             src/main/cpp/ffmpeg_opt.c
             src/main/cpp/ffmpeg_thread.c)

#创建SO标签
add_library(avcodec SHARED IMPORTED)
add_library(avfilter SHARED IMPORTED)
add_library(avformat SHARED IMPORTED)
add_library(avutil SHARED IMPORTED)
add_library(swresample SHARED IMPORTED)
add_library(swscale SHARED IMPORTED)
add_library(avdevice SHARED IMPORTED)
add_library(postproc SHARED IMPORTED)

#绑定SO
set_target_properties(avcodec   PROPERTIES IMPORTED_LOCATION ${FF}/libavcodec.so)
set_target_properties(avfilter  PROPERTIES IMPORTED_LOCATION ${FF}/libavfilter.so)
set_target_properties(avformat  PROPERTIES IMPORTED_LOCATION ${FF}/libavformat.so)
set_target_properties(avutil    PROPERTIES IMPORTED_LOCATION ${FF}/libavutil.so)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${FF}/libswresample.so)
set_target_properties(swscale   PROPERTIES IMPORTED_LOCATION ${FF}/libswscale.so)
set_target_properties(avdevice  PROPERTIES IMPORTED_LOCATION ${FF}/libavdevice.so)
set_target_properties(postproc  PROPERTIES IMPORTED_LOCATION ${FF}/libpostproc.so)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# 绑定SO
target_link_libraries( # Specifies the target library.
                       JDVideoEditor

                      avformat avdevice avcodec avfilter avutil postproc swresample swscale
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )