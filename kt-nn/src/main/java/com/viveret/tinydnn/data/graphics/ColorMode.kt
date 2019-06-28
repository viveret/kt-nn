package com.viveret.tinydnn.data.graphics

interface ColorMode {
    val id: Int
    fun filter(input: Int): Int
}