package dependencies

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

class InstrumentationTestsDependencies {
    private val all by lazy {
        listOf(
            Libraries.ANDROID_TEST_RULES,
            Libraries.ANDROID_TEST_RUNNER,
            Libraries.ANDROID_TEST_JUNIT_KTX,
            Libraries.ESPRESSO_CORE,
            Libraries.ESPRESSO_CONTRIB
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
        fun instrumentationTest(block: InstrumentationTestsDependencies.() -> Unit) =
            InstrumentationTestsDependencies().apply(block)
    }
}
