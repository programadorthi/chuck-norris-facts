import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType
import modules.ProjectModules

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    id(PluginIds.KOTLIN)
}

dependencies {
    implementation(project(ProjectModules.Shared.DOMAIN))

    implementation(Libraries.KOTLIN_STDLIB)
    implementation(Libraries.KOTLIN_SERIALIZATION)

    implementation(Libraries.RX_JAVA)

    implementation(Libraries.OKHTTP)
    implementation(Libraries.RETROFIT)
    implementation(Libraries.RETROFIT_KOTLIN_SERIALIZATION_CONVERTER)
    implementation(Libraries.RETROFIT_RX_JAVA_ADAPTER)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }
}
