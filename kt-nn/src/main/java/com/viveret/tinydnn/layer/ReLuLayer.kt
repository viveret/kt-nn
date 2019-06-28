package com.viveret.tinydnn.layer

import android.text.InputType
import com.viveret.tinydnn.reflection.annotations.CustomUserField
import com.viveret.tinydnn.reflection.annotations.UserConstructor
import com.viveret.tinydnn.reflection.annotations.UserField
import com.viveret.tinydnn.reflection.annotations.UserFields

class ReLuLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor("@layer_relu", "")
    constructor(@UserField(UserFields.InWidth) width: Long,
                @UserField(UserFields.InHeight) height: Long,
                @CustomUserField("@nmaps", "@nmaps_hint", InputType.TYPE_CLASS_NUMBER) nmaps: Long) : this(
        staticConstructor(width, height, nmaps)
    )

    companion object {
        @JvmStatic
        external fun staticConstructor(width: Long, height: Long, nmaps: Long): Long

        fun attach(handle: Long): ReLuLayer = ReLuLayer(handle)
    }
}
