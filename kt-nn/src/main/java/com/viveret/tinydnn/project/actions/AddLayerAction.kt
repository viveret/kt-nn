package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.layer.LayerBase
import com.viveret.tinydnn.project.NeuralNetProject

class AddLayerAction(newLayer: LayerBase) : ProjectAction {
    override val name: String = "@save"
    override fun doAction(project: NeuralNetProject): Boolean { return true }
}