package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.enums.TrainResult
import com.viveret.tinydnn.project.NeuralNetProject

class TrainingResults(val result: TrainResult, val trainingData: TrainingDataValues) : ProjectAction {
    override val name: String = "@train_results"

    override fun doAction(project: NeuralNetProject): Boolean {
        //project.(ConsoleMessage.MessageLevel.LOG, "Train result: $result")
        return true
    }
}