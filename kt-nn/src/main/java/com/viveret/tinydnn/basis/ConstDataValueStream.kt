package com.viveret.tinydnn.basis

import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.DataSliceReader
import kotlin.math.min

class ConstDataValueStream(roles: Array<DataRole>) : DataSliceReader {
    override val openRoles: Array<DataRole>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun open(inputSelection: InputSelection) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun vectReader(role: DataRole): VectReader? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun labelReader(role: DataRole): LabelReader? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seek(relativeTo: AnchorPoint, offset: Int): Boolean {
        return when (relativeTo) {
            AnchorPoint.Start -> {
                if (offset in 0 until size) {
                    dataPosition = offset
                    true
                } else {
                    false
                }
            }
            AnchorPoint.Middle -> seek(AnchorPoint.Start, size / 2 + offset)
            AnchorPoint.End -> seek(AnchorPoint.Start, size - offset)
            AnchorPoint.Current -> seek(AnchorPoint.Start, dataPosition + offset)
        }
    }

    private val intAttributes = HashMap<DataAttr, Int?>()
    private val stringAttributes = HashMap<DataAttr, String?>()
    private val booleanAttributes = HashMap<DataAttr, Boolean?>()
    private var dataPosition = 0
    private val size: Int
        get() = vects.map { it.value.size }.max() ?: 0

    override fun getInt(attr: DataAttr): Int? = intAttributes[attr]

    override fun getString(attr: DataAttr): String? = stringAttributes[attr]

    override fun getBoolean(attr: DataAttr): Boolean? = booleanAttributes[attr]

    fun setIntAttr(attr: DataAttr, value: Int?) {
        intAttributes[attr] = value
    }

    fun setStringAttr(attr: DataAttr, value: String?) {
        stringAttributes[attr] = value
    }

    fun setBooleanAttr(attr: DataAttr, value: Boolean?) {
        booleanAttributes[attr] = value
    }

    private val vects = HashMap<DataRole, ArrayList<Vect>>()
    private val labels = HashMap<DataRole, ArrayList<Long>>()

    init {
        for (r in roles) {
            vects[r] = ArrayList()
            labels[r] = ArrayList()
        }
    }

    fun push(data: Vect, role: DataRole) {
        vects[role]!!.add(data)
    }

    fun push(data: Long, role: DataRole) {
        labels[role]!!.add(data)
    }


    fun push(input: Collection<Vect>, label: Collection<Long>, role: DataRole) {
        vects[role]!!.addAll(input)
        val labelRole = if (role == DataRole.Input) DataRole.InputLabels else DataRole.FitToLabels
        labels[labelRole]!!.addAll(label)
    }

    override fun read(destination: DataValues, offset: Int, amountToRead: Int): Int {
        for (i in 0 until min(destination.size, size)) {

            val inputVectList = vects[DataRole.Input]
            if (inputVectList != null && inputVectList.isNotEmpty()) {
                val inputLabelList = labels[DataRole.InputLabels]
                val label = if (inputLabelList != null && inputLabelList.isNotEmpty()) inputLabelList.removeAt(0) else -1

                destination[DataRole.Input]!!.vects[i] = inputVectList.removeAt(0)
                destination[DataRole.Input]!!.labels[i] = label
            } else {
                return i
            }

            val fitToVectList = vects[DataRole.FitTo]
            val fitToLabelList = labels[DataRole.FitToLabels]
            if (fitToVectList != null && fitToVectList.isNotEmpty()) {
                val label = if (fitToLabelList != null && fitToLabelList.isNotEmpty()) fitToLabelList.removeAt(0) else -1

                destination[DataRole.FitTo]!!.vects[i] = fitToVectList.removeAt(0)
                destination[DataRole.FitTo]!!.labels[i] = label
            } else {
                return i
            }
        }
        return destination.size
    }
}