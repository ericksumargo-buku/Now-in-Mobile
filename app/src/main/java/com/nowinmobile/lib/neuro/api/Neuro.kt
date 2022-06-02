package com.nowinmobile.lib.neuro.api

interface Neuro {
    fun register(signalHandlers: List<SignalHandler>)

    fun direct(
        source: SourceLink,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit,
    )
}