package dev.rep.template.e2e

import dev.rep.template.di.AndroidApplicationComponent
import dev.rep.template.e2e.features.newsList.presentation.di.TestApplicationComponent
import dev.rep.template.e2e.features.newsList.presentation.di.create
import dev.rep.template.root.App

class TestApp : App() {
    override val component: AndroidApplicationComponent by lazy {
        TestApplicationComponent.create(this) // Pass application instance if needed by the component
    }
}
