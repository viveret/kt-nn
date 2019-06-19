package com.viveret.tinydnn.data

import com.viveret.tinydnn.tinydnn.enums.ContentType
import com.viveret.tinydnn.tinydnn.enums.FileFormat
import java.io.File

class SaveConfig(val outputFile: File, val saveFormat: FileFormat, val saveWhat: ContentType) {
    override fun toString(): String = "$saveWhat as $saveFormat at ${outputFile.absolutePath}"
    private var myIsSaved = false

    val isSaved
        get() = this.myIsSaved

    fun saved() {
        this.myIsSaved = true
    }
}
