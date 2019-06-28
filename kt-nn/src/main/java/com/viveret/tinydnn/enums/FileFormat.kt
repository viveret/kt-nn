package com.viveret.tinydnn.enums

import com.viveret.tinydnn.R

enum class FileFormat(val stringResId: Int) {
    Binary(R.string.binary),
    PortableBinary(R.string.portable_binary),
    Json(R.string.json)
}
