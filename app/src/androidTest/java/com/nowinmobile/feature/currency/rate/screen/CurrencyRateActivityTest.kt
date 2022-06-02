package com.nowinmobile.feature.currency.rate.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.nowinmobile.R
import com.nowinmobile.base.messenger.api.MessageRecorder
import com.nowinmobile.base.remote.api.model.FakeResponse
import com.nowinmobile.base.screen.BaseScreenTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertTrue
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CurrencyRateActivityTest : BaseScreenTest() {
    @Inject
    lateinit var messageRecorder: MessageRecorder

    @Test
    fun selected_currency_displayed() = test {
        // given
        val responses = listOf(
            FakeResponse(
                method = "GET",
                path = "/live",
                body = """
                {
                    "quotes": {
                        "USDABC": 1.0
                    }
                }
                """.trimIndent(),
                code = 200,
            )
        )
        remoteServer.submit(responses)

        // when
        launchActivity<CurrencyRateActivity>()

        onView(
            allOf(
                withId(R.id.currencySpinner),
                withSpinnerText(containsString("ABC")),
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun currency_rate_displayed() = test {
        // given
        val responses = listOf(
            FakeResponse(
                method = "GET",
                path = "/live",
                body = """
                {
                    "quotes": {
                        "USDABC": 1.0,
                        "USDDEF": 2.0,
                        "USDGHI": 3.0
                    }
                }
                """.trimIndent(),
                code = 200,
            )
        )
        remoteServer.submit(responses)

        // when
        launchActivity<CurrencyRateActivity>()

        onView(withId(R.id.amountInput))
            .perform(typeText("1"))

        // then
        onView(withId(R.id.listView))
            .check(matches(hasChildCount(3)))
    }

    @Test
    fun currency_detail_navigation() = test {
        // given
        val responses = listOf(
            FakeResponse(
                method = "GET",
                path = "/live",
                body = """
                {
                    "quotes": {
                        "USDABC": 1.0,
                        "USDDEF": 2.0,
                        "USDGHI": 3.0
                    }
                }
                """.trimIndent(),
                code = 200,
            )
        )
        remoteServer.submit(responses)

        // when
        launchActivity<CurrencyRateActivity>()

        onView(withId(R.id.amountInput))
            .perform(typeText("1"))

        // then
        onView(withText("DEF"))
            .perform(click())

        onView(withText("DEF"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun error_message_displayed() = test {
        // given
        val responses = listOf(
            FakeResponse(
                method = "GET",
                path = "/live",
                body = """
                {
                    "error": {
                        "code": 100,
                        "type": "invalid_request",
                        "info": "Internal error"   
                    }
                }
                """.trimIndent(),
                code = 200,
            )
        )
        remoteServer.submit(responses)

        // when
        launchActivity<CurrencyRateActivity>()

        // then
        onView(withText("Internal error"))
            .check(matches(isDisplayed()))

        val hasMessage = messageRecorder.hasSnackBarMessage(message = "Internal error")
        assertTrue(hasMessage)
    }
}
