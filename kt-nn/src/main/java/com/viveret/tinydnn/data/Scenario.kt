package com.viveret.tinydnn.data

import android.content.Context
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface Scenario {
    val id: UUID
    val dataMethod: DataMethod
    val name: String
    val summary: String
    val source: String
    fun compatibleWithNetwork(project: NeuralNetProject): Boolean
    fun init(project: NeuralNetProject, context: Context)
}