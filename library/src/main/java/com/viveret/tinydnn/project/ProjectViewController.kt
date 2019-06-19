package com.viveret.tinydnn.project

import android.os.Bundle

interface ProjectViewController : ProjectController {
    fun switchToMode(mode: String)
    fun switchToMode(mode: String, args: (Bundle) -> Unit)
    fun message(exception: Exception, message: String)
}