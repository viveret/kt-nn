package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class SaveProjectAction: ProjectAction {
    override val name: String = "@save"
    override fun doAction(project: NeuralNetProject): OnSelectedResult { return OnSelectedResult(true) }
}