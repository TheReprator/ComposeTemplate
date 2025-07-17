package dev.rep.template.features.newsList.domain

import kotlinx.serialization.Serializable

@Serializable
data class NewsModel(
    val imageUrl: String, val id: String, val author: String,
    val title: String, val description: String, val detailUrl: String
)