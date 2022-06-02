package com.nowinmobile.feature.currency.detail.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.nowinmobile.base.screen.BaseScreenTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class CurrencyDetailActivityTest : BaseScreenTest() {
    @Test
    fun target_currency_displayed() = test {
        // when
        val data = mapOf("code" to "ERICK")
        launchActivity<CurrencyDetailActivity>(data)

        // then
        onView(withText("ERICK"))
            .check(matches(isDisplayed()))
    }
}
