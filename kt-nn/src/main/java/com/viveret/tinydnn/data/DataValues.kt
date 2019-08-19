package com.viveret.tinydnn.data

import com.viveret.tinydnn.basis.DataRole
import com.viveret.tinydnn.basis.DataSlice
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.train.DataSliceReader
import java.util.*
import kotlin.collections.HashMap

open class DataValues: HashMap<DataRole, DataValues.Role> {
    constructor(roles: Array<DataRole>, amount: Int) {
        for (role in roles) {
            this[role] = Role(amount)
        }
    }

    val elementCount: Int
        get() = values.firstOrNull()?.size ?: 0

    fun get(index: Int): DataSlice {
        val pairs = this.entries.map{ it.key to (it.value.vects[index] to it.value.labels[index])}
        return DataSlice(pairs)
    }

    var format: DataSliceReader? = null

    class Role {
        val size: Int
            get() = vects.size

        val vects: Array<Vect>
        val labels: Array<Long>
        var valueMetaInfo: Stream? = null
        var labelMetaInfo: Stream? = null

        constructor(inputValues: Array<Vect>, inputLabels: Array<Long>) {
            this.vects = inputValues
            this.labels = inputLabels
        }

        constructor(size: Int, fillVect: Vect = Vect.empty, fillLabel: Long = 0L) {
            vects = Array(size) { fillVect }
            labels = Array(size) { fillLabel }
        }

        constructor(size: Int, fillVect: (Int) -> Vect, fillLabel: (Int) -> Long) {
            vects = Array(size, fillVect)
            labels = Array(size, fillLabel)
        }
    }

    constructor(init: (role: DataRole) -> Role, vararg roles: DataRole): super() {
        for (role in roles) {
            this[role] = init(role)
        }
    }

    constructor(size: Int, vararg roles: DataRole): super() {
        for (role in roles) {
            this[role] = Role(size)
        }
    }

    constructor(): super()
}