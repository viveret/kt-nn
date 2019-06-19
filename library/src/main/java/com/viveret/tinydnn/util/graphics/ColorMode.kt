package com.viveret.tinydnn.util.graphics

interface ColorMode {
    val id: Int
    fun filter(input: Int): Int
}