package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject

interface ProjectAction {
    val name: String
    fun doAction(project: NeuralNetProject): Boolean
}