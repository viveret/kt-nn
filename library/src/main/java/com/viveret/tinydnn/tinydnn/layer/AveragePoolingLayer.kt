package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields
import com.viveret.tinydnn.tinydnn.Vect

class AveragePoolingLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param in_width    [in] width of input image
     * @param in_height   [in] height of input image
     * @param in_channels [in] the number of input image channels(depth)
     * @param pool_size   [in] factor by which to downscale
     */
    @UserConstructor(R2.string.layer_avg_pool, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.PoolSize) pool_size: Long) : this(staticConstructor(in_width, in_height, in_channels, pool_size))

    /**
     * @param in_width    [in] width of input image
     * @param in_height   [in] height of input image
     * @param in_channels [in] the number of input image channels(depth)
     * @param pool_size   [in] factor by which to downscale
     */
    @UserConstructor(R2.string.layer_avg_pool, 0)
    constructor(@UserField(UserFields.PreviousLayer) prevLayer: LayerBase,
                @UserField(UserFields.PoolSize) pool_size: Long,
                @UserField(UserFields.Stride) stride: Long) :
            this(staticConstructor(prevLayer.nativeObjectHandle, pool_size, stride))

    /**
     * @param in_width    [in] width of input image
     * @param in_height   [in] height of input image
     * @param in_channels [in] the number of input image channels(depth)
     * @param pool_size   [in] factor by which to downscale
     * @param stride      [in] interval at which to apply the filters to the input
     */
    @UserConstructor(R2.string.layer_avg_pool, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.PoolSize) pool_size: Long,
                @UserField(UserFields.Stride) stride: Long) : this(staticConstructor(in_width, in_height, in_channels, pool_size, stride))

    /**
     * @param in_width    [in] width of input image
     * @param in_height   [in] height of input image
     * @param in_channels [in] the number of input image channels(depth)
     * @param pool_size_x [in] factor by which to downscale
     * @param pool_size_y [in] factor by which to downscale
     * @param stride_x    [in] interval at which to apply the filters to the input
     * @param stride_y    [in] interval at which to apply the filters to the input
     * @param pad_type    [in] Padding mode(Fill/None)
     */
    @UserConstructor(R2.string.layer_avg_pool, 0)
    constructor(@UserField(UserFields.InWidth) in_width: Long,
                @UserField(UserFields.InHeight) in_height: Long,
                @UserField(UserFields.InChannels) in_channels: Long,
                @UserField(UserFields.PoolSizeX) pool_size_x: Long,
                @UserField(UserFields.PoolSizeY) pool_size_y: Long,
                @UserField(UserFields.StrideX) stride_x: Long,
                @UserField(UserFields.StrideY) stride_y: Long,
                @UserField(UserFields.Padding) pad_type: Padding) : this(staticConstructor(in_width, in_height, in_channels, pool_size_x, pool_size_y, stride_x, stride_y, pad_type.ordinal.toLong()))

    constructor(in_shape: Vect,
                pool_size: Long,
                stride: Long) : this(staticConstructor(in_shape.nativeObjectHandle, pool_size, stride))

    companion object {
        /**
         * @param in_width    [in] width of input image
         * @param in_height   [in] height of input image
         * @param in_channels [in] the number of input image channels(depth)
         * @param pool_size   [in] factor by which to downscale
         */
        @JvmStatic
        external fun staticConstructor(in_width: Long,
                                       in_height: Long,
                                       in_channels: Long,
                                       pool_size: Long): Long

        /**
         * @param in_width    [in] width of input image
         * @param in_height   [in] height of input image
         * @param in_channels [in] the number of input image channels(depth)
         * @param pool_size   [in] factor by which to downscale
         * @param stride      [in] interval at which to apply the filters to the input
         */
        @JvmStatic
        external fun staticConstructor(in_width: Long,
                                       in_height: Long,
                                       in_channels: Long,
                                       pool_size: Long,
                                       stride: Long): Long

        /**
         * @param prevLayer    [in] previous layer
         * @param pool_size   [in] factor by which to downscale
         * @param stride      [in] interval at which to apply the filters to the input
         */
        @JvmStatic
        external fun staticConstructor(prevLayer: Long,
                                       pool_size: Long,
                                       stride: Long): Long

        /**
         * @param in_width    [in] width of input image
         * @param in_height   [in] height of input image
         * @param in_channels [in] the number of input image channels(depth)
         * @param pool_size_x [in] factor by which to downscale
         * @param pool_size_y [in] factor by which to downscale
         * @param stride_x    [in] interval at which to apply the filters to the input
         * @param stride_y    [in] interval at which to apply the filters to the input
         * @param pad_type    [in] Padding mode(Fill/None)
         */
        @JvmStatic
        external fun staticConstructor(in_width: Long,
                                       in_height: Long,
                                       in_channels: Long,
                                       pool_size_x: Long,
                                       pool_size_y: Long,
                                       stride_x: Long,
                                       stride_y: Long,
                                       pad_type: Long): Long

        fun wrap(handle: Long): AveragePoolingLayer = AveragePoolingLayer(handle)
    }
}
