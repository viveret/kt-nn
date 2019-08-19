package com.viveret.tinydnn.basis

import android.content.Context
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.formats.*
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.DataSliceReader
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

class InputDataValueStream(val context: Context, val project: NeuralNetProject): DataSliceReader {
    constructor(context: Context, project: NeuralNetProject, inputs: InputSelection): this(context, project) {
        open(inputs)
    }

    override val openRoles: Array<DataRole>
        get() = inputSelection.keys.toTypedArray()

    lateinit var inputSelection: InputSelection

    override fun open(inputSelection: InputSelection) {
        this.inputSelection = inputSelection
        format = resolveFormat(context, inputSelection, inputSelection.getString(DataAttr.MIME) ?: error("MIME required"))
    }

    private lateinit var format: DataSliceReader

    override fun vectReader(role: DataRole): VectReader? = format.vectReader(role)

    override fun labelReader(role: DataRole): LabelReader? = format.labelReader(role)

    override fun seek(relativeTo: AnchorPoint, offset: Int): Boolean {
        return if (relativeTo == AnchorPoint.Start && offset == absolutePosition) {
            true
        } else {
            format.seek(relativeTo, offset)
        }
    }

    private var absolutePosition = 0

    override fun getInt(attr: DataAttr): Int? {
        return if (attr == DataAttr.ByteCount) {
            inputSelection.values.map { it.size }.sum()
        } else {
            val ret = format.getInt(attr)
            if (ret != null) {
                ret
            } else {
                for (s in inputSelection.values.map { it.source }) {
                    val r = s.getInt(attr)
                    if (r != null) {
                        return r
                    }
                }
                null
            }
        }
    }

    override fun getString(attr: DataAttr): String? {
        val ret = format.getString(attr)
        return if (ret != null) {
            ret
        } else {
            for (s in inputSelection.values.map { it.source }) {
                val r = s.getString(attr)
                if (r != null) {
                    return r
                }
            }
            null
        }
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        val ret = format.getBoolean(attr)
        return if (ret != null) {
            ret
        } else {
            for (s in inputSelection.values.map { it.source }) {
                val r = s.getBoolean(attr)
                if (r != null) {
                    return r
                }
            }
            null
        }
    }

    override fun read(destination: DataValues, offset: Int, amountToRead: Int): Int {
        val ret = format.read(destination, offset, amountToRead)
        absolutePosition += ret
        return ret
    }

    companion object {
        val formats = arrayOf(
            MnistDataSliceFormat::class.java,
            CMUMovieSummaryCorpusFormat::class.java,
            OnlineMovieReviewSentimentsFormat::class.java,
            Cifar10Parser::class.java).flatMap { it.getAnnotation(Mime::class.java)!!.mimes.map { mime -> mime to it } }.toMap()

        private fun resolveFormat(context: Context, inputSelection: InputSelection, mime: String): DataSliceReader {
            val mimeFormat = formats[mime] ?: error("invalid mime $mime")
            val constructor = mimeFormat.getConstructor(Context::class.java)
            val fmt = constructor.newInstance(context)
            fmt.open(inputSelection)
            return fmt
        }
    }
}