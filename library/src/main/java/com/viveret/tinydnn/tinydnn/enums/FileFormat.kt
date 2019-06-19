package com.viveret.tinydnn.tinydnn.enums

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2

enum class FileFormat(val stringResId: Int) {
    Binary(R2.string.binary),
    PortableBinary(R2.string.portable_binary),
    Json(R2.string.json)
}
