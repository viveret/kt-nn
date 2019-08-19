package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.DataRole
import com.viveret.tinydnn.basis.Stream
import java.io.DataOutputStream
import java.io.OutputStream

class OutputSelection : HashMap<DataRole, OutputSelection.Item>() {
    fun close() {
        for (s in this.values) {
            s.stream.close()
        }
    }

    var elementCount = 0

    class Item(val info: Stream, val stream: DataOutputStream) {
        constructor(info: Stream, stream: OutputStream): this(info, DataOutputStream(stream))
    }
}