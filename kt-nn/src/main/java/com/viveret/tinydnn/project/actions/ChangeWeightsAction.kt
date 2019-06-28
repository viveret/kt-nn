package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

class ChangeWeightsAction: ProjectAction {
    override val name: String = "Change Weights"

    override fun doAction(project: NeuralNetProject): Boolean {
        return true
    }
}