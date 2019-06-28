package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.layer.LayerBase
import com.viveret.tinydnn.project.NeuralNetProject

class RemoveLayerAction(layerPosition: Long, layer: LayerBase) : ProjectAction {
    override val name: String = "Remove Layer"
    override fun doAction(project: NeuralNetProject): Boolean { return true }
}