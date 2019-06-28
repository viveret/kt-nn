package com.viveret.tinydnn.layer

import android.text.InputType
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class PowerLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor("@layer_power", "")
    constructor(@UserField(UserFields.PreviousLayer) prevLayer: LayerBase,
                @CustomUserField("@factor", "@factor_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL) factor: Double,
                @CustomUserField("@scale", "@scale_hint", InputType.TYPE_NUMBER_FLAG_DECIMAL)  scale: Double) :
            this(staticConstructor(prevLayer.nativeObjectHandle, factor, scale))

    companion object {
        @JvmStatic
        external fun staticConstructor(prevLayer: Long, factor: Double, scale: Double): Long

        fun attach(handle: Long): PowerLayer = PowerLayer(handle)
    }
}
