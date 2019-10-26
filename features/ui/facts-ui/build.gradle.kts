import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType
import modules.ProjectModules

val module = LibraryModule(rootDir, LibraryType.Android)

apply(from = module.script())

plugins {
    id(PluginIds.ANDROID_LIBRARY)
}

dependencies {
    implementation(project(ProjectModules.Shared.DOMAIN))
    implementation(project(ProjectModules.Shared.NETWORK))
    implementation(project(ProjectModules.Feature.Data.FACTS))
    implementation(project(ProjectModules.Feature.Domain.FACTS))

    implementation(Libraries.KOTLIN_STDLIB)

    implementation(Libraries.APP_COMPAT)
    implementation(Libraries.CONSTRAINT_LAYOUT)
    implementation(Libraries.RECYCLER_VIEW)

    implementation(Libraries.LIVEDATA)
    implementation(Libraries.VIEWMODEL)

    implementation(Libraries.MATERIAL_DESIGN)

    implementation(Libraries.RX_JAVA)

    implementation(Libraries.KOIN_SCOPE)
    implementation(Libraries.KOIN_VIEWMODEL)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }

        testImplementation(Libraries.ANDROID_TEST_ARCH_CORE)
        testImplementation(Libraries.ANDROID_TEST_CORE)
        testImplementation(Libraries.ROBOLECTRIC)
    }
}
