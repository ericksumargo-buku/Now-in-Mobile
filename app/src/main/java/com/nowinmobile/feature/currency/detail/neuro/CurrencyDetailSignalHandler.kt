package com.nowinmobile.feature.currency.detail.neuro

import android.content.Intent
import com.nowinmobile.base.neuro.api.NavigationHandler
import com.nowinmobile.base.neuro.api.AppSignalHandler
import com.nowinmobile.base.utils.toBundle
import com.nowinmobile.feature.currency.detail.screen.CurrencyDetailActivity
import com.nowinmobile.lib.neuro.api.Signal

class CurrencyDetailSignalHandler : AppSignalHandler() {
    override val paths: Set<String> = setOf("/currency-detail")

    override fun handle(signal: Signal) {
        val context = signal.context
        if (context !is NavigationHandler) return

        val intent = Intent(context, CurrencyDetailActivity::class.java).apply {
            putExtras(signal.parameter.toBundle())
            putExtras(signal.query.toBundle())
        }
        context.navigate(intent)
    }
}
