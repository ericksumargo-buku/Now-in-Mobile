package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.Signal
import java.util.LinkedList

internal class Ranvier {
    fun transmit(root: Node, impulse: Impulse) {
        val terminal = checkNotNull(findTerminal(root, impulse)) { "Signal handler is not found" }
        val signal = createSignal(impulse, terminal)

        val signalHandler = terminal.node.signalHandler
        signalHandler?.handle(signal)
    }

    private fun findTerminal(root: Node, impulse: Impulse): Terminal? {
        val queue = LinkedList<Terminal>().also { queue ->
            val terminal = Terminal(
                node = root,
                index = 0,
                parameter = mapOf(),
            )
            queue.add(terminal)
        }

        while (queue.isNotEmpty()) {
            val terminal = queue.poll() ?: continue

            val node = terminal.node
            val signalHandler = node.signalHandler

            val index = terminal.index
            val parameter = terminal.parameter

            if (index == impulse.paths.size
                && signalHandler != null
                && impulse.scheme in signalHandler.schemes.map(String::trim)
                && impulse.host in signalHandler.hosts.map(String::trim)
            ) return terminal

            if (index == impulse.paths.size) continue

            val path = impulse.paths[index]
            val child = node.edges[path]

            if (child != null) {
                val nextTerminal = Terminal(
                    node = child,
                    index = index + 1,
                    parameter = parameter,
                )
                queue.add(nextTerminal)
            } else {
                val candidates = node.edges.filterKeys { key ->
                    key.first() == '<' && key.last() == '>'
                }

                for ((key, vertex) in candidates) {
                    val name = key.filterIndexed { i, c ->
                        (i == 0 && c == '<').not() && (i == key.lastIndex && c == '>').not()
                    }
                    val value = path.mapType()

                    val newParameter = mutableMapOf(name to value)
                    newParameter.putAll(parameter)

                    val nextTerminal = Terminal(
                        node = vertex,
                        index = index + 1,
                        parameter = newParameter,
                    )
                    queue.add(nextTerminal)
                }
            }
        }

        return null
    }

    private fun createSignal(impulse: Impulse, terminal: Terminal): Signal {
        return Signal(
            context = impulse.context,
            scheme = impulse.scheme,
            host = impulse.host,
            parameter = terminal.parameter,
            query = impulse.query,
        )
    }
}