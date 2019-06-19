package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface StreamPackage: Indexable {
    val titleStringId: Int
    val dataNameStringId: Int
    val host: Host
    val formatId: UUID
    val encryptedProtocol: Boolean

    val inputFile: Stream
    val labelFile: Stream?
    val fitToFile: Stream?
    val fitToLabelsFile: Stream?

    fun loadDataValues(source: DataSource, context: Context, fitTo: Boolean, project: NeuralNetProject?): DataValues
}