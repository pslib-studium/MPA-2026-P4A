package cz.pslib.jetpack

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Tyto testy běží na zařízení a kontrolují UI i obnovu stavu skutečné Activity.
@RunWith(AndroidJUnit4::class)
class CounterInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun initialValueIsZeroAndResetIsDisabled() {
        assertCount(0)
        composeRule.onNodeWithText(resetText()).assertIsNotEnabled()
    }

    @Test
    fun incrementAndResetUpdateTheScreen() {
        increment(3)
        assertCount(3)
        composeRule.onNodeWithText(resetText()).assertIsEnabled().performClick()
        assertCount(0)
        composeRule.onNodeWithText(resetText()).assertIsNotEnabled()
    }

    @Test
    fun countSurvivesActivityRecreation() {
        increment(5)
        // recreate vytvoří Activity znovu s uloženým stavem, stejně jako změna konfigurace.
        composeRule.activityRule.scenario.recreate()
        assertCount(5)
        increment(1)
        assertCount(6)
    }

    @Test
    fun countSurvivesRotation() {
        increment(5)
        composeRule.runOnUiThread {
            composeRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }
        assertCount(5)
        composeRule.runOnUiThread {
            composeRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        }
        assertCount(5)
    }

    private fun increment(times: Int) {
        val text = composeRule.activity.getString(R.string.increment)
        repeat(times) { composeRule.onNodeWithText(text).performClick() }
    }

    private fun assertCount(count: Int) {
        val text = composeRule.activity.getString(R.string.counter_value, count)
        composeRule.onNodeWithText(text).assertExists()
    }

    private fun resetText(): String = composeRule.activity.getString(R.string.reset)
}
