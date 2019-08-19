package com.viveret.tinydnn.project

import com.viveret.tinydnn.data.train.DataSliceReader
import java.util.*

interface ProjectProvider {
    val project: NeuralNetProject?
}