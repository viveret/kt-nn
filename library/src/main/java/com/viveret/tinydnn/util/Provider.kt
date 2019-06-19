package com.viveret.tinydnn.util

import com.viveret.tinydnn.util.async.TypedObjectProvider

interface Provider<T>: TypedObjectProvider {
    fun get(): T
}