import configs.AndroidConfig
import configs.FlavorConfig
import dependencies.InstrumentationTestsDependencies.Companion.instrumentationTest
import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(PluginIds.ANDROID_APPLICATION)
    id(PluginIds.KOTLIN_ANDROD)
    id(PluginIds.KTLINT)
}

android {

    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        applicationId = AndroidConfig.APPLICATION_ID
        versionCode = Versioning.version.code
        versionName = Versioning.version.name
        testInstrumentationRunner = AndroidConfig.INSTRUMENTATION_TEST_RUNNER
    }

    buildTypes {
        getByName(FlavorConfig.BuildType.RELEASE) {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(Libraries.KOTLIN_STDLIB)

    implementation(Libraries.APP_COMPAT)
    implementation(Libraries.CORE_KTX)
    implementation(Libraries.CONSTRAINT_LAYOUT)

    unitTest {
        forEachDependency { testImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }

    instrumentationTest {
        forEachDependency { androidTestImplementation(it) }

        forEachProjectDependency(this@dependencies) {
            testImplementation(it)
        }
    }
}
