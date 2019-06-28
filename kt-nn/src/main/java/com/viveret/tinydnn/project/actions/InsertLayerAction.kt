package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.layer.LayerBase
import com.viveret.tinydnn.project.NeuralNetProject

class InsertLayerAction(insertLayerPosition: Long, newLayer: LayerBase) : ProjectAction {
    override val name: String = "Insert Layer"
    override fun doAction(project: NeuralNetProject): Boolean { return true }
}