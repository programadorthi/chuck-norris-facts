import com.vanniktech.android.junit.jacoco.JunitJacocoExtension
import configs.KotlinConfig
import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        jcenter()
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

    apply(plugin = PluginIds.DETEKT)

    detekt {
        config = files("$rootDir/default-detekt-config.yml")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = KotlinConfig.targetJVM
    }
}

tasks.register("clean").configure {
    delete(rootProject.buildDir)
}

apply(plugin = PluginIds.JACOCO_UNIFIED)

configure<JunitJacocoExtension> {
    jacocoVersion = "0.8.4"
}
