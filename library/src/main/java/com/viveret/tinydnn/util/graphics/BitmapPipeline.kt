package com.viveret.tinydnn.util.graphics

import android.graphics.Bitmap

class BitmapPipeline(val decorators: List<BitmapDecorator>) : BitmapDecorator {
    override fun apply(src: Bitmap): Bitmap {
        var last = src
        for (d in decorators) {
            last = d.apply(last)
        }
        return last
    }
}