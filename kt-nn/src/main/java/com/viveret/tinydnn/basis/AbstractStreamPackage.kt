package com.viveret.tinydnn.basis

import android.content.Context
import java.io.File

abstract class AbstractStreamPackage(val context: Context) : StreamPackage {
    override fun isAvailable(source: DataSource): Boolean =
        this.streams.values.all { f -> f.isAvailable(source) }

    override fun delete(source: DataSource) {
        if (source == DataSource.LocalFile) {
            val suiteDir = File(context.cacheDir, this.id.toString())
            if (suiteDir.isDirectory) {
                for (f in this.streams.values) {
                    f.delete(source)
                }
                suiteDir.deleteRecursively()
            }
        } else {
            throw IllegalArgumentException("Cannot delete $source")
        }

//        if (this.isAvailable(source)) {
//            throw Exception("Deleted but still exists, problems can occur")
//        }
    }

//    fun read(
//        fmt: DataSliceReader,
//        files: InputSelection,
//        destination: DataValues,
//        count: Int,
//        project: NeuralNetProject
//    ): Int {
//        return 0
////
////        data.valueMetaInfo = this.streams.getValue(DataRole.Input)
////        data.labelMetaInfo = this.streams[DataRole.InputLabels]
////        if (data is TrainingDataValues) {
////            if (fitTo && data.fitTo != null) {
////                data.fitTo.valueMetaInfo = this.streams[DataRole.FitTo]
////                data.fitTo.labelMetaInfo = this.streams[DataRole.FitToLabels]
////            } else {
////                error("data.fitTo (${data.fitTo}) != null && $fitTo was false")
////            }
////        }
////
////        return data
//    }

    override fun sizeOfStreams(source: DataSource): Long =
        this.streams.values.map { it.size(source) }.sum()

    override fun equals(other: Any?): Boolean = other is StreamPackage && this.id == other.id

    override fun hashCode(): Int = this.id.hashCode()
}