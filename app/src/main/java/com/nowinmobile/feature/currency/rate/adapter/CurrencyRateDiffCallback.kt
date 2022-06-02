package com.nowinmobile.feature.currency.rate.adapter

import androidx.recyclerview.widget.DiffUtil
import com.nowinmobile.data.currency.api.model.CurrencyRate

class CurrencyRateDiffCallback : DiffUtil.ItemCallback<CurrencyRate>() {
    override fun areItemsTheSame(
        oldItem: CurrencyRate,
        newItem: CurrencyRate
    ): Boolean {
        return oldItem.target.code == newItem.target.code
    }

    override fun areContentsTheSame(
        oldItem: CurrencyRate,
        newItem: CurrencyRate
    ): Boolean {
        return oldItem == newItem
    }
}
