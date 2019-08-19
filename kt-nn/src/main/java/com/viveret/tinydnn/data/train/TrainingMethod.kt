package com.viveret.tinydnn.data.train

import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.optimizer.Optimizer

interface TrainingMethod {
    val batchSize: Long
    val epochs: Int
    val optimizer: Optimizer
    val dataMethod: DataMethod
    val fitToOutput: Boolean
    val percentToInclude: Double
}