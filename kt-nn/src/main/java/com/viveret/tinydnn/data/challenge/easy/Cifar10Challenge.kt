package com.viveret.tinydnn.data.challenge.easy

import com.viveret.tinydnn.R
import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.data.challenge.ChallengeMetaInfo
import com.viveret.tinydnn.data.challenge.DifficultyRating
import com.viveret.tinydnn.network.SequentialNetworkModelWithWeights
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.project.TemplateNetwork
import java.util.*

class Cifar10Challenge: ChallengeMetaInfo {
    override val id: UUID = UUID.fromString("d53fcf60-6f10-11e9-a923-1681be663d3e")
    override val name: Int = R.string.cifar10_classification
    override val description: Int = R.string.challenge_cifar10_desc
    override val difficulty: DifficultyRating = DifficultyRating.Hard
    override val minAccuracyToPass: Float = 0.94f

    override val dataSuiteId: UUID = UUID.fromString("17620240-5879-11e9-8647-d663bd873d93")
    override val minLayersCanUse: Int? = 0
    override val maxLayersCanUse: Int? = 15
    override val inputSize: Int = 32 * 32 * 3
    override val outputSize: Int = 10
    override val dataMethods: Array<DataMethod> = arrayOf(DataMethod.BinaryFile, DataMethod.Camera, DataMethod.Canvas)

    override fun judgeSolution(nn: NeuralNetProject): Boolean {
        return false
    }

    override fun giveUp(nn: SequentialNetworkModelWithWeights) = TemplateNetwork.Cifar10Classification().apply(nn)
}