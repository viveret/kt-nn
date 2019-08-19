package com.viveret.tinydnn.basis

import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface StreamPackage: Indexable {
    val title: String
    val dataName: String
    val host: Host
    val formatId: UUID
    val encryptedProtocol: Boolean

    val streams: Map<DataRole, Stream>

    fun sizeOfStreams(source: DataSource): Long

    fun open(source: DataSource, fitTo: Boolean, project: NeuralNetProject?): InputDataValueStream
}