package com.viveret.tinydnn.data.io

import java.io.File
import java.io.FileInputStream

class SmartFileInputStream(val file: File) : FileInputStream(file) {
    init {
        if (file.exists() && file.length() == 0L) {
            file.delete()
        }
    }
}