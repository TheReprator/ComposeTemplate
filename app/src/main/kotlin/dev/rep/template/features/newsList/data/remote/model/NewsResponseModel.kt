package dev.rep.template.features.newsList.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseModel(
    val urlToImage: String?, val publishedAt: String?,
    val author: String?, val title: String?, val description: String?, val url: String?
)


@Serializable
data class ResponseModelDTO<out T>(
    val status: String?, val totalResults: Int?,
    val articles: T)


@Serializable
data class ResponseFailModelDTO(
    val status: String?, val code: Int?,
    val message: String?
)