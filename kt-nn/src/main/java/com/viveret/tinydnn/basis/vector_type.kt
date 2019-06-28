package com.viveret.tinydnn.basis

enum class vector_type constructor(// layer-specific storage
        val `val`: Int) {
    // 0x0001XXX : in/out data,  // input/output data, fed by other layer or input channel
    _data(0x0001000),

    // 0x0002XXX : trainable parameters, updated for each back propagation
    weight(0x0002000),
    bias(0x0002001),

    label(0x0004000),
    aux(0x0010000)
}
