package configs

import org.gradle.api.JavaVersion

object AndroidConfig {
    const val APPLICATION_ID = "br.com.programadorthi.chucknorrisfacts"

    const val COMPILE_SDK_VERSION = 29
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = COMPILE_SDK_VERSION

    const val BUILD_TOOLS_VERSION = "29.0.2"

    const val INSTRUMENTATION_TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"

    val compileOptionsCompatibility = JavaVersion.VERSION_1_8

    val generatedDensities = emptyArray<String>()

    val resConfigs = arrayOf("en")
}
