package dev.rep.template.features.newsList.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCase
import dev.rep.template.util.AppError
import dev.rep.template.util.AppSuccess
import dev.rep.template.util.base.mvi.MVI
import dev.rep.template.util.base.mvi.mvi
import dev.rep.template.util.wrapper.AppCoroutineDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class NewsListViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val newsUseCase: FetchNewsUseCase,
    private val dispatchers: AppCoroutineDispatchers,
) : ViewModel(), MVI<NewsListState, NewsListEvent, NewsListEffect> by mvi(NewsListState.initial()) {

    override fun onAction(uiAction: NewsListEvent) {
        when (uiAction) {
            is NewsListEvent.FetchNews -> {
                fetchNews()
            }

            is NewsListEvent.RetryFetchNews -> {
                retryFetchNews()
                fetchNews()
            }

            is NewsListEvent.NavigateToDetail -> {
                viewModelScope.emitSideEffect(NewsListEffect.NavigateToDetail(uiAction.newsModel))
            }
        }
    }

    private fun retryFetchNews() {
        updateUiState { copy(
            newsLoading = true,
            errorMessage = "",
            isError = false
        ) }
    }

    private fun fetchNews() {
        viewModelScope.launch(dispatchers.io) {
            val result = newsUseCase()
            withContext(dispatchers.main) {
                when (result) {
                    is AppSuccess<List<NewsModel>> -> {
                        updateUiState { copy(  newsLoading = false,
                            newsList = result.data ) }
                    }

                    is AppError -> {
                        updateUiState { copy(
                            newsLoading = false,
                            isError = true,
                            errorMessage = result.message ?: result.throwable?.message ?: ""
                        ) }
                    }
                }
            }
        }
    }
}