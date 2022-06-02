package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.Neuro
import com.nowinmobile.lib.neuro.api.SignalHandler
import com.nowinmobile.lib.neuro.api.SourceLink

class DefaultNeuro : Neuro {
    private val nucleus: Nucleus by lazy {
        Nucleus(
            soma = Soma(
                axon = Axon(
                    ranvier = Ranvier()
                ),
                uriParser = uriParser,
            )
        )
    }

    internal var uriParser: UriParser = DefaultUriParser()

    override fun register(signalHandlers: List<SignalHandler>) {
        nucleus.register(signalHandlers)
    }

    override fun direct(
        source: SourceLink,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        nucleus.process(source, onSuccess, onFailure)
    }
}