package dev.rep.template.features.newsList.data.remote

import dev.rep.template.features.newsList.data.FetchNewsDataRepository
import dev.rep.template.features.newsList.data.remote.model.NewsResponseModel
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.AppError
import dev.rep.template.util.AppResult
import dev.rep.template.util.AppSuccess
import dev.rep.template.util.Mapper
import dev.rep.template.util.hitApiWithClient
import dev.rep.template.util.toListMapper
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class FetchNewRemoteImplRepository(
    private val httpClient: HttpClient, private val mapper:
    Mapper<NewsResponseModel, NewsModel>
) : FetchNewsDataRepository {

    override suspend fun fetchNews(): AppResult<List<NewsModel>> {
        val response = httpClient.hitApiWithClient<List<NewsResponseModel>>(
            "/everything?q=world",
            HttpMethod.Get
        )

        return when (response) {
            is AppSuccess -> {
                AppSuccess(mapper.toListMapper()(response.data))
            }

            is AppError -> {
                response
            }
        }
    }

    override suspend fun searchNews(query: String): Flow<AppResult<List<NewsModel>>> {
        val response = httpClient.hitApiWithClient<List<NewsResponseModel>>(
            "/everything?q=$query",
            HttpMethod.Get)

        val result = when (response) {
            is AppSuccess -> {
                AppSuccess(mapper.toListMapper()(response.data))
            }

            is AppError -> {
                response
            }
        }
        return flowOf(result)
    }
}