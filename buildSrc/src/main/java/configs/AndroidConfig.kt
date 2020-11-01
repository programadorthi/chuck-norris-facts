package configs

import org.gradle.api.JavaVersion
import java.util.Arrays

object AndroidConfig {
    const val APPLICATION_ID = "br.com.programadorthi.chucknorrisfacts"

    const val COMPILE_SDK_VERSION = 30
    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = COMPILE_SDK_VERSION

    const val BUILD_TOOLS_VERSION = "30.0.2"

    const val INSTRUMENTATION_TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"

    val compileOptionsCompatibility = JavaVersion.VERSION_1_8

    val dynamicFeatures = mutableSetOf(LibraryModules.FEATURE_FACTS)
    val generatedDensities = emptyArray<String>()

    val resConfigs = arrayOf("en")

    // Configs below are used in .gradle files. Don't remove!!!!
    val generatedDensitiesGroovy = Arrays.asList(*generatedDensities)
    val resConfigsGroovy = Arrays.asList(*resConfigs)
}
