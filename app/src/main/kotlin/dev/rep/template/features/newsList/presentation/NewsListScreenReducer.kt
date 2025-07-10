package dev.rep.template.features.newsList.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.rep.template.features.newsList.domain.NewsModel


@Stable
sealed interface NewsListEvent {
    object FetchNews : NewsListEvent
    object RetryFetchNews : NewsListEvent

    data class NavigateToDetail(val newsModel: NewsModel) : NewsListEvent
}

@Stable
sealed interface NewsListEffect {
    data class NavigateToDetail(val newsModel: NewsModel) : NewsListEffect
    data class ShowError(val message: String) : NewsListEffect
}

@Immutable
data class NewsListState(
    val newsLoading: Boolean,
    val isError: Boolean,
    val errorMessage: String,
    val newsList: List<NewsModel>
) {
    companion object {
        fun initial(): NewsListState {
            return NewsListState(
                newsLoading = true,
                isError = false,
                errorMessage = "",
                newsList = emptyList()
            )
        }
    }
}

/*
class NewsListScreenReducer :
    Reducer<NewsListScreenReducer.NewsListState, NewsListScreenReducer.NewsListEvent,
            NewsListScreenReducer.NewsListEffect> {

    override fun reduce(
        previousState: NewsListState,
        event: NewsListEvent
    ): Pair<NewsListState, NewsListEffect?> {
        return when (event) {
            is NewsListEvent.FetchNews -> {
                previousState.copy(
                    newsLoading = true
                ) to null
            }

            is NewsListEvent.FetchNewsError -> {
                previousState.copy(
                    newsLoading = false,
                    isError = true,
                    errorMessage = event.errorMessage
                ) to null
            }

            is NewsListEvent.FetchNewsList -> {
                previousState.copy(
                    newsLoading = false,
                    newsList = event.newsList
                ) to null
            }

            is NewsListEvent.RetryFetchNews -> {
                previousState.copy(
                    newsLoading = true,
                    errorMessage = "",
                    isError = false
                ) to null
            }
        }
    }
}*/
