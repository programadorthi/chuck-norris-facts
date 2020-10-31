import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.Android)

apply(from = module.script())

plugins {
    id(PluginIds.ANDROID_LIBRARY)
}

dependencies {
    implementation(project(JavaModules.SHARED_DOMAIN))
    implementation(project(JavaModules.SHARED_NETWORK))
    implementation(project(JavaModules.Features.Domain.FACTS_DOMAIN))
    implementation(project(JavaModules.Features.Data.FACTS_DATA))

    implementation(kotlin("stdlib-jdk8"))

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
