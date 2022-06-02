package com.nowinmobile.base.udf.api.state

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.nowinmobile.base.udf.implementation.state.DefaultStateManager
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

@HiltAndroidTest
class StateManagerTest {
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var stateManager: StateManager<Int>

    private val key: String
        get() = Integer::class.java.canonicalName.orEmpty()

    @Test
    fun initial_state_emission() = runTest {
        // given
        savedStateHandle = SavedStateHandle()
        stateManager = DefaultStateManager(0, savedStateHandle)

        // then
        stateManager.stateFlow.test {
            val state = awaitItem()
            assert(state == 0)
        }
    }

    @Test
    fun initial_state_emission_with_old_state_cache() = runTest {
        // given
        savedStateHandle = SavedStateHandle().apply {
            set(key, bundleOf("state" to 100))
        }
        stateManager = DefaultStateManager(0, savedStateHandle)

        // then
        stateManager.stateFlow.test {
            val state = awaitItem()
            assert(state == 100)
        }
    }

    @Test
    fun new_state_emission() = runTest {
        // given
        savedStateHandle = SavedStateHandle()
        stateManager = DefaultStateManager(0, savedStateHandle)

        // then
        stateManager.stateFlow.test {
            val state = awaitItem()
            assert(state == 0)

            repeat(100) { i ->
                stateManager.produce { state ->
                    state + 1
                }

                val newState = awaitItem()
                assert(newState == i + 1)
            }
        }
    }
}