package dev.rep.template.di

import android.app.Activity
import dev.rep.template.features.newsList.di.NewsListDiProvider
import dev.rep.template.root.AppRouteFactory
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class AndroidActivityComponent(
  @get:Provides
  val activity: Activity,
  @Component val applicationComponent: CommonAppComponent,
): AppRouteFactoryComponent, NewsListDiProvider {

  abstract val routeFactories: Set<AppRouteFactory>

  companion object
}
