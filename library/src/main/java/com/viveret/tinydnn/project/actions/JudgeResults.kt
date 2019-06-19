package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject

class JudgeResults(val tallyGood: Int, val trainingData: TrainingDataValues) : ProjectAction {
    override val nameResId: Int = R2.string.judge_results

    override fun doAction(project: NeuralNetProject): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}