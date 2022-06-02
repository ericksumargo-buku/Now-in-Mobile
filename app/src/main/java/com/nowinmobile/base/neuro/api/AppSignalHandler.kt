package com.nowinmobile.base.neuro.api

import com.nowinmobile.lib.neuro.api.SignalHandler

abstract class AppSignalHandler : SignalHandler {
    override val schemes: Set<String> = setOf(INTERNAL_SCHEME)

    override val hosts: Set<String> = setOf(INTERNAL_HOST)
}
