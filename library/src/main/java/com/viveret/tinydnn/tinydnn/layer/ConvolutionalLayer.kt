package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields
import com.viveret.tinydnn.tinydnn.util.ConnectionTable
import com.viveret.tinydnn.tinydnn.util.LayerImage
import com.viveret.tinydnn.tinydnn.util.LayerVizualization

class ConvolutionalLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor(R2.string.layer_conv, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.WindowSize) window_size: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.OutChannels) out_channels: Long,
                @UserField(UserFields.Padding) pad_type: Padding,
                @UserField(UserFields.HasBias) has_bias: Boolean,
                @UserField(UserFields.StrideX) w_stride: Long,
                @UserField(UserFields.StrideY)  h_stride: Long) : this(staticConstructor(in_width, in_height, window_size, window_size, in_channels, out_channels, pad_type.ordinal, has_bias, w_stride, h_stride)) {
        // backend_t backend_type = core::default_engine()
    }

    @UserConstructor(R2.string.layer_conv, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.WindowSize) window_size: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.OutChannels) out_channels: Long,
                @UserField(UserFields.Padding) pad_type: Padding,
                @UserField(UserFields.HasBias) has_bias: Boolean) : this(staticConstructor(in_width, in_height, window_size, window_size, in_channels, out_channels, pad_type.ordinal, has_bias, 1, 1)) {
        // backend_t backend_type = core::default_engine()
    }

    //@UserConstructor(R2.string.layer_conv, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.WindowSize) window_size: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.OutChannels) out_channels: Long,
                connectionTable: ConnectionTable,
                @UserField(UserFields.Padding) pad_type: Padding,
                @UserField(UserFields.HasBias) has_bias: Boolean) : this(staticConstructorConnTable(in_width, in_height, window_size, window_size, in_channels, out_channels, connectionTable.nativeObjectHandle, pad_type.ordinal, has_bias, 1, 1)) {
        // backend_t backend_type = core::default_engine()
    }

    @UserConstructor(R2.string.layer_conv, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.WindowWidth) window_w: Long,
                @UserField(UserFields.WindowHeight) window_h: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.OutChannels) out_channels: Long,
                @UserField(UserFields.Padding) pad_type: Padding,
                @UserField(UserFields.HasBias) has_bias: Boolean,
                @UserField(UserFields.StrideX) w_stride: Long,
                @UserField(UserFields.StrideY) h_stride: Long) : this(staticConstructor(in_width, in_height, window_w, window_h, in_channels, out_channels, pad_type.ordinal, has_bias, w_stride, h_stride)) {
        // backend_t backend_type = core::default_engine()
    }

    override fun getVisualizations(): List<LayerVizualization> {
        val ret = super.getVisualizations()

        val imgHandle = jniWeightsToImage(nativeObjectHandle)
        if (imgHandle != 0L && ret is MutableList) {
            val image = LayerImage.attach(imgHandle)
            ret.add(LayerVizualization(R2.string.weights_lbl, image))
        }

        return ret
    }

    companion object {
        @JvmStatic
        external fun staticConstructor(in_width: Long, in_height: Long,
                                       window_w: Long, window_h: Long, in_channels: Long, out_channels: Long,
                                       pad_type: Int, has_bias: Boolean, w_stride: Long, h_stride: Long): Long
        @JvmStatic
        external fun staticConstructorConnTable(in_width: Long, in_height: Long,
                                       window_w: Long, window_h: Long, in_channels: Long, out_channels: Long,
                                       connection_table_handle: Long,
                                       pad_type: Int, has_bias: Boolean, w_stride: Long, h_stride: Long): Long

        @JvmStatic
        external fun jniWeightsToImage(handle: Long): Long

        fun wrap(handle: Long): ConvolutionalLayer = ConvolutionalLayer(handle)
    }
}
