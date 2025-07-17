package dev.rep.template.features.newsList.di

import dev.rep.template.di.ActivityScope
import dev.rep.template.features.newsList.data.FetchNewsDataRepository
import dev.rep.template.features.newsList.data.FetchNewsDataRepositoryImpl
import dev.rep.template.features.newsList.data.remote.FetchNewRemoteImplRepository
import dev.rep.template.features.newsList.data.remote.model.NewsListMapper
import dev.rep.template.features.newsList.data.remote.model.NewsResponseModel
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.repository.FetchNewsRepository
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCase
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCaseImpl
import dev.rep.template.features.newsList.presentation.NewsListAction
import dev.rep.template.features.newsList.presentation.NewsListEffect
import dev.rep.template.features.newsList.presentation.NewsListMiddleware
import dev.rep.template.features.newsList.presentation.NewsListScreenReducer
import dev.rep.template.features.newsList.presentation.NewsListState
import dev.rep.template.util.Mapper
import dev.rep.template.util.base.mvi.Middleware
import dev.rep.template.util.base.mvi.Reducer
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface NewsListDiProvider {
    @Provides
    @ActivityScope
    fun provideFetchNewsRepository(bind: FetchNewsDataRepositoryImpl): FetchNewsRepository = bind

    @Provides
    @ActivityScope
    fun provideFetchNewsDataRepository(bind: FetchNewRemoteImplRepository): FetchNewsDataRepository = bind

    @Provides
    @ActivityScope
    fun provideNewsListMapper(bind: NewsListMapper): Mapper<NewsResponseModel, NewsModel> = bind

    @Provides
    @ActivityScope
    fun provideFetchNewsUseCaseImpl(bind: FetchNewsUseCaseImpl): FetchNewsUseCase = bind

    @Provides
    @ActivityScope
    @IntoSet
    fun bindNewsListMiddleWare(middleWare: NewsListMiddleware): Middleware<NewsListState, NewsListAction, NewsListEffect> = middleWare


    @Provides
    @ActivityScope
    fun bindNewsListReducer(reducer: NewsListScreenReducer): Reducer<NewsListState, NewsListAction, NewsListEffect> = reducer
}