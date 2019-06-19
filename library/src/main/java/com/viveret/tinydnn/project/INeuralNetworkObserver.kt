package com.viveret.tinydnn.project

import com.viveret.tinydnn.project.actions.ProjectAction

interface INeuralNetworkObserver {
    fun onNeuralNetworkChange(project: NeuralNetProject, event: ProjectAction)
}