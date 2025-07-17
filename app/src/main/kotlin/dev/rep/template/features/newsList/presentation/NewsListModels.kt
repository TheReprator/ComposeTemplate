package dev.rep.template.features.newsList.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.base.mvi.SideEffect
import dev.rep.template.util.base.mvi.UiAction
import dev.rep.template.util.base.mvi.UiState

@Stable
sealed interface NewsListAction : UiAction {
    object FetchNews : NewsListAction
    object RetryFetchNews : NewsListAction

    data class NewsClicked(val news: NewsModel) : NewsListAction

    data class UpdateNews(val newsList: List<NewsModel>) : NewsListAction
    data class NewsError(val message: String) : NewsListAction
}

@Stable
sealed interface NewsListEffect : SideEffect {
    data class NavigateToDetail(val newsModel: NewsModel) : NewsListEffect
    data class ShowError(val message: String) : NewsListEffect
}

@Immutable
data class NewsListState(
    val newsLoading: Boolean,
    val isError: Boolean,
    val errorMessage: String,
    val newsList: List<NewsModel>,
) : UiState {
    companion object {
        fun initial(): NewsListState {
            return NewsListState(
                newsLoading = false,
                isError = false,
                errorMessage = "",
                newsList = emptyList(),
            )
        }
    }
}