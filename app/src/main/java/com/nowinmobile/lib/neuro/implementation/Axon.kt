package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.SignalHandler

internal class Axon(private val ranvier: Ranvier) {
    private val root: Node = Node.default

    fun initialize(signalHandlers: List<SignalHandler>) {
        for (signalHandler in signalHandlers) {
            buildNode(signalHandler)
        }
    }

    private fun buildNode(signalHandler: SignalHandler) {
        for (path in signalHandler.paths) {
            var node = root

            val segments = path
                .trim(Char::isWhitespace)
                .split('/')
                .filter(String::isNotBlank)

            for (segment in segments) {
                val normalized = segment.trim(Char::isWhitespace)
                val child = node.edges[normalized] ?: Node.default

                node.edges[normalized] = child
                node = child
            }

            node.signalHandler = signalHandler
        }
    }

    fun transmit(impulse: Impulse) {
        return ranvier.transmit(root, impulse)
    }
}