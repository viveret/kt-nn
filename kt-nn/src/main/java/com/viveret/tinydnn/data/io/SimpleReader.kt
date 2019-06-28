package com.viveret.tinydnn.data.io

interface SimpleReader<T> {
    fun open(dataSelection: DataSelection)
    fun read(destination: Array<T>): Int
    val isOpen: Boolean
    fun close()
    val supportsSeek: Boolean
    fun seek(pos: Long, relativeTo: SeekRelativity)
}