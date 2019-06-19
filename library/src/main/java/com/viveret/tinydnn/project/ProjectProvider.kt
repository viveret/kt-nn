package com.viveret.tinydnn.project

import com.viveret.tinydnn.data.train.TrainingDataReader
import java.util.*

interface ProjectProvider {
    fun getDataFormat(formatId: UUID): TrainingDataReader
    val project: NeuralNetProject?
}