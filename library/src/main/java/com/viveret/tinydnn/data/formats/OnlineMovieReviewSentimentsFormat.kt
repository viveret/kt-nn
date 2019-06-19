package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.StreamPackage
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.DataSelection
import com.viveret.tinydnn.data.io.SeekRelativity
import com.viveret.tinydnn.data.io.TsvReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.TrainingDataReader
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.tinydnn.Vect
import java.io.EOFException
import java.io.File
import java.util.*
import java.util.regex.Pattern

class OnlineMovieReviewSentimentsFormat(context: Context) : TrainingDataReader {
    override fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues {
        val inputVects = readVects("input", tasks.getValue(dataSuite.inputFile), dataSuite.inputFile, this.inputVectReader, project!!.get().in_data_size())
        val inputLabels = if (dataSuite.labelFile != null && tasks.containsKey(dataSuite.labelFile!!)) readLabels("label", tasks.getValue(dataSuite.labelFile!!), dataSuite.labelFile!!) else ArrayList()
        val vals = DataValues(inputVects, inputLabels)
        vals.format = this
        return vals
    }

    override fun getTrainingData(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues? {
        val inputVects = readVects("input", tasks.getValue(dataSuite.inputFile), dataSuite.inputFile, this.inputVectReader, project!!.get().in_data_size())
        val inputLabels = if (dataSuite.labelFile != null && tasks.containsKey(dataSuite.labelFile!!)) readLabels("label", tasks.getValue(dataSuite.labelFile!!), dataSuite.labelFile!!) else ArrayList()

        return if (dataSuite.fitToFile != null && tasks.containsKey(dataSuite.fitToFile!!)) {
            val fitToVects = readVects("fitTo", tasks.getValue(dataSuite.fitToFile!!), dataSuite.fitToFile!!, this.fitToVectReader, project.get().out_data_size())
            val fitToLabels = if (dataSuite.fitToLabelsFile != null && tasks.containsKey(dataSuite.fitToLabelsFile!!)) readLabels("fitToLabel", tasks.getValue(dataSuite.fitToLabelsFile!!), dataSuite.fitToLabelsFile!!) else ArrayList()
            val vals = TrainingDataValues(inputVects, inputLabels, fitToVects, fitToLabels)
            vals.format = this
            vals.fitTo!!.format = this
            vals
        } else {
            val vals = TrainingDataValues(inputVects, inputLabels)
            vals.format = this
            vals
        }
    }

    fun readVects(purpose: String, file: File, metaInfo: Stream, vectReader: VectReader, maxLength: Long): ArrayList<Vect> {
        val dataSelection = DataSelection()
        dataSelection[purpose] = DataSelection.Item(metaInfo, file.inputStream())
        val vects = ArrayList<Vect>()
        vectReader.open(dataSelection)
        val bufVect = Array(10) { Vect.empty }
        var n = 1
        var bytesRead = 0L
        while (n > 0 && bytesRead < (1000 * 1000)) {
            n = vectReader.read(bufVect, maxLength)
            for (i in 0 until n) {
                vects.add(bufVect[i])
                bytesRead += bufVect[i].vals.size * 4
            }
        }
        vectReader.close()
        return vects
    }

    fun readLabels(purpose: String, file: File, metaInfo: Stream): ArrayList<Long> {
        throw Exception()
    }

    override val formatId = UUID.fromString("bd6162ba-503d-11e9-8647-d663bd873d93")!!
    override val inputVectReader = ReviewVectReader(true)
    override val inputLabelReader
        get() = throw Exception()
    override val fitToVectReader = SentimentVectReader(false)
    override val fitToLabelReader
        get() = throw Exception()

    class ReviewVectReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false, delimiter = Regex(Pattern.quote("|"))), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(dataSelection: DataSelection) {
            this.openTsv(dataSelection)
        }

        override fun read(destination: Array<Vect>): Int = this.read(destination, -1)

        override fun read(destination: Array<Vect>, maxLength: Long): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val line = this.readLine()
                val summaryString = if (line.isNotEmpty()) line[0] else ""
                val summaryText = FloatArray(if (maxLength > 0) maxLength.toInt() else summaryString.length, if (normalizeBytes) {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() / Char.MAX_VALUE.toFloat() else 0.0f }
                } else {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() else 0.0f }
                })
                destination[vectIndex] = Vect(summaryText)
                numVectsRead++
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class SentimentVectReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false, delimiter = Regex(Pattern.quote("|"))), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(dataSelection: DataSelection) {
            this.openTsv(dataSelection)
        }

        override fun read(destination: Array<Vect>): Int = this.read(destination, -1)

        override fun read(destination: Array<Vect>, maxLength: Long): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            try {
                for (vectIndex in 0 until numberOfVectsToRead) {
                    val line = this.readLine()
                    if (line.size > 1) {
                        val probabilities = FloatArray(1) {  line[1].toFloat() }
                        destination[numVectsRead] = Vect(probabilities)
                        numVectsRead++
                    }
                }
            } catch (e: EOFException) {
                this.isOpen = false
                this.close()
            }
            return numVectsRead
        }

        override var isOpen: Boolean = true

        override fun close() = this.inputStream.close()
    }
}