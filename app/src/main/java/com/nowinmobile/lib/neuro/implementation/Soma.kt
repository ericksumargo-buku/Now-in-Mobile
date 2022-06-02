package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.SignalHandler
import com.nowinmobile.lib.neuro.api.SourceLink

internal class Soma(
    private val axon: Axon,
    private val uriParser: UriParser,
) {
    fun initialize(signalHandlers: List<SignalHandler>) {
        axon.initialize(signalHandlers)
    }

    fun process(source: SourceLink) {
        val impulse = createImpulse(source)
        return axon.transmit(impulse)
    }

    private fun createImpulse(source: SourceLink): Impulse {
        val context = source.context
        val data = uriParser(source.link)

        val scheme = checkNotNull(data.scheme) { "scheme must not be null" }
        val host = checkNotNull(data.host) { "host must not be null" }

        val paths = data.path
            .orEmpty()
            .trim('/')
            .split('/')
            .dropWhile(String::isBlank)

        val query = data.query
            .orEmpty()
            .split('&')
            .filter(String::isNotBlank)
            .associate(::mapQuery)

        return Impulse(context, scheme, host, paths, query)
    }

    private fun mapQuery(query: String): Pair<String, Any> {
        val splitIndex = query.indexOf('=')
        val key = query.take(splitIndex)
        val value = query.takeLast(query.length - splitIndex - 1).mapType()

        return key to value
    }
}