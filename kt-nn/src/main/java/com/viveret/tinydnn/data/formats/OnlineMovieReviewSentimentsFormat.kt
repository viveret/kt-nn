package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.*
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.*
import com.viveret.tinydnn.data.train.DataSliceReader
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.EOFException
import java.io.File
import java.util.*
import java.util.regex.Pattern

@Mime(arrayOf("text/csv"))
class OnlineMovieReviewSentimentsFormat(context: Context) : DataSliceReader {
    override val openRoles: Array<DataRole>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun read(destination: DataValues, offset: Int, amountToRead: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun open(inputSelection: InputSelection) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    override fun vectReader(role: DataRole): VectReader? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun labelReader(role: DataRole): LabelReader? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues {
//        val inputFile = dataSuite.streams.getValue(DataRole.Input)
//        val labelFile = dataSuite.streams[DataRole.InputLabels]
//
//        val inputVects = readVects(DataRole.Input, tasks.getValue(inputFile), inputFile, this.inputVectReader, project!!.get().in_data_size())
//        val inputLabels = if (labelFile != null && tasks.containsKey(labelFile)) readLabels(DataRole.InputLabels, tasks.getValue(labelFile), labelFile) else ArrayList()
//        val vals = DataValues(inputVects, inputLabels)
//        vals.format = this
//        return vals
//    }

//    override fun read(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues? {
//        val inputFile = dataSuite.streams.getValue(DataRole.Input)
//        val labelFile = dataSuite.streams[DataRole.InputLabels]
//        val fitToFile = dataSuite.streams[DataRole.FitTo]
//        val fitToLabelsFile = dataSuite.streams[DataRole.FitToLabels]
//
//        val inputVects = readVects(DataRole.Input, tasks.getValue(inputFile), inputFile, this.inputVectReader, project!!.get().in_data_size())
//        val inputLabels = if (labelFile != null && tasks.containsKey(labelFile)) readLabels(DataRole.InputLabels, tasks.getValue(labelFile), labelFile) else ArrayList()
//
//        return if (fitToFile != null && tasks.containsKey(fitToFile)) {
//            val fitToVects = readVects(DataRole.FitTo, tasks.getValue(fitToFile), fitToFile, this.fitToVectReader, project.get().out_data_size())
//            val fitToLabels = if (fitToLabelsFile != null && tasks.containsKey(fitToLabelsFile)) readLabels(DataRole.FitToLabels, tasks.getValue(fitToLabelsFile), fitToLabelsFile) else ArrayList()
//            val vals = TrainingDataValues(inputVects, inputLabels, fitToVects, fitToLabels)
//            vals.format = this
//            vals.fitTo!!.format = this
//            vals
//        } else {
//            val vals = TrainingDataValues(inputVects, inputLabels)
//            vals.format = this
//            vals
//        }
//    }

    fun readLabels(purpose: DataRole, file: File, metaInfo: Stream): ArrayList<Long> {
        throw Exception()
    }

//    override val inputVectReader = ReviewVectReader(true)
//    override val inputLabelReader
//        get() = throw Exception()
//    override val fitToVectReader = SentimentVectReader(false)
//    override val fitToLabelReader
//        get() = throw Exception()

    class ReviewVectReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false, delimiter = Regex(Pattern.quote("|"))), VectReader {
        override val supportsSeek: Boolean = false
        override fun getInt(attr: DataAttr): Int? = null

        override fun getString(attr: DataAttr): String? = null

        override fun getBoolean(attr: DataAttr): Boolean? = null

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(inputStream: BetterInputStream) {
            this.openTsv(inputStream)
        }

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val line = this.readLine()
                val summaryString = if (line.isNotEmpty()) line[0] else ""
                val summaryText = FloatArray(if (count > 0) count.toInt() else summaryString.length, if (normalizeBytes) {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() / Char.MAX_VALUE.toFloat() else 0.0f }
                } else {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() else 0.0f }
                })
                destination[vectIndex] = Vect(summaryText, summaryText.size)
                numVectsRead++
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class SentimentVectReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false, delimiter = Regex(Pattern.quote("|"))), VectReader {
        override val supportsSeek: Boolean = false
        override fun getInt(attr: DataAttr): Int? = null

        override fun getString(attr: DataAttr): String? = null

        override fun getBoolean(attr: DataAttr): Boolean? = null

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(inputStream: BetterInputStream) {
            this.openTsv(inputStream)
        }

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            try {
                for (vectIndex in 0 until numberOfVectsToRead) {
                    val line = this.readLine()
                    if (line.size > 1) {
                        val probabilities = FloatArray(1) {  line[1].toFloat() }
                        destination[numVectsRead] = Vect(probabilities, probabilities.size)
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