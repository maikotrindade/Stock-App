package io.github.maikotrindade.stockapp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.github.maikotrindade.stockapp.ui.MainActivity
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
internal class SearchUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun shouldSearchByTicker(): Unit = with(composeTestRule) {
        val ticker = "TNET"
        val expectedCompanyName = "Fusion Inc"

        onNodeWithText("Stocks").assertIsDisplayed()

        onNodeWithContentDescription("search")
            .performClick()

        onNodeWithText("Search stocks…")
            .performTextInput(ticker)

        waitUntilAtLeastOneExists(hasText(expectedCompanyName), 10000)

        onNodeWithText(expectedCompanyName)
            .performClick()

        onNodeWithText("Stock Details")
            .assertIsDisplayed()

        onNodeWithText("Company Name: $expectedCompanyName")
            .assertIsDisplayed()
    }

    @Test
    fun shouldSearchByPartialCompanyName(): Unit = with(composeTestRule) {
        val partialCompanyName = "Omni"
        val companyName = "Omni Industries"
        val expectedTicker = "GZRQ"

        onNodeWithText("Stocks")
            .assertIsDisplayed()

        onNodeWithContentDescription("search")
            .performClick()

        onNodeWithText("Search stocks…")
            .performTextInput(partialCompanyName)

        waitUntilAtLeastOneExists(hasText(expectedTicker), 10000)

        onNodeWithText(expectedTicker)
            .performClick()

        onNodeWithText("Stock Details")
            .assertIsDisplayed()

        onNodeWithText("Company Name: $companyName")
            .assertIsDisplayed()
    }
}