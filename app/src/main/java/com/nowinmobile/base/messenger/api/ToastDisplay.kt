package com.nowinmobile.base.messenger.api

import android.content.Context

interface ToastDisplay {
    /** Display Toast [message] for [duration] long. */
    fun displayToast(context: Context, message: String, duration: Int = 2000)
}
