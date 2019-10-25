import configs.AndroidConfig
import configs.FlavorConfig
import configs.ProguardConfig
import configs.SigningConfig
import dependencies.InstrumentationTestsDependencies.Companion.instrumentationTest
import dependencies.Libraries
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.ProjectModules

plugins {
    id(PluginIds.ANDROID_APPLICATION)
    id(PluginIds.KOTLIN_ANDROD)
    id(PluginIds.KTLINT)
}

base.archivesBaseName = "norris-facts-${Versioning.version.name}"

android {

    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)

        applicationId = AndroidConfig.APPLICATION_ID
        testInstrumentationRunner = AndroidConfig.INSTRUMENTATION_TEST_RUNNER
        versionCode = Versioning.version.code
        versionName = Versioning.version.name

        vectorDrawables.apply {
            useSupportLibrary = true
            generatedDensities(*(AndroidConfig.generatedDensities))
        }

        resConfigs(*(AndroidConfig.resConfigs))
    }

    signingConfigs {

        create(FlavorConfig.BuildType.RELEASE) {
            storeFile = SigningConfig.storeFile(rootProject)
            storePassword = SigningConfig.STORE_PASSWORD
            keyAlias = SigningConfig.KEY_ALIAS
            keyPassword = SigningConfig.KEY_PASSWORD
        }
    }

    buildTypes {

        getByName(FlavorConfig.BuildType.DEBUG) {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isTestCoverageEnabled = true
        }

        getByName(FlavorConfig.BuildType.RELEASE) {
            isMinifyEnabled = true
            isShrinkResources = true

            val proguardConfig = ProguardConfig("$rootDir/proguard")
            proguardFiles(*(proguardConfig.customRules))
            proguardFiles(getDefaultProguardFile(ProguardConfig.ANDROID_OPTIMIZE_FILE))

            signingConfig = signingConfigs.findByName(FlavorConfig.BuildType.RELEASE)
        }
    }

    flavorDimensions(*(arrayOf(FlavorConfig.DEFAULT_DIMENSION_NAME)))

    productFlavors {

        create(FlavorConfig.Flavor.DEVELOPMENT) {
            dimension = FlavorConfig.DEFAULT_DIMENSION_NAME

            buildConfigField("String", "BASE_URL", FlavorConfig.Endpoint.DEVELOPMENT)
        }

        create(FlavorConfig.Flavor.PRODUCTION) {
            dimension = FlavorConfig.DEFAULT_DIMENSION_NAME

            buildConfigField("String", "BASE_URL", FlavorConfig.Endpoint.PRODUCTION)
        }
    }

    compileOptions {
        sourceCompatibility = AndroidConfig.compileOptionsCompatibility
        targetCompatibility = AndroidConfig.compileOptionsCompatibility
    }
}

dependencies {
    implementation(project(ProjectModules.Shared.DOMAIN))
    implementation(project(ProjectModules.Shared.NETWORK))
    implementation(project(ProjectModules.Feature.Data.FACTS))
    implementation(project(ProjectModules.Feature.Domain.FACTS))
    implementation(project(ProjectModules.Feature.Ui.FACTS))

    implementation(Libraries.KOTLIN_STDLIB)

    implementation(Libraries.APP_COMPAT)
    implementation(Libraries.CORE_KTX)
    implementation(Libraries.CONSTRAINT_LAYOUT)
    implementation(Libraries.PREFERENCE)

    implementation(Libraries.KOIN_ANDROID)

    implementation(Libraries.OKHTTP)
    implementation(Libraries.OKHTTP_LOGGER)
    implementation(Libraries.RETROFIT)

    implementation(Libraries.LOGGER)
    implementation(Libraries.TIMBER)

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
