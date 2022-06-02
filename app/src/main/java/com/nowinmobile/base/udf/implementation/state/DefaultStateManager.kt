package com.nowinmobile.base.udf.implementation.state

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import com.nowinmobile.base.udf.api.state.StateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate

class DefaultStateManager<S : Any>(
    initState: S,
    private val savedStateHandle: SavedStateHandle,
) : StateManager<S> {
    private val key: String = initState::class.java.canonicalName.orEmpty()

    private val mutableStateFlow: MutableStateFlow<S> = MutableStateFlow(
        restoreState() ?: initState
    )

    override val stateFlow: Flow<S> get() = mutableStateFlow

    private fun restoreState(): S? {
        configureSavedStateProvider()
        return savedStateHandle.get<Bundle>(key)?.run {
            get("state") as? S
        }
    }

    override fun produce(reduce: (S) -> S) {
        mutableStateFlow.getAndUpdate { reduce(it) }
    }

    private fun configureSavedStateProvider() {
        savedStateHandle.setSavedStateProvider(key) {
            try {
                bundleOf("state" to mutableStateFlow.value)
            } catch (cause: Exception) {
                bundleOf()
            }
        }
    }
}
