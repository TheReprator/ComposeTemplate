package dev.rep.template.di.appInitializerBind

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import dev.rep.template.util.wrapper.AppFlavor
import dev.rep.template.util.wrapper.AppInitializer
import dev.rep.template.util.wrapper.ApplicationInfo
import me.tatarka.inject.annotations.Inject

@Inject
class KermitInitializer constructor(
  private val applicationInfo: ApplicationInfo,
) : AppInitializer {
  override fun initialize() {
    Logger.Companion.withTag("")
    Logger.setMinSeverity(
      when {
        applicationInfo.debugBuild || applicationInfo.flavor == AppFlavor.UAT -> Severity.Debug
        else -> Severity.Error
      },
    )
  }
}