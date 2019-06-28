package com.viveret.tinydnn.network

import java.io.File

class SequentialNetworkModelWithWeights private constructor(nativeObjectHandle: Long) : AbstractNetworkModelWithWeights(nativeObjectHandle) {
    constructor(name: String? = null) : this(staticConstructor(name))

    constructor(name: String, file: File) : this(name) {
        load(file.canonicalPath)
    }

    fun addLayer(layer: Layer<*>): SequentialNetworkModelWithWeights {
        jniAddLayer(nativeObjectHandle, layer.nativeObjectHandle)
        return this
    }

    fun insertLayer(insertPosition: Long, layer: Layer<*>): SequentialNetworkModelWithWeights {
        jniInsertLayer(nativeObjectHandle, insertPosition, layer.nativeObjectHandle)
        return this
    }

    override fun toString(): String = name
    fun removeLayers() {
        jniRemoveLayers(this.nativeObjectHandle)
    }

    companion object {
        @JvmStatic
        external fun staticConstructor(name: String?): Long

        @JvmStatic
        external fun jniAddLayer(handle: Long, layerHandle: Long)

        @JvmStatic
        external fun jniInsertLayer(handle: Long, insertPosition: Long, layerHandle: Long)

        @JvmStatic
        external fun jniRemoveLayers(handle: Long)

        fun attach(nativeObjectHandle: Long): SequentialNetworkModelWithWeights = SequentialNetworkModelWithWeights(nativeObjectHandle)
    }
}
