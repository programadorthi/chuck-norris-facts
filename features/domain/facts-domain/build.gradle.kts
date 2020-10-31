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

    implementation(Libraries.RX_JAVA)

    implementation(Libraries.KOIN_CORE)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }
}
