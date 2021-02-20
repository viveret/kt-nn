package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.io.FileInputStream
import com.viveret.tinydnn.data.io.HttpInputStream
import com.viveret.tinydnn.data.io.SmartFileInputStream
import com.viveret.tinydnn.data.io.SmartFileOutputStream
import com.viveret.tinydnn.error.UserException
import org.json.JSONObject
import java.io.DataInputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class FileStream : Stream {
    val context: Context
    override val name: String
    override val extension: String
    override val namespace: String
    override val mime: String
    override val id: UUID
    override val host: Host?
    override val role: DataRole

    constructor(
        context: Context,
        name: String,
        extension: String,
        namespace: String,
        mime: String,
        id: UUID,
        host: Host?,
        role: DataRole
    ) {
        this.context = context
        this.name = name
        this.extension = extension
        this.namespace = namespace
        this.mime = mime
        this.id = id
        this.host = host
        this.role = role
    }

    override fun getInt(attr: DataAttr): Int? {
        return when (attr) {
            DataAttr.ByteCount -> File(this.sourcePath(DataSource.LocalFile)).length().toInt()
            DataAttr.ElementCount -> {
                return DataInputStream(File(this.sourcePath(DataSource.LocalFile)).inputStream()).use {
                    it.readInt()
                    it.readInt()
                }
            }
            else -> null
        }
    }

    override fun getString(attr: DataAttr): String? = when (attr) {
        DataAttr.MIME -> mime
        else -> error("Invalid attr $attr")
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var tempFile: File? = null
    var protocol: String = ""
    var args: String = ""
    var remotePath: String = ""

    constructor(context: Context, namespace: String, name: String, mime: String, extension: String, id: UUID, role: DataRole = DataRole.NA, host: Host? = null, protocol: String = "", args: String = "", remotePath: String? = null):
            this(context, name, extension, namespace, mime, id, host, role) {
        this.protocol = protocol
        this.args = if (args.isNotEmpty()) "?$args" else args
        this.remotePath = remotePath ?: "$namespace/$name$extension$args"
    }

    constructor(context: Context, json: JSONObject, host: Host, suite: StreamPackage): this(context, URL("http${if (suite.encryptedProtocol) "s" else ""}://${host.siteName}/${json.getString("endpoint")}"),
        UUID.fromString(json.getString("id")), DataRole.valueOf(json.getString("role")), host)

    constructor(context: Context, file: File, id: UUID, role: DataRole = DataRole.NA, host: Host? = null, protocol: String = "", args: String = "", remotePath: String? = null):
            this(context, file.parent!!, file.nameWithoutExtension, extensionToMime(file.extension), ".${file.name.substringAfter(".")}", id, role, host, protocol, args, remotePath)

    constructor(context: Context, path: String, id: UUID):
            this(context, File(path), id)

    constructor(context: Context, url: URL, id: UUID, role: DataRole = DataRole.NA, host: Host? = null):
            this(context, File(url.path), id, role, host, protocol = url.protocol, args = url.query ?: "", remotePath = url.path)

    constructor(context: Context, fileName: String, id: UUID, role: DataRole = DataRole.NA, url: URL? = null, host: Host? = null) {
        this.context = context
        this.id = id
        this.host = host
        this.role = role
        try {
            val lastPathSlashIndex = fileName.lastIndexOf('/')
            if (lastPathSlashIndex < 0) {
                this.extension = fileName.substring(fileName.indexOf('.'))
                this.namespace = ""
            } else {
                this.namespace = fileName.substring(0, lastPathSlashIndex + 1)
                this.extension = fileName.substring(fileName.indexOf('.', lastPathSlashIndex))
            }
            val nameOnly = fileName.substring(fileName.lastIndexOf('/') + 1)
            this.name = nameOnly.substring(0, nameOnly.length - extension.length - 1)
            this.mime = extensionToMime(extension)
        } catch (e: Exception) {
            throw Exception("Invalid filename $fileName", e)
        }
    }

    override fun destinationStream(source: DataSource): OutputStream {
        return when (source) {
            DataSource.TempFile -> {
                this.tempFile = File.createTempFile(this.name, this.extension)
                return SmartFileOutputStream(this.tempFile!!)
            }
            DataSource.LocalFile -> {
                // FileInputStream(File(context.cacheDir, "cached-data/" + this.id.toString()))
                val suiteDir = File(context.cacheDir, this.host!!.id.toString())
                if ((suiteDir.exists() && suiteDir.isDirectory) || suiteDir.mkdirs()) {
                    SmartFileOutputStream(File(suiteDir, this.id.toString()))
                } else {
                    throw Exception("Could not get host directory")
                }
            }
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun sourcePath(source: DataSource): String {
        return when(source) {
            DataSource.TempFile -> if (this.tempFile != null) this.tempFile!!.canonicalPath else throw UserException("Temp file does not exist for ${this}")
            DataSource.RemoteFile -> if (host != null) "${this.protocol}://${host.siteName}$remotePath" else throw Exception("Source $source not supported")
            DataSource.LocalFile -> {
                val suiteDir = File(context.cacheDir, this.host?.id.toString())
                if (suiteDir.isDirectory || suiteDir.mkdirs()) {
                    File(suiteDir, this.id.toString()).canonicalPath
                } else {
                    throw Exception("Could not get host directory")
                }
            }
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun size(source: DataSource): Long {
        return when(source) {
            DataSource.TempFile -> if (this.tempFile != null) this.tempFile!!.length() else throw UserException("Temp file does not exist for ${this}")
            DataSource.RemoteFile -> if (host != null) -1L else throw Exception("Source $source not supported")
            DataSource.LocalFile -> {
                val suiteDir = File(context.cacheDir, this.host?.id.toString())
                if (suiteDir.isDirectory || suiteDir.mkdirs()) {
                    File(suiteDir, this.id.toString()).length()
                } else {
                    throw Exception("Could not get host directory")
                }
            }
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun sourceStream(source: DataSource): BetterInputStream {
        return when (source) {
            DataSource.TempFile -> FileInputStream(this.tempFile!!, this)
            DataSource.LocalFile -> {
                val f = File(this.sourcePath(source))
                if (f.exists() && f.isFile) {
                    FileInputStream(f, this)
                } else {
                    throw Exception("File not found: ${f.canonicalPath}")
                }
            }
            DataSource.RemoteFile -> {
                HttpInputStream(URL(this.sourcePath(source)), this)
            }
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun isAvailable(source: DataSource): Boolean {
        return when (source) {
            DataSource.LocalFile -> {
                val f = File(this.sourcePath(source))
                return f.exists() && f.isFile
            }
            DataSource.TempFile -> this.tempFile != null
            DataSource.RemoteFile -> true
            DataSource.LocalSensor -> false
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun delete(source: DataSource) {
        when (source) {
            DataSource.LocalFile -> {
                val file = File(this.sourcePath(source))
                if (file.isFile) {
                    file.delete()
                }
            }
            DataSource.TempFile -> {
                this.tempFile?.delete()
                this.tempFile = null
            }
            else -> throw Exception("Cannot delete $source")
        }

//        if (this.isAvailable(source)) {
//            throw Exception("Deleted but still exists, problems can occur")
//        }
    }

    override fun toString(): String = "$name.$extension"

    override fun equals(other: Any?): Boolean =
            other != null && other is Stream && (this === other || this.id == other.id)

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun extensionToMime(extension: String): String {
            return when (extension) {
                "txt" -> "application/text"
                "gz" -> "application/gzip"
                "cifar10.gz" -> "application/cifar10"
                "cifar10" -> "application/cifar10"
                else -> "*/*"
            }
        }

        val idResolvers = ArrayList<(UUID) -> Stream?>()

        fun addResolver(fn: (UUID) -> Stream?) {
            idResolvers.add(fn)
        }

        fun fromPath(path: String): FileStream {
            throw NotImplementedError()
        }

        fun fromId(id: UUID): Stream {
            for (resolver in idResolvers) {
                val ret = resolver(id)
                if (ret != null) {
                    return ret
                }
            }
            throw IllegalArgumentException("Stream $id not found")
        }
    }
//    override fun load(source: DataSource, context: Context): InputStream {
//        return when (source) {
//            DataSource.LocalFile -> FileInputStream(File(context.cacheDir, "cached-data/" + this.id.toString()))
//            else -> throw java.lang.Exception("Unsupported source $source")
//        }
//    }

//    override fun isAvailable(source: DataSource, context: Context): Boolean {
//        return when (source) {
//            DataSource.RemoteFile -> true
//            DataSource.LocalFile -> {
//                val f = File(context.cacheDir, "cached-data/" + this.id.toString())
//                f.exists() && f.isFile
//            }
//            else -> false
//        }
//    }

//    override fun delete(source: DataSource, context: Context) {
//        when (source) {
//            DataSource.LocalFile -> {
//                File(context.cacheDir, "cached-data/" + this.id.toString()).delete()
//                DataManager.get(context).detachFileId(this.id)
//            }
//        }
//    }
}