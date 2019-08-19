package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.BetterInputStream
import com.viveret.tinydnn.basis.Stream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpInputStream(val url: URL, override val source: Stream) : BetterInputStream() {
    private var contentSize = 0
    override val size: Int
        get() = contentSize

    override fun open(): InputStream {
        val conn = url.openConnection() as HttpURLConnection
        conn.connect()
        val code = conn.responseCode
        if (code == 200) {
            contentSize = conn.contentLength
            return conn.inputStream
        } else {
            throw Exception("$url returned $code: ${conn.responseMessage}")
        }
    }
}