package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface StreamPackage: Indexable {
    val title: String
    val dataName: String
    val host: Host
    val formatId: UUID
    val encryptedProtocol: Boolean

    val streams: Map<DataRole, Stream>

    fun sizeOfStreams(source: DataSource, context: Context): Long

    fun loadDataValues(source: DataSource, context: Context, fitTo: Boolean, project: NeuralNetProject?): DataValues
}