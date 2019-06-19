package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.DataSource
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.data.io.DataSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.SeekRelativity
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.tinydnn.Vect
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.util.*

// https://github.com/tiny-dnn/tiny-dnn/blob/1c5259477b8b4eab376cc19fd1d55ae965ef5e5a/tiny_dnn/io/cifar10_parser.h
class Cifar10Parser(val context: Context) : BufferedSingleTrainingDataReader() {
    override fun countElementsIn(vectReader: LabelReader): Int = 100

    override fun countElementsIn(vectReader: VectReader): Int = 100 // todo: make not static constant

    override val formatId: UUID = UUID.fromString("176200b0-5879-11e9-8647-d663bd873d93")
    override val inputVectReader = Cifar10InputReader(context)
    override val inputLabelReader: LabelReader? = null
    override val fitToVectReader = Cifar10FitToReader(context)
    override val fitToLabelReader: LabelReader? = null

    class Cifar10InputReader(context: Context) : Cifar10FileReader(context), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {

        }

        override fun read(destination: Array<Vect>, maxLength: Long): Int = this.read(destination)

        override fun open(dataSelection: DataSelection) = this.openIdx(dataSelection)

        override fun read(destination: Array<Vect>): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0

            try {
                while (numVectsRead < numberOfVectsToRead) {
                    while (inputStream.skip(1) < 1L);
                    destination[numVectsRead] = readImage()
                    numVectsRead++
                }
            } catch (e: EOFException) {
                this.close()
            }
            return numVectsRead
        }
    }

    class Cifar10FitToReader(context: Context) : Cifar10FileReader(context), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun read(destination: Array<Vect>, maxLength: Long): Int = this.read(destination)

        override fun open(dataSelection: DataSelection) = this.openIdx(dataSelection)

        override fun read(destination: Array<Vect>): Int {
            val numberOfVectsToRead = destination.size
            val vectLength = 32 * 32 * 3L
            val possibleOutputs = 10
            var numVectsRead = 0
            val floatArray = FloatArray(possibleOutputs)
            try {
                for (vectIndex in 0 until numberOfVectsToRead) {
                    for (i in 0 until floatArray.size) {
                        floatArray[i] = 0.0f
                    }
                    floatArray[inputStream.readByte().toInt()] = 1.0f
                    destination[vectIndex] = Vect(floatArray)
                    numVectsRead++
                    var skipCount = vectLength
                    while (skipCount > 0) {
                        skipCount -= inputStream.skip(skipCount)
                    }
                }
            } catch (e: EOFException) {
                this.close()
            }
            return numVectsRead
        }
    }

    open class Cifar10FileReader(val context: Context) {
        lateinit var stream: Stream
        lateinit var inputStream: DataInputStream
        private var myIsOpen = false
        protected var persistBufferIndex = 0

        fun openIdx(dataSelection: DataSelection) {
            val tmp = dataSelection.values.first { x -> ".cifar10" == x.info.extension }
            this.stream = tmp.info
            this.inputStream = DataInputStream(BufferedInputStream(this.stream.sourceStream(DataSource.LocalFile, context)))
            this.myIsOpen = true
        }

        fun readImage(): Vect {
            val vectLength = 32 * 32 * 3
            val byteArray = ByteArray(vectLength)
            val floatArray = FloatArray(vectLength)

            var bytesLeft = vectLength
            var totalBytesRead = 0
            while (bytesLeft > 0) {
                val bytesRead = inputStream.read(byteArray, 0, bytesLeft)
                if (bytesRead < 0) {
                    break
                }
                for (i in 0 until bytesRead) {
                    floatArray[totalBytesRead + i] = (byteArray[i].toUByte().toFloat() + 128.0f) / 255.0f
                }
                bytesLeft -= bytesRead
                totalBytesRead += bytesRead
            }
            if (bytesLeft > 0) {
                throw Exception("Not enough image data read (should have been $vectLength, but only read $totalBytesRead)")
            }
            return Vect(floatArray)
        }

        val isOpen = myIsOpen

        fun close() = this.inputStream.close()
    }
}