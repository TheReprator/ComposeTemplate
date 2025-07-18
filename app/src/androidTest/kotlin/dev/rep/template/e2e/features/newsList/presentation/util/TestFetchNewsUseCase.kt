package dev.rep.template.e2e.features.newsList.presentation.util

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.repository.FetchNewsRepository
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCase
import dev.rep.template.util.AppResult
import kotlinx.coroutines.flow.Flow

class TestFetchNewsUseCase(private val newsRepository: FetchNewsRepository): FetchNewsUseCase {
    override suspend fun invokeQuery(query: String): AppResult<List<NewsModel>> {
        return newsRepository.fetchNews(query)
    }

    override suspend fun invoke(query: String): Flow<AppResult<List<NewsModel>>> {
        TODO("Not yet implemented")
    }
}