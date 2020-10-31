package configs

import org.gradle.api.JavaVersion

object KotlinConfig {
    const val VERSION = "1.4.10"
    val targetJVM = JavaVersion.VERSION_1_8.toString()
}