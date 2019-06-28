package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

class SaveProjectAction: ProjectAction {
    override val name: String = "@save"
    override fun doAction(project: NeuralNetProject): Boolean { return true }
}