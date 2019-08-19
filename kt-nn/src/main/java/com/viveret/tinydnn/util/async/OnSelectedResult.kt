package com.viveret.tinydnn.util.async

class OnSelectedResult(val dismiss: Boolean, val callback: () -> Unit) {
    constructor(dismiss: Boolean): this(dismiss, {})
}