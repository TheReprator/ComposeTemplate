package dev.rep.template.features.newsList.domain.repository

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.AppResult
import kotlinx.coroutines.flow.Flow

interface FetchNewsRepository {
    suspend fun fetchNews(query: String): AppResult<List<NewsModel>>
    suspend fun searchNews(query: String): Flow<AppResult<List<NewsModel>>>
}