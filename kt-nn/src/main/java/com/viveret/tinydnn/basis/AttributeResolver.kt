package com.viveret.tinydnn.basis

interface AttributeResolver {
    fun getInt(attr: DataAttr): Int?
    fun getString(attr: DataAttr): String?
    fun getBoolean(attr: DataAttr): Boolean?
}