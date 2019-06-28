package com.viveret.tinydnn.error

class RetryableException : Exception {
    constructor(message: String?) : super(message)
}