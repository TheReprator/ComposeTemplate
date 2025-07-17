package dev.rep.template.features.newsList.presentation

import co.touchlab.kermit.Logger
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.domain.usecase.FetchNewsUseCase
import dev.rep.template.util.AppError
import dev.rep.template.util.AppSuccess
import dev.rep.template.util.base.mvi.Middleware
import dev.rep.template.util.wrapper.AppCoroutineDispatchers
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import kotlin.coroutines.CoroutineContext

@Inject
class NewsListMiddleware(
    private val newsUseCase: FetchNewsUseCase,
    private val dispatchers: AppCoroutineDispatchers
) : Middleware<NewsListState, NewsListAction, NewsListEffect>, CoroutineScope, AutoCloseable {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.e(throwable) { "Coroutine exception" }
    }

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Main.immediate + coroutineExceptionHandler

    private val mviDispatcher = CompletableDeferred<(NewsListAction) -> Unit>()

    override fun close() {
        coroutineContext.cancelChildren()
    }

    override fun attach(dispatcher: (NewsListAction) -> Unit) {
        mviDispatcher.complete(dispatcher)
    }

    override fun onAction(
        action: NewsListAction,
        state: NewsListState
    ) {
        when (action) {
            is NewsListAction.FetchNews -> {
                fetchNews()
            }

            is NewsListAction.RetryFetchNews -> {
                fetchNews()
            }

            else -> {}
        }
    }


    private fun fetchNews() {
        launch(dispatchers.io) {
            val result = newsUseCase.invokeQuery()
            withContext(dispatchers.main) {
                when (result) {
                    is AppSuccess<List<NewsModel>> -> {
                        mviDispatcher.await()(NewsListAction.UpdateNews(result.data))
                    }

                    is AppError -> {
                        mviDispatcher.await()(
                            NewsListAction.NewsError(
                                result.message ?: result.throwable?.localizedMessage ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

}