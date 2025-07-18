package dev.rep.template.e2e.features.newsList.presentation.di

import android.app.Application
import dev.rep.template.di.ApplicationScope
import dev.rep.template.di.CommonAppComponent
import dev.rep.template.e2e.features.newsList.presentation.util.MockClientResponseHandlerImpl
import dev.rep.template.e2e.features.newsList.presentation.util.errorResponse
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class TestApplicationComponent( @get:Provides override val application: Application):
    CommonAppComponent {

    @ApplicationScope
    @Provides
    override fun provideHttpClientEngine(): HttpClientEngine {
        return MockEngine { request ->
            val handlerList = listOf(MockClientResponseHandlerImpl())

            handlerList.forEach { handler ->
                val response = handler.handleRequest(this, request)
                if (response != null) {
                    return@MockEngine response
                }
            }
            return@MockEngine errorResponse()
        }
    }

    companion object
}
