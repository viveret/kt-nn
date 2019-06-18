package com.viveret.kd3.scale

import com.viveret.scale.R

interface Interpolator {
    val nameStridId: Int
    fun transform(t: Double): Double

    class Linear(val min: Double, val max: Double): Interpolator {
        override val nameStridId: Int = R.string.interpolator_linear

        override fun transform(t: Double): Double = (max - min) * t
    }
}