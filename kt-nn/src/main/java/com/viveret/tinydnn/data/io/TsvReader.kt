package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.BetterInputStream
import com.viveret.tinydnn.basis.Stream
import java.io.DataInputStream

open class TsvReader(val normalizeBytes: Boolean, val hasHeaders: Boolean, val delimiter: Regex = Regex("\t")) {
    lateinit var stream: Stream
    lateinit var inputStream: DataInputStream
    lateinit var columns: Array<String>

    fun openTsv(inputStream: BetterInputStream) {
        this.stream = inputStream.source
        this.inputStream = DataInputStream(inputStream.currentStream)
        if (hasHeaders) {
            this.columns = this.readLine()
        }
    }

    fun readLine(): Array<String> {
        val tsvString = inputStream.readLine()
        return if (tsvString != null) {
            try {
                tsvString.split(delimiter).map { x -> x.trim(' ', '"', '\'') }.toTypedArray()
            } catch (e: Exception) {
                throw Exception("Invalid format for input $tsvString")
            }
        } else {
            emptyArray()
        }
    }
}