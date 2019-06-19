package com.viveret.tinydnn.tinydnn

import com.viveret.tinydnn.util.JniObject

class Index3D private constructor(override val nativeObjectHandle: Long) : JniObject {

    internal val width: Long
        get() = jniGetWidth(nativeObjectHandle)

    internal val height: Long
        get() = jniGetHeight(nativeObjectHandle)

    internal val depth: Long
        get() = jniGetDepth(nativeObjectHandle)

    constructor(width: Long, height: Long, depth: Long) : this(staticConstructor(width, height, depth))

    constructor() : this(staticConstructor())

    internal fun reshape(width: Long, height: Long, depth: Long) {
        jniReshape(nativeObjectHandle, width, height, depth)
    }

    internal fun get_index(x: Long, y: Long, channel: Long): Long = jniGetIndex(nativeObjectHandle, x, y, channel)

    internal fun area(): Long = jniArea(nativeObjectHandle)

    internal fun size(): Long = jniSize(nativeObjectHandle)

    override fun toString(): String = "[$width, $height, $depth]"

    companion object {
        @JvmStatic
        external fun staticConstructor(width: Long, height: Long, depth: Long): Long

        @JvmStatic
        external fun staticConstructor(): Long

        @JvmStatic
        external fun jniReshape(handle: Long, width: Long, height: Long, depth: Long): Long

        @JvmStatic
        external fun jniGetIndex(handle: Long, x: Long, y: Long, channel: Long): Long

        @JvmStatic
        external fun jniArea(handle: Long): Long

        @JvmStatic
        external fun jniSize(handle: Long): Long

        @JvmStatic
        external fun jniGetWidth(handle: Long): Long

        @JvmStatic
        external fun jniGetHeight(handle: Long): Long

        @JvmStatic
        external fun jniGetDepth(handle: Long): Long

        fun attach(handle: Long): Index3D = Index3D(handle)
    }
}
