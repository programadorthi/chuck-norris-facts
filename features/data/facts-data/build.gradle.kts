import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType
import modules.ProjectModules

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    id(PluginIds.KOTLIN)
    id(PluginIds.KOTLIN_SERIALIZATION)
}

dependencies {
    implementation(project(ProjectModules.Shared.DOMAIN))
    implementation(project(ProjectModules.Shared.NETWORK))
    implementation(project(ProjectModules.Feature.Domain.FACTS))

    implementation(Libraries.KOTLIN_STDLIB)
    implementation(Libraries.KOTLIN_SERIALIZATION)

    implementation(Libraries.RX_JAVA)

    implementation(Libraries.RETROFIT)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }
}
