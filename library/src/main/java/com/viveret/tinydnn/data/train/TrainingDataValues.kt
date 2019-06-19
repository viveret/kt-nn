package com.viveret.tinydnn.data.train

import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.tinydnn.Vect
import java.util.*

class TrainingDataValues : DataValues {
    val fitTo: DataValues?

    constructor() : super() {
        this.fitTo = null
    }

    constructor(inputValues: ArrayList<Vect>, inputLabels: ArrayList<Long>) : super(inputValues, inputLabels) {
        this.fitTo = null
    }

    constructor(inputValues: ArrayList<Vect>, inputLabels: ArrayList<Long>, fitToValues: ArrayList<Vect>, fitToLabels: ArrayList<Long>) : super(inputValues, inputLabels) {
        this.fitTo = DataValues(fitToValues, fitToLabels)
    }
}