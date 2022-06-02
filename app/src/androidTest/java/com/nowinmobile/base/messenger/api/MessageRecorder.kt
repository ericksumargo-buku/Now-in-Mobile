package com.nowinmobile.base.messenger.api

interface MessageRecorder : Messenger {
    fun hasSnackBarMessage(message: String): Boolean

    fun hasToastMessage(message: String): Boolean
}
