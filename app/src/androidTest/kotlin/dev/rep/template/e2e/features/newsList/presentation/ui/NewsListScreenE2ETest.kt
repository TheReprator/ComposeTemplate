package dev.rep.template.e2e.features.newsList.presentation.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.rep.template.R
import dev.rep.template.root.MainActivity
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsListScreenE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun newsList_displaysDataFromMockEngine_whenApiCallSuccessful() = runTest {
        composeTestRule.waitUntil(timeoutMillis = TIMEOUT_MILLIS) {
            composeTestRule.onAllNodesWithTag(context.getString(R.string.testTag_loader))
                .fetchSemanticsNodes().isEmpty()
        }

        composeTestRule.onNodeWithText(RESPONSE_FIRST_ITEM_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithText(RESPONSE_SECOND_ITEM_TITLE).assertIsDisplayed()

        composeTestRule.onNodeWithTag(context.getString(R.string.testTag_loader)).assertDoesNotExist()
    }

    @Test
    fun newsList_showsLoader_whileFetching() = runTest {

        composeTestRule.onNodeWithTag(context.getString(R.string.testTag_loader)).assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = TIMEOUT_MILLIS) {
            composeTestRule.onAllNodesWithText(RESPONSE_FIRST_ITEM_TITLE)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun newsList_displaysError_whenApiCallFails() = runTest {
        composeTestRule.waitUntil(timeoutMillis = TIMEOUT_MILLIS) {
            composeTestRule.onAllNodesWithText(context.getString(R.string.app_retry), useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(context.getString(R.string.app_retry)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.app_error_generic)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.testTag_loader)).assertDoesNotExist()
    }

    @Test
    fun clickingNewsItem_navigatesToDetailScreen() = runTest {
        composeTestRule.waitUntil(timeoutMillis = TIMEOUT_MILLIS) {
            composeTestRule.onAllNodesWithText(RESPONSE_FIRST_ITEM_TITLE)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(RESPONSE_FIRST_ITEM_TITLE).performClick()

        composeTestRule.onNodeWithText(RESPONSE_FIRST_ITEM_TITLE, substring = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(context.getString(R.string.testTag_newsDetail)).assertIsDisplayed()
    }
    
    companion object {
        private const val TIMEOUT_MILLIS = 5000L
        private const val RESPONSE_FIRST_ITEM_TITLE = "These Transcribing Eyeglasses Put Subtitles on the World"
        private const val RESPONSE_SECOND_ITEM_TITLE = "The World Is Producing More Food than Ever—but Not for Long"
    }
}