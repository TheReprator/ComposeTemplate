package dev.rep.template.features.newsList.data

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.repository.FetchNewsRepository
import dev.rep.template.util.AppResult
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class FetchNewsDataRepositoryImpl (private val dataRepository: FetchNewsDataRepository): FetchNewsRepository {

    override suspend fun fetchNews(): AppResult<List<NewsModel>> {
       return dataRepository.fetchNews()
    }

    override suspend fun searchNews(query: String): Flow<AppResult<List<NewsModel>>> {
        return dataRepository.searchNews(query)
    }
}