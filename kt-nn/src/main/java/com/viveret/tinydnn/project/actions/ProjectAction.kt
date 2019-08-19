package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

interface ProjectAction {
    val name: String
    fun doAction(project: NeuralNetProject): OnSelectedResult
}