#include "pocketn2.h"

extern "C" // adagrad
JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_AdaptiveGradientOptimizer_staticConstructor(
    JNIEnv* env, jobject thiz) {

    return (jlong)new adagrad();
}

extern "C" // adamax
JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_AdamaxOptimizer_staticConstructor(
    JNIEnv* env, jobject thiz) {

    return (jlong)new adamax();
}

extern "C" // adam
 JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_AdamOptimizer_staticConstructor(
     JNIEnv* env, jobject thiz) {

     return (jlong)new adam();
 }

extern "C" // RMSprop
 JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_RMSpropOptimizer_staticConstructor(
     JNIEnv* env, jobject thiz) {

     return (jlong)new RMSprop();
 }

extern "C" // gradient_descent
 JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_GradientDescentOptimizer_staticConstructor(
     JNIEnv* env, jobject thiz) {

     return (jlong)new gradient_descent();
 }

extern "C" // momentum
 JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_MomentumOptimizer_staticConstructor(
     JNIEnv* env, jobject thiz) {

     return (jlong)new momentum();
 }

extern "C" // nesterov_momentum
 JNIEXPORT jlong Java_com_viveret_tinydnn_optimizer_NesterovMomentumOptimizer_staticConstructor(
     JNIEnv* env, jobject thiz) {

     return (jlong)new nesterov_momentum();
 }