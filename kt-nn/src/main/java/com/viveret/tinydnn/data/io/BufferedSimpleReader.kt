package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.AnchorPoint
import com.viveret.tinydnn.basis.BetterInputStream
import kotlin.math.min

class BufferedSimpleReader<T>(private val innerReader: SimpleReader<T>, private val buf: Array<T>) : SimpleReader<T> where T : Any {
    override val supportsSeek: Boolean = false

    override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun open(inputStream: BetterInputStream) = innerReader.open(inputStream)

    override fun read(destination: Array<T>, offset: Int, count: Int): Int {
        if (destination.size % buf.size != 0) {
            throw IllegalArgumentException("Destination of elementCount ${destination.size} is not a multiple of buffer elementCount ${buf.size}")
        }
        var n = 0
        var nbuf: Int
        while (n < destination.size) {
            nbuf = innerReader.read(this.buf, offset, count)
            val ndest = min(nbuf, destination.size - n)
            buf.copyInto(destination, n, 0, ndest)
            n += ndest
        }
        return n
    }

    override val isOpen = innerReader.isOpen

    override fun close() = innerReader.close()
}