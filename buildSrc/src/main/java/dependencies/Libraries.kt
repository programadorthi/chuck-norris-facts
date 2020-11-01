package dependencies

object Libraries {

    // Kotlin serialization
    const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}"
    // Kotlin Coroutines
    const val KOTLIN_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES}"

    // OKHttp and Retrofit
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGER = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_KOTLIN_SERIALIZATION_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.RETROFIT_KOTLIN_SERIALIZATION_CONVERTER}"

    // Android
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val PREFERENCE = "androidx.preference:preference-ktx:${Versions.PREFERENCE}"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}"

    // Lifecycle
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"

    // Material Design
    const val MATERIAL_DESIGN = "com.google.android.material:material:${Versions.MATERIAL_DESIGN}"

    // Dependency Injection
    const val KODEIN_CORE = "org.kodein.di:kodein-di:${Versions.KODEIN}"
    const val KODEIN_ANDROID = "org.kodein.di:kodein-di-framework-android-x:${Versions.KODEIN}"

    // Logger
    const val LOGGER = "com.orhanobut:logger:${Versions.LOGGER}"
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    // Unit test
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val ASSERTJ = "org.assertj:assertj-core:${Versions.ASSERTJ}"
    const val KOTLIN_COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.KOTLIN_COROUTINES}"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"

    // Instrumentation test
    const val ANDROID_TEST_ARCH_CORE = "androidx.arch.core:core-testing:${Versions.ANDROID_TEST_ARCH_CORE}"
    const val ANDROID_TEST_CORE = "androidx.test:core:${Versions.ANDROID_TEST}"
    const val ANDROID_TEST_JUNIT_KTX = "androidx.test.ext:junit-ktx:${Versions.ANDROID_TEST_JUNIT_KTX}"
    const val ANDROID_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROID_TEST}"
    const val ANDROID_TEST_RULES = "androidx.test:rules:${Versions.ANDROID_TEST}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    const val ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO}"

    private object Versions {
        const val KOTLIN_SERIALIZATION = "1.0.1"
        const val KOTLIN_COROUTINES = "1.4.0"

        const val OKHTTP = "4.2.0"
        const val RETROFIT = "2.6.2"
        const val RETROFIT_KOTLIN_SERIALIZATION_CONVERTER = "0.8.0"

        const val ACTIVITY_KTX = "1.1.0"
        const val APP_COMPAT = "1.1.0"
        const val CORE_KTX = "1.1.0"
        const val CONSTRAINT_LAYOUT = "1.1.3"
        const val PREFERENCE = "1.1.0"
        const val RECYCLER_VIEW = "1.0.0"

        const val LIFECYCLE = "2.2.0"

        const val MATERIAL_DESIGN = "1.0.0"

        const val KODEIN = "7.1.0"

        const val LOGGER = "2.2.0"
        const val TIMBER = "4.7.1"

        const val JUNIT = "4.12"
        const val ASSERTJ = "3.11.1"
        const val MOCKK = "1.10.2"
        const val ROBOLECTRIC = "4.3.1"

        const val ANDROID_TEST_ARCH_CORE = "2.1.0"
        const val ANDROID_TEST_JUNIT_KTX = "1.1.1"
        const val ANDROID_TEST = "1.2.0"
        const val ESPRESSO = "3.2.0"
    }
}
