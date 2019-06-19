package com.viveret.tinydnn.data.knowledge.basis

import com.viveret.tinydnn.basis.Host
import java.net.URL
import java.util.*

abstract class OnlineKnowledgeCatalogItem(url: URL, id: UUID, hostId: UUID): BaseKnowledgeCalogItem(url, id, Host.fromId(hostId)) {

}