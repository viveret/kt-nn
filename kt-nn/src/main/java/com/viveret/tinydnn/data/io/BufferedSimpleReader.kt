package com.viveret.tinydnn.data.io

class BufferedSimpleReader<T>(private val innerReader: SimpleReader<T>, private val buf: Array<T>) : SimpleReader<T> where T : Any {
    override val supportsSeek: Boolean = false

    override fun seek(pos: Long, relativeTo: SeekRelativity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun open(dataSelection: DataSelection) = innerReader.open(dataSelection)

    override fun read(destination: Array<T>): Int {
        if (destination.size % buf.size != 0) {
            throw IllegalArgumentException("Destination of size ${destination.size} is not a multiple of buffer size ${buf.size}")
        }
        var n = 0
        var nbuf = 0
        while (n < destination.size) {
            nbuf = innerReader.read(this.buf)
            val ndest = Math.min(nbuf, destination.size - n)
            buf.copyInto(destination, n, 0, ndest)
            n += ndest
        }
        return n
    }

    override val isOpen = innerReader.isOpen

    override fun close() = innerReader.close()
}