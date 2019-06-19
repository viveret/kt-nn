package com.viveret.tinydnn.basis

import android.content.Context
import java.io.InputStream
import java.io.OutputStream

interface Stream: Indexable {
    val name: String
    val extension: String
    val namespace: String
    val mime: String
    val host: Host?
    fun sourcePath(source: DataSource, context: Context): String
    fun sourceStream(source: DataSource, context: Context): InputStream
    fun destinationStream(source: DataSource, context: Context): OutputStream
}