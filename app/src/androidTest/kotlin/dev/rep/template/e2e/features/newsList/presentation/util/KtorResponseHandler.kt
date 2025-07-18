package dev.rep.template.e2e.features.newsList.presentation.util

import dev.rep.template.e2e.features.newsList.presentation.util.MockClientResponseHandler.Companion.REQUEST_API_KEY
import dev.rep.template.e2e.features.newsList.presentation.util.MockClientResponseHandler.Companion.REQUEST_PATH
import dev.rep.template.e2e.features.newsList.presentation.util.MockClientResponseHandler.Companion.REQUEST_PATH_QUERY
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import dev.rep.template.e2e.features.newsList.presentation.util.MockClientResponseHandler.Companion.REQUEST_PATH_QUERY_SEARCH
import kotlinx.coroutines.delay

interface MockClientResponseHandler {
    suspend fun handleRequest(
        scope: MockRequestHandleScope,
        request: HttpRequestData
    ): HttpResponseData?

    companion object {
        const val REQUEST_PATH = "everything"
        const val REQUEST_PATH_QUERY_SEARCH = "world"
        const val REQUEST_PATH_QUERY = "q"
        const val REQUEST_API_KEY = "apiKey"
    }
}

class MockClientResponseHandlerImpl : MockClientResponseHandler {

    override suspend fun handleRequest(
        scope: MockRequestHandleScope,
        request: HttpRequestData
    ): HttpResponseData? {

        if (request.url.encodedPath.contains(REQUEST_PATH).not()) {
            return null
        }

        val fileName = if (!request.url.parameters.contains(REQUEST_API_KEY)) {
            "news_error.json"
        } else {
            val searchKeyword = request.url.parameters[REQUEST_PATH_QUERY] ?: ""
            when (searchKeyword.lowercase()) {
                REQUEST_PATH_QUERY_SEARCH -> "news_success.json"
                else -> "news_empty.json"
            }
        }
        delay(1000L)
        return scope.respond(
            content = readResourceFile(fileName),
            status =   HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
}

private fun readResourceFile(fileName: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader?.getResourceAsStream(fileName)
        ?: throw IllegalArgumentException("Resource file $fileName not found.")

    return inputStream.bufferedReader().use { it.readText() }
}

internal fun MockRequestHandleScope.errorResponse(): HttpResponseData {
    return respond(
        content = readResourceFile("news_error.json"),
        status = HttpStatusCode.BadRequest,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}