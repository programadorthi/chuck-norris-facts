package dependencies

import configs.KotlinConfig

object Libraries {

    // Kotlin
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinConfig.VERSION}"

    // Kotlin serialization
    const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.KOTLIN_SERIALIZATION}"

    // RxJava
    const val RX_JAVA = "io.reactivex.rxjava2:rxjava:${Versions.RX_JAVA}"

    // OKHttp and Retrofit
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGER = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_RX_JAVA_ADAPTER = "com.squareup.retrofit2:adapter-rxjava:${Versions.RETROFIT_RX_JAVA_ADAPTER}"
    const val RETROFIT_KOTLIN_SERIALIZATION_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.RETROFIT_KOTLIN_SERIALIZATION_CONVERTER}"

    // Android
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}"

    // Lifecycle
    const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"

    // Material Design
    const val MATERIAL_DESIGN = "com.google.android.material:material:${Versions.MATERIAL_DESIGN}"

    // Logger
    const val LOGGER = "com.orhanobut:logger:${Versions.LOGGER}"

    // Unit test
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val ASSERTJ = "org.assertj:assertj-core:${Versions.ASSERTJ}"

    // Instrumentation test
    const val ANDROID_TEST_JUNIT_KTX = "androidx.test.ext:junit-ktx:${Versions.ANDROID_TEST_JUNIT_KTX}"
    const val ANDROID_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROID_TEST}"
    const val ANDROID_TEST_RULES = "androidx.test:rules:${Versions.ANDROID_TEST}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    const val ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO}"

    private object Versions {
        const val KOTLIN_SERIALIZATION = "0.12.0"

        const val RX_JAVA = "2.2.13"

        const val OKHTTP = "4.2.0"
        const val RETROFIT = "2.6.2"
        const val RETROFIT_RX_JAVA_ADAPTER = "2.6.2"
        const val RETROFIT_KOTLIN_SERIALIZATION_CONVERTER = "0.4.0"

        const val APP_COMPAT = "1.1.0"
        const val CORE_KTX = "1.1.0"
        const val CONSTRAINT_LAYOUT = "1.1.3"
        const val RECYCLER_VIEW = "1.0.0"

        const val LIFECYCLE = "2.1.0"

        const val MATERIAL_DESIGN = "1.0.0"

        const val LOGGER = "2.2.0"

        const val JUNIT = "4.12"
        const val ASSERTJ = "3.11.1"

        const val ANDROID_TEST_JUNIT_KTX = "1.1.1"
        const val ANDROID_TEST = "1.2.0"
        const val ESPRESSO = "3.2.0"
    }
}
