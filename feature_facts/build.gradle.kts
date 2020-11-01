import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.DynamicFeature)

apply(from = module.script())

plugins {
    id(PluginIds.ANDROID_DYNAMIC_FEATURE)
    kotlin("plugin.serialization")
}

android {
    defaultConfig {
        applicationId = "br.com.programadorthi.facts"
    }
}

dependencies {
    implementation(project(ApplicationModules.APP))
    implementation(project(JavaModules.SHARED_DOMAIN))
    implementation(project(JavaModules.SHARED_NETWORK))

    implementation(kotlin("stdlib-jdk8"))
    implementation(Libraries.KOTLIN_SERIALIZATION)

    implementation(Libraries.APP_COMPAT)
    implementation(Libraries.CONSTRAINT_LAYOUT)
    implementation(Libraries.RECYCLER_VIEW)

    implementation(Libraries.LIVEDATA)
    implementation(Libraries.VIEWMODEL)

    implementation(Libraries.MATERIAL_DESIGN)

    implementation(Libraries.RX_JAVA)

    implementation(Libraries.KODEIN_ANDROID)

    implementation(Libraries.RETROFIT)

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
