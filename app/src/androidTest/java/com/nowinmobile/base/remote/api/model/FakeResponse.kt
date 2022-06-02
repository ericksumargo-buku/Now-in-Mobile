package com.nowinmobile.base.remote.api.model

data class FakeResponse(
    val method: String,
    val path: String,
    val body: String,
    val code: Int,
)
