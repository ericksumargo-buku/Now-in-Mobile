package com.nowinmobile.feature.currency.widget.listener

import android.text.Editable
import android.text.TextWatcher

class OnTextChangedListener(
    private val callback: (text: String) -> Unit,
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editor: Editable?) {
        callback(editor?.toString().orEmpty())
    }
}
