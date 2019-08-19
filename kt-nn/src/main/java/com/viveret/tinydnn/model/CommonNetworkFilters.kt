package com.viveret.tinydnn.model

class CommonNetworkFilters {
    companion object {
        val MNIST = object : NetworkModelFilter {
            override fun isMatch(project: INetworkModelWithWeights): Boolean =
                project.in_data_size() == 1024L && project.out_data_size() == 10L
        }

        val Cifar10 = object : NetworkModelFilter {
            override fun isMatch(project: INetworkModelWithWeights): Boolean =
                project.in_data_size() == 1024L * 3 && project.out_data_size() == 10L
        }
    }
}