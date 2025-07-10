package dev.rep.template.util.wrapper

data class ApplicationInfo(
    val packageName: String,
    val debugBuild: Boolean,
    val flavor: AppFlavor,
    val versionName: String,
    val versionCode: Int,
    val cachePath: () -> String,
    val platform: AppSupportedPlatform,
)

enum class AppSupportedPlatform {
    IOS,
    ANDROID,
    DESKTOP,
    WEB,
}

enum class AppFlavor {
    UAT,
    Standard,
}
