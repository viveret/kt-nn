package com.viveret.tinydnn.tinydnn.layer

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.reflection.annotations.UserConstructor

class AsinhLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor(R2.string.layer_asinh, 0)
    constructor() : this(staticConstructor())

    companion object {
        @JvmStatic
        external fun staticConstructor(): Long

        fun attach(handle: Long): AsinhLayer = AsinhLayer(handle)
    }
}
