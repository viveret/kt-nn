package com.viveret.tinydnn.data

import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.train.TrainingDataReader
import java.util.*

open class DataValues {
    val inputValues: ArrayList<Vect>
    val inputLabels: ArrayList<Long>
    var format: TrainingDataReader? = null
    var valueMetaInfo: Stream? = null
    var labelMetaInfo: Stream? = null

    constructor() {
        this.inputValues = ArrayList()
        this.inputLabels = ArrayList()
    }

    constructor(inputValues: ArrayList<Vect>, inputLabels: ArrayList<Long>) {
        this.inputValues = inputValues
        this.inputLabels = inputLabels
    }
}