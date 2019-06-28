package com.viveret.tinydnn.basis

import android.content.Context
import java.util.*

interface Indexable {
    val id: UUID
    fun isAvailable(source: DataSource, context: Context): Boolean
    fun delete(source: DataSource, context: Context)
}