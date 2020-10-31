import io.labs.dotanuki.magicmodules.MagicModulesExtension

rootProject.name = "Chuck Norris Facts"

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        classpath("com.github.dotanuki-labs:magic-modules:0.0.3")
    }
}

apply(plugin = "io.labs.dotanuki.magicmodules")

configure<MagicModulesExtension> {
    maxDepthToBuildScript = 4
    rawApplicationPlugins = listOf("PluginIds.ANDROID_APPLICATION")
    rawLibraryPlugins = listOf("PluginIds.ANDROID_DYNAMIC_FEATURE", "PluginIds.ANDROID_LIBRARY")
}
