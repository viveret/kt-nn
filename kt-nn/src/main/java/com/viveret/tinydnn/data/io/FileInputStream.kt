package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.BetterInputStream
import com.viveret.tinydnn.basis.Stream
import java.io.File
import java.io.InputStream

class FileInputStream(val file: File, override val source: Stream): BetterInputStream() {
    override val size: Int
        get() = file.length().toInt()

    override fun open(): InputStream = SmartFileInputStream(file)
}