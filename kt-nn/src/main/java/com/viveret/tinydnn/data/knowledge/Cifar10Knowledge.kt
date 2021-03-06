package com.viveret.tinydnn.data.knowledge

import android.content.Context
import com.viveret.tinydnn.data.knowledge.basis.OnlineKnowledgeCatalogItem
import com.viveret.tinydnn.model.CommonNetworkFilters
import com.viveret.tinydnn.model.INetworkModelWithWeights
import java.net.URL
import java.util.*

class Cifar10Knowledge(context: Context): OnlineKnowledgeCatalogItem(context, URL("https://github.com/tiny-dnn/tiny-dnn/raw/master/examples/cifar10/cifar-weights"),
        UUID.fromString("97fc30da-7c2b-11e9-8f9e-2a86e4085a59"), UUID.fromString("a304354a-4da5-11e9-8646-d663bd873d92")) {
    override val title: String = "Cifar10 Knowledge" // Cifar10 Classification
    override val description: String = "32x32 colour images in 10 classes, with 6000 images per class"

    override fun isMatch(project: INetworkModelWithWeights): Boolean = CommonNetworkFilters.Cifar10.isMatch(project)
}