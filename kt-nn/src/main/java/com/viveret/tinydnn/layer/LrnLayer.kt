package com.viveret.tinydnn.layer

import android.text.InputType
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class LrnLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param in_width    [in] the width of input data
     * @param in_height   [in] the height of input data
     * @param local_size  [in] the number of channels(depths) to sum over
     * @param alpha       [in] the scaling parameter (Fill to caffe's LRN)
     * @param beta        [in] the scaling parameter (Fill to caffe's LRN)
     *
    float_t alpha      = 1.0,
    float_t beta       = 5.0,
    norm_region region = norm_region::across_channels
     **/
    @UserConstructor("@layer_lrn", "")
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @CustomUserField("@local_size", "@local_size_hint", InputType.TYPE_CLASS_NUMBER) local_size: Long,
                @CustomUserField("@alpha", "@alpha_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) alpha: Double,
                @CustomUserField("@beta", "@beta_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) beta: Double) :
            this(staticConstructor(in_width, in_height, local_size, alpha, beta))

    /**
     * @param layer       [in] the previous layer connected to this
     * @param local_size  [in] the number of channels(depths) to sum over
     * @param in_channels [in] the number of channels of input data
     * @param alpha       [in] the scaling parameter (Fill to caffe's LRN)
     * @param beta        [in] the scaling parameter (Fill to caffe's LRN)
    lrn_layer(layer *prev,
    size_t local_size,
    float_t alpha      = 1.0,
    float_t beta       = 5.0,
    norm_region region = norm_region::across_channels)
     **/
    @UserConstructor("@layer_lrn", "")
    constructor(@UserField(UserFields.PreviousLayer) prevLayer: LayerBase,
                @CustomUserField("@local_size", "@local_size_hint", InputType.TYPE_CLASS_NUMBER) local_size: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @CustomUserField("@alpha", "@alpha_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) alpha: Double,
                @CustomUserField("@beta", "@beta_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) beta: Double) :
            this(staticConstructor(prevLayer.nativeObjectHandle, local_size, in_channels, alpha, beta))

    companion object {
        /**
         * @param in_width    [in] the width of input data
         * @param in_height   [in] the height of input data
         * @param local_size  [in] the number of channels(depths) to sum over
         * @param in_channels [in] the number of channels of input data
         * @param alpha       [in] the scaling parameter (Fill to caffe's LRN)
         * @param beta        [in] the scaling parameter (Fill to caffe's LRN)
         *
        float_t alpha      = 1.0,
        float_t beta       = 5.0,
        norm_region region = norm_region::across_channels
         **/
        @JvmStatic
        external fun staticConstructor(in_width: Long, in_height: Long, local_size: Long, in_channels: Long, alpha: Double, beta: Double): Long

        /**
         * @param layer       [in] the previous layer connected to this
         * @param local_size  [in] the number of channels(depths) to sum over
         * @param in_channels [in] the number of channels of input data
         * @param alpha       [in] the scaling parameter (Fill to caffe's LRN)
         * @param beta        [in] the scaling parameter (Fill to caffe's LRN)
        lrn_layer(layer *prev,
        size_t local_size,
        float_t alpha      = 1.0,
        float_t beta       = 5.0,
        norm_region region = norm_region::across_channels)
         **/
        @JvmStatic
        external fun staticConstructor(prevLayer: Long, local_size: Long, in_channels: Long, alpha: Double, beta: Double): Long

        fun wrap(handle: Long): LrnLayer = LrnLayer(handle)
    }
}
