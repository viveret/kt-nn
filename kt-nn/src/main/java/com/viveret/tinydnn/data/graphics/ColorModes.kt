package com.viveret.tinydnn.data.graphics

import com.viveret.tinydnn.R
import kotlin.math.floor

class ColorModes {
    companion object {
        val RED = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_red
            override fun filter(input: Int): Int = input.and(0xFF0000).shr(16)
        }
        val GREEN = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_green
            override fun filter(input: Int): Int = input.and(0x00FF00).shr(8)
        }
        val BLUE = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_blue
            override fun filter(input: Int): Int = input.and(0x0000FF)
        }
        val YELLOW = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_yellow
            override fun filter(input: Int): Int = floor((RED.filter(input) + GREEN.filter(input)) / 2.0).toInt()
        }
        val CYAN = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_cyan
            override fun filter(input: Int): Int = floor((BLUE.filter(input) + GREEN.filter(input)) / 2.0).toInt()
        }
        val MAGENTA = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_magenta
            override fun filter(input: Int): Int = floor((RED.filter(input) + BLUE.filter(input)) / 2.0).toInt()
        }
        val GREYSCALE = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_greyscale
            override fun filter(input: Int): Int = floor((RED.filter(input) + GREEN.filter(input) + BLUE.filter(input)) / 3.0).toInt()
        }
        val LUMINANCE = object : ColorMode {
            override val id: Int = R.string.camera_color_mode_luminance
            override fun filter(input: Int): Int = floor(RED.filter(input) * 0.299 + GREEN.filter(input) * 0.587 + BLUE.filter(input) * 0.114).toInt()
        }

        val all = arrayOf(RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA, GREYSCALE, LUMINANCE)
    }
}