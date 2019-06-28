package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject

class JudgeResults(val tallyGood: Int, val trainingData: TrainingDataValues) : ProjectAction {
    override val name: String = "@judge_results"

    override fun doAction(project: NeuralNetProject): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}