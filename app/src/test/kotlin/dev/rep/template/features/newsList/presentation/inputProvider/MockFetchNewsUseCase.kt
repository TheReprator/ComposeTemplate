package dev.rep.template.features.newsList.presentation.inputProvider

import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.util.AppResult
import dev.rep.template.util.AppSuccess

class MockFetchNewsSuccessUseCase {

    companion object {
        val successNewsList = buildList<NewsModel> {
            repeat(10) {
                add(
                    NewsModel(
                        "https://example.com/image$it.jpg", "$it", "Author $it",
                        "Title $it", "Description $it", "https://example.com/detail$it")
                )
            }
        }

        val errorMessage = "Failed to fetch news"
    }

    suspend operator fun invoke(): AppResult<List<NewsModel>> {
        return AppSuccess(successNewsList)
    }

}