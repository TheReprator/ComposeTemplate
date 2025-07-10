package dev.rep.template.features.newsList.domain.usecase

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.repository.FetchNewsRepository
import dev.rep.template.util.AppResult
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class FetchNewsUseCaseImpl(private val newsRepository: FetchNewsRepository) : FetchNewsUseCase {
    override suspend operator fun invoke() = newsRepository.fetchNews()
    override suspend operator fun invoke(query: String) = newsRepository.searchNews(query)
}

interface FetchNewsUseCase {
    suspend operator fun invoke(): AppResult<List<NewsModel>>
    suspend operator fun invoke(query: String): Flow<AppResult<List<NewsModel>>>
}