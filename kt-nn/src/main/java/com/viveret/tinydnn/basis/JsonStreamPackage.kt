package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.project.NeuralNetProject
import org.json.JSONObject
import java.util.*

class JsonStreamPackage(context: Context, val json: JSONObject) : AbstractStreamPackage(context) {
    override fun open(
        source: DataSource,
        fitTo: Boolean,
        project: NeuralNetProject?
    ): InputDataValueStream {
        val files = InputSelection()
        for (it in streams.values) {
            files[it.role] = InputSelection.FileItem(it, DataSource.LocalFile)
        }

        return InputDataValueStream(context, project!!, files)
    }

    override fun sizeOfStreams(source: DataSource): Long =
        this.streams.values.map { it.size(source) }.sum()

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
                FileStream(context, item, host, this)
            }.map { it.role to (it as Stream) }.toMap()
        }

    override val id: UUID = UUID.fromString(json.getString("id"))
}