package com.viveret.tinydnn.data.nav

interface FileInfo {
    val canonicalPath: String
    val extension: String
    val name: String
    val nameWithoutExtension: String
    val lastModified: Long
    val isDirectory: Boolean
}