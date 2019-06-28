package com.viveret.tinydnn.util

import android.content.Context
import android.view.View

interface ConstructorViewDataBinder<T> {
    fun overrideView(propertyId: Int, context: Context): View?
    fun applyFieldProps(propertyId: Int, view: View)
    fun newInstance(arguments: Map<Int, View>): T
}