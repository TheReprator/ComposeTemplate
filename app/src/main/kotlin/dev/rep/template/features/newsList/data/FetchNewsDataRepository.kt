package dev.rep.template.features.newsList.data

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.AppResult
import kotlinx.coroutines.flow.Flow

interface FetchNewsDataRepository {
    suspend fun fetchNews(): AppResult<List<NewsModel>>
    suspend fun searchNews(query: String): Flow<AppResult<List<NewsModel>>>
}
