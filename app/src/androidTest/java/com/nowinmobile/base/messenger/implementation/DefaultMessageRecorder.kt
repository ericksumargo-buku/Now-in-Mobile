package com.nowinmobile.base.messenger.implementation

import android.content.Context
import android.view.View
import com.nowinmobile.base.messenger.api.MessageRecorder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultMessageRecorder @Inject constructor() : MessageRecorder {
    private val snackBarMessagePool: MutableSet<String> = mutableSetOf()

    private val toastMessagePool: MutableSet<String> = mutableSetOf()

    override fun displaySnackBar(view: View, message: String, duration: Int) {
        snackBarMessagePool.add(message)
    }

    override fun displayToast(context: Context, message: String, duration: Int) {
        toastMessagePool.add(message)
    }

    override fun hasSnackBarMessage(message: String): Boolean {
        return snackBarMessagePool.contains(message)
    }

    override fun hasToastMessage(message: String): Boolean {
        return toastMessagePool.contains(message)
    }
}
