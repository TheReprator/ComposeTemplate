package dev.rep.template.util

import dev.rep.template.features.newsList.data.remote.model.ResponseModelDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.encodeURLPath
import io.ktor.http.encodedPath
import io.ktor.http.path
import okio.ByteString.Companion.encode

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): AppResult<T> = try {
    val response = request { block() }
    val body = response.body<ResponseModelDTO<T>>()
    if (body.status != "ok") {
        AppError(message = "Something went wrong")
    } else {
        AppSuccess(body.articles)
    }
} catch (e: Exception) {
    AppError(message = e.message ?: "Something went wrong")
}


suspend inline fun <reified T> HttpClient.hitApiWithClient(
    newUrl: String,
    methodName: HttpMethod = HttpMethod.Post,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
) = safeRequest<T> {
    url {
        method = methodName
        encodedPath = "/everything"
        parameters.append("q", "world")
    }
    block(this@safeRequest)
}