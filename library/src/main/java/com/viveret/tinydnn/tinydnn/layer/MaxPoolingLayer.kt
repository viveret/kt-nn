package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class MaxPoolingLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param in_width     [in] width of input image
     * @param in_height    [in] height of input image
     * @param in_channels  [in] the number of input image channels(depth)
     * @param pooling_size [in] factor by which to downscale
     **/
    @UserConstructor(R2.string.layer_max_pool, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.PoolSize) pooling_size: Long) :
            this(staticConstructor(in_width, in_height, in_channels, pooling_size))



    /**
     * @param in_width     [in] width of input image
     * @param in_height    [in] height of input image
     * @param in_channels  [in] the number of input image channels(depth)
     * @param pooling_size [in] factor by which to downscale
     * @param stride       [in] interval at which to apply the filters to the input
     **/
    @UserConstructor(R2.string.layer_max_pool, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.PoolSizeX) pooling_size_x: Long,
                @UserField(UserFields.PoolSizeY) pooling_size_y: Long,
                @UserField(UserFields.StrideX) stride_x: Long,
                @UserField(UserFields.StrideY) stride_y: Long,
                @UserField(UserFields.Padding) pad_type: Padding) :
            this(staticConstructor(in_width, in_height, in_channels, pooling_size_x,
                    pooling_size_y, stride_x, stride_y, pad_type.ordinal))

    companion object {
        /**
         * @param in_width     [in] width of input image
         * @param in_height    [in] height of input image
         * @param in_channels  [in] the number of input image channels(depth)
         * @param pooling_size [in] factor by which to downscale
         **/
        @JvmStatic
        external fun staticConstructor(in_width: Long, in_height: Long, in_channels: Long, pooling_size: Long): Long

        /**
         * @param in_width     [in] width of input image
         * @param in_height    [in] height of input image
         * @param in_channels  [in] the number of input image channels(depth)
         * @param pooling_size [in] factor by which to downscale
         * @param stride       [in] interval at which to apply the filters to the input
         **/
        @JvmStatic
        external fun staticConstructor(in_width: Long, in_height: Long, in_channels: Long, pooling_size_x: Long,
                                       pooling_size_y: Long, stride_x: Long, stride_y: Long, pad_type: Int): Long

        fun wrap(handle: Long): MaxPoolingLayer = MaxPoolingLayer(handle)
    }
}
