package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.layer.LayerBase
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class RemoveLayerAction(layerPosition: Long, layer: LayerBase) : ProjectAction {
    override val name: String = "Remove Layer"
    override fun doAction(project: NeuralNetProject): OnSelectedResult { return OnSelectedResult(true) }
}