package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.*
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException

// https://github.com/tiny-dnn/tiny-dnn/blob/1c5259477b8b4eab376cc19fd1d55ae965ef5e5a/tiny_dnn/io/cifar10_parser.h
@Mime(arrayOf("application/cifar10"))
class Cifar10Parser(val context: Context) : BufferedSingleDataSliceReader() {

    override fun getInt(attr: DataAttr): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(attr: DataAttr): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seek(relativeTo: AnchorPoint, offset: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun vectReader(role: DataRole): VectReader? = if (role == DataRole.Input)
        Cifar10InputReader(context)
    else
        Cifar10FitToReader(context)

    override fun labelReader(role: DataRole): LabelReader? = null

    class Cifar10InputReader(context: Context) : Cifar10FileReader(context), VectReader {
        override val supportsSeek: Boolean = false

        override fun getInt(attr: DataAttr): Int? = null

        override fun getString(attr: DataAttr): String? = null

        override fun getBoolean(attr: DataAttr): Boolean? = null

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            return 0
        }

        override fun open(inputStream: BetterInputStream) = this.openIdx(inputStream)

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
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
        override fun getInt(attr: DataAttr): Int? = null

        override fun getString(attr: DataAttr): String? = null

        override fun getBoolean(attr: DataAttr): Boolean? = null

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(inputStream: BetterInputStream) = this.openIdx(inputStream)

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
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
                    destination[vectIndex] = Vect(floatArray, floatArray.size)
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

        fun openIdx(inputStream: BetterInputStream) {
            this.stream = inputStream.source
            this.inputStream = DataInputStream(BufferedInputStream(inputStream.currentStream))
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
            return Vect(floatArray, floatArray.size)
        }

        val isOpen = myIsOpen

        fun close() = this.inputStream.close()
    }
}