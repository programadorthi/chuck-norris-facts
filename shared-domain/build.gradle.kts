import dependencies.Libraries
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    id(PluginIds.KOTLIN)
}

dependencies {
    implementation(Libraries.KOTLIN_STDLIB)
}
