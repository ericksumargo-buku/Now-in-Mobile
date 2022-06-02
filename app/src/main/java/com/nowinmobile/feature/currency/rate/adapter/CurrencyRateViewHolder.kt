package com.nowinmobile.feature.currency.rate.adapter

import androidx.recyclerview.widget.RecyclerView
import com.nowinmobile.data.currency.api.model.CurrencyRate
import com.nowinmobile.databinding.ItemCurrencyRateBinding

class CurrencyRateViewHolder(
    private val viewBinding: ItemCurrencyRateBinding,
    private val onClickListener: (item: CurrencyRate) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun render(data: CurrencyRate) {
        renderRoot(data)
        renderCurrencyText(data)
        renderRateText(data)
    }

    private fun renderRoot(data: CurrencyRate) {
        viewBinding.root.setOnClickListener {
            onClickListener(data)
        }
    }

    private fun renderCurrencyText(data: CurrencyRate) {
        viewBinding.currencyText.also { view ->
            view.text = data.target.code
        }
    }

    private fun renderRateText(data: CurrencyRate) {
        viewBinding.rateText.also { view ->
            view.text = "${data.rate}"
        }
    }
}
