package com.viveret.tinydnn.error

import android.view.View

class UserException(message: String?, cause: Throwable? = null, val relatedView: View? = null) : Exception(message, cause)