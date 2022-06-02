package com.nowinmobile.base.remote.implementation

import com.nowinmobile.base.remote.api.WritableNetworkMonitor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultWritableNetworkMonitor @Inject constructor() : WritableNetworkMonitor {
    override var isConnected: Boolean = true

    override fun enableNetwork() {
        isConnected = true
    }

    override fun disableNetwork() {
        isConnected = false
    }
}
