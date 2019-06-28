package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.repo.Repo
import com.viveret.tinydnn.data.train.TrainingDataReader
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.project.ProjectProvider
import java.io.File
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HostedStreamPackage(override val id: UUID, override val host: Host,
                          override val title: String, override val dataName: String,
                          override val formatId: UUID, override val encryptedProtocol: Boolean,
                          val inputFileUrl: URL, val inputFileId: UUID,
                          val labelFileUrl: URL?, val labelFileId: UUID?,
                          val fitToFileUrl: URL?, val fitToFileId: UUID?,
                          val fitToLabelsFileUrl: URL?, val fitToLabelsFileId: UUID?) : AbstractStreamPackage() {

    val inputFile: Stream = FileStream(this.inputFileUrl, this.inputFileId, DataRole.Input, this.host)
    val labelFile: Stream? = if (this.labelFileId != null) FileStream(this.labelFileUrl!!, this.labelFileId, DataRole.InputLabels, this.host) else null
    val fitToFile: Stream? = if (this.fitToFileId != null) FileStream(this.fitToFileUrl!!, this.fitToFileId, DataRole.FitTo, this.host) else null
    val fitToLabelsFile: Stream? = if (this.fitToLabelsFileId != null) FileStream(this.fitToLabelsFileUrl!!, this.fitToLabelsFileId, DataRole.FitToLabels, this.host) else null

    override val streams: Map<DataRole, Stream> = listOfNotNull(inputFile, labelFile, fitToFile, fitToLabelsFile)
        .map { it.role to it }.toMap()

    constructor(id: UUID, host: Host,
                title: String, dataName: String,
                formatId: UUID, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: UUID,
                labelFileUrl: String?, labelFileId: UUID?,
                fitToFileUrl: String?, fitToFileId: UUID?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: UUID?):
            this(id, host, title, dataName, formatId, encryptedProtocol,
                    URL(if (encryptedProtocol) "https" else "http", host.siteName, inputFileUrl), inputFileId,
                    if (labelFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, labelFileUrl) else null, labelFileId,
                    if (fitToFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, fitToFileUrl) else null, fitToFileId,
                    if (fitToLabelsFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, fitToLabelsFileUrl) else null, fitToLabelsFileId)

    constructor(id: UUID, hostId: UUID,
                title: String, dataName: String,
                formatId: UUID, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: UUID,
                labelFileUrl: String?, labelFileId: UUID?,
                fitToFileUrl: String?, fitToFileId: UUID?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: UUID?):
            this(id, Host.fromId(hostId), title, dataName, formatId, encryptedProtocol,
                    inputFileUrl, inputFileId, labelFileUrl, labelFileId, fitToFileUrl, fitToFileId, fitToLabelsFileUrl, fitToLabelsFileId)

    constructor(id: String, hostId: String,
                title: String, dataName: String,
                formatId: String, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: String,
                labelFileUrl: String?, labelFileId: String?,
                fitToFileUrl: String?, fitToFileId: String?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: String?):
            this(UUID.fromString(id)!!, UUID.fromString(hostId)!!,
                title, dataName, UUID.fromString(formatId), encryptedProtocol,
                inputFileUrl, UUID.fromString(inputFileId)!!,
                labelFileUrl, if (labelFileId != null) UUID.fromString(labelFileId) else null,
                fitToFileUrl, if (fitToFileId != null) UUID.fromString(fitToFileId) else null,
                fitToLabelsFileUrl, if (fitToLabelsFileId != null) UUID.fromString(fitToLabelsFileId) else null)

    override fun loadDataValues(source: DataSource, context: Context, fitTo: Boolean, project: NeuralNetProject?): DataValues {
        val fmt = (context as ProjectProvider).getDataFormat(this.formatId)
        val files = HashMap<Stream, File>()

        try {
            files[this.inputFile] = File(this.inputFile.sourcePath(DataSource.LocalFile, context))
        } catch (e: Exception) {
            throw Exception("Could not create inputs task")
        }

        if (this.labelFile != null) {
            try {
                files[this.labelFile] = File(this.labelFile.sourcePath(DataSource.LocalFile, context))
            } catch (e: Exception) {
                throw Exception("Could not create label task")
            }
        }

        if (this.fitToFile != null) {
            try {
                files[this.fitToFile] = File(this.fitToFile.sourcePath(DataSource.LocalFile, context))
            } catch (e: Exception) {
                throw Exception("Could not create fitTo task")
            }
        }

        if (this.fitToLabelsFile != null) {
            try {
                files[this.fitToLabelsFile] = File(this.fitToLabelsFile.sourcePath(DataSource.LocalFile, context))
            } catch (e: Exception) {
                throw Exception("Could not create fit to labels task")
            }
        }

        return this.loadStreams(fmt, files, fitTo, project)
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