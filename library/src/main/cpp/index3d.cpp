#include "pocketn2.h"

typedef index3d<size_t> regularIndex3d;

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_Index3D_staticConstructor(
        JNIEnv* env, jobject thiz,
        jlong width,
        jlong height,
        jlong depth) {

    auto fn = [&]() {
        regularIndex3d *ret = new regularIndex3d(width, height, depth);
        return (jlong) ((void *) ret);
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_Index3D_jniGetWidth(
        JNIEnv* env, jobject thiz,
        jlong handle) {
    auto fn = [&]() {
        return (jlong) ((regularIndex3d *) handle)->width_;
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_Index3D_jniGetHeight(
        JNIEnv* env, jobject thiz,
        jlong handle) {
    auto fn = [&]() {
        return (jlong) ((regularIndex3d *) handle)->height_;
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_Index3D_jniGetDepth(
        JNIEnv* env, jobject thiz,
        jlong handle) {
    auto fn = [&]() {
        return (jlong) ((regularIndex3d *) handle)->depth_;
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}