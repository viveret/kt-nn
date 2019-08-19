package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.*
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.error.UserException
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.DataInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.zip.GZIPInputStream
import kotlin.math.min

@Mime(["image/*", "application/gzip"])
class MnistDataSliceFormat(val context: Context) : BufferedSingleDataSliceReader() {
    override fun vectReader(role: DataRole): VectReader? = if (role == DataRole.Input)
        MnistImageVectReader(true, 2051, context)
    else
        MnistOutputVectReader(false, 2049, context)

    override fun labelReader(role: DataRole): LabelReader? = if (role == DataRole.InputLabels)
        MnistLabelReader(false, 2051, context)
    else
        MnistLabelReader(false, 2049, context)

    class MnistImageVectReader(normalizeBytes: Boolean, magicNumber: Int, context: Context) :
        IdxReader(normalizeBytes, magicNumber, context), VectReader {
        override val supportsSeek: Boolean = false
        val outputVectSize = 32 * 32
        private lateinit var rawBytes: ByteArray
        val width get() = dims[1]
        val height get() = dims[2]

        val vectBuffer = FloatArray(outputVectSize)

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int =
            inputStream.skipBytes(rawBytes.size * offset) / rawBytes.size

        override fun open(inputStream: BetterInputStream) {
            this.openIdx(inputStream)
            rawBytes = ByteArray(width * height)
            if (rawBytes.isEmpty()) {
                throw java.lang.Exception("Invalid elementCount (dims: ${dims.joinToString(", ")})")
            }
        }

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
            var numVectsRead = 0
            for (vectIndex in 0 until min(count, destination.size - offset)) {
                val n = Companion.read(inputStream, rawBytes, true)
                if (n <= 0) {
                    return numVectsRead
                }

                if (outputVectSize != rawBytes.size) {
                    for (x in 0 until 32) {
                        vectBuffer[0 * 32 + x] = 0.0f
                        vectBuffer[1 * 32 + x] = 0.0f
                        vectBuffer[30 * 32 + x] = 0.0f
                        vectBuffer[31 * 32 + x] = 0.0f
                    }

                    for (y in 2 until 30) {
                        vectBuffer[y * 32 + 0] = 0.0f
                        vectBuffer[y * 32 + 1] = 0.0f
                        for (x in 2 until 30) {
                            vectBuffer[y * 32 + x] = (rawBytes[(y - 2) * 28 + (x - 2)] - 128) / 128.0f
                        }
                        vectBuffer[y * 32 + 30] = 0.0f
                        vectBuffer[y * 32 + 31] = 0.0f
                    }
                } else {
                    for (i in 0 until vectBuffer.size) {
                        vectBuffer[i] = (rawBytes[i] - 128) / 128.0f
                    }
                }
                destination[offset + vectIndex] = Vect(vectBuffer, outputVectSize)
                if (destination[offset + vectIndex].vals.size != outputVectSize) {
                    throw java.lang.Exception("destination[offset + vectIndex].vals.size != outputVectSize")
                }
                numVectsRead++
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class MnistOutputVectReader(normalizeBytes: Boolean, magicNumber: Int, context: Context) :
        IdxReader(normalizeBytes, magicNumber, context), VectReader {

        val outputVectSize = 10
        val vectBuffer = FloatArray(outputVectSize)
        override val supportsSeek: Boolean = false

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int =
            inputStream.skipBytes(offset) //  * outputVectSize

        override fun open(inputStream: BetterInputStream) = this.openIdx(inputStream)

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
            val labelBuffer = ByteArray(min(count, destination.size - offset))
            val numVectsRead = Companion.read(inputStream, labelBuffer, false)

            for (vectIndex in 0 until numVectsRead) {
                for (i in 0 until vectBuffer.size) {
                    vectBuffer[i] = 0.0f
                }

                vectBuffer[labelBuffer[vectIndex].toInt()] = 1.0f
                destination[offset + vectIndex] = Vect(vectBuffer, 10)
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class MnistLabelReader(normalizeBytes: Boolean, magicNumber: Int, context: Context) :
        IdxReader(normalizeBytes, magicNumber, context), LabelReader {
        override val supportsSeek: Boolean = false

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(inputStream: BetterInputStream) {
            this.openIdx(inputStream)
        }

        override fun read(destination: Array<Long>, offset: Int, count: Int): Int {
            var numLabelsRead = 0
            val buf = ByteArray(min(count, destination.size))

            while (numLabelsRead < destination.size) {
                val n = inputStream.read(buf)
                if (n > 0) {
                    for (v in 0 until n) {
                        if (numLabelsRead < destination.size) {
                            destination[numLabelsRead] = (buf[v] + 0L)
                            numLabelsRead++
                        } else {
                            break
                        }
                    }
                } else {
                    break
                }
            }
            return numLabelsRead
        }

        override val isOpen: Boolean = true

        override fun close() = this.inputStream.close()
    }

    open class IdxReader(val normalizeBytes: Boolean, val magicNumber: Int, val context: Context): AttributeResolver {
        override fun getInt(attr: DataAttr): Int? {
            return when (attr) {
                DataAttr.ElementCount -> numItems
                DataAttr.ElementByteSize -> dims.reduce { a, b -> a * b } / numItems
                else -> null
            }
        }

        override fun getString(attr: DataAttr): String? {
            return null
        }

        override fun getBoolean(attr: DataAttr): Boolean? {
            return null
        }

        lateinit var stream: Stream
        lateinit var inputStream: DataInputStream
        lateinit var dims: IntArray
        val numItems get() = dims[0]

        fun openIdx(inputStream: BetterInputStream) {
            val tmp = resolveCompressionType(inputStream)
            this.stream = tmp.info
            this.inputStream = DataInputStream(tmp.stream)

//            val buffer = StringBuffer()
//            var inputStr = inputStream.readLine()
//            while (inputStr.isNotEmpty()) {
//                buffer.append(tmp)
//                Log.e("com.viveret.pocketn2", inputStr)
//                inputStr = inputStream.readLine()
//            }
//
//            throw UserException(buffer.toString())

            val lastChar = this.stream.name.substring(0, this.stream.name.length - "-ubyte".length).last()
            this.dims = IntArray(lastChar.toString().toInt())

            val magicNumber = this.inputStream.readInt()
            if (this.magicNumber != magicNumber) {
                throw Exception("Invalid ${this.javaClass.name} magic number for ${this.stream.name} ($magicNumber != ${this.magicNumber})")
            }

            for (d in 0 until this.dims.size) {
                this.dims[d] = this.inputStream.readInt()
            }
        }

        fun resolveCompressionType(inputStream: BetterInputStream): CompressedStream {
            for (comp in CompressionType.values()) {
                if (comp.extension == inputStream.source.extension) {
                    return CompressedStream(comp, inputStream.source, inputStream.currentStream)
                }
            }
            throw Exception("Could not resolve compression type for $inputStream")
        }
    }

    class CompressedStream(val compressionType: CompressionType, val info: Stream, stream: InputStream) {
        val stream: InputStream = when (compressionType) {
            CompressionType.NONE -> stream
            CompressionType.GZIP -> GZIPInputStream(stream)
            else -> stream
        }
    }

    enum class CompressionType(val extension: String) {
        NONE(""),
        GZIP(".gz"),
        ZIP(".zip"),
        TAR(".tar"),
    }
}