package com.viveret.tinydnn.data

import com.viveret.tinydnn.R

enum class DataMethod(val stringResId: Int, val minInSize: Int, val maxInSize: Int = Int.MAX_VALUE) {
    BinaryGate(R.string.data_method_binary_gate, 2, 3),
    TextFile(R.string.data_method_text_file, 1, Int.MAX_VALUE),
    BinaryFile(R.string.data_method_binary_file, 1),
    Camera(R.string.data_method_camera, 9),
    Microphone(R.string.data_method_microphone, Int.MAX_VALUE),
    Sensors(R.string.data_method_sensors, Int.MAX_VALUE),
    GPS(R.string.data_method_gps, Int.MAX_VALUE),
    Canvas(R.string.data_method_canvas, 9);

    fun supports(inSize: Long): Boolean = this.minInSize <= inSize && inSize < this.maxInSize
}