package com.viveret.tinydnn.basis

import java.io.InputStream

abstract class BetterInputStream: AttributeResolver {
    abstract val source: Stream
    abstract val size: Int
    abstract fun open(): InputStream

    override fun equals(other: Any?): Boolean = other is BetterInputStream && other.hashCode() == hashCode()

    override fun hashCode(): Int = source.hashCode()

    override fun getBoolean(attr: DataAttr): Boolean? = source.getBoolean(attr)

    override fun getInt(attr: DataAttr): Int? {
        return if (attr == DataAttr.ByteCount) {
            size
        } else {
            source.getInt(attr)
        }
    }

    override fun getString(attr: DataAttr): String? = source.getString(attr)

    val currentStream: InputStream
        get() {
            if (openStream == null) {
                openStream = open()
            }
            return openStream!!
        }

    fun reset() {
        openStream?.close()
        openStream = open()
    }

    fun close() {
        openStream?.close()
        openStream = null
    }

    private var openStream: InputStream? = null
}