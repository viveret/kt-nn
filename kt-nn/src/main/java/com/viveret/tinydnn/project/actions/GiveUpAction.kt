package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

class GiveUpAction: ProjectAction {
    override val name: String = "Give Up"

    override fun doAction(project: NeuralNetProject): Boolean {
        return true
    }
}