package com.viveret.tinydnn.basis

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

class FileStreamProvider(val path: String, val id: UUID):InputStreamProvider {
    override fun open(context: Context): BetterInputStream = com.viveret.tinydnn.data.io.FileInputStream(File(path), FileStream(context, path, id))
}