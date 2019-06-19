package com.viveret.tinydnn.data.scenario

import android.content.Context
import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface Scenario {
    val id: UUID
    val dataMethod: DataMethod
    val name: String
    val summary: String
    val source: String
    val iconId: Int
    fun compatibleWithNetwork(project: NeuralNetProject): Boolean
    fun init(project: NeuralNetProject, context: Context)
}