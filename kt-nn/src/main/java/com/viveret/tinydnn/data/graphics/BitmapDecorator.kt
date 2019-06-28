package com.viveret.tinydnn.data.graphics

import android.graphics.Bitmap

interface BitmapDecorator {
    fun apply(src: Bitmap): Bitmap
}