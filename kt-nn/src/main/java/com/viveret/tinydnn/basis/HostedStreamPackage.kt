package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.repo.Repo
import com.viveret.tinydnn.project.NeuralNetProject
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class HostedStreamPackage(context: Context, override val id: UUID, override val host: Host,
                          override val title: String, override val dataName: String,
                          override val formatId: UUID, override val encryptedProtocol: Boolean,
                          val inputFileUrl: URL, val inputFileId: UUID,
                          val labelFileUrl: URL?, val labelFileId: UUID?,
                          val fitToFileUrl: URL?, val fitToFileId: UUID?,
                          val fitToLabelsFileUrl: URL?, val fitToLabelsFileId: UUID?) : AbstractStreamPackage(context) {

    val inputFile: Stream = FileStream(context, this.inputFileUrl, this.inputFileId, DataRole.Input, this.host)
    val labelFile: Stream? = if (this.labelFileId != null) FileStream(context, this.labelFileUrl!!, this.labelFileId, DataRole.InputLabels, this.host) else null
    val fitToFile: Stream? = if (this.fitToFileId != null) FileStream(context, this.fitToFileUrl!!, this.fitToFileId, DataRole.FitTo, this.host) else null
    val fitToLabelsFile: Stream? = if (this.fitToLabelsFileId != null) FileStream(context, this.fitToLabelsFileUrl!!, this.fitToLabelsFileId, DataRole.FitToLabels, this.host) else null

    override val streams: Map<DataRole, Stream> = listOfNotNull(inputFile, labelFile, fitToFile, fitToLabelsFile)
        .map { it.role to it }.toMap()

    constructor(context: Context, id: UUID, host: Host,
                title: String, dataName: String,
                formatId: UUID, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: UUID,
                labelFileUrl: String?, labelFileId: UUID?,
                fitToFileUrl: String?, fitToFileId: UUID?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: UUID?):
            this(context, id, host, title, dataName, formatId, encryptedProtocol,
                    URL(if (encryptedProtocol) "https" else "http", host.siteName, inputFileUrl), inputFileId,
                    if (labelFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, labelFileUrl) else null, labelFileId,
                    if (fitToFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, fitToFileUrl) else null, fitToFileId,
                    if (fitToLabelsFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, fitToLabelsFileUrl) else null, fitToLabelsFileId)

    constructor(context: Context, id: UUID, hostId: UUID,
                title: String, dataName: String,
                formatId: UUID, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: UUID,
                labelFileUrl: String?, labelFileId: UUID?,
                fitToFileUrl: String?, fitToFileId: UUID?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: UUID?):
            this(context, id, Host.fromId(hostId), title, dataName, formatId, encryptedProtocol,
                    inputFileUrl, inputFileId, labelFileUrl, labelFileId, fitToFileUrl, fitToFileId, fitToLabelsFileUrl, fitToLabelsFileId)

    constructor(context: Context, id: String, hostId: String,
                title: String, dataName: String,
                formatId: String, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: String,
                labelFileUrl: String?, labelFileId: String?,
                fitToFileUrl: String?, fitToFileId: String?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: String?):
            this(context, UUID.fromString(id)!!, UUID.fromString(hostId)!!,
                title, dataName, UUID.fromString(formatId), encryptedProtocol,
                inputFileUrl, UUID.fromString(inputFileId)!!,
                labelFileUrl, if (labelFileId != null) UUID.fromString(labelFileId) else null,
                fitToFileUrl, if (fitToFileId != null) UUID.fromString(fitToFileId) else null,
                fitToLabelsFileUrl, if (fitToLabelsFileId != null) UUID.fromString(fitToLabelsFileId) else null)

    override fun open(source: DataSource, fitTo: Boolean, project: NeuralNetProject?): InputDataValueStream {
        val files = InputSelection()

        try {
            files[DataRole.Input] = InputSelection.FileItem(this.inputFile, source)
        } catch (e: Exception) {
            throw Exception("Could not create inputs task")
        }

        if (this.labelFile != null) {
            try {
                files[DataRole.InputLabels] = InputSelection.FileItem(this.labelFile, DataSource.LocalFile)
            } catch (e: Exception) {
                throw Exception("Could not create label task")
            }
        }

        if (this.fitToFile != null) {
            try {
                files[DataRole.FitTo] = InputSelection.FileItem(this.fitToFile, DataSource.LocalFile)
            } catch (e: Exception) {
                throw Exception("Could not create fitTo task")
            }
        }

        if (this.fitToLabelsFile != null) {
            try {
                files[DataRole.FitToLabels] = InputSelection.FileItem(this.fitToLabelsFile, DataSource.LocalFile)
            } catch (e: Exception) {
                throw Exception("Could not create fit to labels task")
            }
        }

        return InputDataValueStream(context, project!!, files)
    }

    companion object {
        private val repos = ArrayList<Repo>()

        fun getPackageList() = repos.flatMap { it.packages }

        private fun dataInfos() = getPackageList().flatMap { it.streams.values }.map { x -> x.id to x }.toMap()

        fun fromId(id: UUID): StreamPackage =
                getPackageList().firstOrNull { x -> x.id == id } ?: error("Hosted stream package with id $id not found")

        fun fromHost(domain: String): Collection<StreamPackage> =
                getPackageList().filter { x -> x.host.siteName.startsWith(domain) }

        fun filter(predicate: (StreamPackage) -> Boolean) : Collection<StreamPackage> =
                getPackageList().filter(predicate)

        fun addRepo(repo: Repo) {
            repos.add(repo)
        }

        fun removeRepo(repo: Repo) {
            repos.remove(repo)
        }

        init {
            FileStream.addResolver { dataInfos()[it] }
        }
    }
}