package com.viveret.tinydnn.data.formats

import com.viveret.tinydnn.basis.*
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.DataSliceReader
import com.viveret.tinydnn.error.UserException
import java.io.DataInputStream
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

abstract class BufferedSingleDataSliceReader : DataSliceReader {
    private val vectReaders = HashMap<DataRole, VectReader>()
    private val labelReaders = HashMap<DataRole, LabelReader>()

    override val openRoles: Array<DataRole>
        get() = vectReaders.keys.union(labelReaders.keys).toTypedArray()

    override fun getInt(attr: DataAttr): Int? {
        for (r in vectReaders.values.plus(labelReaders.values.filterIsInstance<AttributeResolver>())) {
            val ret = r.getInt(attr)
            if (ret != null) {
                return ret
            }
        }
        return null
    }

    override fun getString(attr: DataAttr): String? {
        for (r in vectReaders.values.plus(labelReaders.values.filterIsInstance<AttributeResolver>())) {
            val ret = r.getString(attr)
            if (ret != null) {
                return ret
            }
        }
        return null
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        for (r in vectReaders.values.plus(labelReaders.values.filterIsInstance<AttributeResolver>())) {
            val ret = r.getBoolean(attr)
            if (ret != null) {
                return ret
            }
        }
        return null
    }

    override fun open(inputSelection: InputSelection) {
        for (kvp in inputSelection) {
            val role = kvp.key
            val stream = kvp.value

            when (role) {
                DataRole.Input, DataRole.FitTo -> {
                    val vectReader = vectReader(role)!!
                    vectReaders[role] = vectReader
                    vectReader.open(stream)
                }
                DataRole.InputLabels, DataRole.FitToLabels -> {
                    val labelReader = labelReader(role)!!
                    labelReaders[role] = labelReader
                    labelReader.open(stream)
                }
                DataRole.NA -> {

                }
            }
        }
    }

    override fun read(
       destination: DataValues,
       offset: Int,
       amountToRead: Int
    ): Int {
        var total = 0

//        for (v in vects.entries.toList()) {
//            if (v.value.size < count) {
//                vects[v.key] = Array(nearestPowerOfTwo(count)) { Vect.empty }
//            }
//        }
//
//        for (label in labels.entries.toList()) {
//            if (label.value.size < count) {
//                labels[label.key] = Array(nearestPowerOfTwo(count)) { 0L }
//            }
//        }

        for (vectReader in vectReaders) {
            val destinationVects = destination[vectReader.key]?.vects
            if (destinationVects != null) {
                val n = vectReader.value.read(destinationVects, offset, amountToRead)
                if (n < 1) {
                    return n
                }
                total += n
            }
        }

        for (labelReader in labelReaders) {
            val destinationLabels = destination[labelReader.key]?.labels
            if (destinationLabels != null) {
                val n = labelReader.value.read(destinationLabels, offset, amountToRead)
                if (n < 1) {
                    return n
                }
                total += n
            }
        }

        return total / (vectReaders.size + labelReaders.size)
    }

    override fun seek(relativeTo: AnchorPoint, offset: Int): Boolean {
        for (vects in vectReaders.values) {
            if (vects.seek(relativeTo, offset) < offset) {
                return false
            }
        }
        for (labels in labelReaders.values) {
            if (labels.seek(relativeTo, offset) < offset) {
                return false
            }
        }
        return true
    }

    private fun nearestPowerOfTwo(count: Int): Int = max(1, Integer.highestOneBit(count - 1) shl 1)

    companion object {
        fun read(stream: DataInputStream, buffer: ByteArray, require: Boolean): Int {
            var bytesLeft = buffer.size
            while (bytesLeft > 0) {
                val n = stream.read(buffer, buffer.size - bytesLeft, bytesLeft)
                if (n > 0) {
                    bytesLeft -= n
                } else if (buffer.size != bytesLeft && require) {
                    throw UserException("Stream ended unexpectedly (${buffer.size - bytesLeft}/${buffer.size} bytes read)")
                } else {
                    return buffer.size - bytesLeft
                }
            }
            return buffer.size
        }
    }
}