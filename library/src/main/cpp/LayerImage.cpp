//
// Created by viveret on 3/30/19.
//

#include "pocketn2.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniConstructor(
        JNIEnv *env, jobject thiz, jlong width, jlong height, jlong depth, tiny_dnn::image_type format) {
    auto fn = [&]() {
        auto size = new shape3d(width, height, depth);
        return (jlong)(void*) new tiny_dnn::image<unsigned char>(*size, format);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniResize(JNIEnv *env, jobject thiz,
                                                            jlong handle, jlong width, jlong height) {
    auto fn = [&]() {
        ((tiny_dnn::image<unsigned char> *) handle)->resize(width, height);
        return true;
    };
    jboolean ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniGetPixelData(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    auto fn = [&]() {
        return ((tiny_dnn::image<unsigned char>*) handle)->data();
    };
    std::vector<unsigned char> ret;
    if (!jniTryCatch(env, fn, ret)) {
        ret = std::vector<unsigned char>(0);
    }
    jbyte* jbyteBuffer = new jbyte[ret.size()];
    std::copy(ret.begin(), ret.end(), jbyteBuffer);
    jbyteArray ret2 = env->NewByteArray((jsize)ret.size());
    env->SetByteArrayRegion(ret2, 0, (jsize)ret.size(), jbyteBuffer);
    return ret2;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniGetFormat(JNIEnv *env, jobject thiz,
                                                                    jlong handle) {
    auto fn = [&]() {
        return ((tiny_dnn::image<unsigned char> *) handle)->type();
    };
    tiny_dnn::image_type ret;
    if (jniTryCatch(env, fn, ret)) {
        return (jint) ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniGetWidth(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    auto fn = [&]() {
        return ((tiny_dnn::image<unsigned char> *) handle)->width();
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniGetHeight(JNIEnv *env, jobject thiz,
                                                                    jlong handle) {
    auto fn = [&]() {
        return ((tiny_dnn::image<unsigned char> *) handle)->height();
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniGetDepth(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    auto fn = [&]() {
        return ((tiny_dnn::image<unsigned char> *) handle)->depth();
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_viveret_tinydnn_util_LayerImage_jniEmpty(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    auto fn = [&]() {
        return ((tiny_dnn::image<unsigned char> *) handle)->empty();
    };
    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}