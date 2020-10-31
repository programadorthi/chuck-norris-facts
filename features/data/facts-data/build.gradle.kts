import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    id(PluginIds.KOTLIN)
    id(PluginIds.KOTLIN_SERIALIZATION)
}

dependencies {
    implementation(project(JavaModules.SHARED_DOMAIN))
    implementation(project(JavaModules.SHARED_NETWORK))
    implementation(project(JavaModules.Features.Domain.FACTS_DOMAIN))

    implementation(Libraries.KOTLIN_STDLIB)
    implementation(Libraries.KOTLIN_SERIALIZATION)

    implementation(Libraries.RX_JAVA)

    implementation(Libraries.RETROFIT)

    implementation(Libraries.KOIN_CORE)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }
}
