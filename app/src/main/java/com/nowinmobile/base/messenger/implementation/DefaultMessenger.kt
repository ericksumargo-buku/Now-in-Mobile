package com.nowinmobile.base.messenger.implementation

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.nowinmobile.base.messenger.api.Messenger
import javax.inject.Inject

class DefaultMessenger @Inject constructor() : Messenger {
    override fun displaySnackBar(view: View, message: String, duration: Int) {
        Snackbar.make(view, message, duration).show()
    }

    override fun displayToast(context: Context, message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }
}
