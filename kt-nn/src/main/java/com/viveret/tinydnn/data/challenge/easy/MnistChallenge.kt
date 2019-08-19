package com.viveret.tinydnn.data.challenge.easy

import com.viveret.tinydnn.R
import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.data.challenge.ChallengeMetaInfo
import com.viveret.tinydnn.data.challenge.DifficultyRating
import com.viveret.tinydnn.network.SequentialNetworkModelWithWeights
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.project.TemplateNetwork.MnistRnn2
import java.util.*

class MnistChallenge: ChallengeMetaInfo {
    override val id: UUID = UUID.fromString("cc2fefc4-69d7-11e9-a923-1681be663d3e")
    override val name: Int = R.string.challenge_mnist
    override val description: Int = R.string.challenge_mnist_desc
    override val difficulty: DifficultyRating = DifficultyRating.Hard
    override val minAccuracyToPass: Float = 0.90f

    override val dataSuiteId: UUID = UUID.fromString("45b98b47-69a0-4405-b3ca-6f91887a9d61")
    override val minLayersCanUse: Int? = 0
    override val maxLayersCanUse: Int? = 10
    override val inputSize: Int = 1024
    override val outputSize: Int = 10
    override val dataMethods: Array<DataMethod> = arrayOf(DataMethod.BinaryFile, DataMethod.Camera, DataMethod.Canvas)

    override fun judgeSolution(nn: NeuralNetProject): Boolean {
        return false
    }

    override fun giveUp(nn: SequentialNetworkModelWithWeights) = MnistRnn2().apply(nn)
}