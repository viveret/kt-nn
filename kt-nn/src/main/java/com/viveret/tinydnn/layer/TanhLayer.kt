package com.viveret.tinydnn.layer

import com.viveret.tinydnn.reflection.annotations.UserConstructor

class TanhLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor("@layer_tanh", "")
    constructor() : this(staticConstructor())

    companion object {
        @JvmStatic
        external fun staticConstructor(): Long

        fun attach(handle: Long): TanhLayer = TanhLayer(handle)
    }
}
