package com.nowinmobile

import android.os.StrictMode
import android.os.StrictMode.setThreadPolicy
import android.os.StrictMode.setVmPolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DebugApp : App() {
    override fun onCreate() {
        setStrictMode()
        super.onCreate()
    }

    private fun setStrictMode() {
        setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )

        setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }
}
