package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.Stream
import java.io.DataInputStream

open class TsvReader(val normalizeBytes: Boolean, val hasHeaders: Boolean, val delimiter: Regex = Regex("\t")) {
    lateinit var stream: Stream
    lateinit var inputStream: DataInputStream
    lateinit var columns: Array<String>

    fun openTsv(dataSelection: DataSelection) {
        this.stream = dataSelection.values.first().info
        this.inputStream = DataInputStream(dataSelection.values.first().stream)
        if (hasHeaders) {
            this.columns = this.readLine()
        }
    }

    fun readLine(): Array<String> {
        val tsvString = inputStream.readLine()
        if (tsvString != null) {
            try {
                return tsvString.split(delimiter).map { x -> x.trim(' ', '"', '\'') }.toTypedArray()
            } catch (e: Exception) {
                throw Exception("Invalid format for input $tsvString")
            }
        } else {
            return emptyArray()
        }
    }
}