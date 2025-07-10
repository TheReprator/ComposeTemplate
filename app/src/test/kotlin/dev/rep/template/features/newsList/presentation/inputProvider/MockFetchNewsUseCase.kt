package dev.rep.template.features.newsList.presentation.inputProvider

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.AppResult
import dev.rep.template.util.AppSuccess

class MockFetchNewsSuccessUseCase {

    companion object {
        val newsList = listOf(
        NewsModel(
        "https://example.com/image1.jpg", "1", "Author 1",
        "Title 1", "Description 1", "https://example.com/detail1", false),

        NewsModel(
        "https://example.com/image1.jpg", "1", "Author 1",
        "Title 1", "Description 1", "https://example.com/detail1", false
        ))
    }

    suspend operator fun invoke(): AppResult<List<NewsModel>> {
        return AppSuccess(newsList)
    }

}