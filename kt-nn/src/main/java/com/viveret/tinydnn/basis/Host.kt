package com.viveret.tinydnn.basis

import java.util.*

class Host(val siteName: String, val title: String, override val id: UUID) : Indexable {
    override fun isAvailable(source: DataSource): Boolean =
            HostedStreamPackage.fromHost(this.siteName).any { x -> x.isAvailable(source) }

    override fun delete(source: DataSource) {
        for (pkg in HostedStreamPackage.fromHost(this.siteName)) {
            pkg.delete(source)
        }
    }

    constructor(siteName: String, title: String, id: String) :
            this(siteName, title, UUID.fromString(id)!!)

    companion object {
        val hosts = arrayOf(Host("raw.githubusercontent.com/artem-oppermann/Sentiment-Analysis-of-Netflix-Reviews",
                "Online Movie Reviews", "bd615b76-503d-11e9-8647-d663bd873d93"),
                Host("github.com",
                        "GitHub", "a304354a-4da5-11e9-8646-d663bd873d92"),
                Host("yann.lecun.com", "MNIST handwritten digit database, Yann LeCun, Corinna Cortes and Chris Burges",
                        UUID.fromString("08feb736-7aa5-11e9-8f9e-2a86e4085a59")))

        val hostMap = hosts.map { x -> x.id to x }.toMap()

        fun fromId(id: UUID): Host = hostMap[id] ?: error("Host $id not found")
    }
}