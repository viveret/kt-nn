package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.AnchorPoint
import com.viveret.tinydnn.basis.BetterInputStream

interface SimpleReader<T> {
    fun open(inputStream: BetterInputStream)
    fun read(destination: Array<T>, offset: Int, count: Int): Int
    val isOpen: Boolean
    fun close()
    val supportsSeek: Boolean
    fun seek(relativeTo: AnchorPoint, offset: Int): Int
}