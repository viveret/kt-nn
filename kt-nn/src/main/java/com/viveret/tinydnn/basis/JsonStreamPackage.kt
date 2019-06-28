package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.project.ProjectProvider
import org.json.JSONObject
import java.io.File
import java.util.*

class JsonStreamPackage(val json: JSONObject): AbstractStreamPackage() {
    override fun loadDataValues(source: DataSource, context: Context, fitTo: Boolean, project: NeuralNetProject?): DataValues {
        val fmt = (context as ProjectProvider).getDataFormat(this.formatId)
        val files = streams.values.map { it to File(it.sourcePath(DataSource.LocalFile, context)) }.toMap()

        return this.loadStreams(fmt, files, fitTo, project)
    }

    override fun sizeOfStreams(source: DataSource, context: Context): Long =
        this.streams.values.map { it.size(source, context) }.sum()

    override val title: String = json.getString("title")
    override val dataName: String = json.getString("summary")
    override val host: Host = Host.fromId(UUID.fromString(json.getString("host")))
    override val formatId: UUID = UUID.fromString(json.getString("format"))
    override val encryptedProtocol: Boolean = json.getBoolean("encrypted")

    override val streams: Map<DataRole, Stream>
        get() {
            val array = json.getJSONArray("files")
            return List(array.length()) {
                val item = array.getJSONObject(it)
                FileStream(item, host, this)
            }.map { it.role to (it as Stream) }.toMap()
        }

    override val id: UUID = UUID.fromString(json.getString("id"))
}