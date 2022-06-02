package com.nowinmobile.base.remote.implementation

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.nowinmobile.base.remote.api.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {
    override val isConnected: Boolean
        get() {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (VERSION.SDK_INT >= VERSION_CODES.M) {
                val network = manager.activeNetwork
                val capabilities = manager.getNetworkCapabilities(network)

                capabilities != null && (capabilities.hasTransport(TRANSPORT_WIFI) || capabilities.hasTransport(TRANSPORT_CELLULAR))
            } else {
                val network = manager.activeNetworkInfo
                network != null && (network.type == TYPE_WIFI || network.type == TYPE_MOBILE)
            }
        }
}
