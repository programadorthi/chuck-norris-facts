import configs.KotlinConfig

private object Versions {
    const val ANDROID_GRADLE = "3.5.0"
    const val TEST_LOGGER = "1.7.0"
    const val KTLINT = "8.2.0"
    const val JACOCO_UNIFIED = "0.15.0"
    const val DETEKT = "1.0.1"
}

object PluginDependencies {
    const val ANDROID_GRADLE = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${KotlinConfig.VERSION}"
    const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlin:kotlin-serialization:${KotlinConfig.VERSION}"
    const val TEST_LOGGER = "com.adarshr:gradle-test-logger-plugin:${Versions.TEST_LOGGER}"
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}"
    const val JACOCO_UNIFIED = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Versions.JACOCO_UNIFIED}"
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}"
}

object PluginIds {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val KOTLIN = "kotlin"
    const val KOTLIN_ANDROD = "kotlin-android"
    const val KOTLIN_ANDROID_EXTENSIONS = "kotlin-android-extensions"
    const val KOTLIN_SERIALIZATION = "kotlinx-serialization"
    const val JACOCO_UNIFIED = "com.vanniktech.android.junit.jacoco"
    const val KTLINT = "org.jlleitschuh.gradle.ktlint"
    const val DETEKT = "io.gitlab.arturbosch.detekt"
}
