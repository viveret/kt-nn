package com.viveret.tinydnn.data.train

import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.tinydnn.Optimizer

class BasicTrainingConfig(override val batchSize: Long, override val epochs: Int, override val optimizer: Optimizer, override val dataMethod: DataMethod, override val fitToOutput: Boolean) : TrainingMethod {
}