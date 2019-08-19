package com.viveret.tinydnn.basis

import java.util.*

interface Indexable {
    val id: UUID
    fun isAvailable(source: DataSource): Boolean
    fun delete(source: DataSource)
}