package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.io.SmartFileInputStream
import com.viveret.tinydnn.data.io.SmartFileOutputStream
import com.viveret.tinydnn.error.UserException
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class FileStream: Stream {
    override val name: String
    override val extension: String
    override val namespace: String
    override val mime: String
    override val id: UUID
    override val host: Host?
    override val role: DataRole
    private var tempFile: File? = null
    var protocol: String = ""
    var args: String = ""
    var remotePath: String = ""

    constructor(namespace: String, name: String, mime: String, extension: String, id: UUID, role: DataRole = DataRole.NA, host: Host? = null, protocol: String = "", args: String = "", remotePath: String? = null) {
        this.id = id
        this.name = name
        this.namespace = namespace
        this.mime = mime
        this.extension = extension
        this.host = host
        this.role = role
        this.protocol = protocol
        this.args = if (args.isNotEmpty()) "?$args" else args
        this.remotePath = remotePath ?: "$namespace/$name$extension$args"
    }

    constructor(json: JSONObject, host: Host, suite: StreamPackage): this(URL("http${if (suite.encryptedProtocol) "s" else ""}://${host.siteName}/${json.getString("endpoint")}"),
        UUID.fromString(json.getString("id")), DataRole.valueOf(json.getString("role")), host)

    constructor(file: File, id: UUID, role: DataRole = DataRole.NA, host: Host? = null, protocol: String = "", args: String = "", remotePath: String? = null):
            this(file.parent, file.nameWithoutExtension, extensionToMime(file.extension), ".${file.name.substringAfter(".")}", id, role, host, protocol, args, remotePath)

    constructor(path: String, id: UUID):
            this(File(path), id)

    constructor(url: URL, id: UUID, role: DataRole = DataRole.NA, host: Host? = null):
            this(File(url.path), id, role, host, protocol = url.protocol, args = url.query ?: "", remotePath = url.path)

    constructor(fileName: String, id: UUID, role: DataRole = DataRole.NA, url: URL? = null, host: Host? = null) {
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

    override fun destinationStream(source: DataSource, context: Context): OutputStream {
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

    override fun sourcePath(source: DataSource, context: Context): String {
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

    override fun size(source: DataSource, context: Context): Long {
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

    override fun sourceStream(source: DataSource, context: Context): InputStream {
        return when (source) {
            DataSource.TempFile -> SmartFileInputStream(this.tempFile!!)
            DataSource.LocalFile -> {
                val f = File(this.sourcePath(source, context))
                if (f.exists() && f.isFile) {
                    SmartFileInputStream(f)
                } else {
                    throw Exception("File not found: ${f.canonicalPath}")
                }
            }
            DataSource.RemoteFile -> {
                val url = this.sourcePath(source, context)
                val conn = URL(url).openConnection() as HttpURLConnection
                val code = conn.responseCode
                return when (code) {
                    200 -> conn.inputStream
                    else -> throw Exception("$url returned $code: ${conn.responseMessage}")
                }
            }
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun isAvailable(source: DataSource, context: Context): Boolean {
        return when (source) {
            DataSource.LocalFile -> {
                val f = File(this.sourcePath(source, context))
                return f.exists() && f.isFile
            }
            DataSource.TempFile -> this.tempFile != null
            DataSource.RemoteFile -> true
            DataSource.LocalSensor -> false
            else -> throw IllegalArgumentException("Source $source is not supported")
        }
    }

    override fun delete(source: DataSource, context: Context) {
        when (source) {
            DataSource.LocalFile -> {
                val suiteDir = File(context.cacheDir, this.host!!.id.toString())
                if (suiteDir.exists() && suiteDir.isDirectory) {
                    val file = File(suiteDir, this.id.toString())
                    if (file.isFile) {
                        file.delete()
                    }
                }
            }
            DataSource.TempFile -> {
                this.tempFile?.delete()
                this.tempFile = null
            }
            else -> throw Exception("Cannot delete $source")
        }

        if (this.isAvailable(source, context)) {
            throw Exception("Deleted but still exists, problems can occur")
        }
    }

    override fun toString(): String = "$name.$extension"

    override fun equals(other: Any?): Boolean =
            other != null && other is Stream && (this === other || this.id == other.id)

    override fun hashCode(): Int = id.hashCode()

    companion object {
        private fun extensionToMime(extension: String): String {
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