package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class JudgeResults(val tallyGood: Int, val trainingData: DataValues) : ProjectAction {
    override val name: String = "@judge_results"

    override fun doAction(project: NeuralNetProject): OnSelectedResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}