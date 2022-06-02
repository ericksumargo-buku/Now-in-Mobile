package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.SignalHandler

internal data class Node(
    val edges: MutableMap<String, Node>,
    var signalHandler: SignalHandler? = null,
) {
    companion object {
        val default: Node
            get() = Node(
                edges = mutableMapOf(),
                signalHandler = null,
            )
    }
}