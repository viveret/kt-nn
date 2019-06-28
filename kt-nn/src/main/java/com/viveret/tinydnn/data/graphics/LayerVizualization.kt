package com.viveret.tinydnn.data.graphics

import android.graphics.Bitmap

class LayerVizualization(val subject: VizualizationSubject, val image: LayerImage) {
    fun asBitmap(width: Int, height: Int): Bitmap {
        val bmp = image.asBitmap()
        return Bitmap.createScaledBitmap(bmp, width, height, false)
    }

    fun asBitmap(): Bitmap = image.asBitmap()

    fun asIcon(): Bitmap = this.asBitmap(256, 256)

    fun asBitmapAtLeast(minWidth: Int, minHeight: Int): Bitmap {
        return if (image.width > image.height && image.width < minWidth) {
            asBitmap(minWidth, Math.round(image.height * minWidth.toDouble() / image.width).toInt())
        } else if (image.width < image.height && image.height < minHeight) {
            asBitmap(Math.round(image.width * minHeight.toDouble() / image.height).toInt(), minHeight)
        } else {
            asBitmap()
        }
    }
}