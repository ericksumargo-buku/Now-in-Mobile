package com.nowinmobile.feature.currency.rate.screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.lifecycleScope
import com.nowinmobile.R
import com.nowinmobile.base.data.api.Response
import com.nowinmobile.base.messenger.api.Messenger
import com.nowinmobile.base.neuro.api.NavigationHandler
import com.nowinmobile.base.neuro.api.navigate
import com.nowinmobile.base.udf.api.screen.UdfActivity
import com.nowinmobile.base.udf.api.screen.viewBinding
import com.nowinmobile.base.udf.api.state.observeState
import com.nowinmobile.databinding.ActivityCurrencyRateBinding
import com.nowinmobile.feature.currency.rate.adapter.CurrencyRateAdapter
import com.nowinmobile.feature.currency.widget.listener.OnSpinnerItemSelectedListener
import com.nowinmobile.feature.currency.widget.listener.OnTextChangedListener
import com.nowinmobile.lib.neuro.api.Neuro
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyRateActivity : UdfActivity(), NavigationHandler {
    @Inject
    lateinit var neuro: Neuro

    @Inject
    lateinit var messenger: Messenger

    private val viewBinding: ActivityCurrencyRateBinding by viewBinding()

    private val viewModel: CurrencyRateViewModel by viewModels()

    private val adapter: CurrencyRateAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        CurrencyRateAdapter(
            itemPerPage = RATE_LIMIT,
            onClickListener = { item ->
                neuro.navigate(
                    context = this,
                    path = "/currency-detail",
                    query = mapOf(
                        "code" to item.target.code,
                        "resultCode" to CURRENCY_DETAIL_RESULT_CODE,
                    ),
                )
            },
            onNextPageListener = { page ->
                viewModel.fetchCurrencyRates(
                    amount = viewBinding.amountInput.text.toString().toDoubleOrNull() ?: -1.0,
                    code = (viewBinding.currencySpinner.selectedItem as? String).orEmpty(),
                    page = page,
                    limit = RATE_LIMIT,
                )
            }
        )
    }

    private val activityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode != CURRENCY_DETAIL_RESULT_CODE) return@registerForActivityResult

            val data = result.data
            // Do something with the data...
        }

    override fun navigate(intent: Intent) {
        activityLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        setupView()
        observeState(source = viewModel.stateFlow, action = ::render)

        viewModel.fetchCurrency(refresh = false)
        viewModel.fetchSourceCurrency()
        viewModel.fetchTheme()
    }

    private fun setupView() {
        viewBinding.swipeRefreshLayout.also { layout ->
            layout.setOnRefreshListener {
                layout.isRefreshing = false
                viewModel.fetchCurrency(refresh = true)
            }
        }

        viewBinding.amountInput.also { input ->
            callbackFlow {
                val onTextChangedListener = OnTextChangedListener(::trySend)

                input.addTextChangedListener(onTextChangedListener)
                awaitClose { input.removeTextChangedListener(onTextChangedListener) }
            }
                .onEach { textAmount ->
                    if (textAmount.isBlank()) viewModel.clearCurrencyRate()
                    viewModel.updateAmount(amount = textAmount.toDoubleOrNull() ?: -1.0)
                }
                .filter(String::isNotBlank)
                .debounce { 250L }
                .onEach { textAmount ->
                    viewModel.fetchCurrencyRates(
                        amount = textAmount.toDouble(),
                        code = (viewBinding.currencySpinner.selectedItem as? String).orEmpty(),
                        page = 1,
                        limit = RATE_LIMIT,
                    )
                }
                .launchIn(scope = lifecycleScope)
        }

        viewBinding.listView.also { view ->
            view.adapter = adapter
        }

        viewBinding.emptyStateLayout.also { layout ->
            layout.actionButton.also { button ->
                button.setOnClickListener {
                    viewModel.fetchCurrency(refresh = true)
                }
            }
        }
    }

    private fun render(state: CurrencyRateState) {
        handleErrorMessage(state)
        handleReloadRate(state)

        renderEmptyState(state)
        renderAmountInput(state)
        renderCurrencySelector(state)
        renderCurrencyRate(state)
        renderThemeButton(state)
    }

    private fun handleErrorMessage(state: CurrencyRateState) {
        val message = state.errorMessage ?: return
        messenger.displaySnackBar(view = viewBinding.root, message)
        viewModel.clearErrorMessage()
    }

    private fun handleReloadRate(state: CurrencyRateState) {
        if (!state.reloadCurrencyRate) return

        if (viewBinding.amountInput.text.isNullOrBlank()) return
        if (viewBinding.currencySpinner.selectedItemId < 0) return

        viewModel.fetchCurrencyRates(
            amount = viewBinding.amountInput.text.toString().toDouble(),
            code = (viewBinding.currencySpinner.selectedItem as? String).orEmpty(),
            page = 1,
            limit = RATE_LIMIT,
        )
        viewModel.clearReloadRate()
    }

    private fun renderEmptyState(state: CurrencyRateState) {
        when (state.currencyResponse) {
            is Response.Loading -> {
                if (viewBinding.currencySpinner.adapter == null) {
                    viewBinding.loadingBar.show()
                }
                viewBinding.emptyStateLayout.root.visibility = View.GONE
            }
            is Response.Error -> {
                viewBinding.loadingBar.hide()

                viewBinding.emptyStateLayout.also { layout ->
                    layout.descriptionText.also { view ->
                        view.text = state.currencyResponse.message
                    }

                    layout.actionButton.also { button ->
                        button.text = getString(R.string.try_again)
                    }

                    if (viewBinding.currencySpinner.adapter == null) {
                        layout.root.visibility = View.VISIBLE
                    } else {
                        layout.root.visibility = View.GONE
                    }
                }

                if (viewBinding.currencySpinner.adapter == null) {
                    viewBinding.amountContainer.visibility = View.GONE
                    viewBinding.listView.visibility = View.GONE
                }
            }
            else -> {
                viewBinding.loadingBar.hide()
                viewBinding.emptyStateLayout.root.visibility = View.GONE
                viewBinding.amountContainer.visibility = View.VISIBLE
                viewBinding.listView.visibility = View.VISIBLE
            }
        }
    }

    private fun renderAmountInput(state: CurrencyRateState) {
        if (state.amount < 0) return
        val textAmount = "${state.amount}"

        if (textAmount.isEmpty()) {
            viewBinding.amountInput.setText("")
            return
        }

        val textForm = "${viewBinding.amountInput.text}".toDoubleOrNull().toString()
        if (textForm != textAmount) {
            viewBinding.amountInput.setText(textAmount)
        }
    }

    private fun renderCurrencySelector(state: CurrencyRateState) {
        viewBinding.currencySpinner.also { spinner ->
            if (state.currencyResponse !is Response.Success) return@also

            val currencies = state.currencyResponse.data.map { currency ->
                currency.code
            }
            val sourceIndex = currencies.indexOf(state.source.code)

            val adapter = ArrayAdapter(
                this,
                R.layout.item_currency,
                currencies.toTypedArray()
            )

            spinner.adapter = adapter
            spinner.onItemSelectedListener = OnSpinnerItemSelectedListener { index ->
                val selectedCurrency = currencies[index]
                if (selectedCurrency == state.source.code) return@OnSpinnerItemSelectedListener

                viewModel.changeCurrency(code = selectedCurrency)
                if (viewBinding.amountInput.text?.isNotBlank() == true) {
                    viewModel.fetchCurrencyRates(
                        amount = viewBinding.amountInput.text.toString().toDouble(),
                        code = selectedCurrency,
                        page = 1,
                        limit = RATE_LIMIT,
                    )
                }
            }

            if (sourceIndex != spinner.selectedItemPosition) {
                spinner.setSelection(sourceIndex)
            }
        }
    }

    private fun renderCurrencyRate(state: CurrencyRateState) {
        if (state.currencyRateResponse.isEmpty()) {
            adapter.submitList(listOf())
            return
        }

        state.currencyRateResponse.forEach { response ->
            when (response) {
                is Response.Loading, is Response.Error, is Response.Empty -> {
                    if (state.currencyRateResponse.size <= 1) {
                        adapter.submitList(listOf())
                    }
                }
                is Response.Success -> {
                    adapter.submitList(response.data)
                }
            }
        }
    }

    private fun renderThemeButton(state: CurrencyRateState) {
        viewBinding.themeButton.also { button ->
            button.setOnClickListener {
                val newTheme = if (state.theme == 0) 1 else 0
                setDefaultNightMode(MODE_NIGHT_YES.takeIf { newTheme == 1 } ?: MODE_NIGHT_NO)

                viewModel.changeTheme(newTheme)
            }
        }
    }

    private companion object {
        const val RATE_LIMIT: Int = 15

        const val CURRENCY_DETAIL_RESULT_CODE: Int = 42
    }
}
