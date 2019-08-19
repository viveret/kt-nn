package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class ProjectRenameEvent(val projectName: String): ProjectAction {
    override val name: String = "Project Renamed"

    override fun doAction(project: NeuralNetProject): OnSelectedResult {
        project.name = projectName
        return OnSelectedResult(true)
    }
}