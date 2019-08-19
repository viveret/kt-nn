package com.viveret.tinydnn.data.knowledge.basis

import android.content.Context
import com.viveret.tinydnn.basis.FileStream
import com.viveret.tinydnn.basis.Host
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.data.knowledge.KnowledgeCatalogItem
import java.net.URL
import java.util.*

abstract class BaseKnowledgeCalogItem(context: Context, url: URL, id: UUID, host: Host?) : KnowledgeCatalogItem {
    override val stream: Stream = FileStream(context, url, id, host = host)
}