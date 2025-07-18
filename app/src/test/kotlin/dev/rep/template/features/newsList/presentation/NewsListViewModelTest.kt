package dev.rep.template.features.newsList.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCase
import dev.rep.template.features.newsList.presentation.inputProvider.MockFetchNewsSuccessUseCase.Companion.errorMessage
import dev.rep.template.features.newsList.presentation.inputProvider.MockFetchNewsSuccessUseCase.Companion.successNewsList
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
import kotlin.collections.List

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
            middleWareList = setOf(NewsListMiddleware(fetchNewsUseCase, dispatchers)),
            reducer = NewsListScreenReducer()
        )
    }

    @Test
    fun `Initial setup`() = runTest {
        newsListViewModel.sideEffect.test {
            expectNoEvents()
        }

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()
            expectNoEvents()
        }
    }

    @Test
    fun `fetch news successfully from server on Action FetchNews`() = runTest {
        everySuspend {
              fetchNewsUseCase.invokeQuery()
        } returns AppSuccess<List<NewsModel>>(successNewsList)

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()

            newsListViewModel.onAction(NewsListAction.FetchNews)

            val nextItemLoading = awaitItem()
            with(nextItemLoading){
                assert(newsList.isEmpty())
                assert(errorMessage.isEmpty())
                assert(!isError)
                assert(newsLoading)
            }

            val nextItem = awaitItem()
            with(nextItem) {
                assert(newsList.isNotEmpty())
                assert(newsList.size == newsList.size)
                assert(errorMessage.isEmpty())
                assert(!isError)
                assert(!newsLoading)
            }
            expectNoEvents()
        }
    }

    @Test
    fun `fetch news failed from server on Action FetchNews`() = runTest {

        everySuspend {
              fetchNewsUseCase.invokeQuery()
        } returns AppError(message = errorMessage)

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()

            newsListViewModel.onAction(NewsListAction.FetchNews)

            val nextItemLoading = awaitItem()
            with(nextItemLoading){
                assert(this.newsList.isEmpty())
                assert(this.errorMessage.isEmpty())
                assert(!isError)
                assert(newsLoading)
            }

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
    fun `retry to fetch news failed from server on Action RetryFetchNews`() = runTest {
        everySuspend {
              fetchNewsUseCase.invokeQuery()
        } returns AppError(message = errorMessage)

        newsListViewModel.uiState.test {
            expectMostRecentItem() == NewsListState.initial()

            newsListViewModel.onAction(NewsListAction.FetchNews)
            with(awaitItem()){
                assert(this.newsList.isEmpty())
                assert(this.errorMessage.isEmpty())
                assert(!isError)
                assert(newsLoading)
            }

            with(awaitItem()) {
                assert(newsList.isEmpty())
                assert(errorMessage.isNotEmpty())
                assert(errorMessage == errorMessage)
                assert(isError)
                assert(!newsLoading)
            }

            everySuspend {
                  fetchNewsUseCase.invokeQuery()
            } returns AppSuccess<List<NewsModel>>(successNewsList)

            newsListViewModel.onAction(NewsListAction.RetryFetchNews)
            with(awaitItem()) {
                assert(this.newsList.isEmpty())
                assert(this.errorMessage.isEmpty())
                assert(!isError)
                assert(newsLoading)
            }

            with(awaitItem()) {
                assert(newsList.isNotEmpty())
                assert(newsList.size == newsList.size)
                assert(errorMessage.isEmpty())
                assert(!isError)
                assert(!newsLoading)
            }
            expectNoEvents()
        }
    }

    @Test
    fun `Navigate to detail on Action NavigateToDetail via side effect`() = runTest {
        val clickedItem = successNewsList.first<NewsModel>()

        everySuspend {
              fetchNewsUseCase.invokeQuery()
        } returns AppSuccess<List<NewsModel>>(successNewsList)

        newsListViewModel.onAction(NewsListAction.FetchNews)
        advanceUntilIdle()

        newsListViewModel.onAction(NewsListAction.NewsClicked(clickedItem))

        newsListViewModel.sideEffect.test {
            val nextItem = awaitItem()
            assert(nextItem is NewsListEffect.NavigateToDetail)
            assert((nextItem as NewsListEffect.NavigateToDetail).newsModel == clickedItem)

            expectNoEvents()
        }
    }
}

