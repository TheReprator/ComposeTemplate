package dev.rep.template.features.newsList.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.rep.template.util.base.mvi.MVI
import dev.rep.template.util.base.mvi.Middleware
import dev.rep.template.util.base.mvi.Reducer
import dev.rep.template.util.base.mvi.mvi
import dev.rep.template.util.wrapper.AppCoroutineDispatchers
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class NewsListViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val dispatchers: AppCoroutineDispatchers,
    private val middleWareList: Set<Middleware<NewsListState, NewsListAction, NewsListEffect>>,
    private val reducer: Reducer<NewsListState, NewsListAction, NewsListEffect>,
) : ViewModel(), MVI<NewsListState, NewsListAction, NewsListEffect> by
mvi(dispatchers, reducer, middleWareList, NewsListState.initial())