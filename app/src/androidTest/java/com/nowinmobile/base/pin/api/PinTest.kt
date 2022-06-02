package com.nowinmobile.base.pin.api

import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.nowinmobile.base.pin.implementation.DataStorePin
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class PinTest {
    private lateinit var pin: Pin

    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        pin = DataStorePin(
            name = "dummy",
            context = ApplicationProvider.getApplicationContext(),
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun save_functionality() = runTest(testDispatcher) {
        // given
        val key1 = "key1"
        val value1 = 123

        val key2 = "key2"
        val value2 = 123L

        val key3 = "key3"
        val value3 = 123.0

        val key4 = "key4"
        val value4 = "123"

        val key5 = "key5"
        val value5 = true

        // when
        pin.save(key1, value1)
        pin.save(key2, value2)
        pin.save(key3, value3)
        pin.save(key4, value4)
        pin.save(key5, value5)

        // then
        val result1 = pin.read(key1, 0)
        assert(result1 == 123)

        val result2 = pin.read(key2, 0L)
        assert(result2 == 123L)

        val result3 = pin.read(key3, 0.0)
        assert(result3 == 123.0)

        val result4 = pin.read(key4, "")
        assert(result4 == "123")

        val result5 = pin.read(key5, false)
        assert(result5)
    }

    @Test
    fun save_functionality_with_overwrite() = runTest(testDispatcher) {
        // given
        val key = "key"
        val value = 123
        val value2 = "123"

        // when
        pin.save(key, value)
        pin.save(key, value2)

        // then
        val result = pin.read(key, "")
        assert(result == "123")
    }

    @Test
    fun read_functionality_with_invalid_cast() = runTest(testDispatcher) {
        // given
        val key = "key"
        val value = 123

        // when
        pin.save(key, value)

        // then
        val result = pin.read(key, "")
        assert(result.isEmpty())
    }

    @Test
    fun observe_functionality() = runTest(testDispatcher) {
        // given
        val key1 = "key1"
        val defaultValue1 = 0

        val key2 = "key2"
        val defaultValue2 = 0L

        val key3 = "key3"
        val defaultValue3 = 0.0

        val key4 = "key4"
        val defaultValue4 = ""

        val key5 = "key5"
        val defaultValue5 = false

        // then
        pin.observe(key1, defaultValue1).test {
            assert(awaitItem() == 0)

            pin.save(key1, 1)
            assert(awaitItem() == 1)

            pin.save(key1, 2)
            assert(awaitItem() == 2)

            pin.save(key1, 3)
            assert(awaitItem() == 3)
        }

        pin.observe(key2, defaultValue2).test {
            assert(awaitItem() == 0L)

            pin.save(key2, 1L)
            assert(awaitItem() == 1L)

            pin.save(key2, 2L)
            assert(awaitItem() == 2L)

            pin.save(key2, 3L)
            assert(awaitItem() == 3L)
        }

        pin.observe(key3, defaultValue3).test {
            assert(awaitItem() == 0.0)

            pin.save(key3, 1.0)
            assert(awaitItem() == 1.0)

            pin.save(key3, 2.0)
            assert(awaitItem() == 2.0)

            pin.save(key3, 3.0)
            assert(awaitItem() == 3.0)
        }

        pin.observe(key4, defaultValue4).test {
            assert(awaitItem().isEmpty())

            pin.save(key4, "1")
            assert(awaitItem() == "1")

            pin.save(key4, "2")
            assert(awaitItem() == "2")

            pin.save(key4, "3")
            assert(awaitItem() == "3")
        }

        pin.observe(key5, defaultValue5).test {
            assert(!awaitItem())

            pin.save(key5, true)
            assert(awaitItem())

            pin.save(key5, false)
            assert(!awaitItem())

            pin.save(key5, true)
            assert(awaitItem())
        }
    }

    @Test
    fun clear_functionality() = runTest(testDispatcher) {
        // given
        val key = "key"
        val value = 123

        // when
        pin.save(key, value)
        pin.clear()

        // then
        val result = pin.read(key, 0)
        assert(result == 0)
    }

    @After
    fun tearDown() {
        runBlocking {
            pin.clear()
        }
    }
}
