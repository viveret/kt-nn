package com.viveret.tinydnn.layer

import com.viveret.tinydnn.reflection.annotations.UserConstructor

class SigmoidLayer private constructor(nativeObjectHandle: Long) : LayerBase(nativeObjectHandle) {
    @UserConstructor("@layer_sigmoid", "")
    constructor() : this(staticConstructor())

    companion object {
        @JvmStatic
        external fun staticConstructor(): Long

        fun attach(handle: Long): SigmoidLayer = SigmoidLayer(handle)
    }
}
