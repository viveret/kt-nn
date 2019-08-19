package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class GiveUpAction: ProjectAction {
    override val name: String = "Give Up"

    override fun doAction(project: NeuralNetProject): OnSelectedResult = OnSelectedResult(true)
}