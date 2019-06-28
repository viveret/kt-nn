package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.train.TrainingDataReader
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.File

abstract class AbstractStreamPackage : StreamPackage {
    override fun isAvailable(source: DataSource, context: Context): Boolean =
        this.streams.values.all { f -> f.isAvailable(source, context) }

    override fun delete(source: DataSource, context: Context) {
        if (source == DataSource.LocalFile) {
            val suiteDir = File(context.cacheDir, this.id.toString())
            if (suiteDir.isDirectory) {
                for (f in this.streams.values) {
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

    fun loadStreams(
        fmt: TrainingDataReader,
        files: Map<Stream, File>,
        fitTo: Boolean,
        project: NeuralNetProject?
    ): DataValues {
        val data = if (fitTo) {
            fmt.getTrainingData(files, this, project)!!
        } else {
            fmt.getDataValues(files, this, project)
        }

        data.valueMetaInfo = this.streams.getValue(DataRole.Input)
        data.labelMetaInfo = this.streams[DataRole.InputLabels]
        if (data is TrainingDataValues) {
            if (fitTo && data.fitTo != null) {
                data.fitTo.valueMetaInfo = this.streams[DataRole.FitTo]
                data.fitTo.labelMetaInfo = this.streams[DataRole.FitToLabels]
            } else {
                error("data.fitTo (${data.fitTo}) != null && $fitTo was false")
            }
        }

        return data
    }

    override fun sizeOfStreams(source: DataSource, context: Context): Long =
        this.streams.values.map { it.size(source, context) }.sum()

    override fun equals(other: Any?): Boolean = other is StreamPackage && this.id == other.id

    override fun hashCode(): Int = this.id.hashCode()
}