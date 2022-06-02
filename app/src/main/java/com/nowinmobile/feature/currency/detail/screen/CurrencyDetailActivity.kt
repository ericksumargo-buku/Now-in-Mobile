package com.nowinmobile.feature.currency.detail.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.nowinmobile.R
import com.nowinmobile.base.udf.api.screen.UdfActivity
import com.nowinmobile.base.udf.api.screen.viewBinding
import com.nowinmobile.base.udf.api.state.observeState
import com.nowinmobile.databinding.ActivityCurrencyDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyDetailActivity : UdfActivity() {
    private val viewBinding: ActivityCurrencyDetailBinding by viewBinding()

    private val viewModel: CurrencyDetailViewModel by viewModels()

    private var resultCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        setupView()
        observeState(source = viewModel.stateFlow, action = ::render)

        resultCode = intent.getIntExtra("resultCode", -1)

        val code = intent.getStringExtra("code").orEmpty()
        viewModel.updateSource(code)
    }

    private fun setupView() {
        viewBinding.toolbar.also { toolbar ->
            toolbar.isTitleCentered = true

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener { closeScreen() }
        }
    }

    private fun render(state: CurrencyDetailState) {
        renderToolbar(state)
    }

    private fun renderToolbar(state: CurrencyDetailState) {
        viewBinding.toolbar.also { toolbar ->
            toolbar.title = state.source.code
        }
    }

    private fun closeScreen() {
        val data = Intent()
        setResult(resultCode, data)
        finish()
    }
}
