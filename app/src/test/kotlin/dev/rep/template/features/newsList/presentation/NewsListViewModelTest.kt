package dev.rep.template.features.newsList.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCase
import dev.rep.template.features.newsList.presentation.inputProvider.MockFetchNewsSuccessUseCase.Companion.newsList
import dev.rep.template.features.newsList.util.MainDispatcherRule
import dev.rep.template.util.AppError
import dev.rep.template.util.AppSuccess
import dev.rep.template.util.wrapper.AppCoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedStateHandle = SavedStateHandle()

    private val fetchNewsUseCase = mock<FetchNewsUseCase>()

    val dispatchers = AppCoroutineDispatchers(
        mainDispatcherRule.testDispatcher,
        mainDispatcherRule.testDispatcher,
        mainDispatcherRule.testDispatcher,
        mainDispatcherRule.testDispatcher,
        mainDispatcherRule.testDispatcher
    )

    private lateinit var newsListViewModel: NewsListViewModel

    @Before
    fun setup() {
        newsListViewModel = NewsListViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = dispatchers,
            newsUseCase = fetchNewsUseCase
        )
    }

    @Test
    fun `Initial setup` () = runTest {
        newsListViewModel.sideEffect.test {
            expectNoEvents()
        }

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()
            expectNoEvents()
        }
    }

    @Test
    fun `fetch news successfully from server on Action FetchNews` () = runTest {
        everySuspend {
            fetchNewsUseCase()
        } returns AppSuccess(newsList)

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()

            newsListViewModel.onAction(NewsListAction.FetchNews)

            val nextItem = awaitItem()
            assert(nextItem.newsList.isNotEmpty())
            assert(nextItem.newsList.size == newsList.size)
            assert(nextItem.errorMessage.isEmpty())
            assert(!nextItem.isError)
            assert(!nextItem.newsLoading)

            expectNoEvents()
        }
    }

    @Test
    fun `fetch news failed from server on Action FetchNews` () = runTest {
        val errorMessage = "Failed to fetch news"

        everySuspend {
            fetchNewsUseCase()
        } returns AppError(message = errorMessage)

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()

            newsListViewModel.onAction(NewsListAction.FetchNews)

            val nextItem = awaitItem()
            assert(nextItem.newsList.isEmpty())
            assert(nextItem.errorMessage.isNotEmpty())
            assert(nextItem.errorMessage == errorMessage)
            assert(nextItem.isError)
            assert(!nextItem.newsLoading)

            expectNoEvents()
        }
    }

    @Test
    fun `Navigate to detail on Action NavigateToDetail via side effect` () = runTest {
        val clickedItem = newsList.first()

        everySuspend {
            fetchNewsUseCase()
        } returns AppSuccess(newsList)

        newsListViewModel.onAction(NewsListAction.FetchNews)
        advanceUntilIdle()

        newsListViewModel.onAction(NewsListAction.NavigateToDetail(clickedItem))

        newsListViewModel.sideEffect.test {
            val nextItem = awaitItem()
            assert(nextItem is NewsListEffect.NavigateToDetail)
            assert((nextItem as NewsListEffect.NavigateToDetail).newsModel == clickedItem)

            expectNoEvents()
        }
    }
}

