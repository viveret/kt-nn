package com.viveret.tinydnn.tinydnn.util

import android.graphics.Bitmap
import android.graphics.Color
import com.viveret.tinydnn.tinydnn.Vect
import com.viveret.tinydnn.tinydnn.enums.LayerImageFormat
import com.viveret.tinydnn.util.JniObject

class LayerImage: JniObject {
    private val MAX_SIZE = 4096 * 2

    constructor(handle: Long) {
        this.nativeObjectHandle = if (handle > 0) handle else throw IllegalArgumentException("Invalid handle for layer image ($handle)")
        when {
            this.width > MAX_SIZE -> throw Exception("Width too big (${this.width} > $MAX_SIZE)")
            this.height > MAX_SIZE -> throw Exception("Height too big (${this.height} > $MAX_SIZE)")
            this.depth > MAX_SIZE -> throw Exception("Depth too big (${this.depth} > $MAX_SIZE)")
        }
    }

    fun asBitmap(): Bitmap {
        val bmp = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val pixelIntData = this.pixelData.map { x -> x.toInt() }
        val bmpPixelData: IntArray = when (this.format) {
            LayerImageFormat.GrayScale -> {
                val minIntensity = pixelIntData.min()!!
                val maxIntensity = pixelIntData.max()!!
                val diffIntensity = maxIntensity - minIntensity
                if (diffIntensity >= 1 || diffIntensity < 0) {
                    pixelIntData.map { x ->
                        val px = (x - minIntensity) * 255 / diffIntensity
                        Color.argb(255, px, px, px)
                    }.toIntArray()
                } else {
                    pixelIntData.map { x -> Color.argb(255, x, x, x) }.toIntArray()
                }
            }
            LayerImageFormat.BGR -> {
                val tmpData = IntArray((width * height).toInt())
                for (i in 0 until pixelIntData.size) {
                    tmpData[i] = Color.argb(255,
                            pixelIntData[i * 3 + 2],
                            pixelIntData[i * 3 + 1],
                            pixelIntData[i * 3 + 0])
                }
                tmpData
            }
            LayerImageFormat.RGB -> {
                val tmpData = IntArray((width * height).toInt())
                for (i in 0 until pixelIntData.size) {
                    tmpData[i] = Color.argb(255,
                            pixelIntData[i * 3 + 0],
                            pixelIntData[i * 3 + 1],
                            pixelIntData[i * 3 + 2])
                }
                tmpData
            }
        }
        bmp.setPixels(bmpPixelData, 0, width.toInt(), 0, 0, width.toInt(), height.toInt())
        return bmp
    }

    override var nativeObjectHandle: Long = 0
        protected set

    val pixelData: ByteArray
            get() = jniGetPixelData(nativeObjectHandle)

    val format: LayerImageFormat
        get()  = LayerImageFormat.values()[jniGetFormat(nativeObjectHandle)]

    val width: Long
        get()  = jniGetWidth(nativeObjectHandle)

    val height: Long
        get()  = jniGetHeight(nativeObjectHandle)

    val depth: Long
        get()  = jniGetDepth(nativeObjectHandle)

    /**
     * create image from raw data
     */
    constructor(data: FloatArray, width: Long, height: Long, type: LayerImageFormat) {
        this.nativeObjectHandle = jniConstructor(data, width, height, type.ordinal)
    }

    fun resize(width: Long, height: Long) {
        jniResize(this.nativeObjectHandle, width, height)
    }

    val empty: Boolean
        get() = jniEmpty(this.nativeObjectHandle)

    companion object {
        @JvmStatic
        external fun jniConstructor(data: FloatArray, width: Long, height: Long, type: Int): Long

        @JvmStatic
        external fun jniResize(handle: Long, width: Long, height: Long)

        @JvmStatic
        external fun jniGetPixelData(handle: Long): ByteArray

        @JvmStatic
        external fun jniGetFormat(handle: Long): Int

        @JvmStatic
        external fun jniGetWidth(handle: Long): Long

        @JvmStatic
        external fun jniGetHeight(handle: Long): Long

        @JvmStatic
        external fun jniGetDepth(handle: Long): Long

        @JvmStatic
        external fun jniEmpty(handle: Long): Boolean

        fun attach(handle: Long): LayerImage = LayerImage(handle)

        val empty = Vect(emptyArray<Float>().toFloatArray())
    }
}