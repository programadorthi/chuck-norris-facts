import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(JavaModules.SHARED_DOMAIN))

    implementation(kotlin("stdlib-jdk8"))
    implementation(Libraries.KOTLIN_SERIALIZATION)
    implementation(Libraries.KOTLIN_COROUTINES)

    implementation(Libraries.KODEIN_CORE)

    implementation(Libraries.OKHTTP)
    implementation(Libraries.RETROFIT)
    implementation(Libraries.RETROFIT_KOTLIN_SERIALIZATION_CONVERTER)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }
}
