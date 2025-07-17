package dev.rep.template.di.appInitializerBind

import dev.rep.template.util.wrapper.AppInitializer
import me.tatarka.inject.annotations.Inject

@Inject
class AppInitializers (
    private val initializers: Lazy<Set<AppInitializer>>,
) : AppInitializer {
    override fun initialize() {
        for (initializer in initializers.value) {
            initializer.initialize()
        }
    }
}
