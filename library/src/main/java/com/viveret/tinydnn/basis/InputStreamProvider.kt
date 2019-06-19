package com.viveret.tinydnn.basis

import android.content.Context
import java.io.InputStream

interface InputStreamProvider {
    fun open(context: Context): InputStream
}