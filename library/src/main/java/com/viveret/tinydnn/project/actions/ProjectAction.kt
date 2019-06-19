package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

interface ProjectAction {
    val nameResId: Int
    fun doAction(project: NeuralNetProject): Boolean
}