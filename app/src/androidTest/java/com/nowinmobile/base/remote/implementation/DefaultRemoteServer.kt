package com.nowinmobile.base.remote.implementation

import com.nowinmobile.base.remote.api.RemoteServer
import com.nowinmobile.base.remote.api.model.FakeResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRemoteServer @Inject constructor() : RemoteServer {
    private val mutableResponsePool: MutableMap<String, MutableList<FakeResponse>> = mutableMapOf()

    override val responsePool: Map<String, List<FakeResponse>>
        get() = mutableResponsePool

    override fun submit(responses: List<FakeResponse>) {
        responses.forEach { response ->
            val method = response.method.uppercase()
            val path = response.path.trim()
            val key = method + "+" + "/".takeIf { path.first() != '/' }.orEmpty() + path

            val pool = mutableResponsePool[key] ?: mutableListOf()
            pool.add(response)
            mutableResponsePool[key] = pool
        }
    }
}
