package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class DeconvolutionalLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor(R2.string.layer_deconv, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.WindowSize) window_size: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.OutChannels) out_channels: Long,
                @UserField(UserFields.Padding) pad_type: Padding,
                @UserField(UserFields.HasBias) has_bias: Boolean,
                @UserField(UserFields.StrideX) w_stride: Long,
                @UserField(UserFields.StrideY) h_stride: Long) : this(staticConstructor(in_width, in_height, window_size, in_channels, out_channels, pad_type.ordinal, has_bias, w_stride, h_stride)) {
        // backend_t backend_type = core::default_engine()
    }

    @UserConstructor(R2.string.layer_deconv, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.WindowSize) window_size: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.OutChannels) out_channels: Long,
                @UserField(UserFields.Padding) pad_type: Padding,
                @UserField(UserFields.HasBias) has_bias: Boolean = true) : this(staticConstructor(in_width, in_height, window_size, in_channels, out_channels, pad_type.ordinal, has_bias, 1, 1)) {
        // backend_t backend_type = core::default_engine()
    }

    companion object {

        @JvmStatic
        external fun staticConstructor(in_width: Long, in_height: Long,
                                       window_size: Long, in_channels: Long, out_channels: Long,
                                       pad_type: Int, has_bias: Boolean, w_stride: Long, h_stride: Long): Long

        fun wrap(handle: Long): DeconvolutionalLayer = DeconvolutionalLayer(handle)
    }
}