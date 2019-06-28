package com.viveret.tinydnn.data.formats

import com.viveret.tinydnn.basis.DataRole
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.StreamPackage
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.DataSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.TrainingDataReader
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.File
import java.util.*
import kotlin.math.min

abstract class BufferedSingleTrainingDataReader: TrainingDataReader {
    override fun getTrainingData(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues? {
        val inputFile = dataSuite.streams.getValue(DataRole.Input)
        val labelFile = dataSuite.streams[DataRole.InputLabels]
        val fitToFile = dataSuite.streams[DataRole.FitTo]
        val fitToLabelsFile = dataSuite.streams[DataRole.FitToLabels]

        val inputVects = readVects(DataRole.Input, tasks.getValue(inputFile), inputFile, this.inputVectReader)
        val inputLabels = if (labelFile != null && tasks.containsKey(labelFile)) readLabels(DataRole.InputLabels, tasks.getValue(labelFile), labelFile) else ArrayList()

        return if (fitToFile != null && tasks.containsKey(fitToFile)) {
            val fitToVects = readVects(DataRole.FitTo, tasks.getValue(fitToFile), fitToFile, this.fitToVectReader!!)
            val fitToLabels = if (fitToLabelsFile != null && tasks.containsKey(fitToLabelsFile)) readLabels(DataRole.FitToLabels, tasks.getValue(fitToLabelsFile), fitToLabelsFile) else ArrayList()
            val vals = TrainingDataValues(inputVects, inputLabels, fitToVects, fitToLabels)
            vals.format = this
            vals.fitTo!!.format = this
            vals.valueMetaInfo = inputFile
            vals.labelMetaInfo = labelFile
            vals.fitTo.valueMetaInfo = fitToFile
            vals.fitTo.labelMetaInfo = fitToLabelsFile
            vals
        } else {
            val vals = TrainingDataValues(inputVects, inputLabels)
            vals.format = this
            vals.valueMetaInfo = inputFile
            vals.labelMetaInfo = labelFile
            vals
        }
    }

    override fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues {
        val inputFile = dataSuite.streams.getValue(DataRole.Input)
        val labelFile = dataSuite.streams[DataRole.InputLabels]
        val inputVects = readVects(DataRole.Input, tasks.getValue(inputFile), inputFile, this.inputVectReader)
        val inputLabels = if (labelFile != null && tasks.containsKey(labelFile)) readLabels(DataRole.InputLabels, tasks.getValue(labelFile), labelFile) else ArrayList()
        val vals = DataValues(inputVects, inputLabels)
        vals.format = this
        vals.valueMetaInfo = inputFile
        vals.labelMetaInfo = labelFile
        return vals
    }

    private fun readVects(purpose: DataRole, file: File, metaInfo: Stream, vectReader: VectReader): ArrayList<Vect> {
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

    private fun readLabels(purpose: DataRole, file: File, metaInfo: Stream): ArrayList<Long> {
        val labels = ArrayList<Long>()
        val labelReader = this.inputLabelReader
        if (labelReader != null) {
            val dataSelection = DataSelection()
            dataSelection[purpose] = DataSelection.Item(metaInfo, file.inputStream())
            labelReader.open(dataSelection)
            val bufLabels = Array(min(10, countElementsIn(labelReader))) { 0L }
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