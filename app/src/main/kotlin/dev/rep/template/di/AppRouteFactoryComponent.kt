package dev.rep.template.di

import dev.rep.template.features.newsDetail.NewsListDetailRouteFactory
import dev.rep.template.features.newsList.ui.NewsListRouteFactory
import dev.rep.template.root.AppRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides


interface AppRouteFactoryComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun bindHomeRouteFactory(bind: NewsListRouteFactory): AppRouteFactory = bind

    @IntoSet
    @ActivityScope
    @Provides
    fun bindNewsDetailRouteFactory(bind: NewsListDetailRouteFactory): AppRouteFactory = bind
}