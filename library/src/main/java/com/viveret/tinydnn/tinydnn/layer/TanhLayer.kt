package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.UserConstructor

class TanhLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor(R2.string.layer_tanh, 0)
    constructor() : this(staticConstructor())

    companion object {
        @JvmStatic
        external fun staticConstructor(): Long

        fun attach(handle: Long): TanhLayer = TanhLayer(handle)
    }
}
