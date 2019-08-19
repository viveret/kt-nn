package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class ChangeWeightsAction: ProjectAction {
    override val name: String = "Change Weights"

    override fun doAction(project: NeuralNetProject): OnSelectedResult = OnSelectedResult(true)
}