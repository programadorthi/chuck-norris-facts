package modules

import java.io.File

class LibraryModule(
    private val rootDir: File,
    private val type: LibraryType
) {

    fun script() = "$rootDir/buildSrc/shared/${target()}"

    private fun target() = when (type) {
        LibraryType.Kotlin -> KOTLIN_MODULE
        LibraryType.Android -> ANDROID_MODULE
        LibraryType.DynamicFeature -> ANDROID_MODULE
    }

    // Why using .gradle instead of .gradle.kts files?
    // https://github.com/gradle/kotlin-dsl-samples/issues/1287#issuecomment-446110725
    private companion object {
        private const val ANDROID_MODULE = "android-module.gradle"
        private const val KOTLIN_MODULE = "kotlin-module.gradle"
    }
}
