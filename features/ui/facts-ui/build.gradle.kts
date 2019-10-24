import dependencies.InstrumentationTestsDependencies.Companion.instrumentationTest
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
    implementation(project(ProjectModules.Feature.Data.FACTS))
    implementation(project(ProjectModules.Feature.Domain.FACTS))

    implementation(Libraries.KOTLIN_STDLIB)

    implementation(Libraries.APP_COMPAT)
    implementation(Libraries.CONSTRAINT_LAYOUT)
    implementation(Libraries.RECYCLER_VIEW)

    implementation(Libraries.LIVEDATA)
    implementation(Libraries.VIEWMODEL)

    implementation(Libraries.MATERIAL_DESIGN)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }

    instrumentationTest {
        forEachDependency { androidTestImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            androidTestImplementation(it)
        }
    }
}
