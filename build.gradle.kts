// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(PluginDependencies.ANDROID_GRADLE)
        classpath(PluginDependencies.KOTLIN)
        classpath(PluginDependencies.KOTLIN_SERIALIZATION)
        classpath(PluginDependencies.KTLINT)
        classpath(PluginDependencies.DETEKT)
        classpath(PluginDependencies.JACOCO_UNIFIED)
        classpath(PluginDependencies.TEST_LOGGER)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean").configure {
    delete(rootProject.buildDir)
}
