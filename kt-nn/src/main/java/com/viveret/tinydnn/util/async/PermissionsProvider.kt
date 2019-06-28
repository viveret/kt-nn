package com.viveret.tinydnn.util.async

import android.content.Context

interface PermissionsProvider {
    fun requestReadAndWritePermissions(callback: (Context) -> Unit)
    fun requestInternetPermissions(callback: (Context) -> Unit)
}