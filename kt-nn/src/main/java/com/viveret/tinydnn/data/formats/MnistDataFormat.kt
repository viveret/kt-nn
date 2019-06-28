package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.io.DataSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.SeekRelativity
import com.viveret.tinydnn.data.io.VectReader
import java.io.DataInputStream
import java.util.*
import java.util.zip.GZIPInputStream

class MnistDataFormat(val context: Context) : BufferedSingleTrainingDataReader() {
    override fun countElementsIn(vectReader: LabelReader): Int = (vectReader as IdxReader).numItems

    override fun countElementsIn(vectReader: VectReader): Int = (vectReader as IdxReader).numItems

    override val formatId = UUID.fromString("44f43102-3054-4602-a10c-7f3c0d8cd3f7")!!
    override val inputVectReader = MnistVectReader(true, context)
    override val inputLabelReader = MnistLabelReader(false, context)
    override val fitToVectReader = MnistVectReader(false, context)
    override val fitToLabelReader = MnistLabelReader(false, context)

    class MnistVectReader(normalizeBytes: Boolean, context: Context) : IdxReader(normalizeBytes, context), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun read(destination: Array<Vect>, maxLength: Long): Int = this.read(destination)

        override fun open(dataSelection: DataSelection) = this.openIdx(dataSelection)

        override fun read(destination: Array<Vect>): Int {
            val numberOfVectsToRead = destination.size
            val numChannels = 1
            val vectLength = (if (this.dims.isNotEmpty()) this.dims.reduce { acc, n -> acc * n } else 1) * numChannels
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val floatArray: FloatArray
                if (normalizeBytes) {
                    if (vectLength > 1) {
                        floatArray = FloatArray(32 * 32 * numChannels)
                        for (x in 0 until 32) {
                            floatArray[0 * 32 + x] = 0.0f
                            floatArray[1 * 32 + x] = 0.0f
                            floatArray[30 * 32 + x] = 0.0f
                            floatArray[31 * 32 + x] = 0.0f
                        }

                        for (y in 2 until 30) {
                            floatArray[y * 32 + 0] = 0.0f
                            floatArray[y * 32 + 1] = 0.0f
                            for (x in 2 until 30) {
                                floatArray[y * 32 + x] = (inputStream.readUnsignedByte() - 128) / 128.0f
                            }
                            floatArray[y * 32 + 30] = 0.0f
                            floatArray[y * 32 + 31] = 0.0f
                        }
                    } else {
                        floatArray = FloatArray(vectLength)
                        for (p in 0 until vectLength) {
                            floatArray[p] = (inputStream.readUnsignedByte() - 128) / 128.0f
                        }
                    }
                } else {
                    floatArray = FloatArray(10)
                    floatArray[inputStream.readUnsignedByte()] = 1.0f
                }
                destination[vectIndex] = Vect(floatArray)
                numVectsRead++
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class MnistLabelReader(normalizeBytes: Boolean, context: Context) : IdxReader(normalizeBytes, context), LabelReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(dataSelection: DataSelection) {
            this.openIdx(dataSelection)
        }

        override fun read(destination: Array<Long>): Int {
            val numLabels = 128
            var numLabelsRead = 0
            val buf = ByteArray(Math.min(numLabels, destination.size))

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

    open class IdxReader(val normalizeBytes: Boolean, val context: Context) {
        lateinit var stream: Stream
        lateinit var inputStream: DataInputStream
        var numItems: Int = -1
        lateinit var dims: IntArray

        fun openIdx(dataSelection: DataSelection) {
            val tmp = dataSelection.values.first { x -> ".gz" == x.info.extension }
            this.stream = tmp.info
            val lastChar = this.stream.name.substring(0, this.stream.name.length - "-ubyte".length).last()
            this.dims = IntArray(lastChar.toString().toInt() - 1)
            this.inputStream = DataInputStream(GZIPInputStream(tmp.stream))
            if (inputStream.readInt() != 2049 + this.dims.size) {
                throw Exception("Invalid magic number")
            }
            this.numItems = Math.min(100, inputStream.readInt())
            for (d in 0 until this.dims.size) {
                this.dims[d] = inputStream.readInt()
            }
        }
    }
}