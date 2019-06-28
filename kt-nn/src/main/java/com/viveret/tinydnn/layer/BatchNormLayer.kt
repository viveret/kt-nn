package com.viveret.tinydnn.layer

import android.text.InputType
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class BatchNormLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    /**
     * @param prev_layer      [LayerBase] previous layer to be connected with this layer
     * @param epsilon         [Double] small positive value to avoid zero-division
     * @param momentum        [Double] momentum in the computation of the exponential average of the mean/stddev of the data
     **/
    @UserConstructor("@layer_batch_norm", "")
    constructor(@UserField(UserFields.PreviousLayer) prev_layer: LayerBase,
                @CustomUserField("@epsilon", "@epsilon_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) epsilon: Double,
                @CustomUserField("@momentum", "@momentum_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) momentum: Double) :
            this(staticConstructor(prev_layer.nativeObjectHandle, epsilon, momentum))

    /**
     * @param prev_layer      [LayerBase] previous layer to be connected with this layer
     **/
    @UserConstructor("@layer_batch_norm", "")
    constructor(@UserField(UserFields.PreviousLayer) prev_layer: LayerBase) :
            this(staticConstructor(prev_layer.nativeObjectHandle, 0.00001, 0.999))

    companion object {
        @JvmStatic
        external fun staticConstructor(prev_layer: Long, epsilon: Double, momentum: Double): Long

        fun attach(handle: Long): BatchNormLayer = BatchNormLayer(handle) // batch_normalization_layer
    }
}