package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

class ProjectRenameEvent(val projectName: String): ProjectAction {
    override val name: String = "Project Renamed"

    override fun doAction(project: NeuralNetProject): Boolean {
        project.name = projectName
        return true
    }
}