package com.viveret.tinydnn.layer

import android.text.InputType
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class MaxUnpoolingLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param in_width     [in] width of input image
     * @param in_height    [in] height of input image
     * @param in_channels  [in] the number of input image channels(depth)
     * @param pooling_size [in] factor by which to downscale
     * @param stride       [in] interval at which to apply the filters to the input
     **/
    @UserConstructor("@layer_max_unpool", "")
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @CustomUserField("@unpooling_size", "@unpool_size_hint", InputType.TYPE_CLASS_NUMBER) unpooling_size: Long,
                @UserField(UserFields.Stride) stride: Long) :
            this(staticConstructor(in_width, in_height, in_channels, unpooling_size, stride))

    companion object {

        /**
         * @param in_width     [in] width of input image
         * @param in_height    [in] height of input image
         * @param in_channels  [in] the number of input image channels(depth)
         * @param pooling_size [in] factor by which to downscale
         * @param stride       [in] interval at which to apply the filters to the input
         **/
        @JvmStatic
        external fun staticConstructor(in_width: Long, in_height: Long, in_channels: Long,
                                       unpooling_size: Long, stride: Long): Long

        fun wrap(handle: Long): MaxUnpoolingLayer = MaxUnpoolingLayer(handle)
    }
}
