package com.viveret.tinydnn.model

interface NetworkModelFilter {
    fun isMatch(project: INetworkModelWithWeights): Boolean
}