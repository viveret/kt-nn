package com.viveret.tinydnn.data.io

import java.io.File
import java.io.FileOutputStream

class SmartFileOutputStream(val file: File) : FileOutputStream(file) {
}