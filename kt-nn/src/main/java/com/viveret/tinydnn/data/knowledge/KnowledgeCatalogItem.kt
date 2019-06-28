package com.viveret.tinydnn.data.knowledge

import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.model.INetworkModelWithWeights

interface KnowledgeCatalogItem {
    val stream: Stream
    val title: String
    val description: String
    fun supportsNetwork(project: INetworkModelWithWeights): Boolean
}