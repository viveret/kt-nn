package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.tinydnn.layer.LayerBase

class RemoveLayerAction(layerPosition: Long, layer: LayerBase) : ProjectAction {
    override val nameResId: Int = R2.string.save
    override fun doAction(project: NeuralNetProject): Boolean { return true }
}