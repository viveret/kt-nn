package com.viveret.tinydnn.data.formats

import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.StreamPackage
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.DataSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.TrainingDataReader
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.tinydnn.Vect
import java.io.File
import java.util.*

abstract class BufferedSingleTrainingDataReader: TrainingDataReader {
    override fun getTrainingData(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues? {
        val inputVects = readVects("input", tasks.getValue(dataSuite.inputFile), dataSuite.inputFile, this.inputVectReader)
        val inputLabels = if (dataSuite.labelFile != null && tasks.containsKey(dataSuite.labelFile!!)) readLabels("label", tasks.getValue(dataSuite.labelFile!!), dataSuite.labelFile!!) else ArrayList()

        return if (dataSuite.fitToFile != null && tasks.containsKey(dataSuite.fitToFile!!)) {
            val fitToVects = readVects("fitTo", tasks.getValue(dataSuite.fitToFile!!), dataSuite.fitToFile!!, this.fitToVectReader!!)
            val fitToLabels = if (dataSuite.fitToLabelsFile != null && tasks.containsKey(dataSuite.fitToLabelsFile!!)) readLabels("fitToLabel", tasks.getValue(dataSuite.fitToLabelsFile!!), dataSuite.fitToLabelsFile!!) else ArrayList()
            val vals = TrainingDataValues(inputVects, inputLabels, fitToVects, fitToLabels)
            vals.format = this
            vals.fitTo!!.format = this
            vals.valueMetaInfo = dataSuite.inputFile
            vals.labelMetaInfo = dataSuite.labelFile
            vals.fitTo.valueMetaInfo = dataSuite.fitToFile
            vals.fitTo.labelMetaInfo = dataSuite.fitToLabelsFile
            vals
        } else {
            val vals = TrainingDataValues(inputVects, inputLabels)
            vals.format = this
            vals.valueMetaInfo = dataSuite.inputFile
            vals.labelMetaInfo = dataSuite.labelFile
            vals
        }
    }

    override fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues {
        val inputVects = readVects("input", tasks.getValue(dataSuite.inputFile), dataSuite.inputFile, this.inputVectReader)
        val inputLabels = if (dataSuite.labelFile != null && tasks.containsKey(dataSuite.labelFile!!)) readLabels("label", tasks.getValue(dataSuite.labelFile!!), dataSuite.labelFile!!) else ArrayList()
        val vals = DataValues(inputVects, inputLabels)
        vals.format = this
        vals.valueMetaInfo = dataSuite.inputFile
        vals.labelMetaInfo = dataSuite.labelFile
        return vals
    }

    fun readVects(purpose: String, file: File, metaInfo: Stream, vectReader: VectReader): ArrayList<Vect> {
        val dataSelection = DataSelection()
        dataSelection[purpose] = DataSelection.Item(metaInfo, file.inputStream())
        val vects = ArrayList<Vect>()
        vectReader.open(dataSelection)
        val bufVect = Array(10) { Vect.empty }
        var n = 1
        while (n > 0 && vects.size < this.countElementsIn(vectReader)) {
            n = vectReader.read(bufVect)
            for (i in 0 until n) {
                vects.add(bufVect[i])
            }
        }
        vectReader.close()
        return vects
    }

    abstract fun countElementsIn(vectReader: VectReader): Int

    abstract fun countElementsIn(vectReader: LabelReader): Int

    fun readLabels(purpose: String, file: File, metaInfo: Stream): ArrayList<Long> {
        val labels = ArrayList<Long>()
        val labelReader = this.inputLabelReader
        if (labelReader != null) {
            val dataSelection = DataSelection()
            dataSelection[purpose] = DataSelection.Item(metaInfo, file.inputStream())
            labelReader.open(dataSelection)
            val bufLabels = Array(Math.min(10, countElementsIn(labelReader))) { 0L }
            var n = 1
            while (n > 0 && labels.size < countElementsIn(labelReader)) {
                n = labelReader.read(bufLabels)
                for (i in 0 until n) {
                    labels.add(bufLabels[i])
                }
            }
            labelReader.close()
        }
        return labels
    }
}