package com.viveret.tinydnn.basis

import com.viveret.tinydnn.util.JniObject
import kotlin.math.abs

class Vect private constructor(override val nativeObjectHandle: Long) : JniObject {
    constructor(vals: FloatArray, requiredSize: Int) : this(staticConstructor(vals, requiredSize))

    constructor(vals: ArrayList<Int>, requiredSize: Int) : this(vals.map { x -> x.toFloat() }.toFloatArray(), requiredSize)

    override fun toString(): String {
        val sb = StringBuilder("${vals.size} [")
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

    val vals: FloatArray = jniGetElements(nativeObjectHandle)

    override fun equals(other: Any?): Boolean = other is Vect && this.vals.contentEquals(other.vals)

    fun percentDifference(other: Vect): Double {
        if (this.vals.size != other.vals.size) {
            throw Exception("Size mismatch")
        } else {
            var accum = 0.0
            for (i in 0 until this.vals.size) {
                val a = this.vals[i]
                val b = other.vals[i]
                val diff = abs(b - a)
                val sum = abs(a + b)
                if (diff < 0.0001 || sum < 0.0001) {
                    continue
                }
                accum += diff / (0.5 * sum)
            }
            return accum / this.vals.size
        }
    }

    fun correctPredictionOf(solution: Vect): Boolean {
        if (this.vals.size != solution.vals.size) {
            throw Exception("Size mismatch")
        } else {
            var maxSelf = Float.MIN_VALUE
            var maxSelfIndex = -1
            var maxSolution = Float.MIN_VALUE
            var maxSolutionIndex = -2
            for (i in 0 until this.vals.size) {
                val a = this.vals[i]
                val b = solution.vals[i]
                if (a > maxSelf) {
                    maxSelf = a
                    maxSelfIndex = i
                }

                if (b > maxSolution) {
                    maxSolution = b
                    maxSolutionIndex = i
                }
            }
            return maxSelfIndex == maxSolutionIndex
        }
    }

    override fun hashCode(): Int {
        return vals.contentHashCode()
    }

    companion object {
        @JvmStatic
        external fun staticConstructor(vals: FloatArray, size: Int): Long

        @JvmStatic
        external fun jniGetElements(handle: Long): FloatArray

        fun attach(handle: Long): Vect = Vect(handle)

        val empty = Vect(emptyArray<Float>().toFloatArray(), 0)
    }
}
