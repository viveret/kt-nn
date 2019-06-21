#ifndef POCKETNN_H
#define POCKETNN_H

//
// Created by viveret steele (viveret.amant@gmail.com) on 1/23/19.
//

#include <jni.h>
#include <string>

#define DNN_USE_IMAGE_API

/**
 * define if you want to use intel TBB library
 */
// #define CNN_USE_TBB

/**
 * define to enable avx vectorization
 */
// #define CNN_USE_AVX

/**
 * define to enable sse2 vectorization
 */
// #define CNN_USE_SSE

/**
 * define to enable OMP parallelization
 */
//#define CNN_USE_OMP

/**
 * define to enable Grand Central Dispatch parallelization
 */
// #define CNN_USE_GCD

/**
 * define to use exceptions
 */
#define CNN_USE_EXCEPTIONS

/**
 * comment out if you want tiny-dnn to be quiet
 */
#define CNN_USE_STDOUT

/**
 * disable serialization/deserialization function
 * You can uncomment this to speedup compilation & linking time,
 * if you don't use network::save / network::load functions.
 **/
// #define CNN_NO_SERIALIZATION

#include "tiny_dnn/tiny_dnn.h"
#include "tiny_dnn/util/image.h"
#include "tiny_dnn/layers/layer.h"

#include "exceptions.h"

using namespace tiny_dnn;
using namespace tiny_dnn::activation;
using namespace tiny_dnn::layers;

typedef long objectHandleJni;
typedef void* objectHandle;

#define OBJ_HANDLE_FROM_JNI(X) (objectHandle)((objectHandleJni)(X))
#define OBJ_HANDLE_TO_JNI(X) (objectHandleJni)((objectHandle)(X))

#endif //POCKETNN_H