package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.project.ProjectProvider
import java.io.File
import java.net.URL
import java.util.*

class HostedStreamPackage(override val id: UUID, override val host: Host,
                          override val titleStringId: Int, override val dataNameStringId: Int,
                          override val formatId: UUID, override val encryptedProtocol: Boolean,
                          val inputFileUrl: URL, val inputFileId: UUID,
                          val labelFileUrl: URL?, val labelFileId: UUID?,
                          val fitToFileUrl: URL?, val fitToFileId: UUID?,
                          val fitToLabelsFileUrl: URL?, val fitToLabelsFileId: UUID?) : StreamPackage {

    constructor(id: UUID, host: Host,
                titleStringId: Int, dataNameStringId: Int,
                formatId: UUID, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: UUID,
                labelFileUrl: String?, labelFileId: UUID?,
                fitToFileUrl: String?, fitToFileId: UUID?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: UUID?):
            this(id, host, titleStringId, dataNameStringId, formatId, encryptedProtocol,
                    URL(if (encryptedProtocol) "https" else "http", host.siteName, inputFileUrl), inputFileId,
                    if (labelFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, labelFileUrl) else null, labelFileId,
                    if (fitToFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, fitToFileUrl) else null, fitToFileId,
                    if (fitToLabelsFileUrl != null) URL(if (encryptedProtocol) "https" else "http", host.siteName, fitToLabelsFileUrl) else null, fitToLabelsFileId)

    constructor(id: UUID, hostId: UUID,
                titleStringId: Int, dataNameStringId: Int,
                formatId: UUID, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: UUID,
                labelFileUrl: String?, labelFileId: UUID?,
                fitToFileUrl: String?, fitToFileId: UUID?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: UUID?):
            this(id, Host.fromId(hostId), titleStringId, dataNameStringId, formatId, encryptedProtocol,
                    inputFileUrl, inputFileId, labelFileUrl, labelFileId, fitToFileUrl, fitToFileId, fitToLabelsFileUrl, fitToLabelsFileId)

    override fun loadDataValues(source: DataSource, context: Context, fitTo: Boolean, project: NeuralNetProject?): DataValues {
        val fmt = (context as ProjectProvider).getDataFormat(this.formatId)
        val files = HashMap<Stream, File>()

        try {
            files[this.inputFile] = File(context.cacheDir, this.id.toString() + "/" + this.inputFile.id.toString())
        } catch (e: Exception) {
            throw Exception("Could not create inputs task")
        }

        if (this.labelFile != null) {
            try {
                files[this.labelFile!!] = File(context.cacheDir, this.id.toString() + "/" + this.labelFile!!.id.toString())
            } catch (e: Exception) {
                throw Exception("Could not create label task")
            }
        }

        if (this.fitToFile != null) {
            try {
                files[this.fitToFile!!] = File(context.cacheDir, this.id.toString() + "/" + this.fitToFile!!.id.toString())
            } catch (e: Exception) {
                throw Exception("Could not create fitTo task")
            }
        }

        if (this.fitToLabelsFile != null) {
            try {
                files[this.fitToLabelsFile!!] = File(context.cacheDir, this.id.toString() + "/" + this.fitToLabelsFile!!.id.toString())
            } catch (e: Exception) {
                throw Exception("Could not create fit to labels task")
            }
        }

        val data = if (fitTo) {
            fmt.getTrainingData(files, this, project)!!
        } else {
            fmt.getDataValues(files, this, project)
        }

        data.valueMetaInfo = this.inputFile
        data.labelMetaInfo = this.labelFile
        if (data is TrainingDataValues) {
            data.fitTo!!.valueMetaInfo = this.fitToFile
            data.fitTo.labelMetaInfo = this.fitToLabelsFile
        }

        return data
    }

    constructor(id: String, hostId: String,
                titleStringId: Int, dataNameStringId: Int,
                formatId: String, encryptedProtocol: Boolean,
                inputFileUrl: String, inputFileId: String,
                labelFileUrl: String?, labelFileId: String?,
                fitToFileUrl: String?, fitToFileId: String?,
                fitToLabelsFileUrl: String?, fitToLabelsFileId: String?):
            this(UUID.fromString(id)!!, UUID.fromString(hostId)!!,
                    titleStringId, dataNameStringId, UUID.fromString(formatId), encryptedProtocol,
                    inputFileUrl, UUID.fromString(inputFileId)!!,
                    labelFileUrl, if (labelFileId != null) UUID.fromString(labelFileId) else null,
                    fitToFileUrl, if (fitToFileId != null) UUID.fromString(fitToFileId) else null,
                    fitToLabelsFileUrl, if (fitToLabelsFileId != null) UUID.fromString(fitToLabelsFileId) else null)

    override val inputFile: Stream
        get() = FileStream(this.inputFileUrl, this.inputFileId, this.host)

    override val labelFile: Stream?
        get() = if (this.labelFileId != null) FileStream(this.labelFileUrl!!, this.labelFileId, this.host) else null

    override val fitToFile: Stream?
        get() = if (this.fitToFileId != null) FileStream(this.fitToFileUrl!!, this.fitToFileId, this.host) else null

    override val fitToLabelsFile: Stream?
        get() = if (this.fitToLabelsFileId != null) FileStream(this.fitToLabelsFileUrl!!, this.fitToLabelsFileId, this.host) else null

    override fun isAvailable(source: DataSource, context: Context): Boolean {
        return if (source == DataSource.LocalFile) {
            val suiteDir = File(context.cacheDir, this.id.toString())
            if (suiteDir.isDirectory) {
                listOfNotNull(this.inputFile, this.labelFile, this.fitToFile, this.fitToLabelsFile)
                        .all { f -> f.isAvailable(source, context) }
            } else {
                false
            }
        } else {
            listOfNotNull(this.inputFile, this.labelFile, this.fitToFile, this.fitToLabelsFile)
                    .all { f -> f.isAvailable(source, context) }
        }
    }

    override fun delete(source: DataSource, context: Context) {
        if (source == DataSource.LocalFile) {
            val suiteDir = File(context.cacheDir, this.id.toString())
            if (suiteDir.isDirectory) {
                for (f in listOfNotNull(this.inputFile, this.labelFile, this.fitToFile, this.fitToLabelsFile)) {
                    f.delete(source, context)
                }
                suiteDir.deleteRecursively()
            }
        } else {
            throw IllegalArgumentException("Cannot delete $source")
        }

        if (this.isAvailable(source, context)) {
            throw Exception("Deleted but still exists, problems can occur")
        }
    }

    override fun equals(other: Any?): Boolean = other is StreamPackage && this.id == other.id

    override fun hashCode(): Int = this.id.hashCode()

    companion object {
        fun getPackageList() = arrayOf(// https://github.com/viveret/tinydnn_Data/raw/master/Cifar10/data_batch_2.cifar10
                HostedStreamPackage("45b98b47-69a0-4405-b3ca-6f91887a9d61", "08feb736-7aa5-11e9-8f9e-2a86e4085a59",
                        R2.string.mnist, R2.string.mnist_data_name, "44f43102-3054-4602-a10c-7f3c0d8cd3f7", false,
                        "train-images-idx3-ubyte.gz", "629ccf42-4140-11e9-b210-d663bd873d93",
                        null, null,
                        "train-labels-idx1-ubyte.gz", "629cd79e-4140-11e9-b210-d663bd873d93",
                        null, null),
                HostedStreamPackage("bd615b76-503d-11e9-8647-d663bd873d93", "bd615b76-503d-11e9-8647-d663bd873d93",
                        R2.string.online_movie_reviews, R2.string.online_moview_reviews_data_name, "bd6162ba-503d-11e9-8647-d663bd873d93", true,
                        "master/data/preprocessed/reviews_labeled.txt", "629ccf42-4140-11e9-b210-d663bd873d93",
                        null, null,
                        "master/data/preprocessed/reviews_labeled.txt", "629ccf42-4140-11e9-b210-d663bd873d93",
                        null, null),
                HostedStreamPackage("17620240-5879-11e9-8647-d663bd873d93", "a304354a-4da5-11e9-8646-d663bd873d93",
                        R2.string.cifar10_classification, R2.string.cifar10_classification, "176200b0-5879-11e9-8647-d663bd873d93", true,
                        "/viveret/tinydnn_Data/raw/master/Cifar10/data_batch_2.cifar10?raw=true", "d53fc74a-6f10-11e9-a923-1681be663d3e",
                        null, null,
                        "/viveret/tinydnn_Data/raw/master/Cifar10/data_batch_2.cifar10?raw=true", "d53fc74a-6f10-11e9-a923-1681be663d3e",
                        null, null))


        val cmuIgnore = HostedStreamPackage("a304354a-4da5-11e9-8646-d663bd873d93","a304354a-4da5-11e9-8646-d663bd873d93",
                R2.string.cmu_movie_summary_corpus, R2.string.cmu_movie_summary_corpus_data_name, "a30433b0-4da5-11e9-8646-d663bd873d93", true,
        "blob/master/MovieSummaries/movies11.tsv?raw=true", "a30436f8-4da5-11e9-8646-d663bd873d93",
        null, null,
        "blob/master/MovieSummaries/movies11.tsv?raw=true", "a30436f8-4da5-11e9-8646-d663bd873d93",
        null, null)

        private fun dataInfos() = getPackageList().map { x -> listOfNotNull(x.inputFile, x.labelFile, x.fitToFile, x.fitToLabelsFile) }.flatten().map { x -> x.id to x }.toMap()

        fun fromId(id: UUID): HostedStreamPackage =
                getPackageList().firstOrNull { x -> x.id == id } ?: error("Hosted stream package with id $id not found")

        fun fromHost(domain: String): Collection<HostedStreamPackage> =
                getPackageList().filter { x -> x.host.siteName.startsWith(domain) }

        fun filter(predicate: (HostedStreamPackage) -> Boolean) : Collection<HostedStreamPackage> =
                getPackageList().filter(predicate)

        init {
            FileStream.addResolver { dataInfos()[it] }
        }
    }
}