package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

class ProjectInitializedEvent: ProjectAction {
    override val name: String = "Project Initialized"

    override fun doAction(project: NeuralNetProject): Boolean {
        return true
    }
}