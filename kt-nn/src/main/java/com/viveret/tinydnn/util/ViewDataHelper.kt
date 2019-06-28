package com.viveret.tinydnn.util

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.viveret.tinydnn.error.UserException

open class ViewDataHelper {
    private var valView: EditText? = null

    fun arg2str(propertyId: Int, args: Map<Int, View>): String {
        this.valView = args[propertyId] as EditText
        return this.valView!!.text.toString()
    }

    fun view2str(v: View): String {
        this.valView = v as EditText
        return this.valView!!.text.toString()
    }

    fun arg2long(propertyId: Int, args: Map<Int, View>): Long {
        try {
            return arg2str(propertyId, args).toLong()
        } catch (e: NumberFormatException) {
            throw UserException("Invalid format for ${this.valView?.context?.getString(propertyId)} (Must be a whole number)", e, this.valView)
        }
    }

    fun view2long(v: View): Long {
        try {
            return view2str(v).toLong()
        } catch (e: NumberFormatException) {
            throw UserException("Invalid format (Must be a whole number)", e, this.valView)
        }
    }

    fun arg2bool(propertyId: Int, args: Map<Int, View>): Boolean = (args[propertyId] as CheckBox).isChecked

    fun view2bool(v: View): Boolean = (v as CheckBox).isChecked

    fun arg2float(propertyId: Int, args: Map<Int, View>): Double {
        try {
            return arg2str(propertyId, args).toDouble()
        } catch (e: NumberFormatException) {
            throw UserException("Invalid format for ${this.valView?.context?.getString(propertyId)} (Must be a decimal number)", e, this.valView)
        }
    }

    fun view2float(v: View): Double {
        try {
            return view2str(v).toDouble()
        } catch (e: NumberFormatException) {
            throw UserException("Invalid format (Must be a decimal number)", e, this.valView)
        }
    }

    fun setHint(v: TextView, stringResourceId: Int) {
        v.hint = v.context.getString(stringResourceId)
    }
}