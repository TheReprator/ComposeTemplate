package dev.rep.template.features.newsList.domain

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class NewsModel(
    val imageUrl: String, val id: String, val author: String,
    val title: String, val description: String, val detailUrl: String,
    val isSelected: Boolean
)