package dev.rep.template.root

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import dev.rep.template.di.AndroidApplicationComponent
import dev.rep.template.di.create

open class App: Application() {

    open val component: AndroidApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        AndroidApplicationComponent.create(this)
    }


    override fun onCreate() {
        super.onCreate()
      //  setupStrictMode()

        component.initializers.initialize()
    }
}

private fun setupStrictMode() {
    StrictMode.setThreadPolicy(
        ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build(),
    )
    StrictMode.setVmPolicy(
        VmPolicy.Builder()
            .detectLeakedSqlLiteObjects()
            .detectActivityLeaks()
            .detectLeakedClosableObjects()
            .detectLeakedRegistrationObjects()
            .detectFileUriExposure()
            .detectCleartextNetwork()
            .apply {
                if (Build.VERSION.SDK_INT >= 26) {
                    detectContentUriWithoutPermission()
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    detectCredentialProtectedWhileLocked()
                }
                if (Build.VERSION.SDK_INT >= 31) {
                    detectIncorrectContextUse()
                    detectUnsafeIntentLaunch()
                }
            }
            .penaltyLog()
            .build(),
    )
}