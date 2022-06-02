package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.SignalHandler
import com.nowinmobile.lib.neuro.api.SourceLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

internal class Nucleus(private val soma: Soma) {
    private val isInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val deferredRequest: MutableStateFlow<Request?> = MutableStateFlow(null)

    fun register(signalHandlers: List<SignalHandler>) {
        lockProcess { soma.initialize(signalHandlers) }
        processDeferredRequest()
    }

    private inline fun lockProcess(action: () -> Unit) {
        isInitialized.update { false }
        action()
        isInitialized.update { true }
    }

    private fun processDeferredRequest() {
        val request = deferredRequest.getAndUpdate { null } ?: return
        process(
            source = request.source,
            onSuccess = request.onSuccess,
            onFailure = request.onFailure,
        )
    }

    fun process(
        source: SourceLink,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        val isInitialized = isInitialized.getAndUpdate { it }
        if (!isInitialized) {
            val request = Request(source, onSuccess, onFailure)
            deferredRequest.update { request }
            return
        }

        try {
            soma.process(source)
            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }
}