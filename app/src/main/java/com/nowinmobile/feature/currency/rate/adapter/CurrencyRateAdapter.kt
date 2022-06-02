package com.nowinmobile.feature.currency.rate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nowinmobile.data.currency.api.model.CurrencyRate
import com.nowinmobile.databinding.ItemCurrencyRateBinding
import kotlin.math.ceil

class CurrencyRateAdapter(
    private val itemPerPage: Int,
    private val onClickListener: (item: CurrencyRate) -> Unit,
    private val onNextPageListener: (page: Int) -> Unit,
) : ListAdapter<CurrencyRate, CurrencyRateViewHolder>(CurrencyRateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemCurrencyRateBinding.inflate(inflater, parent, false)
        return CurrencyRateViewHolder(viewBinding, onClickListener)
    }

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)

        if (position < itemCount - 1) return
        onNextPageListener(ceil(itemCount.toDouble() / itemPerPage.toDouble()).toInt() + 1)
    }
}
