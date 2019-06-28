#include "pocketn2.h"

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniSave__JLjava_lang_String_2II(
        JNIEnv *env, jobject thiz, jlong handle, jstring path, jint saveWhat, jint saveFormat) {
    auto fn = [&]() {
        jboolean isCopy;
        const char * str = env->GetStringUTFChars(path, &isCopy);

        ((network<sequential> *) handle)->save(str, (content_type) saveWhat, (file_format) saveFormat);
        //env->ReleaseStringChars(path, str);
        return 0L;
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jboolean Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniLoad__JLjava_lang_String_2II(
    JNIEnv* env, jobject thiz, jlong handle, jstring path, jint what, jint format) {
    auto fn = [&]() {
        jboolean isCopy;
        const char *pathStr = env->GetStringUTFChars(path, &isCopy);

        auto ntwk = (network<sequential>*)handle;
        ntwk->load(pathStr, (content_type)what, (file_format)format);

        return true;
    };
    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return false;
    }
}

extern "C"
JNIEXPORT jboolean Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniLoadWeightsFromPath__JLjava_lang_String_2I(
        JNIEnv* env, jobject thiz, jlong handle, jstring path, jint format) {
    auto fn = [&]() {
        jboolean isCopy;
        const char *pathStr = env->GetStringUTFChars(path, &isCopy);

        auto ntwk = (network<sequential>*)handle;
        std::ifstream ifs(pathStr);
        ifs >> *ntwk;

        return true;
    };
    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetInDataSize(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() {
        return (jlong) ((network<sequential> *) handle)->in_data_size();
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT void Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniStopOngoingTraining(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() {
        ((network<sequential> *) handle)->stop_ongoing_training();
        return (jlong) 0;
    };

    jlong ret;
    jniTryCatch(env, fn, ret);
}

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetOutDataSize(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() {
        return (jlong) ((network<sequential> *) handle)->out_data_size();
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetSize(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto fn = [&]() {
        return (jlong) ((network<sequential> *) handle)->layer_size();
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jlong Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetLayerAt(
        JNIEnv *env, jobject thiz, jlong handle, jlong index) {
    auto fn = [&]() {
        return (jlong) (*((network<sequential> *) handle))[(size_t) index];
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jboolean
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniFit__Lcom_viveret_pocketn2_tinydnn_network_AbstractNetworkModelWithWeights_2JJ_3J_3JJI(
        JNIEnv *env, jobject thiz, jlong handle,
                                                          jlong optimizerHandle,
                                                          jlongArray inputsJava,
                                                          jlongArray desired_outputsJava,
                                                          jlong batch_size, jint epochs) {
    auto fn = [&]() {
        std::vector<vec_t> inputs;
        std::vector<vec_t> desired_outputs;

        jsize arrSize = env->GetArrayLength(inputsJava);
        jlong *arrElems = env->GetLongArrayElements(inputsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            inputs.push_back(*((vec_t *) arrElems[i]));
        }

        arrSize = env->GetArrayLength(desired_outputsJava);
        arrElems = env->GetLongArrayElements(desired_outputsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            desired_outputs.push_back(*((vec_t *) arrElems[i]));
        }

        adam optimizer;//optimizer& opt = *((optimizer*)optimizerHandle);
        return (jboolean) ((network<sequential> *) handle)->fit<mse>(optimizer, inputs,
                                                                     desired_outputs,
                                                                     (size_t) batch_size,
                                                                     (int) epochs);
    };

    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}

extern "C"
JNIEXPORT jboolean
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniFit__Lcom_viveret_tinydnn_network_AbstractNetworkModelWithWeights_2JJ_3J_3JJIZI_3J(
        JNIEnv *env, jobject thiz1, jobject thiz, jlong handle,
        jlong optimizerHandle, jlongArray inputsJava, jlongArray desired_outputsJava,
        jlong batch_size, jint epochs,
        jboolean reset_weights, jint n_threads, jlongArray costsJava) {
    auto fn = [&]() {
        std::vector<vec_t> inputs; // Lkotlin_jvm_functions_Function1_2
        std::vector<vec_t> desired_outputs;
        std::vector<vec_t> costs;

        jsize arrSize = env->GetArrayLength(inputsJava);
        jlong *arrElemsInputs = env->GetLongArrayElements(inputsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            inputs.push_back(*((vec_t *) arrElemsInputs[i]));
        }
        env->ReleaseLongArrayElements(inputsJava, arrElemsInputs, 0);

        arrSize = env->GetArrayLength(desired_outputsJava);
        jlong *arrElemsOutputs = env->GetLongArrayElements(desired_outputsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            desired_outputs.push_back(*((vec_t *) arrElemsOutputs[i]));
        }
        env->ReleaseLongArrayElements(desired_outputsJava, arrElemsOutputs, 0);

        arrSize = env->GetArrayLength(costsJava);
        jlong *arrElemsCosts = env->GetLongArrayElements(costsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            costs.push_back(*((vec_t *) arrElemsCosts[i]));
        }
        env->ReleaseLongArrayElements(costsJava, arrElemsCosts, 0);

        auto fptrEpoch = ([env, thiz]() { // (void (*)())
            auto classRef = env->GetObjectClass(thiz);
            auto onEpochUpdateId = env->GetMethodID(classRef, "onEpochUpdate", "()V");
            env->CallVoidMethod(thiz, onEpochUpdateId);
            env->DeleteLocalRef(classRef);
        });

        auto fptrBatch = ([env, thiz]() { // (void (*)())
            auto classRef = env->GetObjectClass(thiz);
            auto onBatchUpdateId = env->GetMethodID(classRef, "onBatchUpdate", "()V");
            env->CallVoidMethod(thiz, onBatchUpdateId);
            env->DeleteLocalRef(classRef);
        });

        optimizer &opt = *((optimizer*)optimizerHandle);
        return (jboolean) ((network<sequential> *) handle)->fit<mse>(opt, inputs,
                                                                     desired_outputs,
                                                                     (size_t) batch_size,
                                                                     (int) epochs, fptrBatch,
                                                                     fptrEpoch, reset_weights,
                                                                     (int) n_threads, costs);
    };

    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}

extern "C"
JNIEXPORT jboolean
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniTrain__Lcom_viveret_pocketn2_tinydnn_network_AbstractNetworkModelWithWeights_2JJ_3J_3JJIZI_3J(
        JNIEnv *env, jobject thiz1, jobject thiz, jlong handle,
        jlong optimizerHandle, jlongArray inputsJava,
        jlong batch_size, jint epochs,
        jboolean reset_weights, jint n_threads, jlongArray costsJava) {
    auto fn = [&]() {
        std::vector<vec_t> inputs; // Lkotlin_jvm_functions_Function1_2
        std::vector<vec_t> costs;

        jsize arrSize = env->GetArrayLength(inputsJava);
        jlong *arrElemsInputs = env->GetLongArrayElements(inputsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            inputs.push_back(*((vec_t *) arrElemsInputs[i]));
        }
        env->ReleaseLongArrayElements(inputsJava, arrElemsInputs, 0);

        arrSize = env->GetArrayLength(costsJava);
        jlong *arrElemsCosts = env->GetLongArrayElements(costsJava, 0);
        for (int i = 0; i < arrSize; i++) {
            costs.push_back(*((vec_t *) arrElemsCosts[i]));
        }
        env->ReleaseLongArrayElements(costsJava, arrElemsCosts, 0);

        auto fptrEpoch = ([env, thiz]() { // (void (*)())
            auto classRef = env->GetObjectClass(thiz);
            auto onEpochUpdateId = env->GetMethodID(classRef, "onEpochUpdate", "()V");
            env->CallVoidMethod(thiz, onEpochUpdateId);
            env->DeleteLocalRef(classRef);
        });

        auto fptrBatch = ([env, thiz]() { // (void (*)())
            auto classRef = env->GetObjectClass(thiz);
            auto onBatchUpdateId = env->GetMethodID(classRef, "onBatchUpdate", "()V");
            env->CallVoidMethod(thiz, onBatchUpdateId);
            env->DeleteLocalRef(classRef);
        });

        std::vector<tiny_dnn::label_t> lbls;
        optimizer &opt = *((optimizer*)optimizerHandle);
        return (jboolean) ((network<sequential> *) handle)->train<mse>(opt, inputs, lbls,
                                                                     (size_t) batch_size,
                                                                     (int) epochs, fptrBatch,
                                                                     fptrEpoch, reset_weights,
                                                                     (int) n_threads, costs);
    };

    jboolean ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return (jboolean) false;
    }
}

//

extern "C"
JNIEXPORT jlong
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniPredict(JNIEnv *env, jobject thiz,
                                                             jlong handle, jlong vectHandle) {
    auto fn = [&]() {
        return (jlong) new vec_t(
                ((network<sequential> *) handle)->predict(*((vec_t *) vectHandle)));
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

extern "C"
JNIEXPORT jstring
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetName__J(JNIEnv *env, jobject thiz,
                                                             jlong handle) {
    auto fn = [&]() {
        return env->NewStringUTF(((network<sequential> *) handle)->name().c_str());
    };
    jstring ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return env->NewStringUTF("");
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetEpochAt(JNIEnv *env, jobject thiz,
                                                                jlong handle) {
    auto fn = [&]() {
        return ((network<sequential> *) handle)->epoch_i();
    };

    jint ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_viveret_tinydnn_network_AbstractNetworkModelWithWeights_jniGetBatchAt(JNIEnv *env, jobject thiz,
                                                                jlong handle) {
    auto fn = [&]() {
        return ((network<sequential> *) handle)->batch_at();
    };

    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return -1;
    }
}