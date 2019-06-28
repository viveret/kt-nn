package com.viveret.tinydnn.network

class GraphNetworkModelWithWeights private constructor(nativeObjectHandle: Long) : AbstractNetworkModelWithWeights(nativeObjectHandle) {

    @JvmOverloads
    constructor(name: String? = null) : this(staticConstructor(name))

    companion object {
        private external fun staticConstructor(name: String?): Long
        private external fun jniAddLayer(handle: Long, layerHandle: Long)

        fun attach(nativeObjectHandle: Long): GraphNetworkModelWithWeights = GraphNetworkModelWithWeights(nativeObjectHandle)
    }
}
