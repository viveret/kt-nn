#include "pocketn2.h"

// aSinH layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_AsinhLayer_staticConstructor(JNIEnv *env, jobject _2) {
    auto fn = [&]() {
        return (jlong) new asinh_layer();
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Average pooling layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_AveragePoolingLayer_staticConstructor__JJJJ(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong in_channels,
        jlong pool_size) {
    auto fn = [&]() {
        return (jlong) new average_pooling_layer((size_t) in_width, (size_t) in_height,
                                                 (size_t) in_channels, (size_t) pool_size);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_AveragePoolingLayer_staticConstructor__JJJ(
        JNIEnv *env, jobject thiz, jlong prev_layer_handle, jlong pool_size, jlong stride) {
    auto fn = [&]() {
        return (jlong) new average_pooling_layer(((layer *) prev_layer_handle)->out_shape().front(),
                                                 (size_t) pool_size, (size_t) stride);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Average unpooling layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_AverageUnpoolingLayer_staticConstructor__JJJJ(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong in_channels,
        jlong pool_size) {
    auto fn = [&]() {
        return (jlong) new average_unpooling_layer((size_t) in_width, (size_t) in_height,
                                                   (size_t) in_channels, (size_t) pool_size);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_AverageUnpoolingLayer_staticConstructor__JJJJJ(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong in_channels,
        jlong pool_size, jlong stride) {
    auto fn = [&]() {
        return (jlong) new average_unpooling_layer((size_t) in_width, (size_t) in_height,
                                                   (size_t) in_channels, (size_t) pool_size,
                                                   (size_t) stride);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Batch Normalization
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_BatchNormLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong prev_layer, jdouble epsilon, jdouble momentum) {
    auto fn = [&]() {
        return (jlong) new batch_normalization_layer(*((layer *) prev_layer), (float_t) epsilon,
                                                     (float_t) momentum);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Convolutional layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_ConvolutionalLayer_staticConstructor__JJJJJJIZJJ(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height,
        jlong window_w, jlong window_h, jlong in_channels, jlong out_channels,
        jint pad_type, jboolean has_bias, jlong w_stride, jlong h_stride) {
    auto fn = [&]() {
        return (jlong) new conv(in_width, in_height, window_w, window_h, in_channels, out_channels,
                                (tiny_dnn::padding) pad_type, has_bias, w_stride, h_stride);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Convolutional layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_ConvolutionalLayer_staticConstructorConnTable(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height,
        jlong window_w, jlong window_h, jlong in_channels, jlong out_channels,
        jlong connection_table_handle,
        jint pad_type, jboolean has_bias, jlong w_stride, jlong h_stride) {
    auto fn = [&]() {
        return (jlong) new conv(in_width, in_height, window_w, window_h, in_channels, out_channels,
                                *((tiny_dnn::core::connection_table*)connection_table_handle),
                                (tiny_dnn::padding) pad_type, has_bias, w_stride, h_stride);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Deconvolutional layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_DeconvolutionalLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height,
        jlong window_size, jlong in_channels, jlong out_channels,
        jint pad_type, jboolean has_bias, jlong w_stride, jlong h_stride) {
    auto fn = [&]() {
        return (jlong) new deconv(in_width, in_height, window_size, in_channels, out_channels,
                                  (tiny_dnn::padding) pad_type, has_bias, w_stride, h_stride);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Dropout layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_DropoutLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong in_dim, jfloat dropout_rate) {
    auto fn = [&]() {
        return (jlong) new dropout_layer(in_dim, dropout_rate); // (size_t) (float_t)
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Fully connected layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_FullyConnectedLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong in_dim, jlong out_dim, jboolean has_bias) {
    auto fn = [&]() {
        return (jlong) new fc(in_dim, out_dim, has_bias);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Linear layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_LinearLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong dim, jfloat scale, jfloat bias) {
    auto fn = [&]() {
        return (jlong) new linear_layer(dim, scale, bias); // (size_t) (float_t)
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// local response normalization layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_LrnLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong local_size,
        jlong in_channels, jfloat alpha, jfloat beta) {
    auto fn = [&]() {
        return (jlong) new lrn_layer((size_t) in_width, (size_t) in_height, (size_t) local_size,
                                     (size_t) in_channels, alpha, beta);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_LrnLayer_staticConstructor2(
        JNIEnv *env, jobject thiz, jlong prev_layer_handle, jlong local_size, jfloat alpha,
        jfloat beta) {
    auto fn = [&]() {
        return (jlong) new lrn_layer((layer *) prev_layer_handle, (size_t) local_size, alpha, beta);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Max pooling layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_MaxPoolingLayer_staticConstructor__JJJJ(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong in_channels,
        jlong pooling_size) {
    auto fn = [&]() {
        return (jlong) new max_pooling_layer((size_t) in_width, (size_t) in_height,
                                             (size_t) in_channels, (size_t) pooling_size);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_MaxPoolingLayer_staticConstructor__JJJJJJJI(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong in_channels,
        jlong pooling_size_x, jlong pooling_size_y, jlong stride_x, jlong stride_y, jint pad_type) {
    auto fn = [&]() {
        return (jlong) new max_pooling_layer((size_t) in_width, (size_t) in_height,
                                             (size_t) in_channels, (size_t) pooling_size_x,
                                             (size_t) pooling_size_y, (size_t) stride_x,
                                             (size_t) stride_y, pad_type != 0);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Max unpooling layer
extern "C" JNIEXPORT jlong
Java_com_viveret_tinydnn_layer_MaxUnpoolingLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong in_width, jlong in_height, jlong in_channels,
        jlong unpooling_size, jlong stride) {
    auto fn = [&]() {
        return (jlong) new max_unpooling_layer((size_t) in_width, (size_t) in_height,
                                               (size_t) in_channels, (size_t) unpooling_size,
                                               (size_t) stride);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Power layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_PowerLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong prev_layer_handle, jfloat factor, jfloat scale) {
    auto fn = [&]() {
        return (jlong) new power_layer(*((layer *) prev_layer_handle), factor, scale);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// ReLu layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_ReLuLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong width, jlong height, jlong nmaps) {
    auto fn = [&]() {
        return (jlong) new relu_layer((size_t) width, (size_t) height, (size_t) nmaps);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Sigmoid layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_SigmoidLayer_staticConstructor(
        JNIEnv *env, jobject thiz) {
    auto fn = [&]() {
        return (jlong) new sigmoid_layer();
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Slice layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_SliceLayer_staticConstructor(
        JNIEnv *env, jobject thiz, jlong prev_layer_handle, slice_type slice_t, jlong num_outputs) {
    auto fn = [&]() {
        return (jlong) new slice_layer(*((layer *) prev_layer_handle), slice_t, num_outputs);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Soft Max layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_SoftMaxLayer_staticConstructor__JJ(
        JNIEnv *env, jobject thiz, jlong in_dim, jlong outDim) {
    auto fn = [&]() {
        return (jlong) new softmax_layer((size_t) in_dim);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Soft Max layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_SoftMaxLayer_staticConstructor__JJJ(
        JNIEnv *env, jobject thiz, jlong width, jlong height, jlong inChannels) {
    auto fn = [&]() {
        return (jlong) new softmax_layer((size_t) width, (size_t) height, (size_t) inChannels);
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}

// Tanh layer
extern "C" JNIEXPORT jlong Java_com_viveret_tinydnn_layer_TanhLayer_staticConstructor(
        JNIEnv *env, jobject thiz) {
    auto fn = [&]() {
        return (jlong) new tanh_layer();
    };
    jlong ret;
    if (jniTryCatch(env, fn, ret)) {
        return ret;
    } else {
        return 0;
    }
}