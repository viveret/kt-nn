#include "pocketn2.h"

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_SequentialNetworkModelWithWeights_staticConstructor__Ljava_lang_String_2(
        JNIEnv* env, jobject thiz, jstring name) {
    auto fn = [&]() {
        if (name) {
            jboolean isCopy;
            const char *nameStr = env->GetStringUTFChars(name, &isCopy);
            return (jlong) new network<sequential>(nameStr);
        } else {
            return (jlong) new network<sequential>();
        }
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT void Java_com_viveret_tinydnn_SequentialNetworkModelWithWeights_jniAddLayer(JNIEnv* env, jobject thiz,
jlong handle, jlong layerHandle) {
    auto fn = [&]() {
        (*((network<sequential>*)handle)) << *((layer*) layerHandle);
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT void Java_com_viveret_tinydnn_SequentialNetworkModelWithWeights_jniInsertLayer(JNIEnv* env, jobject thiz,
jlong handle, jlong insertPosition, jlong layerHandle) {
    auto fn = [&]() {
            (*((network<sequential>*)handle)).insert((size_t) insertPosition, ((layer*) layerHandle));
            return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT void Java_com_viveret_tinydnn_SequentialNetworkModelWithWeights_jniRemoveLayers(JNIEnv* env, jobject thiz,
                                                                               jlong handle) {
    auto fn = [&]() {
        ((network<sequential>*)handle)->removeLayers();
        return true;
    };
    bool ret;
    jniTryCatch(env, fn, ret);
}