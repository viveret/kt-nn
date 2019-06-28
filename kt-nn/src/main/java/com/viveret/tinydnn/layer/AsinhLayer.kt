package com.viveret.tinydnn.layer

import com.viveret.tinydnn.reflection.annotations.UserConstructor

class AsinhLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor("@layer_asinh", "")
    constructor() : this(staticConstructor())

    companion object {
        @JvmStatic
        external fun staticConstructor(): Long

        fun attach(handle: Long): AsinhLayer = AsinhLayer(handle)
    }
}
