package com.viveret.tinydnn.data.train

import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.StreamPackage
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.File
import java.util.*

interface TrainingDataReader {
    fun getTrainingData(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues?
    fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues

    val formatId: UUID
    val inputVectReader: VectReader
    val inputLabelReader: LabelReader?
    val fitToVectReader: VectReader?
    val fitToLabelReader: LabelReader?
}