package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.enums.TrainResult
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class TrainingResults(val result: TrainResult, val trainingData: DataValues) : ProjectAction {
    override val name: String = "@train_results"

    override fun doAction(project: NeuralNetProject): OnSelectedResult {
        //project.(ConsoleMessage.MessageLevel.LOG, "Train result: $result")
        return OnSelectedResult(true)
    }
}