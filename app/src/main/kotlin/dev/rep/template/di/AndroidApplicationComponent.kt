package dev.rep.template.di

import android.app.Application
import android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE
import dev.rep.template.di.appInitializerBind.AppInitializers
import dev.rep.template.di.appInitializerBind.KermitInitializer
import dev.rep.template.util.wrapper.AppCoroutineDispatchers
import dev.rep.template.util.wrapper.AppFlavor
import dev.rep.template.util.wrapper.AppInitializer
import dev.rep.template.util.wrapper.AppSupportedPlatform
import dev.rep.template.util.wrapper.ApplicationInfo
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(@get:Provides open val application: Application): NetworkModule  {

  abstract val initializers: AppInitializers
  abstract val dispatchers: AppCoroutineDispatchers
  abstract val httpClient: HttpClient

  @Provides
  @IntoSet
  fun provideKermitInitializer(impl: KermitInitializer): AppInitializer = impl

  @Provides
  fun provideFlavor(): AppFlavor = AppFlavor.Standard

  @ApplicationScope
  @Provides
  fun provideApplicationInfo(
    application: Application,
    flavor: AppFlavor,
  ): ApplicationInfo {

    val packageManager = application.packageManager
    val applicationInfo = packageManager.getApplicationInfo(application.packageName, 0)
    val packageInfo = packageManager.getPackageInfo(application.packageName, 0)

    return ApplicationInfo(
      packageName = application.packageName,
      debugBuild = (applicationInfo.flags and FLAG_DEBUGGABLE) != 0,
      flavor = flavor,
      versionName = packageInfo?.versionName.orEmpty(),
      versionCode = @Suppress("DEPRECATION") packageInfo.versionCode,
      cachePath = { application.cacheDir.absolutePath },
      platform = AppSupportedPlatform.ANDROID,
    )
  }

  @ApplicationScope
  @Provides
  fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
    main = Dispatchers.Main.immediate,
    io = Dispatchers.IO,
    computation = Dispatchers.Default,
    singleThread = Dispatchers.IO.limitedParallelism(1),
    databaseRead = Dispatchers.IO.limitedParallelism(4))

  @ApplicationScope
  @Provides
  fun provideApplicationCoroutineScope(
    dispatchers: AppCoroutineDispatchers,
  ): ApplicationCoroutineScope = CoroutineScope(dispatchers.main + SupervisorJob())

  companion object
}
