package com.viveret.tinydnn.util

import android.content.Context

class StringIdMap(val ids: Array<Int>) {

    private lateinit var map: Map<String, Int>

    fun strs(): Set<String> = map.keys

    fun str2id(str: String): Int? = map[str]

    fun attach(c: Context) {
        map = ids.map { c.getString(it) to it }.toMap()
    }
}