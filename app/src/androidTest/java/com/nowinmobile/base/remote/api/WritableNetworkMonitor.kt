package com.nowinmobile.base.remote.api

interface WritableNetworkMonitor : NetworkMonitor {
    /** Enable internet connection for test fixture. */
    fun enableNetwork()

    /** Disable internet connection for test fixture. */
    fun disableNetwork()
}
