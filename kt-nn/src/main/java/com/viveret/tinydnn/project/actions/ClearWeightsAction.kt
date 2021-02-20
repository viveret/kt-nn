package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class ClearWeightsAction: ProjectAction {
    override val name: String = "Clear Weights"

    override fun doAction(project: NeuralNetProject): OnSelectedResult = OnSelectedResult(true)
}