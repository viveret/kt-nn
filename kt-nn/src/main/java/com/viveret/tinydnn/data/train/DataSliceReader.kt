package com.viveret.tinydnn.data.train

import com.viveret.tinydnn.basis.AnchorPoint
import com.viveret.tinydnn.basis.AttributeResolver
import com.viveret.tinydnn.basis.DataRole
import com.viveret.tinydnn.basis.DataSlice
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.InputSelection
import com.viveret.tinydnn.data.io.LabelReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.project.NeuralNetProject
import java.util.*

interface DataSliceReader: AttributeResolver {
    fun open(inputSelection: InputSelection)
    fun read(destination: DataValues, offset: Int, amountToRead: Int): Int
    fun seek(relativeTo: AnchorPoint, offset: Int): Boolean
    fun vectReader(role: DataRole): VectReader?
    fun labelReader(role: DataRole): LabelReader?

    val openRoles: Array<DataRole>
}