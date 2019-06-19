package com.viveret.tinydnn.tinydnn.layer

import android.text.InputType
import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class PowerLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor(R2.string.layer_power, 0)
    constructor(@UserField(UserFields.PreviousLayer) prevLayer: LayerBase,
                @CustomUserField(R2.string.factor, R2.string.factor_hint, InputType.TYPE_NUMBER_FLAG_DECIMAL) factor: Double,
                @CustomUserField(R2.string.scale, R2.string.scale_hint, InputType.TYPE_NUMBER_FLAG_DECIMAL)  scale: Double) :
            this(staticConstructor(prevLayer.nativeObjectHandle, factor, scale))

    companion object {
        @JvmStatic
        external fun staticConstructor(prevLayer: Long, factor: Double, scale: Double): Long

        fun attach(handle: Long): PowerLayer = PowerLayer(handle)
    }
}
