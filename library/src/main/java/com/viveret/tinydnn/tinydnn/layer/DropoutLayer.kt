package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class DropoutLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param in_dim       [Long] number of elements of the input
     * @param dropout_rate [Double] (0-1) fraction of the input units to be dropped
     **/
    @UserConstructor(R2.string.layer_dropout, 0)
    constructor(@UserField(UserFields.InDim) in_dim: Long,
                @CustomUserField(R2.string.dropout_rate, R2.string.dropout_rate_hint, 0) dropout_rate: Double) :
            this(staticConstructor(in_dim, dropout_rate))

    @UserConstructor(R2.string.layer_dropout, 0)
    constructor(@UserField(UserFields.PreviousLayer) prevLayer: LayerBase,
                @CustomUserField(R2.string.dropout_rate, R2.string.dropout_rate_hint, 0) dropout_rate: Double) :
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
