package dependencies

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

class UnitTestDependencies {
    private val all by lazy {
        listOf(
            Libraries.JUNIT,
            Libraries.ASSERTJ,
            Libraries.KOTLIN_COROUTINES_TEST,
            Libraries.MOCKK
        )
    }

    private val projects by lazy {
        emptyList<String>()
    }

    fun forEachDependency(consumer: (String) -> Unit) =
        all.forEach { consumer.invoke(it) }

    fun forEachProjectDependency(handler: DependencyHandler, consumer: (Dependency) -> Unit) =
        projects.forEach { consumer.invoke(handler.project(it)) }

    companion object {
        fun unitTest(block: UnitTestDependencies.() -> Unit) =
            UnitTestDependencies().apply(block)
    }
}
