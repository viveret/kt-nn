package com.viveret.tinydnn.basis

import java.io.InputStream
import java.io.OutputStream

interface Stream: Indexable, AttributeResolver {
    val name: String
    val extension: String
    val namespace: String
    val mime: String
    val host: Host?
    val role: DataRole
    fun sourcePath(source: DataSource): String
    fun sourceStream(source: DataSource): BetterInputStream
    fun destinationStream(source: DataSource): OutputStream
    fun size(source: DataSource): Long
}