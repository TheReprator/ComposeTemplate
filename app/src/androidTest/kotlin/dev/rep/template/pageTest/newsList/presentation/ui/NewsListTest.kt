package dev.rep.template.pageTest.newsList.presentation.ui

import dev.rep.template.R
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.presentation.NewsListState
import dev.rep.template.features.newsList.presentation.ui.NewsListScreen
import org.junit.Rule
import org.junit.Test

class NewsListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun showsLoader_whenLoading() {
        val state = NewsListState.initial().copy(newsLoading = true)

        composeTestRule.setContent {
            NewsListScreen(
                state = state,
                newsItemClick = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithTag(context.getString(R.string.testTag_loader)).assertIsDisplayed()
    }

    @Test
    fun showsRetry_whenError() {
        val state = NewsListState.initial().copy(newsLoading = false, isError = true)

        composeTestRule.setContent {
            NewsListScreen(
                state = state,
                newsItemClick = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.app_retry)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.app_error_generic)).assertIsDisplayed()
    }

    @Test
    fun showsList_whenDataAvailable() {

        val fakeNews = FAKE_NEWS_LIST
        val state = NewsListState.initial().copy(newsLoading = false, isError = false, newsList = fakeNews)

        composeTestRule.setContent {
            NewsListScreen(
                state = state,
                newsItemClick = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("$FAKE_NEW_TITLE 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("$FAKE_NEW_TITLE 1").assertIsDisplayed()
    }

    @Test
    fun clickingNewsItem_invokesCallback() {
        val fakeNews = FAKE_NEWS_LIST
        var clicked: NewsModel? = null

        val state = NewsListState.initial().copy(newsList = fakeNews)

        composeTestRule.setContent {
            NewsListScreen(
                state = state,
                newsItemClick = { clicked = it },
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("$FAKE_NEW_TITLE 0").performClick()

        assert(clicked?.id == "0")
    }

    companion object {
        private val FAKE_NEW_TITLE = "Clickable Title"
        private val FAKE_NEWS_MODEL = NewsModel("","","",
            "","","")

        private val FAKE_NEWS_LIST: List<NewsModel> = buildList {
            repeat(10) {
                add(FAKE_NEWS_MODEL.copy(id = it.toString(), title = "$FAKE_NEW_TITLE $it"))
            }
        }
    }
}
