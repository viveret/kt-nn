package com.viveret.tinydnn.data

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2

enum class DataMethod(val stringResId: Int, val minInSize: Int, val maxInSize: Int = Int.MAX_VALUE) {
    BinaryGate(R2.string.data_method_binary_gate, 2, 3),
    TextFile(R2.string.data_method_text_file, 1, Int.MAX_VALUE),
    BinaryFile(R2.string.data_method_binary_file, 1),
    Camera(R2.string.data_method_camera, 9),
    Microphone(R2.string.data_method_microphone, Int.MAX_VALUE),
    Sensors(R2.string.data_method_sensors, Int.MAX_VALUE),
    GPS(R2.string.data_method_gps, Int.MAX_VALUE),
    Canvas(R2.string.data_method_canvas, 9);

    fun supports(inSize: Long): Boolean = this.minInSize <= inSize && inSize < this.maxInSize
}