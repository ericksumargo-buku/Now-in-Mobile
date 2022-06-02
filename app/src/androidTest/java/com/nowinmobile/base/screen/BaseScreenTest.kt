package com.nowinmobile.base.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import com.nowinmobile.base.database.api.DatabaseManager
import com.nowinmobile.base.pin.api.Pin
import com.nowinmobile.base.remote.api.RemoteServer
import com.nowinmobile.base.utils.toBundle
import com.nowinmobile.lib.neuro.api.Neuro
import com.nowinmobile.lib.neuro.api.SignalHandler
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Provider
import androidx.test.core.app.launchActivity as launchActivityX

abstract class BaseScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Inject
    lateinit var remoteServer: RemoteServer

    @Inject
    lateinit var databaseManager: DatabaseManager

    @Inject
    lateinit var pin: Pin

    @Inject
    lateinit var neuro: Neuro

    @Inject
    lateinit var signalHandlers: Provider<Set<SignalHandler>>

    @Before
    fun configure() {
        hiltRule.inject()
        neuro.register(signalHandlers = signalHandlers.get().toList())
    }

    fun test(block: suspend TestScope.() -> Unit) = runTest { block() }

    inline fun <reified T : Activity> launchActivity(
        arguments: Map<String, Any> = mapOf(),
        noinline action: ((activity: T) -> Unit)? = null,
    ) {
        val intent = Intent(context, T::class.java).apply {
            putExtras(arguments.toBundle())
        }

        if (action == null) {
            launchActivityX<T>(intent)
        } else {
            launchActivityX<T>(intent).use { scenario ->
                scenario.onActivity { activity ->
                    action(activity)
                }
            }
        }
    }

    inline fun <reified T : Fragment> launchFragment(
        arguments: Map<String, Any> = mapOf(),
        crossinline action: (fragment: T) -> Unit = {},
    ) = launchFragmentInHiltContainer<T>(arguments.toBundle()) { action(this as T) }

    fun TestScope.await(timeMillis: Long = -1) {
        if (timeMillis < 0) advanceUntilIdle()
        else advanceTimeBy(timeMillis)
    }

    @After
    fun tearDown() {
        runBlocking {
            pin.clear()
        }
    }
}
