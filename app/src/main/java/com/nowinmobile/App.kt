package com.nowinmobile

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.nowinmobile.data.app.api.AppRepository
import com.nowinmobile.lib.neuro.api.Neuro
import com.nowinmobile.lib.neuro.api.SignalHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

open class App : Application() {
    @Inject
    lateinit var appRepository: Provider<AppRepository>

    @Inject
    lateinit var neuro: Provider<Neuro>

    @Inject
    lateinit var signalHandlers: Provider<Set<SignalHandler>>

    private val appScope: CoroutineScope =
        CoroutineScope(context = SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            configureTheme()
            configureNeuro()
        }
    }

    private suspend fun configureTheme() {
        val theme = appRepository.get().retrieveTheme()
        setDefaultNightMode(MODE_NIGHT_YES.takeIf { theme == 1 } ?: MODE_NIGHT_NO)
    }

    private fun configureNeuro() {
        neuro.get().register(signalHandlers.get().toList())
    }
}
