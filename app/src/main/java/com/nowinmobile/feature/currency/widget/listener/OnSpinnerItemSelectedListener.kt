package com.nowinmobile.feature.currency.widget.listener

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener

class OnSpinnerItemSelectedListener(
    private val onItemSelected: (item: Int) -> Unit,
) : OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        onItemSelected(pos)
    }
}
