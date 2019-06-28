#include "pocketn2.h"

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_basis_Vect_staticConstructor(JNIEnv *env, jobject thiz,
                                                                         jfloatArray arrJava) {
    auto fn = [&]() {
        std::vector<float_t> *ret = new std::vector<float_t>();
        jsize sz = env->GetArrayLength(arrJava);
        jfloat *arr = env->GetFloatArrayElements(arrJava, 0);
        for (int i = 0; i < sz; i++) {
            ret->push_back(arr[i]);
        }
        env->ReleaseFloatArrayElements(arrJava, arr, 0);
        return (jlong) ret;
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jfloatArray
Java_com_viveret_tinydnn_basis_Vect_jniGetElements(JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() -> jfloatArray {
        int size = ((vec_t *) handle)->size();
        jfloatArray result = env->NewFloatArray(size);
        if (result == NULL) {
            return NULL; /* out of memory error thrown */
        }

        jfloat fill[size];
        for (int i = 0; i < size; i++) {
            fill[i] = (*((vec_t *) handle))[i];
        }
        // move from the temp structure to the java structure
        env->SetFloatArrayRegion(result, 0, size, fill);
        return result;
    };

    jfloatArray ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}



extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_layer_ConnectionTable_staticConstructor(JNIEnv *env, jobject thiz,
                                                                                         jbooleanArray arrJava, jint rows, jint cols) {
    auto fn = [&]() {
        jsize sz = env->GetArrayLength(arrJava);
        const bool *arr = (const bool*)env->GetBooleanArrayElements(arrJava, 0);
        return (jlong) new tiny_dnn::core::connection_table(arr, rows, cols);
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jbooleanArray Java_com_viveret_tinydnn_layer_ConnectionTable_jniGetElements(JNIEnv *env, jobject thiz,
                                                                                              jlong handle) {
    auto fn = [&]() -> jbooleanArray {
        auto size = ((tiny_dnn::core::connection_table *) handle)->connected_.size();
        jbooleanArray result = env->NewBooleanArray(size);
        if (result == NULL) {
            return NULL; /* out of memory error thrown */
        }

        jboolean fill[size];
        for (int i = 0; i < size; i++) {
            fill[i] = ((tiny_dnn::core::connection_table *) handle)->connected_[i];
        }
        // move from the temp structure to the java structure
        env->SetBooleanArrayRegion(result, 0, size, fill);
        return result;
    };

    jbooleanArray ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}