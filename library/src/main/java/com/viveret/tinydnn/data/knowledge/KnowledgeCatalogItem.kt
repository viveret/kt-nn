package com.viveret.tinydnn.data.knowledge

import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.tinydnn.model.INetworkModelWithWeights

interface KnowledgeCatalogItem {
    val stream: Stream
    val title: Int
    val description: Int
    fun supportsNetwork(project: INetworkModelWithWeights): Boolean
}