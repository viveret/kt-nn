package com.viveret.tinydnn.util

class StringHelper {
    companion object {
        fun extensionToMime(extension: String): String {
            return when (extension) {
                "txt" -> "application/text"
                "gz" -> "application/gzip"
                "cifar10.gz" -> "application/cifar10"
                "cifar10" -> "application/cifar10"
                else -> "*/*"
            }
        }
    }
}