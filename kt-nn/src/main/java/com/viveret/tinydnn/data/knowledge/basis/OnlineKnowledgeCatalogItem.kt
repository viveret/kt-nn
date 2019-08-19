package com.viveret.tinydnn.data.knowledge.basis

import android.content.Context
import com.viveret.tinydnn.basis.Host
import java.net.URL
import java.util.*

abstract class OnlineKnowledgeCatalogItem(context: Context, url: URL, id: UUID, hostId: UUID): BaseKnowledgeCalogItem(context, url, id, Host.fromId(hostId))