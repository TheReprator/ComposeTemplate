package dev.rep.template.features.newsList.presentation

import dev.rep.template.util.base.mvi.Reducer
import me.tatarka.inject.annotations.Inject

@Inject
class NewsListScreenReducer :
    Reducer<NewsListState, NewsListAction, NewsListEffect> {

    override fun reduce(
        previousState: NewsListState,
        action: NewsListAction
    ): Pair<NewsListState, NewsListEffect?> {
        return when (action) {
            is NewsListAction.RetryFetchNews -> {
                previousState.copy(
                    newsLoading = true,
                    errorMessage = "",
                    isError = false
                ) to null
            }

            is NewsListAction.FetchNews -> {
                previousState.copy(
                    newsLoading = true
                ) to null
            }

            is NewsListAction.UpdateNews -> {
                previousState.copy(
                    newsLoading = false,
                    newsList = action.newsList
                ) to null
            }

            is NewsListAction.NewsError -> {
                previousState.copy(
                    newsLoading = false,
                    isError = true,
                    errorMessage = action.message
                ) to null
            }

            is NewsListAction.NewsClicked -> {
                previousState to NewsListEffect.NavigateToDetail(action.news)
            }
        }
    }
}
