package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import java.util.*

class Host(val siteName: String, val titleStringId: Int, override val id: UUID) : Indexable {
    override fun isAvailable(source: DataSource, context: Context): Boolean =
            HostedStreamPackage.fromHost(this.siteName).any { x -> x.isAvailable(source, context) }

    override fun delete(source: DataSource, context: Context) {
        for (pkg in HostedStreamPackage.fromHost(this.siteName)) {
            pkg.delete(source, context)
        }
    }

    constructor(siteName: String, titleStringId: Int, id: String) :
            this(siteName, titleStringId, UUID.fromString(id)!!)

    companion object {
        val hosts = arrayOf(Host("raw.githubusercontent.com/artem-oppermann/Sentiment-Analysis-of-Netflix-Reviews",
                R2.string.online_movie_reviews, "bd615b76-503d-11e9-8647-d663bd873d93"),
                Host("github.com",
                        R2.string.pocket_neural_network, "a304354a-4da5-11e9-8646-d663bd873d93"),
                Host("github.com",
                        R2.string.pocket_neural_network, "a304354a-4da5-11e9-8646-d663bd873d92"),
                Host("yann.lecun.com", R2.string.site_title_com_lecun_yann,
                        UUID.fromString("08feb736-7aa5-11e9-8f9e-2a86e4085a59")))

        val hostMap = hosts.map { x -> x.id to x }.toMap()

        fun fromId(id: UUID): Host = hostMap[id] ?: error("Host $id not found")
    }
}