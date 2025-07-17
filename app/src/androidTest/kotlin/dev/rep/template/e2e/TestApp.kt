package dev.rep.template.e2e

import android.app.Application
import dev.rep.template.e2e.features.newsList.presentation.di.TestApplicationComponent
import dev.rep.template.e2e.features.newsList.presentation.di.create
import dev.rep.template.root.ApplicationProvider

class TestApp : Application(), ApplicationProvider {
    override val appComponent: TestApplicationComponent by lazy {
        TestApplicationComponent.create(this) // Pass application instance if needed by the component
    }
}