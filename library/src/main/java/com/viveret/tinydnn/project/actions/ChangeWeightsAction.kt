package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.project.NeuralNetProject

class ChangeWeightsAction: ProjectAction {
    override val nameResId: Int = R2.string.save

    override fun doAction(project: NeuralNetProject): Boolean {
        return true
    }
}