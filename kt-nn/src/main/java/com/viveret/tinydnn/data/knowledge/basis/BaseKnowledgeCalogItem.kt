package com.viveret.tinydnn.data.knowledge.basis

import com.viveret.tinydnn.basis.FileStream
import com.viveret.tinydnn.basis.Host
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.data.knowledge.KnowledgeCatalogItem
import java.net.URL
import java.util.*

abstract class BaseKnowledgeCalogItem(url: URL, id: UUID, host: Host?) : KnowledgeCatalogItem {
    override val stream: Stream = FileStream(url, id, host = host)
}