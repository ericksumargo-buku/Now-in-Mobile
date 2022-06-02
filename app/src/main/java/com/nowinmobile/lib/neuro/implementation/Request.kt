package com.nowinmobile.lib.neuro.implementation

import com.nowinmobile.lib.neuro.api.SourceLink

internal data class Request(
    val source: SourceLink,
    val onSuccess: () -> Unit,
    val onFailure: (Throwable) -> Unit,
)