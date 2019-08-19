package com.viveret.tinydnn.data.knowledge

import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.model.NetworkModelFilter

interface KnowledgeCatalogItem: NetworkModelFilter {
    val stream: Stream
    val title: String
    val description: String
}