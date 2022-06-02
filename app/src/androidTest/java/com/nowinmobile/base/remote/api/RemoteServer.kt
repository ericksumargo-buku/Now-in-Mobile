package com.nowinmobile.base.remote.api

import com.nowinmobile.base.remote.api.model.FakeResponse

interface RemoteServer {
    val responsePool: Map<String, List<FakeResponse>>

    /** Mock internal API [responses] with [FakeResponse] as the contract for test fixture. */
    fun submit(responses: List<FakeResponse>)
}
