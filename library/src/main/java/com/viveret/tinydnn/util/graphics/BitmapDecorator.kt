package com.viveret.tinydnn.util.graphics

import android.graphics.Bitmap

interface BitmapDecorator {
    fun apply(src: Bitmap): Bitmap
}