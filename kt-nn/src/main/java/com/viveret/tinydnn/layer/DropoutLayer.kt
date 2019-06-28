package com.viveret.tinydnn.layer

import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class DropoutLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param in_dim       [Long] number of elements of the input
     * @param dropout_rate [Double] (0-1) fraction of the input units to be dropped
     **/
    @UserConstructor("@layer_dropout", "")
    constructor(@UserField(UserFields.InDim) in_dim: Long,
                @CustomUserField("@dropout_rate", "@dropout_rate_hint", 0) dropout_rate: Double) :
            this(staticConstructor(in_dim, dropout_rate))

    @UserConstructor("@layer_dropout", "")
    constructor(@UserField(UserFields.PreviousLayer) prevLayer: LayerBase,
                @CustomUserField("@dropout_rate", "@dropout_rate_hint", 0) dropout_rate: Double) :
            this(prevLayer.out_data_size(), dropout_rate)

    companion object {
        /**
         * @param in_dim       [Long] number of elements of the input
         * @param dropout_rate [Double] (0-1) fraction of the input units to be dropped
         */
        @JvmStatic
        external fun staticConstructor(in_dim: Long, dropout_rate: Double): Long

        fun wrap(handle: Long): DropoutLayer = DropoutLayer(handle)
    }
}
