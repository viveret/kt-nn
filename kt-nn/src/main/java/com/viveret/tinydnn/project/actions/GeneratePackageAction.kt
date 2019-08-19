package com.viveret.tinydnn.project.actions

import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.util.async.OnSelectedResult

class GeneratePackageAction(val size: Int, val includeFitTo: Boolean, val path: String): ProjectAction {
    override val name: String = "@Generate Package"
    override fun doAction(project: NeuralNetProject): OnSelectedResult { return OnSelectedResult(true) }
}