package com.viveret.tinydnn.data.graphics

import android.graphics.Bitmap
import kotlin.math.roundToLong

class LayerVizualization(val subject: VizualizationSubject, val image: LayerImage) {
    fun asBitmap(width: Int, height: Int): Bitmap {
        val bmp = image.asBitmap()
        return Bitmap.createScaledBitmap(bmp, width, height, false)
    }

    fun asBitmap(): Bitmap = image.asBitmap()

    fun asIcon(): Bitmap = this.asBitmap(256, 256)

    fun asBitmapAtLeast(minWidth: Int, minHeight: Int): Bitmap {
        return if (image.width > image.height && image.width < minWidth) {
            asBitmap(minWidth, (image.height * minWidth.toDouble() / image.width).roundToLong().toInt())
        } else if (image.width < image.height && image.height < minHeight) {
            asBitmap((image.width * minHeight.toDouble() / image.height).roundToLong().toInt(), minHeight)
        } else {
            asBitmap()
        }
    }
}