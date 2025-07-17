package dev.rep.template.di

import android.app.Application
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(@get:Provides override val application: Application): CommonAppComponent  {
  companion object
}