package com.viveret.tinydnn.util

import android.content.Context

class StringIdMap(val ids: Array<String>) {
    private lateinit var map: Map<String, String>

    fun strs(): Set<String> = map.keys

    fun str2id(str: String): String? = map[str]

    fun attach(c: Context) {
        map = ids.map { (if (it.first() == '@') c.getString(c.resources.getIdentifier(it.substring(1), "string", "com.viveret.pocketn2")) else it) to it }.toMap()
    }
}