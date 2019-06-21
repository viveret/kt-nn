#include "pocketn2.h"

extern "C"
JNIEXPORT void  JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniSetParallelize(
        JNIEnv *env, jobject thiz, jlong handle, jboolean val) {
    auto fn = [&]() {
        ((layer *) handle)->set_parallelize(val);
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniGetParallelize(JNIEnv *env, jobject thiz,
                                                                    jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->parallelize();
    };
    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniGetInChannels(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->in_channels();
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
Java_com_viveret_tinydnn_layer_LayerBase_jniGetOutChannels(JNIEnv *env, jobject thiz,
                                                                    jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->out_channels();
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
Java_com_viveret_tinydnn_layer_LayerBase_jniGetInDataSize(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->in_data_size();
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
Java_com_viveret_tinydnn_layer_LayerBase_jniGetOutDataSize(JNIEnv *env, jobject thiz,
                                                                    jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->out_data_size();
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
Java_com_viveret_tinydnn_layer_LayerBase_jniSetTrainable(JNIEnv *env, jobject thiz,
                                                                  jlong handle, jboolean val) {
    auto fn = [&]() {
        ((layer *) handle)->set_trainable(val);
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniGetTrainable(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->trainable();
    };
    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniGetLayerType(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    auto fn = [&]() {
        return env->NewStringUTF(((layer *) handle)->layer_type().c_str());
    };
    jstring ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return nullptr;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniGetFanInSize(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->fan_in_size();
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
Java_com_viveret_tinydnn_layer_LayerBase_jniGetFanOutSize(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    auto fn = [&]() {
        return ((layer *) handle)->fan_out_size();
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
Java_com_viveret_tinydnn_layer_LayerBase_jniSetup(JNIEnv *env, jobject thiz, jlong handle,
                                                           jboolean weight_reset) {
    auto fn = [&]() {
        ((layer *) handle)->setup(weight_reset);
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniInitWeight(JNIEnv *env, jobject thiz,
                                                                jlong handle) {
    auto fn = [&]() {
        ((layer *) handle)->init_weight();
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniClearGrads(JNIEnv *env, jobject thiz,
                                                                jlong handle) {
    auto fn = [&]() {
        ((layer *) handle)->clear_grads();
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniUpdateWeight(JNIEnv *env, jobject thiz,
                                                                  jlong handle,
                                                                  jlong optimizerHandle) {
    auto fn = [&]() {
        ((layer *) handle)->update_weight((optimizer *) optimizerHandle);
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniSetSampleCount(JNIEnv *env, jobject thiz,
                                                                    jlong handle,
                                                                    jlong sampleCount) {
    auto fn = [&]() {
        ((layer *) handle)->set_sample_count(sampleCount);
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT jlong  JNICALL
Java_com_viveret_tinydnn_layer_LayerBase_jniOutputToImage(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() {
        //unsigned char data[] {0, 0, 0, 0, 255, 0, 0, 255, 255, 0, 0, 255, 0, 0, 0, 0 };
        //return (jlong)(void*)(new tiny_dnn::image<unsigned char>(data, 4, 4, tiny_dnn::image_type::grayscale));
        tiny_dnn::image<unsigned char> *tmp = ((layer *) handle)->output_to_image();
        return (jlong)(void*)tmp;
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT jlong  JNICALL
Java_com_viveret_tinydnn_layer_ConvolutionalLayer_jniWeightsToImage(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() {
        //unsigned char data[] {0, 0, 0, 0, 255, 0, 0, 255, 255, 0, 0, 255, 0, 0, 0, 0 };
        //return (jlong)(void*)(new tiny_dnn::image<unsigned char>(data, 4, 4, tiny_dnn::image_type::grayscale));
        tiny_dnn::image<unsigned char> *tmp = ((convolutional_layer *) handle)->weight_to_image();
        return (jlong)(void*)tmp;
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}