package dev.rep.template.features.newsList.data.remote.model

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.Mapper
import me.tatarka.inject.annotations.Inject

@Inject
class NewsListMapper: Mapper<NewsResponseModel, NewsModel> {

    override suspend fun map(from: NewsResponseModel): NewsModel {
        return NewsModel(from.urlToImage.orEmpty(), from.publishedAt ?: "",
            from.author ?: "", from.title ?: "", from.description ?: "",
            from.url ?: "")
    }
}