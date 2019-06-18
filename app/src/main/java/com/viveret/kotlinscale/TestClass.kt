package com.viveret.kotlinscale

class TestClass {
    private var diff: Double

    constructor(min: Double, max: Double) {
        this.diff = max - min
    }

    override fun toString(): String = diff.toString()
}