package configs

import org.gradle.api.JavaVersion

object KotlinConfig {
    const val VERSION = "1.3.50"
    val targetJVM = JavaVersion.VERSION_1_8.toString()
}