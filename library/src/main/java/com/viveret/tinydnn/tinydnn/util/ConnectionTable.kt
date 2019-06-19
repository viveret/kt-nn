package com.viveret.tinydnn.tinydnn.util

import com.viveret.tinydnn.util.JniObject

class ConnectionTable(override val nativeObjectHandle: Long) : JniObject {
    constructor(vals: BooleanArray, rows: Int, columns: Int) : this(staticConstructor(vals, rows, columns))

    constructor(vals: ArrayList<Boolean>, rows: Int, columns: Int) : this(vals.toBooleanArray(), rows, columns)

    val vals: BooleanArray = jniGetElements(nativeObjectHandle)

    override fun equals(other: Any?): Boolean = other is ConnectionTable && this.vals.contentEquals(other.vals)

    override fun toString(): String {
        val sb = StringBuilder("[")
        if (vals.isNotEmpty()) {
            for (f in vals) {
                sb.append(f)
                sb.append(", ")
            }
            sb.setLength(sb.length - 2)
        }
        sb.append("]")
        return sb.toString()
    }

    companion object {
        @JvmStatic
        external fun staticConstructor(vals: BooleanArray, rows: Int, columns: Int): Long

        @JvmStatic
        external fun jniGetElements(handle: Long): BooleanArray

        fun attach(handle: Long): ConnectionTable = ConnectionTable(handle)
    }
}