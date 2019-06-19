package com.viveret.tinydnn.util.async

interface TypedObjectProvider {
    fun get(t: String): Any
}