package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.project.NeuralNetProject

class ProjectRenameEvent(val name: String): ProjectAction {
    override val nameResId: Int = R2.string.save

    override fun doAction(project: NeuralNetProject): Boolean {
        project.name = name
        return true
    }
}