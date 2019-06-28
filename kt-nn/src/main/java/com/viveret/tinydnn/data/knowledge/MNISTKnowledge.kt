package com.viveret.tinydnn.data.knowledge

import com.viveret.tinydnn.data.knowledge.basis.OnlineKnowledgeCatalogItem
import com.viveret.tinydnn.model.INetworkModelWithWeights
import java.net.URL
import java.util.*

class MNISTKnowledge: OnlineKnowledgeCatalogItem(URL("https://github.com/viveret/kt-nn-data/raw/master/MNIST/mnist-weights-v1"),
        UUID.fromString("97fc2cb6-7c2b-11e9-8f9e-2a86e4085a59"), UUID.fromString("a304354a-4da5-11e9-8646-d663bd873d92")) {
    override val title: String = "MNIST Knowledge"
    override val description: String = "Knowledge to recognize digits"

    override fun supportsNetwork(project: INetworkModelWithWeights): Boolean = project.in_data_size() == 1024L && project.out_data_size() == 10L
}