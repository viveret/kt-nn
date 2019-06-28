package com.viveret.tinydnn.data.challenge

import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.network.SequentialNetworkModelWithWeights
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface ChallengeMetaInfo {
    val id: UUID
    val name: Int
    val description: Int
    val dataSuiteId: UUID
    val dataMethods: Array<DataMethod>
    val difficulty: DifficultyRating
    val minAccuracyToPass: Float
    val minLayersCanUse: Int?
    val maxLayersCanUse: Int?
    val inputSize: Int
    val outputSize: Int
    fun judgeSolution(nn: NeuralNetProject): Boolean
    fun giveUp(nn: SequentialNetworkModelWithWeights)
}