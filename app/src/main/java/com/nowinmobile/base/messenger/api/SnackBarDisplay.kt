package com.nowinmobile.base.messenger.api

import android.view.View

interface SnackBarDisplay {
    /** Display SnackBar [message] for [duration] long with [view] anchor. */
    fun displaySnackBar(view: View, message: String, duration: Int = 1500)
}
