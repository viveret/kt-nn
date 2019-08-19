package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.layer.LayerBase
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class AddLayerAction(newLayer: LayerBase) : ProjectAction {
    override val name: String = "@save"
    override fun doAction(project: NeuralNetProject): OnSelectedResult { return OnSelectedResult(true) }
}